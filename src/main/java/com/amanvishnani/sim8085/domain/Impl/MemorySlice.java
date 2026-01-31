/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.amanvishnani.sim8085.domain.Impl;

import com.amanvishnani.sim8085.domain.IMemory;
import com.amanvishnani.sim8085.domain.IMemorySlice;

/**
 * Implementation of a memory slice.
 */
public class MemorySlice implements IMemorySlice {

    Integer start, end;
    String[] data;
    IMemory memoryRef;

    private MemorySlice(IMemory memoryRef, Integer start, Integer end) {
        this.memoryRef = memoryRef;
        this.start = start;
        this.end = end;
    }

    @Override
    public String[] getHexDataArray() {
        if (data == null) {
            makeDataCopy();
        }
        return data;
    }

    private void makeDataCopy() {
        data = new String[end - start - 1];
        for (int i = start; i < end - 1; i++) {
            data[i] = memoryRef.getHexData(i);
        }
    }

    /**
     * Creates a MemorySlice for the given range.
     * 
     * @param memeory Reference to memory.
     * @param start   Start address.
     * @param end     End address.
     * @return A new MemorySlice instance.
     */
    public static MemorySlice from(IMemory memeory, Integer start, Integer end) {
        return new MemorySlice(memeory, start, end);
    }

}
