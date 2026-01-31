package com.amanvishnani.sim8085.domain.Impl;

import com.amanvishnani.sim8085.domain.Instruction;

/**
 * Implementation of a simulator instruction.
 */
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

    /**
     * Factory method to create an InstructionImpl with an opcode.
     * 
     * @param opcode The hex opcode string.
     * @return A new InstructionImpl instance.
     */
    public static InstructionImpl createInstructionImpl(String opcode) {
        return new InstructionImpl(opcode);
    }

    /**
     * Factory method to create an InstructionImpl with an opcode and a label.
     * 
     * @param opcode The hex opcode string.
     * @param label  The label string.
     * @return A new InstructionImpl instance.
     */
    public static InstructionImpl createInstructionImpl(String opcode, String label) {
        return new InstructionImpl(opcode, label);
    }

    /**
     * Factory method to create an empty InstructionImpl.
     * 
     * @return A new InstructionImpl instance.
     */
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
