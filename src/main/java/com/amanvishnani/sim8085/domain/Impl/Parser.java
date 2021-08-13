package com.amanvishnani.sim8085.domain.Impl;

import com.amanvishnani.sim8085.domain.IAddress;
import com.amanvishnani.sim8085.domain.IData;
import com.amanvishnani.sim8085.domain.IOpcode;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Parser {
    public static final String OPERAND_LABEL_REGEX = "^\\w*\\s*:?\\s*\\w+\\s+(\\w+)$";
    public static final String OPERAND_DATA_REGEX = "^\\w*\\s*:?\\s*\\w+\\s+(\\w\\s*,\\s*)?([A-Fa-f0-9]{2})H?$";
    public static final String LABEL_REGEX = "^(\\w*)\\s*:\\s*\\w+\\s*\\w*$";
    public static final String OPERAND_ADDRESS_REGEX = "^\\w*\\s*:?\\s*\\w+\\s+([A-Fa-f0-9]{4})H?$";
    public Pattern[] patterns = new Pattern[246];
    public final String data = "[0-9A-F]{2}(H)?";
    public final String label = "[A-Za-z]{3}[A-Za-z]*";
    public final String addr = "[0-9A-F]{4}(H)?";
    public final String space = "( )*";
    public final String space1 = "( )+";
    public Matcher m;

    public Parser() {
        this.initializePatt();
    }

    public List<String> getLineInstructions(String code) {
        String[] code_token = code.split("\\n");
        List<String> temp= new ArrayList<>();

        for (int j = 0; j < code_token.length; j++) {
            code_token[j] = trimSpaces(code_token[j]);
            if (0 != code_token[j].length()) {
                temp.add(code_token[j]);
            }
        }
        return temp;
    }

    private String trimSpaces(String x) {
        x = x.trim().replaceAll(" +", " ");
        return x;
    }

    public IOpcode getOpcode(String lineInstruction) {
        // Find instruction index
        Integer instructionIdx = findI(lineInstruction);
        if (instructionIdx == -1) {
            throw new RuntimeException("Instruction not found");
        }
        // Find opcode from index
        String opcode = findOpcode(instructionIdx);
        // Find opcode length
        Integer opcodeLength = getOpcodeLength(opcode);
        // Extract Instruction
        String[] split = lineInstruction.split(":");
        String instr = split.length > 1 ? split[1] : split[0];
        return Opcode.createOpcode(opcode, opcodeLength, trimSpaces(instr));
    }

    private Integer findI(String str) {

        for (int i = 0; i < patterns.length; i++) {
            m = patterns[i].matcher(str);
            if (m.find()) {
                return i;
            }
        }
        return -1;
    }

    private void initializePatt() {
        patterns[0] = Pattern.compile("MOV" + space1 + "B" + space + "," + space + "C");
        patterns[1] = Pattern.compile("MOV" + space1 + "B" + space + "," + space + "D");
        patterns[2] = Pattern.compile("MOV" + space1 + "B" + space + "," + space + "E");
        patterns[3] = Pattern.compile("MOV" + space1 + "B" + space + "," + space + "H");
        patterns[4] = Pattern.compile("MOV" + space1 + "B" + space + "," + space + "L");
        patterns[5] = Pattern.compile("MOV" + space1 + "B" + space + "," + space + "B");
        patterns[6] = Pattern.compile("MOV" + space1 + "C" + space + "," + space + "B");
        patterns[7] = Pattern.compile("MOV" + space1 + "C" + space + "," + space + "C");
        patterns[8] = Pattern.compile("MOV" + space1 + "C" + space + "," + space + "D");
        patterns[9] = Pattern.compile("MOV" + space1 + "C" + space + "," + space + "E");
        patterns[10] = Pattern.compile("MOV" + space1 + "C" + space + "," + space + "H");
        patterns[11] = Pattern.compile("MOV" + space1 + "C" + space + "," + space + "L");
        patterns[12] = Pattern.compile("MOV" + space1 + "D" + space + "," + space + "B");
        patterns[13] = Pattern.compile("MOV" + space1 + "D" + space + "," + space + "C");
        patterns[14] = Pattern.compile("MOV" + space1 + "D" + space + "," + space + "D");
        patterns[15] = Pattern.compile("MOV" + space1 + "D" + space + "," + space + "E");
        patterns[16] = Pattern.compile("MOV" + space1 + "D" + space + "," + space + "H");
        patterns[17] = Pattern.compile("MOV" + space1 + "D" + space + "," + space + "L");
        patterns[18] = Pattern.compile("MOV" + space1 + "E" + space + "," + space + "B");
        patterns[19] = Pattern.compile("MOV" + space1 + "E" + space + "," + space + "C");
        patterns[20] = Pattern.compile("MOV" + space1 + "E" + space + "," + space + "D");
        patterns[21] = Pattern.compile("MOV" + space1 + "E" + space + "," + space + "E");
        patterns[22] = Pattern.compile("MOV" + space1 + "E" + space + "," + space + "H");
        patterns[23] = Pattern.compile("MOV" + space1 + "E" + space + "," + space + "L");
        patterns[24] = Pattern.compile("MOV" + space1 + "H" + space + "," + space + "B");
        patterns[25] = Pattern.compile("MOV" + space1 + "H" + space + "," + space + "C");
        patterns[26] = Pattern.compile("MOV" + space1 + "H" + space + "," + space + "D");
        patterns[27] = Pattern.compile("MOV" + space1 + "H" + space + "," + space + "E");
        patterns[28] = Pattern.compile("MOV" + space1 + "H" + space + "," + space + "H");
        patterns[29] = Pattern.compile("MOV" + space1 + "H" + space + "," + space + "L");
        patterns[30] = Pattern.compile("MOV" + space1 + "L" + space + "," + space + "B");
        patterns[31] = Pattern.compile("MOV" + space1 + "L" + space + "," + space + "C");
        patterns[32] = Pattern.compile("MOV" + space1 + "L" + space + "," + space + "D");
        patterns[33] = Pattern.compile("MOV" + space1 + "L" + space + "," + space + "E");
        patterns[34] = Pattern.compile("MOV" + space1 + "L" + space + "," + space + "H");
        patterns[35] = Pattern.compile("MOV" + space1 + "L" + space + "," + space + "L");
        patterns[36] = Pattern.compile("MOV" + space1 + "A" + space + "," + space + "A");
        patterns[37] = Pattern.compile("MOV" + space1 + "A" + space + "," + space + "B");
        patterns[38] = Pattern.compile("MOV" + space1 + "A" + space + "," + space + "C");
        patterns[39] = Pattern.compile("MOV" + space1 + "A" + space + "," + space + "D");
        patterns[40] = Pattern.compile("MOV" + space1 + "A" + space + "," + space + "E");
        patterns[41] = Pattern.compile("MOV" + space1 + "A" + space + "," + space + "H");
        patterns[42] = Pattern.compile("MOV" + space1 + "A" + space + "," + space + "L");
        patterns[43] = Pattern.compile("MOV" + space1 + "B" + space + "," + space + "A");
        patterns[44] = Pattern.compile("MOV" + space1 + "C" + space + "," + space + "A");
        patterns[45] = Pattern.compile("MOV" + space1 + "D" + space + "," + space + "A");
        patterns[46] = Pattern.compile("MOV" + space1 + "E" + space + "," + space + "A");
        patterns[47] = Pattern.compile("MOV" + space1 + "H" + space + "," + space + "A");
        patterns[48] = Pattern.compile("MOV" + space1 + "L" + space + "," + space + "A");
        patterns[49] = Pattern.compile("MOV" + space1 + "A" + space + "," + space + "M");
        patterns[50] = Pattern.compile("MOV" + space1 + "B" + space + "," + space + "M");
        patterns[51] = Pattern.compile("MOV" + space1 + "C" + space + "," + space + "M");
        patterns[52] = Pattern.compile("MOV" + space1 + "D" + space + "," + space + "M");
        patterns[53] = Pattern.compile("MOV" + space1 + "E" + space + "," + space + "M");
        patterns[54] = Pattern.compile("MOV" + space1 + "H" + space + "," + space + "M");
        patterns[55] = Pattern.compile("MOV" + space1 + "L" + space + "," + space + "M");
        patterns[56] = Pattern.compile("MOV" + space1 + "M" + space + "," + space + "A");
        patterns[57] = Pattern.compile("MOV" + space1 + "M" + space + "," + space + "B");
        patterns[58] = Pattern.compile("MOV" + space1 + "M" + space + "," + space + "C");
        patterns[59] = Pattern.compile("MOV" + space1 + "M" + space + "," + space + "D");
        patterns[60] = Pattern.compile("MOV" + space1 + "M" + space + "," + space + "E");
        patterns[61] = Pattern.compile("MOV" + space1 + "M" + space + "," + space + "H");
        patterns[62] = Pattern.compile("MOV" + space1 + "M" + space + "," + space + "L");
        patterns[63] = Pattern.compile("MVI" + space1 + "A" + space + "," + space + data);
        patterns[64] = Pattern.compile("MVI" + space1 + "B" + space + "," + space + data);
        patterns[65] = Pattern.compile("MVI" + space1 + "C" + space + "," + space + data);
        patterns[66] = Pattern.compile("MVI" + space1 + "D" + space + "," + space + data);
        patterns[67] = Pattern.compile("MVI" + space1 + "E" + space + "," + space + data);
        patterns[68] = Pattern.compile("MVI" + space1 + "H" + space + "," + space + data);
        patterns[69] = Pattern.compile("MVI" + space1 + "L" + space + "," + space + data);
        patterns[70] = Pattern.compile("MVI" + space1 + "M" + space + "," + space + data);
        patterns[71] = Pattern.compile("ACI" + space1 + data);
        patterns[72] = Pattern.compile("ADC" + space1 + "A");
        patterns[73] = Pattern.compile("ADC" + space1 + "B");
        patterns[74] = Pattern.compile("ADC" + space1 + "C");
        patterns[75] = Pattern.compile("ADC" + space1 + "D");
        patterns[76] = Pattern.compile("ADC" + space1 + "E");
        patterns[77] = Pattern.compile("ADC" + space1 + "H");
        patterns[78] = Pattern.compile("ADC" + space1 + "L");
        patterns[79] = Pattern.compile("ADC" + space1 + "M");
        patterns[80] = Pattern.compile("ADD" + space1 + "A");
        patterns[81] = Pattern.compile("ADD" + space1 + "B");
        patterns[82] = Pattern.compile("ADD" + space1 + "C");
        patterns[83] = Pattern.compile("ADD" + space1 + "D");
        patterns[84] = Pattern.compile("ADD" + space1 + "E");
        patterns[85] = Pattern.compile("ADD" + space1 + "H");
        patterns[86] = Pattern.compile("ADD" + space1 + "L");
        patterns[87] = Pattern.compile("ADD" + space1 + "M");
        patterns[88] = Pattern.compile("ADI" + space1 + data);
        patterns[89] = Pattern.compile("ANA" + space1 + "A");
        patterns[90] = Pattern.compile("ANA" + space1 + "B");
        patterns[91] = Pattern.compile("ANA" + space1 + "C");
        patterns[92] = Pattern.compile("ANA" + space1 + "D");
        patterns[93] = Pattern.compile("ANA" + space1 + "E");
        patterns[94] = Pattern.compile("ANA" + space1 + "H");
        patterns[95] = Pattern.compile("ANA" + space1 + "L");
        patterns[96] = Pattern.compile("ANA" + space1 + "M");
        patterns[97] = Pattern.compile("ANI" + space1 + data);
        patterns[98] = Pattern.compile("CALL" + space1 + label);
        patterns[99] = Pattern.compile("CC" + space1 + label);
        patterns[100] = Pattern.compile("CM" + space1 + label);
        patterns[101] = Pattern.compile("CMA");
        patterns[102] = Pattern.compile("CMC");
        patterns[103] = Pattern.compile("CMP" + space1 + "A");
        patterns[104] = Pattern.compile("CMP" + space1 + "B");
        patterns[105] = Pattern.compile("CMP" + space1 + "C");
        patterns[106] = Pattern.compile("CMP" + space1 + "D");
        patterns[107] = Pattern.compile("CMP" + space1 + "E");
        patterns[108] = Pattern.compile("CMP" + space1 + "H");
        patterns[109] = Pattern.compile("CMP" + space1 + "L");
        patterns[110] = Pattern.compile("CMP" + space1 + "M");
        patterns[111] = Pattern.compile("CNC" + space1 + label);
        patterns[112] = Pattern.compile("CNZ" + space1 + label);
        patterns[113] = Pattern.compile("CPE" + space1 + label);
        patterns[114] = Pattern.compile("CPO" + space1 + label);
        patterns[115] = Pattern.compile("CPI" + space1 + data);
        patterns[116] = Pattern.compile("CP" + space1 + label);
        patterns[117] = Pattern.compile("CZ" + space1 + label);
        patterns[118] = Pattern.compile("DAA");
        patterns[119] = Pattern.compile("DAD" + space1 + "B");
        patterns[120] = Pattern.compile("DAD" + space1 + "D");
        patterns[121] = Pattern.compile("DAD" + space1 + "H");
        patterns[122] = Pattern.compile("DAD" + space1 + "SP");
        patterns[123] = Pattern.compile("DCR" + space1 + "A");
        patterns[124] = Pattern.compile("DCR" + space1 + "B");
        patterns[125] = Pattern.compile("DCR" + space1 + "C");
        patterns[126] = Pattern.compile("DCR" + space1 + "D");
        patterns[127] = Pattern.compile("DCR" + space1 + "E");
        patterns[128] = Pattern.compile("DCR" + space1 + "H");
        patterns[129] = Pattern.compile("DCR" + space1 + "L");
        patterns[130] = Pattern.compile("DCR" + space1 + "M");
        patterns[131] = Pattern.compile("DCX" + space1 + "B");
        patterns[132] = Pattern.compile("DCX" + space1 + "D");
        patterns[133] = Pattern.compile("DCX" + space1 + "H");
        patterns[134] = Pattern.compile("DCX" + space1 + "SP");
        patterns[135] = Pattern.compile("DI");
        patterns[136] = Pattern.compile("EI");
        patterns[137] = Pattern.compile("HLT");
        patterns[138] = Pattern.compile("IN" + space1 + data);
        patterns[139] = Pattern.compile("INR" + space1 + "A");
        patterns[140] = Pattern.compile("INR" + space1 + "B");
        patterns[141] = Pattern.compile("INR" + space1 + "C");
        patterns[142] = Pattern.compile("INR" + space1 + "D");
        patterns[143] = Pattern.compile("INR" + space1 + "E");
        patterns[144] = Pattern.compile("INR" + space1 + "H");
        patterns[145] = Pattern.compile("INR" + space1 + "L");
        patterns[146] = Pattern.compile("INR" + space1 + "M");
        patterns[147] = Pattern.compile("INX" + space1 + "B");
        patterns[148] = Pattern.compile("INX" + space1 + "D");
        patterns[149] = Pattern.compile("INX" + space1 + "H");
        patterns[150] = Pattern.compile("INX" + space1 + "SP");
        patterns[151] = Pattern.compile("JC" + space1 + label);
        patterns[152] = Pattern.compile("JMP" + space1 + label);
        patterns[153] = Pattern.compile("JM" + space1 + label);
        patterns[154] = Pattern.compile("JNC" + space1 + label);
        patterns[155] = Pattern.compile("JNZ" + space1 + label);
        patterns[156] = Pattern.compile("JPO" + space1 + label);
        patterns[157] = Pattern.compile("JPE" + space1 + label);
        patterns[158] = Pattern.compile("JP" + space1 + label);
        patterns[159] = Pattern.compile("JZ" + space1 + label);
        patterns[160] = Pattern.compile("LDA" + space1 + addr);
        patterns[161] = Pattern.compile("LDAX" + space1 + "B");
        patterns[162] = Pattern.compile("LDAX" + space1 + "D");
        patterns[163] = Pattern.compile("LHLD" + space1 + addr);
        patterns[164] = Pattern.compile("LXI" + space1 + "B" + space + "," + space + addr);
        patterns[165] = Pattern.compile("LXI" + space1 + "D" + space + "," + space + addr);
        patterns[166] = Pattern.compile("LXI" + space1 + "H" + space + "," + space + addr);
        patterns[167] = Pattern.compile("LXI" + space1 + "SP" + space + "," + space + addr);
        patterns[168] = Pattern.compile("NOP");
        patterns[169] = Pattern.compile("ORA" + space1 + "A");
        patterns[170] = Pattern.compile("ORA" + space1 + "B");
        patterns[171] = Pattern.compile("ORA" + space1 + "C");
        patterns[172] = Pattern.compile("ORA" + space1 + "D");
        patterns[173] = Pattern.compile("ORA" + space1 + "E");
        patterns[174] = Pattern.compile("ORA" + space1 + "H");
        patterns[175] = Pattern.compile("ORA" + space1 + "L");
        patterns[176] = Pattern.compile("ORA" + space1 + "M");
        patterns[177] = Pattern.compile("ORI" + space1 + data);
        patterns[178] = Pattern.compile("OUT" + space1 + data);
        patterns[179] = Pattern.compile("PCHL");
        patterns[180] = Pattern.compile("POP" + space1 + "B");
        patterns[181] = Pattern.compile("POP" + space1 + "D");
        patterns[182] = Pattern.compile("POP" + space1 + "H");
        patterns[183] = Pattern.compile("POP" + space1 + "PSW");
        patterns[184] = Pattern.compile("PUSH" + space1 + "B");
        patterns[185] = Pattern.compile("PUSH" + space1 + "D");
        patterns[186] = Pattern.compile("PUSH" + space1 + "H");
        patterns[187] = Pattern.compile("PUSH" + space1 + "PSW");
        patterns[188] = Pattern.compile("RAL");
        patterns[189] = Pattern.compile("RAR");
        patterns[190] = Pattern.compile("RC");
        patterns[191] = Pattern.compile("RET");
        patterns[192] = Pattern.compile("RIM");
        patterns[193] = Pattern.compile("RLC");
        patterns[194] = Pattern.compile("RM");
        patterns[195] = Pattern.compile("RNC");
        patterns[196] = Pattern.compile("RNZ");
        patterns[197] = Pattern.compile("RP");
        patterns[198] = Pattern.compile("RPE");
        patterns[199] = Pattern.compile("RPO");
        patterns[200] = Pattern.compile("RRC");
        patterns[201] = Pattern.compile("RST" + space1 + "0");
        patterns[202] = Pattern.compile("RST" + space1 + "1");
        patterns[203] = Pattern.compile("RST" + space1 + "2");
        patterns[204] = Pattern.compile("RST" + space1 + "3");
        patterns[205] = Pattern.compile("RST" + space1 + "4");
        patterns[206] = Pattern.compile("RST" + space1 + "5");
        patterns[207] = Pattern.compile("RST" + space1 + "6");
        patterns[208] = Pattern.compile("RST" + space1 + "7");
        patterns[209] = Pattern.compile("RZ");
        patterns[210] = Pattern.compile("SBB" + space1 + "A");
        patterns[211] = Pattern.compile("SBB" + space1 + "B");
        patterns[212] = Pattern.compile("SBB" + space1 + "C");
        patterns[213] = Pattern.compile("SBB" + space1 + "D");
        patterns[214] = Pattern.compile("SBB" + space1 + "E");
        patterns[215] = Pattern.compile("SBB" + space1 + "H");
        patterns[216] = Pattern.compile("SBB" + space1 + "L");
        patterns[217] = Pattern.compile("SBB" + space1 + "M");
        patterns[218] = Pattern.compile("SBI" + space1 + data);
        patterns[219] = Pattern.compile("SHLD" + space1 + addr);
        patterns[220] = Pattern.compile("SIM");
        patterns[221] = Pattern.compile("SPHL");
        patterns[222] = Pattern.compile("STA" + space1 + addr);
        patterns[223] = Pattern.compile("STAX" + space1 + "B");
        patterns[224] = Pattern.compile("STAX" + space1 + "D");
        patterns[225] = Pattern.compile("STC");
        patterns[226] = Pattern.compile("SUB" + space1 + "A");
        patterns[227] = Pattern.compile("SUB" + space1 + "B");
        patterns[228] = Pattern.compile("SUB" + space1 + "C");
        patterns[229] = Pattern.compile("SUB" + space1 + "D");
        patterns[230] = Pattern.compile("SUB" + space1 + "E");
        patterns[231] = Pattern.compile("SUB" + space1 + "H");
        patterns[232] = Pattern.compile("SUB" + space1 + "L");
        patterns[233] = Pattern.compile("SUB" + space1 + "M");
        patterns[234] = Pattern.compile("SUI" + space1 + data);
        patterns[235] = Pattern.compile("XCHD");
        patterns[236] = Pattern.compile("XRA" + space1 + "A");
        patterns[237] = Pattern.compile("XRA" + space1 + "B");
        patterns[238] = Pattern.compile("XRA" + space1 + "C");
        patterns[239] = Pattern.compile("XRA" + space1 + "D");
        patterns[240] = Pattern.compile("XRA" + space1 + "E");
        patterns[241] = Pattern.compile("XRA" + space1 + "H");
        patterns[242] = Pattern.compile("XRA" + space1 + "L");
        patterns[243] = Pattern.compile("XRA" + space1 + "M");
        patterns[244] = Pattern.compile("XRI" + space1 + data);
        patterns[245] = Pattern.compile("XTHL");

    }

    private String findOpcode(int x) {
        switch (x) {
            case 0:
                return "41";
            case 1:
                return "42";
            case 2:
                return "43";
            case 3:
                return "44";
            case 4:
                return "45";
            case 5:
                return "40";
            case 6:
                return "48";
            case 7:
                return "49";
            case 8:
                return "4A";
            case 9:
                return "4B";
            case 10:
                return "4C";
            case 11:
                return "4D";
            case 12:
                return "50";
            case 13:
                return "51";
            case 14:
                return "52";
            case 15:
                return "53";
            case 16:
                return "54";
            case 17:
                return "55";
            case 18:
                return "58";
            case 19:
                return "59";
            case 20:
                return "5A";
            case 21:
                return "5B";
            case 22:
                return "5C";
            case 23:
                return "5D";
            case 24:
                return "60";
            case 25:
                return "61";
            case 26:
                return "62";
            case 27:
                return "63";
            case 28:
                return "64";
            case 29:
                return "65";
            case 30:
                return "68";
            case 31:
                return "69";
            case 32:
                return "6A";
            case 33:
                return "6B";
            case 34:
                return "6C";
            case 35:
                return "6D";
            case 36:
                return "7F";
            case 37:
                return "78";
            case 38:
                return "79";
            case 39:
                return "7A";
            case 40:
                return "7B";
            case 41:
                return "7C";
            case 42:
                return "7D";
            case 43:
                return "47";
            case 44:
                return "4F";
            case 45:
                return "57";
            case 46:
                return "5F";
            case 47:
                return "67";
            case 48:
                return "6F";
            case 49:
                return "7E";
            case 50:
                return "46";
            case 51:
                return "4E";
            case 52:
                return "56";
            case 53:
                return "5E";
            case 54:
                return "66";
            case 55:
                return "6E";
            case 56:
                return "77";
            case 57:
                return "70";
            case 58:
                return "71";
            case 59:
                return "72";
            case 60:
                return "73";
            case 61:
                return "74";
            case 62:
                return "75";
            case 63:
                return "3E";
            case 64:
                return "06";
            case 65:
                return "0E";
            case 66:
                return "16";
            case 67:
                return "1E";
            case 68:
                return "26";
            case 69:
                return "2E";
            case 70:
                return "36";
            case 71:
                return "CE";
            case 72:
                return "8F";
            case 73:
                return "88";
            case 74:
                return "89";
            case 75:
                return "8A";
            case 76:
                return "8B";
            case 77:
                return "8C";
            case 78:
                return "8D";
            case 79:
                return "8E";
            case 80:
                return "87";
            case 81:
                return "80";
            case 82:
                return "81";
            case 83:
                return "82";
            case 84:
                return "83";
            case 85:
                return "84";
            case 86:
                return "85";
            case 87:
                return "86";
            case 88:
                return "C6";
            case 89:
                return "A7";
            case 90:
                return "A0";
            case 91:
                return "A1";
            case 92:
                return "A2";
            case 93:
                return "A3";
            case 94:
                return "A4";
            case 95:
                return "A5";
            case 96:
                return "A6";
            case 97:
                return "E6";
            case 98:
                return "CD";
            case 99:
                return "DC";
            case 100:
                return "FC";
            case 101:
                return "2F";
            case 102:
                return "3F";
            case 103:
                return "BF";
            case 104:
                return "B8";
            case 105:
                return "B9";
            case 106:
                return "BA";
            case 107:
                return "BB";
            case 108:
                return "BC";
            case 109:
                return "BD";
            case 110:
                return "BE";
            case 111:
                return "D4";
            case 112:
                return "C4";
            case 113:
                return "EC";
            case 114:
                return "E4";
            case 115:
                return "FE";
            case 116:
                return "F4";
            case 117:
                return "CC";
            case 118:
                return "27";
            case 119:
                return "09";
            case 120:
                return "19";
            case 121:
                return "29";
            case 122:
                return "39";
            case 123:
                return "3D";
            case 124:
                return "05";
            case 125:
                return "0D";
            case 126:
                return "15";
            case 127:
                return "1D";
            case 128:
                return "25";
            case 129:
                return "2D";
            case 130:
                return "35";
            case 131:
                return "0B";
            case 132:
                return "1B";
            case 133:
                return "2B";
            case 134:
                return "3B";
            case 135:
                return "F3";
            case 136:
                return "FB";
            case 137:
                return "76";
            case 138:
                return "DB";
            case 139:
                return "3C";
            case 140:
                return "04";
            case 141:
                return "0C";
            case 142:
                return "14";
            case 143:
                return "1C";
            case 144:
                return "24";
            case 145:
                return "2C";
            case 146:
                return "34";
            case 147:
                return "03";
            case 148:
                return "13";
            case 149:
                return "23";
            case 150:
                return "33";
            case 151:
                return "DA";
            case 152:
                return "C3";
            case 153:
                return "FA";
            case 154:
                return "D2";
            case 155:
                return "C2";
            case 156:
                return "E2";
            case 157:
                return "EA";
            case 158:
                return "F2";
            case 159:
                return "CA";
            case 160:
                return "3A";
            case 161:
                return "0A";
            case 162:
                return "1A";
            case 163:
                return "2A";
            case 164:
                return "01";
            case 165:
                return "11";
            case 166:
                return "21";
            case 167:
                return "31";
            case 168:
                return "00";
            case 169:
                return "B7";
            case 170:
                return "B0";
            case 171:
                return "B1";
            case 172:
                return "B2";
            case 173:
                return "B3";
            case 174:
                return "B4";
            case 175:
                return "B5";
            case 176:
                return "B6";
            case 177:
                return "F6";
            case 178:
                return "D3";
            case 179:
                return "E9";
            case 180:
                return "C1";
            case 181:
                return "D1";
            case 182:
                return "E1";
            case 183:
                return "F1";
            case 184:
                return "C5";
            case 185:
                return "D5";
            case 186:
                return "E5";
            case 187:
                return "F5";
            case 188:
                return "17";
            case 189:
                return "1F";
            case 190:
                return "D8";
            case 191:
                return "C9";
            case 192:
                return "20";
            case 193:
                return "07";
            case 194:
                return "F8";
            case 195:
                return "D0";
            case 196:
                return "C0";
            case 197:
                return "F0";
            case 198:
                return "E8";
            case 199:
                return "E0";
            case 200:
                return "0F";
            case 201:
                return "C7";
            case 202:
                return "CF";
            case 203:
                return "D7";
            case 204:
                return "DF";
            case 205:
                return "E7";
            case 206:
                return "EF";
            case 207:
                return "F7";
            case 208:
                return "FF";
            case 209:
                return "C8";
            case 210:
                return "9F";
            case 211:
                return "98";
            case 212:
                return "99";
            case 213:
                return "9A";
            case 214:
                return "9B";
            case 215:
                return "9C";
            case 216:
                return "9D";
            case 217:
                return "9E";
            case 218:
                return "DE";
            case 219:
                return "22";
            case 220:
                return "30";
            case 221:
                return "F9";
            case 222:
                return "32";
            case 223:
                return "02";
            case 224:
                return "12";
            case 225:
                return "37";
            case 226:
                return "97";
            case 227:
                return "90";
            case 228:
                return "91";
            case 229:
                return "92";
            case 230:
                return "93";
            case 231:
                return "94";
            case 232:
                return "95";
            case 233:
                return "96";
            case 234:
                return "D6";
            case 235:
                return "EB";
            case 236:
                return "AF";
            case 237:
                return "A8";
            case 238:
                return "A9";
            case 239:
                return "AA";
            case 240:
                return "AB";
            case 241:
                return "AC";
            case 242:
                return "AD";
            case 243:
                return "AE";
            case 244:
                return "EE";
            case 245:
                return "E3";

            default:
                return "Not Found at " + x;

        }
    }

    private Integer getOpcodeLength(String s) {
        switch (s) {
            case "8F":
            case "88":
            case "89":
            case "8A":
            case "8B":
            case "8C":
            case "8D":
            case "8E":
            case "87":
            case "80":
            case "81":
            case "82":
            case "83":
            case "84":
            case "85":
            case "86":
            case "A7":
            case "A0":
            case "A1":
            case "A2":
            case "A3":
            case "A4":
            case "A5":
            case "A6":
            case "2F":
            case "3F":
            case "BF":
            case "B8":
            case "B9":
            case "BA":
            case "BB":
            case "BC":
            case "BD":
            case "BE":
            case "27":
            case "09":
            case "19":
            case "29":
            case "39":
            case "3D":
            case "05":
            case "0D":
            case "15":
            case "1D":
            case "25":
            case "2D":
            case "35":
            case "0B":
            case "1B":
            case "2B":
            case "3B":
            case "F3":
            case "FB":
            case "76":
            case "3C":
            case "04":
            case "0C":
            case "14":
            case "1C":
            case "24":
            case "2C":
            case "34":
            case "03":
            case "13":
            case "23":
            case "33":
            case "0A":
            case "1A":
            case "7F":
            case "78":
            case "79":
            case "7A":
            case "7B":
            case "7C":
            case "7D":
            case "7E":
            case "47":
            case "40":
            case "41":
            case "42":
            case "43":
            case "44":
            case "45":
            case "46":
            case "4F":
            case "48":
            case "49":
            case "4A":
            case "4B":
            case "4C":
            case "4D":
            case "4E":
            case "57":
            case "50":
            case "51":
            case "52":
            case "53":
            case "54":
            case "55":
            case "56":
            case "5F":
            case "58":
            case "59":
            case "5A":
            case "5B":
            case "5C":
            case "5D":
            case "5E":
            case "67":
            case "60":
            case "61":
            case "62":
            case "63":
            case "64":
            case "65":
            case "66":
            case "6F":
            case "68":
            case "69":
            case "6A":
            case "6B":
            case "6C":
            case "6D":
            case "6E":
            case "77":
            case "70":
            case "71":
            case "72":
            case "73":
            case "74":
            case "75":
            case "00":
            case "B7":
            case "B0":
            case "B1":
            case "B2":
            case "B3":
            case "B4":
            case "B5":
            case "B6":
            case "E9":
            case "C1":
            case "D1":
            case "E1":
            case "F1":
            case "C5":
            case "D5":
            case "E5":
            case "F5":
            case "17":
            case "1F":
            case "D8":
            case "C9":
            case "20":
            case "07":
            case "F8":
            case "D0":
            case "F0":
            case "E8":
            case "E0":
            case "0F":
            case "C7":
            case "CF":
            case "D7":
            case "DF":
            case "E7":
            case "EF":
            case "F7":
            case "FF":
            case "C8":
            case "9F":
            case "98":
            case "99":
            case "9A":
            case "9B":
            case "9C":
            case "9D":
            case "9E":
            case "30":
            case "F9":
            case "02":
            case "12":
            case "37":
            case "97":
            case "90":
            case "91":
            case "92":
            case "93":
            case "94":
            case "95":
            case "96":
            case "EB":
            case "AF":
            case "A8":
            case "A9":
            case "AA":
            case "AB":
            case "AC":
            case "AD":
            case "AE":
            case "E3":
                return 1;
            case "CE":
            case "C6":
            case "E6":
            case "FE":
            case "DB":
            case "3E":
            case "06":
            case "0E":
            case "16":
            case "1E":
            case "26":
            case "2E":
            case "36":
            case "F6":
            case "D3":
            case "DE":
            case "D6":
            case "EE":
                return 2;
            case "CD":
            case "DC":
            case "FC":
            case "D4":
            case "C4":
            case "F4":
            case "EC":
            case "E4":
            case "CC":
            case "DA":
            case "FA":
            case "C3":
            case "D2":
            case "C2":
            case "F2":
            case "EA":
            case "E2":
            case "CA":
            case "3A":
            case "2A":
            case "01":
            case "11":
            case "21":
            case "31":
            case "22":
            case "32":
                return 3;
            default:
                throw new RuntimeException("Invalid opcode");
        }
    }

    public String extractLabel(String lineInstruction) {
        Pattern instructionPattern = Pattern.compile(LABEL_REGEX);
        Matcher m1  = instructionPattern.matcher(lineInstruction);
        if(m1.find())
            return m1.group(1);
        else
            return "";
    }

    public IData extractData(String x) {
        Pattern instructionPattern = Pattern.compile(OPERAND_DATA_REGEX);
        Matcher m1  = instructionPattern.matcher(x);
        if(m1.find()) {
            return Data.from(m1.group(2));
        } else {
            return null;
        }
    }

    public String extractOperandLabel(IOpcode opcode) {
        String x = opcode.getInstruction();
        Pattern instructionPattern = Pattern.compile(OPERAND_LABEL_REGEX);
        Matcher m1  = instructionPattern.matcher(x);
        if(m1.find()) {
            return m1.group(1);
        } else {
            return "";
        }
    }

    public IAddress extractOperandAddress(String x) {
        Pattern instructionPattern = Pattern.compile(OPERAND_ADDRESS_REGEX);
        Matcher m1  = instructionPattern.matcher(x);
        if(m1.find()) {
            return Address.from(m1.group(1));
        } else {
            return null;
        }
    }
}
