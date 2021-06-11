package com.amanvishnani.sim8085.domain.Impl;

import com.amanvishnani.sim8085.domain.Flag;
import com.amanvishnani.sim8085.domain.IFlags;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Flags implements IFlags {
    Map<Flag, Integer> store;

    public Flags() {
        store = new HashMap<>();
    }

    public static Flags newInstance() {
        return new Flags();
    }

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
}
