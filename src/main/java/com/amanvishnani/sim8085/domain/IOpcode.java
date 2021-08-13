package com.amanvishnani.sim8085.domain;

public interface IOpcode {
    String getInstruction();
    IData getOpcodeData();
    Integer getOpcodeLength();
}
