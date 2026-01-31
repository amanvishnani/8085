package com.amanvishnani.sim8085.domain;

/**
 * Interface representing a CPU opcode and its associated instruction.
 */
public interface IOpcode {
    /** @return The assembly instruction string (e.g., "MOV A, B"). */
    String getInstruction();

    /** @return The machine code data for this opcode. */
    IData getOpcodeData();

    /** @return The length of the opcode in bytes (1, 2, or 3). */
    Integer getOpcodeLength();
}
