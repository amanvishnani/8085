/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.amanvishnani.sim8085.domain.Impl;

import com.amanvishnani.sim8085.domain.IData;

/**
 *
 * @author Aman Vishnani
 */
public class Data implements IData {
    
    String dataValue;

    private Data(String dataValue) {
        this.dataValue = dataValue;
    }
    
    @Override
    public String hexValue() {
        return dataValue;
    }

    @Override
    public Integer intValue() {
        return Integer.parseInt(dataValue, 16);
    }

    @Override
    public Integer add() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Integer minus() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public static IData from(String value) {
        return new Data(value);
    }
    
}
