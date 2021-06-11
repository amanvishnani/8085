/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.amanvishnani.sim8085.domain.Impl;

import com.amanvishnani.sim8085.Util;
import com.amanvishnani.sim8085.domain.IAddress;
import com.amanvishnani.sim8085.domain.IData;

/**
 *
 * @author Aman Vishnani
 */
public class Address implements IAddress{
    
    private final IData LSB;
    private final IData MSB;
    private final String address;
    
    private IData data = Data.from("00");

    private Address(String MSB, String LSB) {
        this.LSB = Data.from(LSB);
        this.MSB = Data.from(MSB);
        this.address = MSB + LSB;
    }

    @Override
    public IData getData() {
        return data;
    }

    @Override
    public void setData(IData data) {
        this.data = data;
    }
    
    @Override
    public String hexValue() {
        return address;
    }

    @Override
    public Integer intValue() {
        return Integer.parseInt(address, 16);
    }

    @Override
    public IData getLSB() {
        return LSB;
    }

    @Override
    public IData getMSB() {
        return MSB;
    }
    
    public static IAddress from(IData msb, IData lsb) {
        return new Address(msb.hexValue(), lsb.hexValue());
    }
    
    public static IAddress from(IData msb, IData lsb, IData data) {
        IAddress address = Address.from(msb, lsb);
        address.setData(data);
        return address;
    }
    
    public static IAddress from(Integer intAddress) {
        String hexAddress = Integer.toHexString(intAddress);
        return Address.from(hexAddress);
    }
    
    public static IAddress from(String hexAddress) {
        return Address.from(hexAddress, Data.from("00"));
    }
    
    public static IAddress from(String hexAddress, IData data) {
        hexAddress = hexAddress.toUpperCase();
        hexAddress = Util.padThreeZeros(hexAddress);
        IData msb = Data.from(hexAddress.substring(0, 2));
        IData lsb = Data.from(hexAddress.substring(2, 4));
        return Address.from(msb, lsb, data);
    }
    
}
