package com.amanvishnani.sim8085.domain;

public interface IMemoryRow {
    IAddress getAddress();

    IData getData();

    void setData(IData data);

    void setAddress(IAddress address);
}
