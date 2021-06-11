/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.amanvishnani.sim8085.domain;

import com.amanvishnani.sim8085.domain.Impl.Data;
import com.amanvishnani.sim8085.domain.Impl.Flags;
import com.amanvishnani.sim8085.domain.Impl.OperationResult;

/**
 *
 * @author Aman Vishnani
 */
public interface IData {
    IData ZERO = Data.from("00");
    String hexValue();
    Integer intValue();
    IOperationResult add(IData data);
    IOperationResult add(IData data, Integer carry);
    IOperationResult minus(IData data);
    IRegister toRegister();
    public IData getLSB();
}
