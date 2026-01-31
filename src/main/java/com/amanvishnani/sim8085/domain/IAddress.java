/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.amanvishnani.sim8085.domain;

import com.amanvishnani.sim8085.domain.Impl.Address;

/**
 * Represents a 16-bit memory address in the 8085 simulator.
 * Addresses are generally represented in hexadecimal format.
 */
public interface IAddress {
    /**
     * Returns the 4-digit hexadecimal representation of the address.
     * 
     * @return Hex string (e.g., "0000", "FFFF").
     */
    String hexValue();

    /**
     * Returns the integer representation of the address.
     * 
     * @return Address value as an integer (0 to 65535).
     */
    Integer intValue();

    /**
     * Returns the Least Significant Byte (LSB) of the address.
     * 
     * @return IData representing the LSB.
     */
    IData getLSB();

    /**
     * Returns the Most Significant Byte (MSB) of the address.
     * 
     * @return IData representing the MSB.
     */
    IData getMSB();

    /** Constant representing the zero address (0000). */
    IAddress ZERO = Address.from(0);

    /** Constant representing the maximum address (FFFF). */
    IAddress FFFF = Address.from("FFFF");
}
