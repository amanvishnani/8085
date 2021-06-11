package com.amanvishnani.sim8085.domain.Impl;

import com.amanvishnani.sim8085.domain.IData;
import com.amanvishnani.sim8085.domain.IFlags;
import com.amanvishnani.sim8085.domain.IOperationResult;

public class OperationResult implements IOperationResult {

    private IData data;
    private IFlags flags;

    public static IOperationResult from(IData data, Flags flags) {
        OperationResult operationResult = new OperationResult();
        operationResult.setData(data);
        operationResult.setFlags(flags);
        return operationResult;
    }

    @Override
    public IData getData() {
        return data;
    }

    @Override
    public IFlags getFlags() {
        return flags;
    }

    public void setData(IData data) {
        this.data = data;
    }

    public void setFlags(IFlags flags) {
        this.flags = flags;
    }
}
