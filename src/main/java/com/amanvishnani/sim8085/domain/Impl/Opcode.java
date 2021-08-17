package com.amanvishnani.sim8085.domain.Impl;

import com.amanvishnani.sim8085.domain.IData;
import com.amanvishnani.sim8085.domain.IOpcode;

import java.util.Objects;

public class Opcode implements IOpcode {

    private final IData opcode;
    private final Integer opcodeLength;
    private final String instruction;

    private Opcode(String opcode, Integer opcodeLength, String instruction) {
        this.opcode = Data.from(opcode);
        this.opcodeLength = opcodeLength;
        this.instruction = instruction;
    }

    public static Opcode createOpcode(String opcode, Integer opcodeLength, String instruction) {
        return new Opcode(opcode, opcodeLength, instruction);
    }

    @Override
    public String getInstruction() {
        return instruction;
    }

    @Override
    public IData getOpcodeData() {
        return opcode;
    }

    @Override
    public Integer getOpcodeLength() {
        return opcodeLength;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Opcode opcode1 = (Opcode) o;
        return getOpcodeData().equals(opcode1.getOpcodeData()) && getOpcodeLength().equals(opcode1.getOpcodeLength())
                && getInstruction().equals(opcode1.getInstruction());
    }

    @Override
    public int hashCode() {
        return Objects.hash(opcode, opcodeLength, instruction);
    }

    @Override
    public String toString() {
        return "Opcode{" +
                "opcode=" + opcode +
                ", opcodeLength=" + opcodeLength +
                ", instruction='" + instruction + '\'' +
                '}';
    }
}
