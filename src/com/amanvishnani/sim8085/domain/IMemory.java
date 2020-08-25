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
public interface IMemory {
    IData getData(Integer intAddress);
    IAddress getAddress(Integer intAddress);
    String getHexData(Integer intAddress);
    String getHexData(String hexAddress);
    void setData(String hexAddress, String data);
    void setData(Integer intAddress, String data);
    void setData(IAddress address, String data);
    void setData(IAddress address, IData data);
    IMemorySlice getSlice(Integer start, Integer end);
}
