package com.amanvishnani.sim8085.domain.Impl;

import com.amanvishnani.sim8085.domain.IAddress;
import com.amanvishnani.sim8085.domain.IData;

/**
 * Object representing an instruction that has been executed.
 * Used for event notification.
 */
public class InstructionExecuted {
    IData instruction;
    IAddress nextAddress;

    public IData getInstruction() {
        return instruction;
    }

    public void setInstruction(IData instruction) {
        this.instruction = instruction;
    }

    public IAddress getNextAddress() {
        return nextAddress;
    }

    public void setNextAddress(IAddress nextAddress) {
        this.nextAddress = nextAddress;
    }
}
