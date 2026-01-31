package com.amanvishnani.sim8085.domain;

public interface Instruction {
    String getOpcode();
    String getLabel();
    void setOpcode(String s);
    void setLabel(String s);
}
