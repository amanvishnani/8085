/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.amanvishnani.sim8085.domain;

/**
 *
 * @author Aman Vishnani
 */
/**
 * Interface representing the memory of the 8085 simulator.
 */
public interface IMemory {
    /**
     * Gets data from memory at the specified integer address.
     * 
     * @param intAddress Integer address.
     * @return Data at the address.
     */
    IData getData(Integer intAddress);

    /**
     * Gets data at the specified address as a hex string.
     * 
     * @param intAddress Integer address.
     * @return Hex string data at the address.
     */
    String getHexData(Integer intAddress);

    /**
     * Sets data at the specified hex address.
     * 
     * @param hexAddress Hex address.
     * @param data       Hex data to set.
     */
    void setData(String hexAddress, String data);

    /**
     * Sets data at the specified integer address.
     * 
     * @param intAddress Integer address.
     * @param data       Hex data to set.
     */
    void setData(Integer intAddress, String data);

    /**
     * Sets data at the specified address object.
     * 
     * @param address Address object.
     * @param data    Hex data to set.
     */
    void setData(IAddress address, String data);

    /**
     * Sets data at the specified address object.
     * 
     * @param address Address object.
     * @param data    Data object to set.
     */
    void setData(IAddress address, IData data);

    /**
     * Returns a slice of memory between the given range.
     * 
     * @param start Start index.
     * @param end   End index.
     * @return A slice of memory.
     */
    IMemorySlice getSlice(Integer start, Integer end);

    /**
     * Gets data at the specified hex address.
     * 
     * @param hexAddress Hex address.
     * @return Hex string data at the address.
     */
    String getHexData(String hexAddress);
}
