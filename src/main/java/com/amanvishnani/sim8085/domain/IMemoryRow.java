package com.amanvishnani.sim8085.domain;

public interface IMemoryRow {
    IAddress getAddress();
    IData geData();
    void setData(IData data);
    void setAddress(IAddress address);
}
