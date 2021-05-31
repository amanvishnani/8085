package com.amanvishnani.sim8085.domain;

import java.util.Set;

public interface IFlags {
    void setFlag(Flag flagName, int value);
    int getFlag(Flag flagName);
    Set<Flag> getKeys();
}
