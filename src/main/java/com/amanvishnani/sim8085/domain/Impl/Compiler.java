package com.amanvishnani.sim8085.domain.Impl;

import com.amanvishnani.sim8085.domain.IAddress;
import com.amanvishnani.sim8085.domain.IData;
import com.amanvishnani.sim8085.domain.IOpcode;

import java.util.*;

public class Compiler {

    private final Parser parser;
    Map<Integer, InstructionRow> instructionRowMap;
    Map<String, Integer> labelMap;

    public Compiler(Parser parser) {
        this.parser = parser;
        this.resetParams();
    }

    public Compiler() {
        this(new Parser());
    }

    public ArrayList<InstructionRow> compile(String code) {
        this.resetParams();
        List<String> instructions = parser.getLineInstructions(code);
        this.passOne(instructions);
        this.passTwo();
        ArrayList<InstructionRow> rows = new ArrayList<>(instructionRowMap.values());
        rows.sort(new InstructionSortByAddress());
        return rows;
    }

    private void resetParams() {
        instructionRowMap = new HashMap<>();
        labelMap = new HashMap<>();
    }

    private void passOne(List<String> instructions) {
        int localPointer = 0;
        for (String lineInstruction :
                instructions) {

            // Get Opcode
            IOpcode opcode = parser.getOpcode(lineInstruction);

            // extract label
            String label = parser.extractLabel(lineInstruction);
            if(!label.isEmpty()) {
                labelMap.put(label, localPointer);
            }

            InstructionRow row = InstructionRow.createInstructionRow(localPointer);
            row.setData(opcode.getOpcodeData());
            row.setLabel(label);
            row.setInstruction(opcode.getInstruction());
            instructionRowMap.put(localPointer, row);
            localPointer = localPointer + 1;

            // Opcode length = 2
            if (opcode.getOpcodeLength() == 2) {
                // Data flow
                IData data = parser.extractData(lineInstruction);
                row = InstructionRow.createInstructionRow(localPointer);
                row.setData(data);
                instructionRowMap.put(localPointer, row);
                localPointer = localPointer + 1;
            }
            // Opcode length = 3
            else if (opcode.getOpcodeLength() == 3) {
                IAddress address = parser.extractOperandAddress(opcode.getInstruction());
                // if operand has Address
                if (address != null) {
                    // Address flow
                    // Set MSB
                    row = InstructionRow.createInstructionRow(localPointer);
                    row.setData(address.getLSB());
                    instructionRowMap.put(localPointer, row);
                    localPointer = localPointer + 1;
                    // Set LSB
                    row = InstructionRow.createInstructionRow(localPointer);
                    row.setData(address.getMSB());
                    instructionRowMap.put(localPointer, row);
                    localPointer = localPointer + 1;
                } else {
                    // if operand has Label
                    // Label flow
                    String operandLabel = parser.extractOperandLabel(opcode);
                    row.setHasOperandLabel(true);
                    row.setOperandLabel(operandLabel);
                    localPointer = localPointer + 2;
                }
            }
        }
    }

    private void passTwo() {
        var instructionsToAdd = new ArrayList<InstructionRow>();
        for (Map.Entry<Integer, InstructionRow> entry :
                instructionRowMap.entrySet()) {
            Integer addr = entry.getKey();
            InstructionRow row = entry.getValue();
            if (row.getHasLabelOperand()) {
                String label = row.getLabelOperand();
                Integer labelAddr = this.labelMap.get(label);
                IAddress labelAddress = Address.from(labelAddr);
                InstructionRow row1 = InstructionRow.createInstructionRow(addr+1);
                InstructionRow row2 = InstructionRow.createInstructionRow(addr+2);
                row1.setData(labelAddress.getLSB());
                instructionsToAdd.add(row1);
                row2.setData(labelAddress.getMSB());
                instructionsToAdd.add(row2);
            }
        }
        for (var i:
             instructionsToAdd) {
            instructionRowMap.put(i.getAddress().intValue(), i);
        }
    }
}

class InstructionSortByAddress implements Comparator<InstructionRow> {

    @Override
    public int compare(InstructionRow instructionRow, InstructionRow t1) {
        return instructionRow.getAddress().intValue() - t1.getAddress().intValue();
    }
}