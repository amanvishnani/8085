package com.amanvishnani.sim8085.domain.Impl;

import com.amanvishnani.sim8085.domain.Instruction;

import java.util.Optional;

public class InstructionImpl implements Instruction {
    private String opcode;
    private String label;

    @Override
    public void setOpcode(String opcode) {
        this.opcode = opcode;
    }

    @Override
    public void setLabel(String label) {
        this.label = label;
    }

    private InstructionImpl() {
        this.opcode = "";
        this.label = "";
    }

    private InstructionImpl(String opcode) {
        this.opcode = opcode;
    }

    private InstructionImpl(String opcode, String label) {
        this.opcode = opcode;
        this.label = label;
    }

    public static InstructionImpl createInstructionImpl(String opcode) {
        return new InstructionImpl(opcode);
    }

    public static InstructionImpl createInstructionImpl(String opcode, String label) {
        return new InstructionImpl(opcode, label);
    }

    public static InstructionImpl newInstructionImpl() {
        return new InstructionImpl();
    }

    @Override
    public String getOpcode() {
        return opcode;
    }

    @Override
    public String getLabel() {
        return label;
    }
}
