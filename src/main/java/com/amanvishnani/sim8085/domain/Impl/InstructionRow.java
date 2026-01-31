package com.amanvishnani.sim8085.domain.Impl;

import com.amanvishnani.sim8085.domain.IAddress;
import com.amanvishnani.sim8085.domain.IData;

import java.util.Objects;

/**
 * Represents a single row in the compiled machine code.
 * Maps an address to its opcode data and the original assembly instruction.
 */
public class InstructionRow {
    private String label;
    private String instruction;
    private IData data;
    private IAddress address;
    private boolean hasLabelOperand;
    private String labelOperand;

    private InstructionRow(Integer address) {
        this.address = Address.from(address);
        setLabel("");
        setInstruction("");
    }

    /**
     * Factory method to create an InstructionRow at a given address.
     * 
     * @param address The memory address for this row.
     * @return A new InstructionRow instance.
     */
    public static InstructionRow createInstructionRow(Integer address) {
        return new InstructionRow(address);
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getInstruction() {
        return instruction;
    }

    public void setInstruction(String instruction) {
        this.instruction = instruction;
    }

    public IData getData() {
        return data;
    }

    public void setData(IData data) {
        this.data = data;
    }

    public IAddress getAddress() {
        return address;
    }

    public void setAddress(IAddress address) {
        this.address = address;
    }

    public void setHasOperandLabel(boolean hasLabelOperand) {
        this.hasLabelOperand = hasLabelOperand;
    }

    public boolean getHasLabelOperand() {
        return hasLabelOperand;
    }

    public void setOperandLabel(String labelOperand) {
        this.labelOperand = labelOperand;
    }

    public String getLabelOperand() {
        return labelOperand;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        InstructionRow that = (InstructionRow) o;
        return Objects.equals(label, that.label) && Objects.equals(instruction, that.instruction)
                && Objects.equals(data, that.data) && Objects.equals(address, that.address);
    }

    @Override
    public int hashCode() {
        return Objects.hash(label, instruction, data, address);
    }

    @Override
    public String toString() {
        return "InstructionRow{" +
                "label='" + label + '\'' +
                ", instruction='" + instruction + '\'' +
                ", data=" + data +
                ", address=" + address +
                ", hasLabelOperand=" + hasLabelOperand +
                ", labelOperand='" + labelOperand + '\'' +
                '}';
    }
}
