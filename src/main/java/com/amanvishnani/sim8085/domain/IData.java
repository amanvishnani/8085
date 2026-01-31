/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.amanvishnani.sim8085.domain;

import com.amanvishnani.sim8085.domain.Impl.Data;

/**
 * Represents an 8-bit data byte in the 8085 simulator.
 */
public interface IData {
    /** Constant representing zero data (00). */
    IData ZERO = Data.from("00");

    /**
     * Returns the 2-digit hexadecimal representation of the data.
     * 
     * @return Hex string (e.g., "00", "FF").
     */
    String hexValue();

    /**
     * Returns the integer representation of the data.
     * 
     * @return Data value as an integer (0 to 255).
     */
    Integer intValue();

    /**
     * Adds another data byte to this one.
     * 
     * @param data The data to add.
     * @return IOperationResult containing the result and flags.
     */
    IOperationResult add(IData data);

    /**
     * Adds another data byte and a carry bit to this one.
     * 
     * @param data  The data to add.
     * @param carry The carry bit (0 or 1).
     * @return IOperationResult containing the result and flags.
     */
    IOperationResult add(IData data, Integer carry);

    /**
     * Subtracts another data byte from this one.
     * 
     * @param data The data to subtract.
     * @return IOperationResult containing the result and flags.
     */
    IOperationResult minus(IData data);

    /**
     * Converts this data to a register format.
     * 
     * @return IRegister representation of this data.
     */
    IRegister toRegister();

    /**
     * Returns the LSB of this data (relevant for word structures).
     * 
     * @return IData representing the LSB.
     */
    public IData getLSB();
}
