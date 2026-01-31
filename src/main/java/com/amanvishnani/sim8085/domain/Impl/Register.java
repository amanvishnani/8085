package com.amanvishnani.sim8085.domain.Impl;

import com.amanvishnani.sim8085.domain.IData;
import com.amanvishnani.sim8085.domain.IRegister;

/**
 * Implementation of a CPU register.
 */
public class Register extends Data implements IRegister {

    protected Register(String dataValue) {
        super(dataValue);
    }

    /**
     * Factory method to create a new register initialized to 00.
     * 
     * @return A new IRegister instance.
     */
    public static IRegister makeRegister() {
        return new Register("00");
    }

    public void update(IData data) {
        setDataValue(data.hexValue());
    }
}
