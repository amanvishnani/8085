package com.amanvishnani.sim8085.domain;

/**
 * Interface representing a generic instruction in the simulator.
 */
public interface Instruction {
    /** @return The hex opcode string. */
    String getOpcode();

    /** @return The label associated with this instruction, if any. */
    String getLabel();

    /** @param s The opcode to set. */
    void setOpcode(String s);

    /** @param s The label to set. */
    void setLabel(String s);
}
