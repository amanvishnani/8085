package com.amanvishnani.sim8085.domain;

/**
 * Interface representing a row in memory, typically for UI display.
 */
public interface IMemoryRow {
    /** @return The address of this memory row. */
    IAddress getAddress();

    /** @return The data byte at this memory row. */
    IData getData();

    /** @param data The data byte to set. */
    void setData(IData data);

    /** @param address The address to set. */
    void setAddress(IAddress address);
}
