/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.amanvishnani.sim8085.domain.Impl;

import com.amanvishnani.sim8085.domain.IAddress;
import com.amanvishnani.sim8085.domain.IData;
import com.amanvishnani.sim8085.domain.IMemory;
import com.amanvishnani.sim8085.domain.IMemorySlice;

/**
 *
 * @author Aman Vishnani
 */
public class Memory implements IMemory {
    public IAddress[] memory;
    
    private void initMemory() {
        for(int i=0; i<65536; i++) {
            memory[i] = Address.from(i);
        } 
    }

    private Memory() {
        memory = new IAddress[65536];
        initMemory();
    }
    
    public static IMemory makeMemory() {
        return new Memory();
    }
    
    @Override
    public IAddress getAddress(Integer intAddress) {
        return memory[intAddress];
    }

    @Override
    public IData getData(Integer intAddress) {
        return getAddress(intAddress).getData();
    }

    @Override
    public String getHexData(Integer intAddress) {
        return getData(intAddress).hexValue();
    }

    @Override
    public String getHexData(String hexAddress) {
        IAddress addr = Address.from(hexAddress);
        return getHexData(addr.intValue());
    }
    
    @Override
    public void setData(IAddress address, IData data) {
        address.setData(data);
        memory[address.intValue()] = address;
    }
    
    @Override
    public void setData(IAddress address, String data) {
        IData hexData = Data.from(data);
        address.setData(hexData);
        memory[address.intValue()] = address;
    }

    @Override
    public void setData(String hexAddress, String data) {
        IAddress addr = Address.from(hexAddress);
        setData(addr, data);
    }

    @Override
    public void setData(Integer intAddress, String data) {
        IAddress addr = Address.from(intAddress);
        setData(addr.hexValue(), data);
    }

    @Override
    public IMemorySlice getSlice(Integer start, Integer end) {
        return MemorySlice.from(this, start, end);
    }
}
