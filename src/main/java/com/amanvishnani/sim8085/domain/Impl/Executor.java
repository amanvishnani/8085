package com.amanvishnani.sim8085.domain.Impl;


import com.amanvishnani.sim8085.domain.*;

public class Executor implements IExecutor {

    private final I8085 simulator;

    private Executor(I8085 simulator) {
        this.simulator = simulator;
    }

    public static Executor createExecutor(I8085 simulator) {
        return new Executor(simulator);
    }

    /* MOV INSTRUCTIONS */
    @Override
    public void _7F() {
        simulator.setA(simulator.getA());
        simulator.nextInstructionPointer();
    }

    // CMP A
    @Override
    public void _BF() {
        int r1, r2;
        r1 = simulator.getA().intValue();
        r2 = simulator.getA().intValue();
        if (r1 < r2) {
            simulator.setCy(1);
        }
        if (r1 == r2) {
            simulator.setZ(1);
        }
        if (r1 > r2) {
            simulator.setZ(0);
            simulator.setCy(0);
        }
        simulator.nextInstructionPointer();
    }

    // CMP B
    @Override
    public void _B8() {
        int r1, r2;
        r1 = simulator.getA().intValue();
        r2 = simulator.getB().intValue();
        if (r1 < r2) {
            simulator.setCy(1);
        }
        if (r1 == r2) {
            simulator.setZ(1);
        }
        if (r1 > r2) {
            simulator.setZ(0);
            simulator.setCy(0);
        }
        simulator.nextInstructionPointer();
    }

    // CMP C
    @Override
    public void _B9() {
        int r1, r2;
        r1 = simulator.getA().intValue();
        r2 = simulator.getC().intValue();
        if (r1 < r2) {
            simulator.setCy(1);
        }
        if (r1 == r2) {
            simulator.setZ(1);
        }
        if (r1 > r2) {
            simulator.setZ(0);
            simulator.setCy(0);
        }
        simulator.nextInstructionPointer();
    }

    // CMP D
    @Override
    public void _BA() {
        int r1, r2;
        r1 = simulator.getA().intValue();
        r2 = simulator.getD().intValue();
        if (r1 < r2) {
            simulator.setCy(1);
        }
        if (r1 == r2) {
            simulator.setZ(1);
        }
        if (r1 > r2) {
            simulator.setZ(0);
            simulator.setCy(0);
        }
        simulator.nextInstructionPointer();
    }

    // CMP E
    @Override
    public void _BB() {
        int r1, r2;
        r1 = simulator.getA().intValue();
        r2 = simulator.getE().intValue();
        if (r1 < r2) {
            simulator.setCy(1);
        }
        if (r1 == r2) {
            simulator.setZ(1);
        }
        if (r1 > r2) {
            simulator.setZ(0);
            simulator.setCy(0);
        }
        simulator.nextInstructionPointer();
    }

    // CMP H
    @Override
    public void _BC() {
        int r1, r2;
        r1 = simulator.getA().intValue();
        r2 = simulator.getH().intValue();
        if (r1 < r2) {
            simulator.setCy(1);
        }
        if (r1 == r2) {
            simulator.setZ(1);
        }
        if (r1 > r2) {
            simulator.setZ(0);
            simulator.setCy(0);
        }
        simulator.nextInstructionPointer();
    }

    // CMP L
    @Override
    public void _BD() {
        int r1, r2;
        r1 = simulator.getA().intValue();
        r2 = simulator.getL().intValue();
        if (r1 < r2) {
            simulator.setCy(1);
        }
        if (r1 == r2) {
            simulator.setZ(1);
        }
        if (r1 > r2) {
            simulator.setZ(0);
            simulator.setCy(0);
        }
        simulator.nextInstructionPointer();
    }

    @Override
    public void _78() {
        simulator.setA(simulator.getB());
        simulator.nextInstructionPointer();
    }

    @Override
    public void _79() {
        simulator.setA(simulator.getC());
        simulator.nextInstructionPointer();
    }

    @Override
    public void _7A() {
        simulator.setA(simulator.getD());
        simulator.nextInstructionPointer();
    }

    @Override
    public void _7B() {
        simulator.setA(simulator.getE());
        simulator.nextInstructionPointer();
    }

    @Override
    public void _7C() {
        simulator.setA(simulator.getH());
        simulator.nextInstructionPointer();
    }

    @Override
    public void _7D() {
        simulator.setA(simulator.getL());
        simulator.nextInstructionPointer();
    }

    @Override
    public void _7E() {
        simulator.setA(simulator.getM());
        simulator.nextInstructionPointer();
    }

    @Override
    public void _47() {
        simulator.setB(simulator.getA());
        simulator.nextInstructionPointer();
    }

    @Override
    public void _40() {
        simulator.setB(simulator.getB());
        simulator.nextInstructionPointer();
    }

    @Override
    public void _41() {
        simulator.setB(simulator.getC());
        simulator.nextInstructionPointer();
    }

    @Override
    public void _42() {
        simulator.setB(simulator.getD());
        simulator.nextInstructionPointer();
    }

    @Override
    public void _43() {
        simulator.setB(simulator.getE());
        simulator.nextInstructionPointer();
    }

    @Override
    public void _44() {
        simulator.setB(simulator.getH());
        simulator.nextInstructionPointer();
    }

    @Override
    public void _45() {
        simulator.setB(simulator.getL());
        simulator.nextInstructionPointer();
    }

    @Override
    public void _46() {
        simulator.setB(simulator.getM());
        simulator.nextInstructionPointer();
    }

    @Override
    public void _4F() {
        simulator.setC(simulator.getA());
        simulator.nextInstructionPointer();
    }

    @Override
    public void _48() {
        simulator.setC(simulator.getB());
        simulator.nextInstructionPointer();
    }

    @Override
    public void _49() {
        simulator.setC(simulator.getC());
        simulator.nextInstructionPointer();
    }

    @Override
    public void _4A() {
        simulator.setC(simulator.getD());
        simulator.nextInstructionPointer();
    }

    @Override
    public void _4B() {
        simulator.setC(simulator.getE());
        simulator.nextInstructionPointer();
    }

    @Override
    public void _4C() {
        simulator.setC(simulator.getH());
        simulator.nextInstructionPointer();
    }

    @Override
    public void _4D() {
        simulator.setC(simulator.getL());
        simulator.nextInstructionPointer();
    }

    @Override
    public void _4E() {
        simulator.setC(simulator.getM());
        simulator.nextInstructionPointer();
    }

    @Override
    public void _57() {
        simulator.setD(simulator.getA());
        simulator.nextInstructionPointer();
    }

    @Override
    public void _50() {
        simulator.setD(simulator.getB());
        simulator.nextInstructionPointer();
    }

    @Override
    public void _51() {
        simulator.setD(simulator.getC());
        simulator.nextInstructionPointer();
    }

    @Override
    public void _52() {
        simulator.setD(simulator.getD());
        simulator.nextInstructionPointer();
    }

    @Override
    public void _53() {
        simulator.setD(simulator.getE());
        simulator.nextInstructionPointer();
    }

    @Override
    public void _54() {
        simulator.setD(simulator.getH());
        simulator.nextInstructionPointer();
    }

    @Override
    public void _55() {
        simulator.setD(simulator.getL());
        simulator.nextInstructionPointer();
    }

    @Override
    public void _56() {
        simulator.setD(simulator.getM());
        simulator.nextInstructionPointer();
    }

    @Override
    public void _5F() {
        simulator.setE(simulator.getA());
        simulator.nextInstructionPointer();
    }

    @Override
    public void _58() {
        simulator.setE(simulator.getB());
        simulator.nextInstructionPointer();
    }

    @Override
    public void _59() {
        simulator.setE(simulator.getC());
        simulator.nextInstructionPointer();
    }

    @Override
    public void _5A() {
        simulator.setE(simulator.getD());
        simulator.nextInstructionPointer();
    }

    @Override
    public void _5B() {
        simulator.setE(simulator.getE());
        simulator.nextInstructionPointer();
    }

    @Override
    public void _5C() {
        simulator.setE(simulator.getH());
        simulator.nextInstructionPointer();
    }

    @Override
    public void _5D() {
        simulator.setE(simulator.getL());
        simulator.nextInstructionPointer();
    }

    @Override
    public void _5E() {
        simulator.setE(simulator.getM());
        simulator.nextInstructionPointer();
    }

    @Override
    public void _67() {
        simulator.setH(simulator.getA());
        simulator.nextInstructionPointer();
    }

    @Override
    public void _60() {
        simulator.setH(simulator.getB());
        simulator.nextInstructionPointer();
    }

    @Override
    public void _61() {
        simulator.setH(simulator.getC());
        simulator.nextInstructionPointer();
    }

    @Override
    public void _62() {
        simulator.setH(simulator.getD());
        simulator.nextInstructionPointer();
    }

    @Override
    public void _63() {
        simulator.setH(simulator.getE());
        simulator.nextInstructionPointer();
    }

    @Override
    public void _64() {
        simulator.setH(simulator.getH());
        simulator.nextInstructionPointer();
    }

    @Override
    public void _65() {
        simulator.setH(simulator.getL());
        simulator.nextInstructionPointer();
    }

    @Override
    public void _66() {
        simulator.setH(simulator.getM());
        simulator.nextInstructionPointer();
    }

    @Override
    public void _6F() {
        simulator.setL(simulator.getA());
        simulator.nextInstructionPointer();
    }

    @Override
    public void _68() {
        simulator.setL(simulator.getB());
        simulator.nextInstructionPointer();
    }

    @Override
    public void _69() {
        simulator.setL(simulator.getC());
        simulator.nextInstructionPointer();
    }

    @Override
    public void _6A() {
        simulator.setL(simulator.getD());
        simulator.nextInstructionPointer();
    }

    @Override
    public void _6B() {
        simulator.setL(simulator.getE());
        simulator.nextInstructionPointer();
    }

    @Override
    public void _6C() {
        simulator.setL(simulator.getH());
        simulator.nextInstructionPointer();
    }

    @Override
    public void _6D() {
        simulator.setL(simulator.getL());
        simulator.nextInstructionPointer();
    }

    @Override
    public void _6E() {
        simulator.setL(simulator.getM());
        simulator.nextInstructionPointer();
    }

    @Override
    public void _77() {
        simulator.setM(simulator.getA());
        simulator.nextInstructionPointer();
    }

    @Override
    public void _70() {
        simulator.setM(simulator.getB());
        simulator.nextInstructionPointer();
    }

    @Override
    public void _71() {
        simulator.setM(simulator.getC());
        simulator.nextInstructionPointer();
    }

    @Override
    public void _72() {
        simulator.setM(simulator.getD());
        simulator.nextInstructionPointer();
    }

    @Override
    public void _73() {
        simulator.setM(simulator.getE());
        simulator.nextInstructionPointer();
    }

    @Override
    public void _74() {
        simulator.setM(simulator.getH());
        simulator.nextInstructionPointer();
    }

    @Override
    public void _75() {
        simulator.setM(simulator.getL());
        simulator.nextInstructionPointer();
    }

    @Override
    public void _76() {

    }

    @Override
    public void _3E() {
        simulator.nextInstructionPointer();
        simulator.setA(simulator.getDataAtIP());
        simulator.nextInstructionPointer();
    }

    @Override
    public void _06() {
        simulator.nextInstructionPointer();
        simulator.setB(simulator.getDataAtIP());
        simulator.nextInstructionPointer();
    }

    @Override
    public void _0E() {
        simulator.nextInstructionPointer();
        simulator.setC(simulator.getDataAtIP());
        simulator.nextInstructionPointer();
    }

    @Override
    public void _16() {
        simulator.nextInstructionPointer();
        simulator.setD(simulator.getDataAtIP());
        simulator.nextInstructionPointer();
    }

    @Override
    public void _1E() {
        simulator.nextInstructionPointer();
        simulator.setE(simulator.getDataAtIP());
        simulator.nextInstructionPointer();
    }

    @Override
    public void _26() {
        simulator.nextInstructionPointer();
        simulator.setH(simulator.getDataAtIP());
        simulator.nextInstructionPointer();
    }

    @Override
    public void _2E() {
        simulator.nextInstructionPointer();
        simulator.setL(simulator.getDataAtIP());
        simulator.nextInstructionPointer();
    }

    @Override
    public void _36() {
        simulator.nextInstructionPointer();
        simulator.setM(simulator.getDataAtIP());
        simulator.nextInstructionPointer();
    }

    // ADD A
    @Override
    public void _87() {
        IOperationResult r = simulator.getA().add(simulator.getA());
        simulator.updateFlags(r.getFlags());
        simulator.setA(r.getData());
        simulator.nextInstructionPointer();
    }

    // ADD B
    @Override
    public void _80() {
        IOperationResult r = simulator.getA().add(simulator.getB());
        simulator.updateFlags(r.getFlags());
        simulator.setA(r.getData());
        simulator.nextInstructionPointer();
    }

    // ADD C
    @Override
    public void _81() {
        IOperationResult r = simulator.getA().add(simulator.getC());
        simulator.updateFlags(r.getFlags());
        simulator.setA(r.getData());
        simulator.nextInstructionPointer();
    }


    // ADD D
    @Override
    public void _82() {
        IOperationResult r = simulator.getA().add(simulator.getD());
        simulator.updateFlags(r.getFlags());
        simulator.setA(r.getData());
        simulator.nextInstructionPointer();
    }

    // ADD E
    @Override
    public void _83() {
        IOperationResult r = simulator.getA().add(simulator.getE());
        simulator.updateFlags(r.getFlags());
        simulator.setA(r.getData());
        simulator.nextInstructionPointer();
    }

    // ADD H
    @Override
    public void _84() {
        IOperationResult r = simulator.getA().add(simulator.getH());
        simulator.updateFlags(r.getFlags());
        simulator.setA(r.getData());
        simulator.nextInstructionPointer();
    }


    // ADD L
    @Override
    public void _85() {
        IOperationResult r = simulator.getA().add(simulator.getL());
        simulator.updateFlags(r.getFlags());
        simulator.setA(r.getData());
        simulator.nextInstructionPointer();
    }

    // ADD M
    @Override
    public void _86() {
        IOperationResult r = simulator.getA().add(simulator.getM());
        simulator.updateFlags(r.getFlags());
        simulator.setA(r.getData());
        simulator.nextInstructionPointer();
    }

    // ADI data
    @Override
    public void _C6() {
        simulator.nextInstructionPointer();
        IAddress InstPtr = simulator.getIP();
        IOperationResult r = simulator.getA().add(simulator.getData(InstPtr.intValue()));
        simulator.updateFlags(r.getFlags());
        simulator.setA(r.getData());
        simulator.nextInstructionPointer();
    }

    // ADC A
    @Override
    public void _8F() {
        IOperationResult r = simulator.getA().add(simulator.getA(), simulator.getCy());
        simulator.updateFlags(r.getFlags());
        simulator.setA(r.getData());
        simulator.nextInstructionPointer();
    }

    // ADC B
    @Override
    public void _88() {
        IOperationResult r = simulator.getA().add(simulator.getB(), simulator.getCy());
        simulator.updateFlags(r.getFlags());
        simulator.setA(r.getData());
        simulator.nextInstructionPointer();
    }

    // ADC C
    @Override
    public void _89() {
        IOperationResult r = simulator.getA().add(simulator.getC(), simulator.getCy());
        simulator.updateFlags(r.getFlags());
        simulator.setA(r.getData());
        simulator.nextInstructionPointer();
    }

    // ADC D
    @Override
    public void _8A() {
        IOperationResult r = simulator.getA().add(simulator.getD(), simulator.getCy());
        simulator.updateFlags(r.getFlags());
        simulator.setA(r.getData());
        simulator.nextInstructionPointer();
    }

    // ADC E
    @Override
    public void _8B() {
        IOperationResult r = simulator.getA().add(simulator.getD(), simulator.getCy());
        simulator.updateFlags(r.getFlags());
        simulator.setA(r.getData());
        simulator.nextInstructionPointer();
    }

    // ADC H
    @Override
    public void _8C() {
        IOperationResult r = simulator.getA().add(simulator.getH(), simulator.getCy());
        simulator.updateFlags(r.getFlags());
        simulator.setA(r.getData());
        simulator.nextInstructionPointer();
    }

    // ADC L
    @Override
    public void _8D() {
        IOperationResult r = simulator.getA().add(simulator.getL(), simulator.getCy());
        simulator.updateFlags(r.getFlags());
        simulator.setA(r.getData());
        simulator.nextInstructionPointer();
    }

    // ADC M
    @Override
    public void _8E() {
        IOperationResult r = simulator.getA().add(simulator.getM(), simulator.getCy());
        simulator.updateFlags(r.getFlags());
        simulator.setA(r.getData());
        simulator.nextInstructionPointer();
    }

    // ACI
    @Override
    public void _CE() {
        IOperationResult r = simulator.getA().add(simulator.getDataAtIP(), simulator.getCy());
        simulator.updateFlags(r.getFlags());
        simulator.setA(r.getData());
        simulator.nextInstructionPointer();
    }

    void sub(IData r) {
        IOperationResult operationResult = simulator.getA().minus(r);
        IFlags flags = operationResult.getFlags();
        IData result = operationResult.getData();
        simulator.updateFlags(flags);
        simulator.setA(result);
        simulator.nextInstructionPointer();
    }


    // SUB A
    @Override
    public void _97() {
        this.sub(simulator.getA());
    }

    // SUB B
    @Override
    public void _90() {
        this.sub(simulator.getB());
    }

    // SUB C
    @Override
    public void _91() {
        this.sub(simulator.getC());
    }

    // SUB D
    @Override
    public void _92() {
        this.sub(simulator.getD());
    }

    // SUB E
    @Override
    public void _93() {
        this.sub(simulator.getE());
    }

    // SUB H
    @Override
    public void _94() {
        this.sub(simulator.getH());
    }

    // SUB L
    @Override
    public void _95() {
        this.sub(simulator.getL());
    }

    // SUB M
    @Override
    public void _96() {
        this.sub(simulator.getM());
    }

    // SUI Data
    @Override
    public void _D6() {
        IData r = simulator.getDataAtIP();
        this.sub(r);
    }

    // SBB A
    @Override
    public void _9F() {
        int r1, r2, r3;
        r1 = simulator.getA().intValue();
        r2 = simulator.getA().intValue();
        int r4;
        r4 = simulator.getCy();
        r3 = r1 - r2 - r4;
        if (r3 < 0) {
            r3 *= -1;
            simulator.setS(1);
        } else {
            simulator.setS(0);
        }
        if (r3 == 0) {
            simulator.setZ(1);
        } else {
            simulator.setZ(0);
        }
        if (r3 > 255) {
            simulator.setCy(1);
        } else {
            simulator.setCy(0);
        }
        String temp = Integer.toHexString(r3);
        simulator.setA(Data.from(temp));
        PARITY();
        simulator.nextInstructionPointer();
    }

    //SBB B
    @Override
    public void _98() {
        int r1, r2, r3;
        r1 = simulator.getA().intValue();
        r2 = simulator.getB().intValue();
        int r4;
        r4 = simulator.getCy();
        r3 = r1 - r2 - r4;
        if (r3 < 0) {
            r3 *= -1;
            simulator.setS(1);
        } else {
            simulator.setS(0);
        }
        if (r3 == 0) {
            simulator.setZ(1);
        } else {
            simulator.setZ(0);
        }
        if (r3 > 255) {
            simulator.setCy(1);
        } else {
            simulator.setCy(0);
        }
        String temp = Integer.toHexString(r3);
        simulator.setA(Data.from(temp));
        PARITY();
        simulator.nextInstructionPointer();
    }

    // SBB C
    @Override
    public void _99() {
        int r1, r2, r3;
        r1 = simulator.getA().intValue();
        r2 = simulator.getC().intValue();
        int r4;
        r4 = simulator.getCy();
        r3 = r1 - r2 - r4;
        if (r3 < 0) {
            r3 *= -1;
            simulator.setS(1);
        } else {
            simulator.setS(0);
        }
        if (r3 == 0) {
            simulator.setZ(1);
        } else {
            simulator.setZ(0);
        }
        if (r3 > 255) {
            simulator.setCy(1);
        } else {
            simulator.setCy(0);
        }
        String temp = Integer.toHexString(r3);
        simulator.setA(Data.from(temp));
        PARITY();
        simulator.nextInstructionPointer();
    }

    // SBB D
    @Override
    public void _9A() {
        int r1, r2, r3;
        r1 = simulator.getA().intValue();
        r2 = simulator.getD().intValue();
        int r4;
        r4 = simulator.getCy();
        r3 = r1 - r2 - r4;
        if (r3 < 0) {
            r3 *= -1;
            simulator.setS(1);
        } else {
            simulator.setS(0);
        }
        if (r3 == 0) {
            simulator.setZ(1);
        } else {
            simulator.setZ(0);
        }
        if (r3 > 255) {
            simulator.setCy(1);
        } else {
            simulator.setCy(0);
        }
        String temp = Integer.toHexString(r3);
        simulator.setA(Data.from(temp));
        PARITY();
        simulator.nextInstructionPointer();
    }

    // SBB E
    @Override
    public void _9B() {
        int r1, r2, r3;
        r1 = simulator.getA().intValue();
        r2 = simulator.getE().intValue();
        int r4;
        r4 = simulator.getCy();
        r3 = r1 - r2 - r4;
        if (r3 < 0) {
            r3 *= -1;
            simulator.setS(1);
        } else {
            simulator.setS(0);
        }
        if (r3 == 0) {
            simulator.setZ(1);
        } else {
            simulator.setZ(0);
        }
        if (r3 > 255) {
            simulator.setCy(1);
        } else {
            simulator.setCy(0);
        }
        String temp = Integer.toHexString(r3);
        simulator.setA(Data.from(temp));
        PARITY();
        simulator.nextInstructionPointer();
    }

    // SBB H
    @Override
    public void _9C() {
        int r1, r2, r3;
        r1 = simulator.getA().intValue();
        r2 = simulator.getH().intValue();
        int r4;
        r4 = simulator.getCy();
        r3 = r1 - r2 - r4;
        if (r3 < 0) {
            r3 *= -1;
            simulator.setS(1);
        } else {
            simulator.setS(0);
        }
        if (r3 == 0) {
            simulator.setZ(1);
        } else {
            simulator.setZ(0);
        }
        if (r3 > 255) {
            simulator.setCy(1);
        } else {
            simulator.setCy(0);
        }
        String temp = Integer.toHexString(r3);
        simulator.setA(Data.from(temp));
        PARITY();
        simulator.nextInstructionPointer();
    }

    // SBB L
    @Override
    public void _9D() {
        int r1, r2, r3;
        r1 = simulator.getA().intValue();
        r2 = simulator.getL().intValue();
        int r4;
        r4 = simulator.getCy();
        r3 = r1 - r2 - r4;
        if (r3 < 0) {
            r3 *= -1;
            simulator.setS(1);
        } else {
            simulator.setS(0);
        }
        if (r3 == 0) {
            simulator.setZ(1);
        } else {
            simulator.setZ(0);
        }
        if (r3 > 255) {
            simulator.setCy(1);
        } else {
            simulator.setCy(0);
        }
        String temp = Integer.toHexString(r3);
        simulator.setA(Data.from(temp));
        PARITY();
        simulator.nextInstructionPointer();
    }

    // SBB M
    @Override
    public void _9E() {
        int r1, r2, r3;
        r1 = simulator.getA().intValue();
        r2 = simulator.getM().intValue();
        int r4;
        r4 = simulator.getCy();
        r3 = r1 - r2 - r4;
        if (r3 < 0) {
            r3 *= -1;
            simulator.setS(1);
        } else {
            simulator.setS(0);
        }
        if (r3 == 0) {
            simulator.setZ(1);
        } else {
            simulator.setZ(0);
        }
        if (r3 > 255) {
            simulator.setCy(1);
        } else {
            simulator.setCy(0);
        }
        String temp = Integer.toHexString(r3);
        simulator.setA(Data.from(temp));
        PARITY();
        simulator.nextInstructionPointer();
    }

    //SBI
    @Override
    public void _DE() {
        int r1, r2, r3;
        r1 = simulator.getA().intValue();
        simulator.nextInstructionPointer();
        r2 = simulator.getDataAtIP().intValue();
        int r4;
        r4 = simulator.getCy();
        r3 = r1 - r2 - r4;
        if (r3 < 0) {
            r3 *= -1;
            simulator.setS(1);
        } else {
            simulator.setS(0);
        }
        if (r3 == 0) {
            simulator.setZ(1);
        } else {
            simulator.setZ(0);
        }
        if (r3 > 255) {
            simulator.setCy(1);
        } else {
            simulator.setCy(0);
        }
        String temp = Integer.toHexString(r3);
        simulator.setA(Data.from(temp));
        PARITY();
        simulator.nextInstructionPointer();
    }

    public void PARITY() {
        int x, counter = 0;
        x = simulator.getA().intValue();
        String s = Integer.toBinaryString(x);
        char[] c = s.toCharArray();
        for (int i = 0; i < s.length(); i++) {
            if (c[i] == '1') {
                counter++;
            }
        }
        if (counter % 2 == 0) {
            simulator.setP(1);
        } else {
            simulator.setP(0);
        }
    }

    // DAA
    @Override
    public void _27() {
        int s = simulator.getA().intValue();
        String temp = Integer.toString(s);
        simulator.setA(Data.from(temp.substring((temp.length() - 2))));
        if (s == 0) {
            simulator.setZ(1);
        }
        if (s > 99) {
            simulator.setCy(1);
        }
        if (s > 999) {
            simulator.setAc(1);
        }
        if (s < 0) {
            simulator.setS(1);
        }

        PARITY();
        simulator.nextInstructionPointer();
    }

    // INR A
    @Override
    public void _3C() {
        int r;
        r = simulator.getA().intValue();
        r++;
        String temp = Integer.toHexString(r);
        simulator.setA(Data.from(temp));
        simulator.nextInstructionPointer();
    }

    // INR B
    @Override
    public void _04() {
        int r;
        r = simulator.getB().intValue();
        r++;
        String temp = Integer.toHexString(r);
        simulator.setB(Data.from(temp));
        simulator.nextInstructionPointer();
    }

    // INR C
    @Override
    public void _0C() {
        int r;
        r = simulator.getC().intValue();
        r++;
        String temp = Integer.toHexString(r);
        simulator.setC(Data.from(temp));
        simulator.nextInstructionPointer();
    }


    // INR D
    @Override
    public void _14() {
        int r;
        r = simulator.getD().intValue();
        r++;
        String temp = Integer.toHexString(r);
        simulator.setD(Data.from(temp));
        simulator.nextInstructionPointer();
    }

    // INR E
    @Override
    public void _1C() {
        int r;
        r = simulator.getE().intValue();
        r++;
        String temp = Integer.toHexString(r);
        simulator.setE(Data.from(temp));
        simulator.nextInstructionPointer();
    }

    // INR H
    @Override
    public void _24() {
        int r;
        r = simulator.getH().intValue();
        r++;
        String temp = Integer.toHexString(r);
        simulator.setH(Data.from(temp));
        simulator.nextInstructionPointer();
    }

    // INR L
    @Override
    public void _2C() {
        int r;
        r = simulator.getL().intValue();
        r++;
        String temp = Integer.toHexString(r);
        simulator.setL(Data.from(temp));
        simulator.nextInstructionPointer();
    }

    // INR M
    @Override
    public void _34() {
        int r;
        r = simulator.getM().intValue();
        r++;
        String temp = Integer.toHexString(r);
        simulator.setM(Data.from(temp));
        simulator.nextInstructionPointer();
    }

    public void _BE() {
        int r1, r2;
        r1 = simulator.getA().intValue();
        r2 = simulator.getM().intValue();
        if (r1 < r2) {
            simulator.setCy(1);
        }
        if (r1 == r2) {
            simulator.setZ(1);
        }
        if (r1 > r2) {
            simulator.setZ(0);
            simulator.setCy(0);
        }
        simulator.nextInstructionPointer();
    }

    // DCR A
    @Override
    public void _3D() {
        int r;
        r = simulator.getA().intValue();
        r--;
        if (r == 0) {
            simulator.setZ(1);
        }
        String temp = Integer.toHexString(r);
        simulator.setA(Data.from(temp));
        simulator.nextInstructionPointer();
    }
    // DCR B
    @Override
    public void _05() {
        int r;
        r = simulator.getB().intValue();
        r--;
        if (r == 0) {
            simulator.setZ(1);
        }
        String temp = Integer.toHexString(r);
        simulator.setB(Data.from(temp));
        simulator.nextInstructionPointer();
    }

    // DCR C
    @Override
    public void _0D() {
        int r;
        r = simulator.getC().intValue();
        r--;
        if (r == 0) {
            simulator.setZ(1);
        }
        String temp = Integer.toHexString(r);
        simulator.setC(Data.from(temp));
        simulator.nextInstructionPointer();
    }

    // DCR D
    @Override
    public void _15() {
        int r;
        r = simulator.getD().intValue();
        r--;
        if (r == 0) {
            simulator.setZ(1);
        }
        String temp = Integer.toHexString(r);
        simulator.setD(Data.from(temp));
        simulator.nextInstructionPointer();
    }
    // DCR E
    @Override
    public void _1D() {
        int r;
        r = simulator.getE().intValue();
        r--;
        if (r == 0) {
            simulator.setZ(1);
        }
        String temp = Integer.toHexString(r);
        simulator.setE(Data.from(temp));
        simulator.nextInstructionPointer();
    }

    // DCR H
    @Override
    public void _25() {
        int r;
        r = simulator.getH().intValue();
        r--;
        if (r == 0) {
            simulator.setZ(1);
        }
        String temp = Integer.toHexString(r);
        simulator.setH(Data.from(temp));
        simulator.nextInstructionPointer();
    }

    // DCR L
    @Override
    public void _2D() {
        int r;
        r = simulator.getL().intValue();
        r--;
        if (r == 0) {
            simulator.setZ(1);
        }
        String temp = Integer.toHexString(r);
        simulator.setL(Data.from(temp));
        simulator.nextInstructionPointer();
    }

    // DCR M
    @Override
    public void _35() {
        int r;
        r = simulator.getM().intValue();
        r--;
        if (r == 0) {
            simulator.setZ(1);
        }
        String temp = Integer.toHexString(r);
        simulator.setM(Data.from(temp));
        simulator.nextInstructionPointer();
    }

    // XCHG
    @Override
    public void _EB() {
        IData r1, r2;
        r1 = simulator.getD();
        r2 = simulator.getE();
        simulator.setD(simulator.getH());
        simulator.setE(simulator.getL());
        simulator.setH(r1);
        simulator.setL(r2);
        simulator.nextInstructionPointer();
    }

    private void dadOperation(IData leftRegister, IData rightRegister) {
        IAddress r1, r2;
        r1 = Address.from(leftRegister, rightRegister);
        r2 = Address.from(simulator.getH(), simulator.getL());

        int sum = r1.intValue() + r2.intValue();
        if (sum > 65535) {
            simulator.setCy(1);
        }

        IAddress temp = Address.from(sum);
        IData t1 = temp.getLSB();
        IData t2 = temp.getMSB();
        simulator.setL(t1);
        simulator.setH(t2);
        simulator.nextInstructionPointer();
    }

    // DAD B
    @Override
    public void _09() {
        this.dadOperation(simulator.getB(), simulator.getC());
    }

    // DAD D
    @Override
    public void _19() {
        this.dadOperation(simulator.getD(), simulator.getE());
    }

    // DAD H
    @Override
    public void _29() {
        this.dadOperation(simulator.getH(), simulator.getL());
    }

    // ANA A
    @Override
    public void _A7() {
        int r1, r2;
        r1 = simulator.getA().intValue();
        r2 = simulator.getA().intValue();
        int s = r1 & r2;
        String temp = Integer.toHexString(s);
        simulator.setA(Data.from(temp));
        if (s > 127) {
            simulator.setS(1);
        }
        if (s == 0) {
            simulator.setZ(1);
        }
        simulator.nextInstructionPointer();
    }

    // ANA B
    @Override
    public void _A0() {
        int r1, r2;
        r1 = simulator.getA().intValue();
        r2 = simulator.getB().intValue();
        int s = r1 & r2;
        String temp = Integer.toHexString(s);
        simulator.setA(Data.from(temp));
        if (s > 127) {
            simulator.setS(1);
        }
        if (s == 0) {
            simulator.setZ(1);
        }
        simulator.nextInstructionPointer();
    }

    //ANA C
    @Override
    public void _A1() {
        int r1, r2;
        r1 = simulator.getA().intValue();
        r2 = simulator.getC().intValue();
        int s = r1 & r2;
        String temp = Integer.toHexString(s);
        simulator.setA(Data.from(temp));
        if (s > 127) {
            simulator.setS(1);
        }
        if (s == 0) {
            simulator.setZ(1);
        }
        simulator.nextInstructionPointer();
    }

    // ANA D
    @Override
    public void _A2() {
        int r1, r2;
        r1 = simulator.getA().intValue();
        r2 = simulator.getD().intValue();
        int s = r1 & r2;
        String temp = Integer.toHexString(s);
        simulator.setA(Data.from(temp));
        if (s > 127) {
            simulator.setS(1);
        }
        if (s == 0) {
            simulator.setZ(1);
        }
        simulator.nextInstructionPointer();
    }

    // ANA E
    @Override
    public void _A3() {
        int r1, r2;
        r1 = simulator.getA().intValue();
        r2 = simulator.getE().intValue();
        int s = r1 & r2;
        String temp = Integer.toHexString(s);
        simulator.setA(Data.from(temp));
        if (s > 127) {
            simulator.setS(1);
        }
        if (s == 0) {
            simulator.setZ(1);
        }
        simulator.nextInstructionPointer();
    }

    // ANA H
    @Override
    public void _A4() {
        int r1, r2;
        r1 = simulator.getA().intValue();
        r2 = simulator.getH().intValue();
        int s = r1 & r2;
        String temp = Integer.toHexString(s);
        simulator.setA(Data.from(temp));
        if (s > 127) {
            simulator.setS(1);
        }
        if (s == 0) {
            simulator.setZ(1);
        }
        simulator.nextInstructionPointer();
    }

    // ANA L
    @Override
    public void _A5() {
        int r1, r2;
        r1 = simulator.getA().intValue();
        r2 = simulator.getL().intValue();
        int s = r1 & r2;
        String temp = Integer.toHexString(s);
        simulator.setA(Data.from(temp));
        if (s > 127) {
            simulator.setS(1);
        }
        if (s == 0) {
            simulator.setZ(1);
        }
        simulator.nextInstructionPointer();
    }

    // ANA M
    @Override
    public void _A6() {
        int r1, r2;
        r1 = simulator.getA().intValue();
        r2 = simulator.getM().intValue();
        int s = r1 & r2;
        String temp = Integer.toHexString(s);
        simulator.setA(Data.from(temp));
        if (s > 127) {
            simulator.setS(1);
        }
        if (s == 0) {
            simulator.setZ(1);
        }
        simulator.nextInstructionPointer();
    }

    // ANI data
    @Override
    public void _E6() {
        int r1, r2;
        r1 = simulator.getA().intValue();
        r2 = simulator.getM().intValue();
        int s = r1 & r2;
        String temp = Integer.toHexString(s);
        simulator.setA(Data.from(temp));
        if (s > 127) {
            simulator.setS(1);
        }
        if (s == 0) {
            simulator.setZ(1);
        }
        simulator.nextInstructionPointer();
    }

    // ORA A
    @Override
    public void _B7() {
        int r1, r2;
        r1 = simulator.getA().intValue();
        simulator.nextInstructionPointer();
        r2 = simulator.getDataAtIP().intValue();
        int s = r1 | r2;
        String temp = Integer.toHexString(s);
        simulator.setA(Data.from(temp));
        if (s > 127) {
            simulator.setS(1);
        }
        if (s == 0) {
            simulator.setZ(1);
        }
        simulator.nextInstructionPointer();
    }

    // ORA B
    @Override
    public void _B0() {
        int r1, r2;
        r1 = simulator.getA().intValue();
        r2 = simulator.getB().intValue();
        int s = r1 | r2;
        String temp = Integer.toHexString(s);
        simulator.setA(Data.from(temp));
        if (s > 127) {
            simulator.setS(1);
        }
        if (s == 0) {
            simulator.setZ(1);
        }
        simulator.nextInstructionPointer();
    }
    //ORA C
    @Override
    public void _B1() {
        int r1, r2;
        r1 = simulator.getA().intValue();
        r2 = simulator.getC().intValue();
        int s = r1 | r2;
        String temp = Integer.toHexString(s);
        simulator.setA(Data.from(temp));
        if (s > 127) {
            simulator.setS(1);
        }
        if (s == 0) {
            simulator.setZ(1);
        }
        simulator.nextInstructionPointer();
    }
    //ORA D
    @Override
    public void _B2() {
        int r1, r2;
        r1 = simulator.getA().intValue();
        r2 = simulator.getD().intValue();
        int s = r1 | r2;
        String temp = Integer.toHexString(s);
        simulator.setA(Data.from(temp));
        if (s > 127) {
            simulator.setS(1);
        }
        if (s == 0) {
            simulator.setZ(1);
        }
        simulator.nextInstructionPointer();
    }

    //ORA E
    @Override
    public void _B3() {
        int r1, r2;
        r1 = simulator.getA().intValue();
        r2 = simulator.getE().intValue();
        int s = r1 | r2;
        String temp = Integer.toHexString(s);
        simulator.setA(Data.from(temp));
        if (s > 127) {
            simulator.setS(1);
        }
        if (s == 0) {
            simulator.setZ(1);
        }
        simulator.nextInstructionPointer();
    }

    //ORA H
    @Override
    public void _B4() {
        int r1, r2;
        r1 = simulator.getA().intValue();
        r2 = simulator.getH().intValue();
        int s = r1 | r2;
        String temp = Integer.toHexString(s);
        simulator.setA(Data.from(temp));
        if (s > 127) {
            simulator.setS(1);
        }
        if (s == 0) {
            simulator.setZ(1);
        }
        simulator.nextInstructionPointer();
    }

    //ORA L
    @Override
    public void _B5() {
        int r1, r2;
        r1 = simulator.getA().intValue();
        r2 = simulator.getL().intValue();
        int s = r1 | r2;
        String temp = Integer.toHexString(s);
        simulator.setA(Data.from(temp));
        if (s > 127) {
            simulator.setS(1);
        }
        if (s == 0) {
            simulator.setZ(1);
        }
        simulator.nextInstructionPointer();
    }

    //ORA M
    @Override
    public void _B6() {
        int r1, r2;
        r1 = simulator.getA().intValue();
        r2 = simulator.getM().intValue();
        int s = r1 | r2;
        String temp = Integer.toHexString(s);
        simulator.setA(Data.from(temp));
        if (s > 127) {
            simulator.setS(1);
        }
        if (s == 0) {
            simulator.setZ(1);
        }
        simulator.nextInstructionPointer();
    }

    //ORI
    @Override
    public void _F6() {
        int r1, r2;
        r1 = simulator.getA().intValue();
        simulator.nextInstructionPointer();
        r2 = simulator.getDataAtIP().intValue();
        int s = r1 | r2;
        String temp = Integer.toHexString(s);
        simulator.setA(Data.from(temp));
        if (s > 127) {
            simulator.setS(1);
        }
        if (s == 0) {
            simulator.setZ(1);
        }
        simulator.nextInstructionPointer();
    }

    //XRA A
    @Override
    public void _AF() {
        int r1, r2;
        r1 = simulator.getA().intValue();
        r2 = simulator.getA().intValue();
        int s = r1 ^ r2;
        String temp = Integer.toHexString(s);
        simulator.setA(Data.from(temp));
        if (s > 127) {
            simulator.setS(1);
        }
        if (s == 0) {
            simulator.setZ(1);
        }
        simulator.nextInstructionPointer();
    }

    //XRA B
    @Override
    public void _A8() {
        int r1, r2;
        r1 = simulator.getA().intValue();
        r2 = simulator.getB().intValue();
        int s = r1 ^ r2;
        String temp = Integer.toHexString(s);
        simulator.setA(Data.from(temp));
        if (s > 127) {
            simulator.setS(1);
        }
        if (s == 0) {
            simulator.setZ(1);
        }
        simulator.nextInstructionPointer();
    }

    //XRA C
    @Override
    public void _A9() {
        int r1, r2;
        r1 = simulator.getA().intValue();
        r2 = simulator.getC().intValue();
        int s = r1 ^ r2;
        String temp = Integer.toHexString(s);
        simulator.setA(Data.from(temp));
        if (s > 127) {
            simulator.setS(1);
        }
        if (s == 0) {
            simulator.setZ(1);
        }
        simulator.nextInstructionPointer();
    }

    //XRA D
    @Override
    public void _AA() {
        int r1, r2;
        r1 = simulator.getA().intValue();
        r2 = simulator.getD().intValue();
        int s = r1 ^ r2;
        String temp = Integer.toHexString(s);
        simulator.setA(Data.from(temp));
        if (s > 127) {
            simulator.setS(1);
        }
        if (s == 0) {
            simulator.setZ(1);
        }
        simulator.nextInstructionPointer();
    }
    //XRA E
    @Override
    public void _AB() {
        int r1, r2;
        r1 = simulator.getA().intValue();
        r2 = simulator.getE().intValue();
        int s = r1 ^ r2;
        String temp = Integer.toHexString(s);
        simulator.setA(Data.from(temp));
        if (s > 127) {
            simulator.setS(1);
        }
        if (s == 0) {
            simulator.setZ(1);
        }
        simulator.nextInstructionPointer();
    }
    //XRA H
    @Override
    public void _AC() {
        int r1, r2;
        r1 = simulator.getA().intValue();
        r2 = simulator.getH().intValue();
        int s = r1 ^ r2;
        String temp = Integer.toHexString(s);
        simulator.setA(Data.from(temp));
        if (s > 127) {
            simulator.setS(1);
        }
        if (s == 0) {
            simulator.setZ(1);
        }
        simulator.nextInstructionPointer();
    }
    //XRA L
    @Override
    public void _AD() {
        int r1, r2;
        r1 = simulator.getA().intValue();
        r2 = simulator.getL().intValue();
        int s = r1 ^ r2;
        String temp = Integer.toHexString(s);
        simulator.setA(Data.from(temp));
        if (s > 127) {
            simulator.setS(1);
        }
        if (s == 0) {
            simulator.setZ(1);
        }
        simulator.nextInstructionPointer();
    }
    //XRA M
    @Override
    public void _AE() {
        int r1, r2;
        r1 = simulator.getA().intValue();
        r2 = simulator.getM().intValue();
        int s = r1 ^ r2;
        String temp = Integer.toHexString(s);
        simulator.setA(Data.from(temp));
        if (s > 127) {
            simulator.setS(1);
        }
        if (s == 0) {
            simulator.setZ(1);
        }
        simulator.nextInstructionPointer();
    }
    //XRI data
    @Override
    public void _EE() {
        int r1, r2;
        r1 = simulator.getA().intValue();
        simulator.nextInstructionPointer();
        r2 = simulator.getDataAtIP().intValue();
        int s = r1 ^ r2;
        String temp = Integer.toHexString(s);
        simulator.setA(Data.from(temp));
        if (s > 127) {
            simulator.setS(1);
        }
        if (s == 0) {
            simulator.setZ(1);
        }
        simulator.nextInstructionPointer();
    }

    //STC
    @Override
    public void _37() {
        simulator.setCy(1);
        simulator.nextInstructionPointer();
    }

    //CALL LABEL
    @Override
    public void _CD() {
        simulator.nextInstructionPointer();
        String s1 = simulator.getDataAtIP().hexValue();
        simulator.nextInstructionPointer();
        String s2 = simulator.getDataAtIP().hexValue();
        simulator.nextInstructionPointer();
        String s3 = simulator.getIP().hexValue();
        IData t1 = simulator.getIP().getMSB();
        IData t2 = simulator.getIP().getLSB();
        simulator.setData(simulator.getSP(), t2);
        simulator.decrementSP();
        simulator.setData(simulator.getSP(), t1);
        simulator.decrementSP();
        simulator.setIP(Address.from(s2 + s1));
    }
    //RET
    @Override
    public void _C9() {
        simulator.incrementSP();
        String s1 = simulator.getData(simulator.getSP()).hexValue();
        simulator.incrementSP();
        String s2 = simulator.getData(simulator.getSP()).hexValue();
        simulator.setIP(Address.from(s1 + s2));
    }
    //JMP
    @Override
    public void _C3() {
        simulator.nextInstructionPointer();
        String s1 = simulator.getDataAtIP().hexValue();
        simulator.nextInstructionPointer();
        String s2 = simulator.getDataAtIP().hexValue();
        simulator.setIP(Address.from(s2 + s1));
    }

    /**
     * *************** JUMP PSW    ***************
     */

    //JC Cy=1 _DA()
    @Override
    public void _DA() {
        if (simulator.getCy() == 1) {
            _C3();
        } else {
            simulator.nextInstructionPointer();
            simulator.nextInstructionPointer();
            simulator.nextInstructionPointer();
        }
    }
    //JNC Cy=0 _D2()
    @Override
    public void _D2() {
        if (simulator.getCy() == 0) {
            _C3();
        } else {
            simulator.nextInstructionPointer();
            simulator.nextInstructionPointer();
            simulator.nextInstructionPointer();
        }
    }
    //JP S=0 _F2()
    @Override
    public void _F2() {
        if (simulator.getS() == 0) {
            _C3();
        } else {
            simulator.nextInstructionPointer();
            simulator.nextInstructionPointer();
            simulator.nextInstructionPointer();
        }
    }
    //JM S=1 _FA()
    @Override
    public void _FA() {
        if (simulator.getS() == 1) {
            _C3();
        } else {
            simulator.nextInstructionPointer();
            simulator.nextInstructionPointer();
            simulator.nextInstructionPointer();
        }
    }
    //JZ Z=1 _CA()
    @Override
    public void _CA() {
        if (simulator.getZ() == 1) {
            _C3();
        } else {
            simulator.nextInstructionPointer();
            simulator.nextInstructionPointer();
            simulator.nextInstructionPointer();
        }
    }
    //JNZ Z=0 _C2()
    @Override
    public void _C2() {
        if (simulator.getZ() == 0) {
            _C3();
        } else {
            simulator.nextInstructionPointer();
            simulator.nextInstructionPointer();
            simulator.nextInstructionPointer();
        }
    }
    //JPE P=1 _EA()
    @Override
    public void _EA() {
        if (simulator.getP() == 1) {
            _C3();
        } else {
            simulator.nextInstructionPointer();
            simulator.nextInstructionPointer();
            simulator.nextInstructionPointer();
        }
    }
    //JPO P=0 _E2()
    @Override
    public void _E2() {
        if (simulator.getP() == 0) {
            _C3();
        } else {
            simulator.nextInstructionPointer();
            simulator.nextInstructionPointer();
            simulator.nextInstructionPointer();
        }
    }

    /**
     * *************** RETURN PSW ****************
     */
    //RC
    @Override
    public void _D8() {
        if (simulator.getCy() == 1) {
            _C9();
        }
    }
    //RNC
    @Override
    public void _D0() {
        if (simulator.getCy() == 0) {
            _C9();
        }
    }
    //RP
    @Override
    public void _F0() {
        if (simulator.getS() == 0) {
            _C9();
        }
    }
    //RM
    @Override
    public void _F8() {
        if (simulator.getS() == 1) {
            _C9();
        }
    }
    //RZ
    @Override
    public void _C8() {
        if (simulator.getZ() == 1) {
            _C9();
        }
    }
    //RNZ
    @Override
    public void _C0() {
        if (simulator.getZ() == 0) {
            _C9();
        }
    }
    //RPE
    @Override
    public void _E8() {
        if (simulator.getP() == 1) {
            _C9();
        }
    }
    //RPO
    @Override
    public void _E0() {
        if (simulator.getP() == 0) {
            _C9();
        }
    }

    /**
     * ****************** CALL PSW  ********************
     */
    //CC Cy=1 _DC()
    @Override
    public void _DC() {
        if (simulator.getCy() == 1) {
            _CD();
        }
    }
    //CNC Cy=0 _D4()
    @Override
    public void _D4() {
        if (simulator.getCy() == 0) {
            _CD();
        }
    }
    //CP S=0 _F4()
    @Override
    public void _F4() {
        if (simulator.getS() == 0) {
            _CD();
        }
    }
    //CM S=1 _FC()
    @Override
    public void _FC() {
        if (simulator.getS() == 1) {
            _CD();
        }
    }
    //CZ Z=1 _CC()
    @Override
    public void _CC() {
        if (simulator.getZ() == 1) {
            _CD();
        }
    }
    //CNZ Z=0 _C4()
    @Override
    public void _C4() {
        if (simulator.getZ() == 0) {
            _CD();
        }
    }
    //CPE P=1 _EC()
    @Override
    public void _EC() {
        if (simulator.getP() == 1) {
            _CD();
        }
    }
    //CPO P=0 _E4()
    @Override
    public void _E4() {
        if (simulator.getP() == 1) {
            _CD();
        }
    }

    /**
     * **********************************
     */

    //NOP
    @Override
    public void _00() {
        simulator.nextInstructionPointer();
    }

    //LDA
    @Override
    public void _3A() {
        simulator.nextInstructionPointer();
        IData s1 = simulator.getDataAtIP();
        simulator.nextInstructionPointer();
        IData s2 = simulator.getDataAtIP();
        simulator.nextInstructionPointer();

        simulator.setA(simulator.getData(Address.from(s2, s1)));
    }

    //LDAX B
    @Override
    public void _0A() {
        IAddress addr = Address.from(simulator.getB(), simulator.getC());
        IData data = simulator.getData(addr.intValue());
        simulator.setA(data);
        simulator.nextInstructionPointer();
    }

    //LDAX D
    @Override
    public void _1A() {
        IAddress addr = Address.from(simulator.getD(), simulator.getE());
        IData data = simulator.getData(addr.intValue());
        simulator.setA(data);
        simulator.nextInstructionPointer();
    }

    //LXI B _01()
    @Override
    public void _01() {
        simulator.nextInstructionPointer();
        simulator.setC(simulator.getDataAtIP());
        simulator.nextInstructionPointer();
        simulator.setB(simulator.getDataAtIP());
        simulator.nextInstructionPointer();
    }
    //LXI D _11()
    @Override
    public void _11() {
        simulator.nextInstructionPointer();
        simulator.setE(simulator.getDataAtIP());
        simulator.nextInstructionPointer();
        simulator.setD(simulator.getDataAtIP());
        simulator.nextInstructionPointer();
    }

    //LXI H _21()
    @Override
    public void _21() {
        simulator.nextInstructionPointer();
        simulator.setL(simulator.getDataAtIP());
        simulator.nextInstructionPointer();
        simulator.setH(simulator.getDataAtIP());
        simulator.nextInstructionPointer();
    }

    //LXI SP _31()
    @Override
    public void _31() {
        simulator.nextInstructionPointer();
        String s1 = simulator.getDataAtIP().hexValue();
        simulator.nextInstructionPointer();
        String s2 = simulator.getDataAtIP().hexValue();
        simulator.setSP(Address.from(s2 + s1));
        simulator.nextInstructionPointer();
    }

    //LHLD
    @Override
    public void _2A() {
        simulator.nextInstructionPointer();
        IData d1 = simulator.getDataAtIP();
        simulator.nextInstructionPointer();
        IData d2 = simulator.getDataAtIP();
        IAddress x = Address.from(d2, d1);
        simulator.setL(simulator.getData(x));
        IAddress nextAddr = Address.from(x.intValue() + 1);
        simulator.setH(simulator.getData(nextAddr));
        simulator.nextInstructionPointer();
    }

    //STA
    @Override
    public void _32() {
        simulator.nextInstructionPointer();
        IData s1 = simulator.getDataAtIP();
        simulator.nextInstructionPointer();
        IData s2 = simulator.getDataAtIP();
        simulator.setData(Address.from(s2, s1), simulator.getA());
        simulator.nextInstructionPointer();
    }

    //STAX B
    @Override
    public void _02() {
        IData s1 = simulator.getB();
        IData s2 = simulator.getC();
        simulator.setData(Address.from(s1, s2), simulator.getA());
        simulator.nextInstructionPointer();
    }

    //STAX D
    @Override
    public void _12() {
        IData s1 = simulator.getD();
        IData s2 = simulator.getE();
        simulator.setData(Address.from(s1, s2), simulator.getA());
        simulator.nextInstructionPointer();
    }

    //SHLD
    @Override
    public void _22() {
        simulator.nextInstructionPointer();
        IData s1 = simulator.getDataAtIP();
        simulator.nextInstructionPointer();
        IData s2 = simulator.getDataAtIP();
        IAddress x = Address.from(s2, s1);
        simulator.setData(x, simulator.getL());
        IAddress nextAddr = Address.from(x.intValue() + 1);
        simulator.setData(nextAddr, simulator.getH());
        simulator.nextInstructionPointer();
    }
    //RST 0
    @Override
    public void _C7() {
        simulator.setIP(IAddress.ZERO);
    }

    //RST 1
    @Override
    public void _CF() {
        simulator.setIP(Address.from("0008"));
    }

    //RST 2
    @Override
    public void _D7() {
        simulator.setIP(Address.from("0010"));
    }

    //RST 3
    @Override
    public void _DF() {
        simulator.setIP(Address.from("0018"));
    }

    //RST 4
    @Override
    public void _E7() {
        simulator.setIP(Address.from("0020"));
    }

    //RST 5
    @Override
    public void _EF() {
        simulator.setIP(Address.from("0028"));
    }

    //RST 6
    @Override
    public void _F7() {
        simulator.setIP(Address.from("0030"));
    }

    //RST 7
    @Override
    public void _FF() {
        simulator.setIP(Address.from("0030"));
    }

    //RLC
    @Override
    public void _07() {
        int r1 = simulator.getA().intValue();
        int x = r1 << 1;
        int y = x / 255;
        if (y == 1) {
            x = x % 256;
            simulator.setCy(1);
            x = x | 1;
        }
        if (y == 0) {
            simulator.setCy(0);
        }
        simulator.setA(Data.from(x));
    }
    //RRC
    @Override
    public void _0F() {
        int r1 = simulator.getA().intValue();
        int z = r1 & 1;
        int x = r1 >> 1;
        if (z == 1) {
            simulator.setCy(1);
            x = x | 128;
        }
        if (z == 0) {
            simulator.setCy(0);
        }
        simulator.setA(Data.from(x));
    }
    //RAL
    @Override
    public void _17() {
        int r1 = simulator.getA().intValue();
        int x = r1 << 1;
        int y = x / 256;
        if (simulator.getCy() == 1) {
            x = x | 1;
        }
        x = x & 255;
        simulator.setCy(y);
        simulator.setA(Data.from(x));
        simulator.nextInstructionPointer();
    }
    //SPHL
    @Override
    public void _F9() {
        simulator.setSP(Address.from(simulator.getH(), simulator.getL()));
        simulator.nextInstructionPointer();
    }
    //XTHL
    @Override
    public void _E3() {
        simulator.setData(simulator.getSP(), simulator.getL());
        simulator.decrementSP();
        simulator.setData(simulator.getSP(), simulator.getH());
        simulator.decrementSP();
        simulator.nextInstructionPointer();
    }

    //PUSH B
    @Override
    public void _C5() {
        simulator.setData(simulator.getSP(), simulator.getB());
        simulator.decrementSP();
        simulator.setData(simulator.getSP(), simulator.getC());
        simulator.decrementSP();
        simulator.nextInstructionPointer();
    }
    //PUSH D
    @Override
    public void _D5() {
        simulator.setData(simulator.getSP(), simulator.getD());
        simulator.decrementSP();
        simulator.setData(simulator.getSP(), simulator.getE());
        simulator.decrementSP();
        simulator.nextInstructionPointer();
    }
    //PUSH H
    @Override
    public void _E5() {
        simulator.setData(simulator.getSP(), simulator.getH());
        simulator.decrementSP();
        simulator.setData(simulator.getSP(), simulator.getL());
        simulator.decrementSP();
        simulator.nextInstructionPointer();
    }
    //POP B
    @Override
    public void _C1() {
        simulator.incrementSP();
        simulator.setC(simulator.getData(simulator.getSP()));
        simulator.incrementSP();
        simulator.setB(simulator.getData(simulator.getSP()));
        simulator.nextInstructionPointer();
    }
    //POP D
    @Override
    public void _D1() {
        simulator.incrementSP();
        simulator.setE(simulator.getData(simulator.getSP()));
        simulator.incrementSP();
        simulator.setD(simulator.getData(simulator.getSP()));
        simulator.nextInstructionPointer();
    }
    //POP H
    @Override
    public void _E1() {
        simulator.incrementSP();
        simulator.setL(simulator.getData(simulator.getSP()));
        simulator.incrementSP();
        simulator.setH(simulator.getData(simulator.getSP()));
        simulator.nextInstructionPointer();
    }
//POP PSW

    public void _F1() {
        simulator.incrementSP();
        int y = simulator.getData(simulator.getSP()).intValue();
        if ((y & 128) == 128) {
            simulator.setS(1);
        }
        if ((y & 64) == 64) {
            simulator.setZ(1);
        }
        if ((y & 16) == 16) {
            simulator.setAc(1);
        }
        if ((y & 4) == 4) {
            simulator.setP(1);
        }
        if ((y & 1) == 1) {
            simulator.setCy(1);
        }

        simulator.nextInstructionPointer();
        //JOptionPane.showMessageDialog(this, "[WIP - Work In Progress] POP PSW");
    }
    //PUSH PSW
    @Override
    public void _F5() {
        int x = 0;
        if (simulator.getS() == 1) {
            x = x | 128;
        }
        if (simulator.getZ() == 1) {
            x = x | 64;
        }
        if (simulator.getAc() == 1) {
            x = x | 16;
        }
        if (simulator.getP() == 1) {
            x = x | 4;
        }
        if (simulator.getCy() == 1) {
            x = x | 1;
        }
        simulator.setData(simulator.getSP(), Data.from(x));
        simulator.decrementSP();
        simulator.setS(0);
        simulator.setZ(0);
        simulator.setAc(0);
        simulator.setP(0);
        simulator.setCy(0);
        simulator.nextInstructionPointer();
        //JOptionPane.showMessageDialog(this, "[WIP - Work In Progress] PUSH PSW");
    }
    //INX B
    @Override
    public void _03() {
        IData s1 = simulator.getB();
        IData s2 = simulator.getC();
        int x = Address.from(s1, s2).intValue();
        x++;
        IAddress s3 = Address.from(x);
        simulator.setB(s3.getMSB());
        simulator.setC(s3.getLSB());
        simulator.nextInstructionPointer();
    }
    //INX D
    @Override
    public void _13() {
        IData s1 = simulator.getD();
        IData s2 = simulator.getE();
        int x = Address.from(s1, s2).intValue();
        x++;
        IAddress s3 = Address.from(x);
        simulator.setD(s3.getMSB());
        simulator.setE(s3.getLSB());
        simulator.nextInstructionPointer();
    }
    //INX H
    @Override
    public void _23() {
        IData s1 = simulator.getH();
        IData s2 = simulator.getL();
        int x = Address.from(s1, s2).intValue();
        x++;
        IAddress s3 = Address.from(x);
        simulator.setH(s3.getMSB());
        simulator.setL(s3.getLSB());
        simulator.nextInstructionPointer();
    }
    //INX SP
    @Override
    public void _33() {
        simulator.incrementSP();
        simulator.nextInstructionPointer();
    }
    //DCX B
    @Override
    public void _0B() {
        IData s1 = simulator.getB();
        IData s2 = simulator.getC();
        int x = Address.from(s1, s2).intValue();
        x--;
        if (x < 0) {
            x = x * (-1);
        }
        IAddress s3 = Address.from(x);
        simulator.setB(s3.getMSB());
        simulator.setC(s3.getLSB());
        simulator.nextInstructionPointer();
    }
    //DCX D
    @Override
    public void _1B() {
        IData s1 = simulator.getD();
        IData s2 = simulator.getE();
        int x = Address.from(s1, s2).intValue();
        x--;
        if (x < 0) {
            x = x * (-1);
        }
        IAddress s3 = Address.from(x);
        simulator.setD(s3.getMSB());
        simulator.setE(s3.getLSB());
        simulator.nextInstructionPointer();
    }
    //DCX H
    @Override
    public void _2B() {
        IData s1 = simulator.getH();
        IData s2 = simulator.getL();
        int x = Address.from(s1, s2).intValue();
        x--;
        if (x < 0) {
            x = x * (-1);
        }
        IAddress s3 = Address.from(x);
        simulator.setH(s3.getMSB());
        simulator.setL(s3.getLSB());
        simulator.nextInstructionPointer();
    }
    //DCX SP
    @Override
    public void _3B() {
        simulator.decrementSP();
        simulator.nextInstructionPointer();
    }
    //CMA
    @Override
    public void _2F() {
        int r1;
        r1 = simulator.getA().intValue();
        r1 = ~r1;
        String temp = Integer.toHexString(r1);

        simulator.setA( Data.from(temp.substring((temp.length() - 2))) );
        simulator.nextInstructionPointer();
    }
    //CMC
    @Override
    public void _3F() {
        if (simulator.getCy() == 1) {
            simulator.setCy(0);
        } else {
            simulator.setCy(1);
        }
        simulator.nextInstructionPointer();
    }

    //PCHL
    @Override
    public void _E9() {
        simulator.setIP(getHlAddress());
        simulator.nextInstructionPointer();
    }

    private IAddress getHlAddress() {
        IData localH, localL;
        localH = simulator.getH();
        localL = simulator.getL();
        return Address.from(localH, localL);
    }

    //CPI data
    @Override
    public void _FE() {
        int r1, r2;
        r1 = simulator.getA().intValue();
        simulator.nextInstructionPointer();
        r2 = simulator.getDataAtIP().intValue();
        if (r1 < r2) {
            simulator.setCy(1);
        }
        if (r1 == r2) {
            simulator.setZ(1);
        }
        if (r1 > r2) {
            simulator.setZ(0);
            simulator.setCy(0);
        }
        simulator.nextInstructionPointer();
    }
    //RAR _1F
    @Override
    public void _1F() {
        int x, y;
        x = simulator.getA().intValue();
        y = x % 2;
        x = x >> 1;
        if (simulator.getCy() == 1) {
            x = x | 128;
        }
        simulator.setCy(y);
        simulator.setA(Data.from(x));
        simulator.nextInstructionPointer();
    }
}
