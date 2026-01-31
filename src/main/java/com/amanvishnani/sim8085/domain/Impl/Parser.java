package com.amanvishnani.sim8085.domain.Impl;

import com.amanvishnani.sim8085.domain.IAddress;
import com.amanvishnani.sim8085.domain.IData;
import com.amanvishnani.sim8085.domain.IOpcode;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Parser {
    private static final Map<String, Integer> REG_OFFSETS = Map.of(
            "B", 0, "C", 1, "D", 2, "E", 3, "H", 4, "L", 5, "M", 6, "A", 7
    );

    private static final Map<String, String> STATIC_OPCODES = Map.ofEntries(
            Map.entry("CMA", "2F"), Map.entry("CMC", "3F"), Map.entry("DAA", "27"),
            Map.entry("DI", "F3"), Map.entry("EI", "FB"), Map.entry("HLT", "76"),
            Map.entry("NOP", "00"), Map.entry("PCHL", "E9"), Map.entry("RAL", "17"),
            Map.entry("RAR", "1F"), Map.entry("RET", "C9"), Map.entry("RIM", "20"),
            Map.entry("RLC", "07"), Map.entry("RRC", "0F"), Map.entry("SIM", "30"),
            Map.entry("STC", "37"), Map.entry("XCHG", "EB"), Map.entry("XTHL", "E3"),
            Map.entry("SPHL", "F9")
    );

    private static final Map<String, Integer> ARITH_BASES = Map.of(
            "ADD", 0x80, "ADC", 0x88, "SUB", 0x90, "SBB", 0x98,
            "ANA", 0xA0, "XRA", 0xA8, "ORA", 0xB0, "CMP", 0xB8
    );

    public Parser() {}

    public List<String> getLineInstructions(String code) {
        String[] lines = code.split("\n");
        List<String> result = new ArrayList<>();
        for (String line : lines) {
            String trimmed = line.trim().replaceAll(" +", " ");
            if (!trimmed.isEmpty()) result.add(trimmed);
        }
        return result;
    }

    public IOpcode getOpcode(String lineInstruction) {
        String instr = lineInstruction.contains(":") ? lineInstruction.split(":")[1].trim() : lineInstruction.trim();
        String[] parts = instr.split("\s+");
        String mnemonic = parts[0].toUpperCase();
        String operands = parts.length > 1 ? instr.substring(parts[0].length()).trim().replace(" ", "") : "";

        String hexOpcode = resolveOpcode(mnemonic, operands);
        if (hexOpcode == null) throw new RuntimeException("Instruction not found: " + instr);

        return Opcode.createOpcode(hexOpcode, getOpcodeLength(hexOpcode, mnemonic), instr.replaceAll(" +", " "));
    }

    private String resolveOpcode(String mnemonic, String operands) {
        if (STATIC_OPCODES.containsKey(mnemonic)) return STATIC_OPCODES.get(mnemonic);

        if (mnemonic.equals("MOV")) {
            String[] regs = operands.split(",");
            return hex(0x40 | (REG_OFFSETS.get(regs[0]) << 3) | REG_OFFSETS.get(regs[1]));
        }
        if (mnemonic.equals("MVI")) {
            String[] regs = operands.split(",");
            return hex(0x06 | (REG_OFFSETS.get(regs[0]) << 3));
        }
        if (mnemonic.equals("INR")) return hex(0x04 | (REG_OFFSETS.get(operands) << 3));
        if (mnemonic.equals("DCR")) return hex(0x05 | (REG_OFFSETS.get(operands) << 3));
        if (ARITH_BASES.containsKey(mnemonic)) return hex(ARITH_BASES.get(mnemonic) | REG_OFFSETS.get(operands));

        return switch (mnemonic) {
            case "JMP" -> "C3"; case "JZ" -> "CA"; case "JNZ" -> "C2"; case "JC" -> "DA"; case "JNC" -> "D2";
            case "JP" -> "F2"; case "JM" -> "FA"; case "JPE" -> "EA"; case "JPO" -> "E2";
            case "CALL" -> "CD"; case "CZ" -> "CC"; case "CNZ" -> "C4"; case "CC" -> "DC"; case "CNC" -> "D4";
            case "CP" -> "F4"; case "CM" -> "FC"; case "CPE" -> "EC"; case "CPO" -> "E4";
            case "LXI" -> switch (operands.split(",")[0]) { case "B" -> "01"; case "D" -> "11"; case "H" -> "21"; case "SP" -> "31"; default -> null; };
            case "LDA" -> "3A"; case "STA" -> "32"; case "LHLD" -> "2A"; case "SHLD" -> "22";
            case "PUSH" -> switch (operands) { case "B" -> "C5"; case "D" -> "D5"; case "H" -> "E5"; case "PSW" -> "F5"; default -> null; };
            case "POP" -> switch (operands) { case "B" -> "C1"; case "D" -> "D1"; case "H" -> "E1"; case "PSW" -> "F1"; default -> null; };
            case "ADI" -> "C6"; case "ACI" -> "CE"; case "SUI" -> "D6"; case "SBI" -> "DE"; case "ANI" -> "E6"; case "XRI" -> "EE"; case "ORI" -> "F6"; case "CPI" -> "FE";
            default -> null;
        };
    }

    private String hex(int value) {
        return String.format("%02X", value & 0xFF);
    }

    private Integer getOpcodeLength(String hex, String mnemonic) {
        if (List.of("JMP", "JZ", "JNZ", "JC", "JNC", "JP", "JM", "JPE", "JPO",
                  "CALL", "CZ", "CNZ", "CC", "CNC", "CP", "CM", "CPE", "CPO",
                  "LDA", "STA", "LHLD", "SHLD", "LXI").contains(mnemonic)) return 3;
        if (List.of("MVI", "ADI", "ACI", "SUI", "SBI", "ANI", "XRI", "ORI", "CPI", "IN", "OUT").contains(mnemonic)) return 2;
        return 1;
    }

    public String extractLabel(String lineInstruction) {
        if (!lineInstruction.contains(":")) return "";
        return lineInstruction.split(":")[0].trim();
    }

    public IData extractData(String x) {
        Pattern p = Pattern.compile(",\s*([0-9A-Fa-f]{2})H?", Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(x);
        return m.find() ? Data.from(m.group(1)) : null;
    }

    public String extractOperandLabel(IOpcode opcode) {
        String instr = opcode.getInstruction();
        String[] parts = instr.split("\s+");
        if (parts.length < 2) return "";
        String lastPart = parts[parts.length - 1];
        if (lastPart.matches("[0-9A-Fa-f]{2,4}H?")) return "";
        return lastPart;
    }

    public IAddress extractOperandAddress(String x) {
        Pattern p = Pattern.compile("\s+([0-9A-Fa-f]{4})H?", Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(x);
        if (m.find()) return Address.from(m.group(1));
        p = Pattern.compile("\s+([0-9A-Fa-f]{3})H?", Pattern.CASE_INSENSITIVE);
        m = p.matcher(x);
        if (m.find()) return Address.from(m.group(1));
        return null;
    }
}
