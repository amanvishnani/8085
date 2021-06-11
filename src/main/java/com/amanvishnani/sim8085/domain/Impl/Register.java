package com.amanvishnani.sim8085.domain.Impl;

import com.amanvishnani.sim8085.domain.IData;
import com.amanvishnani.sim8085.domain.IRegister;

public class Register extends Data implements IRegister {

    protected Register(String dataValue) {
        super(dataValue);
    }

    public static IRegister makeRegister() {
        return new Register("00");
    }

    public void update(IData data) {
        setDataValue(data.hexValue());
    }
}
