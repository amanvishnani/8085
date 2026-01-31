package com.amanvishnani.sim8085.domain.Impl;

import com.amanvishnani.sim8085.domain.IAddress;
import com.amanvishnani.sim8085.domain.IData;
import com.amanvishnani.sim8085.domain.IMemoryRow;

public class MemoryRow implements IMemoryRow {
    private IData data;
    private IAddress address;

    private MemoryRow(IAddress address) {
        this.address = address;
        this.data = IData.ZERO;
    }

    public static MemoryRow from(Integer address) {
        return new MemoryRow(Address.from(address));
    }

    @Override
    public void setData(IData data) {
        this.data = data;
    }

    @Override
    public IAddress getAddress() {
        return address;
    }

    @Override
    public IData getData() {
        return data;
    }

    @Override
    public void setAddress(IAddress address) {
        this.address = address;
    }
}
