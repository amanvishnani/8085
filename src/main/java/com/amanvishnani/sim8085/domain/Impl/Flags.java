package com.amanvishnani.sim8085.domain.Impl;

import com.amanvishnani.sim8085.domain.Flag;
import com.amanvishnani.sim8085.domain.IFlags;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Implementation of the CPU flags storage.
 */
public class Flags implements IFlags {
    Map<Flag, Integer> store;

    /**
     * Constructs a new Flags object, initializing the internal store.
     */
    public Flags() {
        store = new HashMap<>();
    }

    /** @return A new instance of Flags. */
    public static Flags newInstance() {
        return new Flags();
    }

    /**
     * Sets the value of a specific CPU flag.
     * 
     * @param flagName The name of the flag to set.
     * @param value    The integer value to set for the flag (typically 0 or 1).
     */
    @Override
    public void setFlag(Flag flagName, Integer value) {
        store.put(flagName, value);
    }

    @Override
    public Integer getFlag(Flag flagName) {
        return store.get(flagName);
    }

    @Override
    public Set<Flag> getKeys() {
        return store.keySet();
    }

    @Override
    public void fillZeros() {
        this.setFlag(Flag.Cy, 0);
        this.setFlag(Flag.Ac, 0);
        this.setFlag(Flag.P, 0);
        this.setFlag(Flag.S, 0);
        this.setFlag(Flag.Z, 0);
    }
}
