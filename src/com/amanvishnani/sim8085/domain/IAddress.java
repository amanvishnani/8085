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
public interface IAddress {
    String hexValue();
    Integer intValue();
    IData getLSB();
    IData getMSB();
    IData getData();
    void setData(IData data);
}
