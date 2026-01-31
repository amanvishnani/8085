package com.amanvishnani.sim8085.domain;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import com.amanvishnani.sim8085.domain.Impl.Flags;
import com.amanvishnani.sim8085.domain.Impl.InstructionExecuted;
import com.amanvishnani.sim8085.domain.Impl.InstructionRow;

import java.util.ArrayList;
import java.util.function.Consumer;

/**
 * Main interface for the 8085 microprocessor simulator.
 * Provides access to registers, flags, memory, and execution control.
 */
public interface I8085 {
    /**
     * Increments the Instruction Pointer (IP) to the next instruction.
     */
    void nextInstructionPointer();

    /** @return Internal register A (Accumulator). */
    IRegister getA();

    /** @return Internal register B. */
    IRegister getB();

    /** @return Internal register C. */
    IRegister getC();

    /** @return Internal register D. */
    IRegister getD();

    /** @return Internal register E. */
    IRegister getE();

    /** @return Internal register H. */
    IRegister getH();

    /** @return Internal register L. */
    IRegister getL();

    /** @return Data pointed to by the HL register pair (Memory M). */
    IData getM();

    /** @return The flags register. */
    Flags getFlags();

    /** @param x Data to set in register A. */
    void setA(IData x);

    /** @param x Data to set in register B. */
    void setB(IData x);

    /** @param x Data to set in register C. */
    void setC(IData x);

    /** @param x Data to set in register D. */
    void setD(IData x);

    /** @param x Data to set in register E. */
    void setE(IData x);

    /** @param x Data to set in register H. */
    void setH(IData x);

    /** @param x Data to set in register L. */
    void setL(IData x);

    /** @param x Data to set in memory location pointed by HL. */
    void setM(IData x);

    /**
     * Executes the instruction specified by the opcode string.
     * 
     * @param op Hex string representing the opcode.
     */
    void execute(String op);

    /** @param x Address to set in the Stack Pointer (SP). */
    void setSP(IAddress x);

    /** @param x Address to set in the Instruction Pointer (IP). */
    void setIP(IAddress x);

    /** @return The current Stack Pointer (SP) address. */
    IAddress getSP();

    /** @return The current Instruction Pointer (IP) address. */
    IAddress getIP();

    /** @param x Set Sign flag (0 or 1). */
    void setS(Integer x);

    /** @param x Set Zero flag (0 or 1). */
    void setZ(Integer x);

    /** @param x Set Auxiliary Carry flag (0 or 1). */
    void setAc(Integer x);

    /** @param x Set Parity flag (0 or 1). */
    void setP(Integer x);

    /** @param x Set Carry flag (0 or 1). */
    void setCy(Integer x);

    /** @return Sign flag value. */
    Integer getS();

    /** @return Zero flag value. */
    Integer getZ();

    /** @return Parity flag value. */
    Integer getP();

    /** @return Carry flag value. */
    Integer getCy();

    /** @return Auxiliary Carry flag value. */
    Integer getAc();

    /**
     * Gets data from memory at the specified integer index.
     * 
     * @param i Memory index.
     * @return Data at the specified index.
     */
    IData getData(Integer i);

    /**
     * Gets data from memory at the specified address.
     * 
     * @param i Memory address.
     * @return Data at the specified address.
     */
    IData getData(IAddress i);

    /**
     * Sets data in memory at the specified address.
     * 
     * @param i    Memory address.
     * @param data Data to set.
     */
    void setData(IAddress i, IData data);

    /**
     * Sets data in memory at the specified integer index using hex string.
     * 
     * @param i    Memory index.
     * @param data Hex string representation of data.
     */
    void setData(Integer i, String data);

    /** Initializes the simulator state (registers, flags, memory). */
    void initialize();

    /** @return Data at the current Instruction Pointer (IP). */
    IData getDataAtIP();

    /** @param flags Flags to update. */
    void updateFlags(IFlags flags);

    /** Decrements the Stack Pointer (SP). */
    void decrementSP();

    /** Increments the Stack Pointer (SP). */
    void incrementSP();

    /**
     * Compiles 8085 assembly code into machine code rows.
     * 
     * @param code Assembly code string.
     * @return List of instruction rows representing compiled code.
     */
    ArrayList<InstructionRow> compile(String code);

    /**
     * Registers a callback for when an instruction is executed.
     * 
     * @param callback Consumer of InstructionExecuted event.
     */
    void onInstructionExecuted(Consumer<InstructionExecuted> callback);

    /**
     * Registers a callback for when an error occurs during execution.
     * 
     * @param callback Consumer of RuntimeException.
     */
    void onError(Consumer<RuntimeException> callback);
}
