/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.amanvishnani.sim8085.domain.Impl;

import com.amanvishnani.sim8085.domain.*;

/**
 *
 * @author Aman Vishnani
 */
public class Memory implements IMemory {
    public IMemoryRow[] memory;

    private void initMemory() {
        for (int i = 0; i < 65536; i++) {
            memory[i] = MemoryRow.from(i);
        }
    }

    private Memory() {
        memory = new IMemoryRow[65536];
        initMemory();
    }

    public static IMemory makeMemory() {
        return new Memory();
    }

    @Override
    public IData getData(Integer intAddress) {
        return memory[intAddress].getData();
    }

    @Override
    public String getHexData(Integer intAddress) {
        return getData(intAddress).hexValue();
    }

    @Override
    public String getHexData(String hexAddress) {
        IAddress address = Address.from(hexAddress);
        return getHexData(address.intValue());
    }

    @Override
    public void setData(IAddress address, IData data) {
        memory[address.intValue()].setData(data);
    }

    @Override
    public void setData(IAddress address, String data) {
        IData hexData = Data.from(data);
        memory[address.intValue()].setData(hexData);
    }

    @Override
    public void setData(String hexAddress, String data) {
        IAddress address = Address.from(hexAddress);
        setData(address, data);
    }

    @Override
    public void setData(Integer intAddress, String data) {
        IAddress address = Address.from(intAddress);
        setData(address.hexValue(), data);
    }

    @Override
    public IMemorySlice getSlice(Integer start, Integer end) {
        return MemorySlice.from(this, start, end);
    }
}
