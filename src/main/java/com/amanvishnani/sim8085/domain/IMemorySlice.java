/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.amanvishnani.sim8085.domain;

/**
 *
 * @author aman6
 */
/**
 * Interface representing a slice (subset) of memory.
 */
public interface IMemorySlice {
    /**
     * Returns an array of hex strings representing the data in this slice.
     * 
     * @return Array of hex strings.
     */
    String[] getHexDataArray();
}
