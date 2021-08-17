/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.amanvishnani.sim8085.domain.Impl;

import com.amanvishnani.sim8085.domain.Flag;
import com.amanvishnani.sim8085.domain.IData;
import com.amanvishnani.sim8085.domain.IOperationResult;
import com.amanvishnani.sim8085.domain.IRegister;

import java.util.Objects;

/**
 *
 * @author Aman Vishnani
 */
public class Data implements IData {

    String dataValue;

    public Data(String dataValue) {
        this.setDataValue(dataValue);
    }
    
    @Override
    public String hexValue() {
        return getDataValue();
    }

    @Override
    public Integer intValue() {
        return Integer.parseInt(dataValue, 16);
    }

    @Override
    public IOperationResult add(IData data) {
        return this.add(data, 0);
    }

    // @TODO: Unit test this
    @Override
    public IData getLSB() {
        String s = this.getDataValue();
        return Data.from(s.substring(s.length() - 2));
    }

    @Override
    public IOperationResult add(IData data, Integer carry) {
        Flags flags = Flags.newInstance();
        int r1, r2, r3;
        r1 = this.intValue();
        r2 = data.intValue();
        r3 = r1 + r2 + carry;
        if (r3 > 255) {
            flags.setFlag(Flag.Cy, 1);
        }
        if (this.getLSB().intValue() + data.getLSB().intValue() + carry > 15) {
            flags.setFlag(Flag.Ac, 1);
        }
        Data temp = Data.from(r1 + r2 + carry);
        flags.setFlag(Flag.P, temp.getParity());
        return OperationResult.from(temp, flags);
    }

    @Override
    public IOperationResult minus(IData data) {
        Flags flags = Flags.newInstance();
        int r1, r2, r3;
        r1 = this.intValue();
        r2 = data.intValue();
        r3 = r1 - r2;
        if (r3 < 0) {
            r3 = Math.abs(r3);
            flags.setFlag(Flag.S, 1);
        }
        if (r3 == 0) {
            flags.setFlag(Flag.Z, 1);
        }
        String temp = Integer.toHexString(r3);
        Data result = Data.from(temp);
        flags.setFlag(Flag.P, result.getParity());
        return OperationResult.from(result, flags);
    }

    @Override
    public IRegister toRegister() {
        return new Register(this.dataValue);
    }

    @Override
    public String toString() {
        return "Data{" +
                "dataValue='" + dataValue + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Data data = (Data) o;
        return dataValue.equals(data.dataValue);
    }

    @Override
    public int hashCode() {
        return Objects.hash(dataValue);
    }

    public static Data from(String value) {
        return new Data(value);
    }

    public static Data from(int value) {
        String temp = Integer.toHexString(value);
        temp = temp.substring((temp.length() - 2));
        return new Data(temp);
    }

    private String padOneZero(String hexData) {
        if (hexData.length() == 1) {
            hexData = "0" + hexData;
        }
        return hexData;
    }

    public String getDataValue() {
        return dataValue;
    }

    public void setDataValue(String dataValue) {
        this.dataValue = padOneZero(dataValue);
    }

    private int getParity() {
        int x, counter = 0;
        x = this.intValue();
        String s = Integer.toBinaryString(x);
        char[] c = s.toCharArray();
        for (int i = 0; i < s.length(); i++) {
            if (c[i] == '1') {
                counter++;
            }
        }
        if (counter % 2 == 0) {
            return 1;
        } else {
            return 0;
        }
    }
}
