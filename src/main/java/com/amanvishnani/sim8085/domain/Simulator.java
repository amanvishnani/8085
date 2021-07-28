package com.amanvishnani.sim8085.domain;

import com.amanvishnani.sim8085.domain.Impl.*;
import io.reactivex.rxjava3.subjects.PublishSubject;

import java.lang.reflect.InvocationTargetException;
import java.util.Set;

public class Simulator implements I8085 {

    private final Flags flags;
    private IRegister A, B, C, D, E, H, L;
    private IAddress IP, SP;
    private final IMemory memory = Memory.makeMemory();

    private PublishSubject<InstructionExecuted> instructionExecuted$;
    private PublishSubject<RuntimeException> onError$;

    private final IExecutor executor;

    public Simulator() {
        flags = Flags.newInstance();
        this.initialize();
        executor = Executor.createExecutor(this);
    }

    public IMemory getMemory() {
        return memory;
    }

    @Override
    public void initialize() {
        setA(IData.ZERO);
        setB(IData.ZERO);
        setC(IData.ZERO);
        setD(IData.ZERO);
        setE(IData.ZERO);
        setH(IData.ZERO);
        setL(IData.ZERO);
        setCy(0);
        setAc(0);
        setS(0);
        setZ(0);
        setP(0);
        setIP(IAddress.ZERO);
        setSP(IAddress.FFFF);
        setInstructionExecuted$(PublishSubject.create());
        setOnError$(PublishSubject.create());
    }

    @Override
    public PublishSubject<InstructionExecuted> getInstructionExecuted$() {
        return instructionExecuted$;
    }

    public void setInstructionExecuted$(PublishSubject<InstructionExecuted> instructionExecuted$) {
        this.instructionExecuted$ = instructionExecuted$;
    }

    @Override
    public PublishSubject<RuntimeException> getOnError$() {
        return onError$;
    }

    public void setOnError$(PublishSubject<RuntimeException> onError$) {
        this.onError$ = onError$;
    }

    @Override
    public void setA(IData x) {
        IRegister a = getA();
        if(a == null) {
            this.A = Register.makeRegister();
        }
        getA().update(x);
    }

    @Override
    public void setB(IData x) {
        IRegister r = getB();
        if(r == null) {
            this.B = Register.makeRegister();
        }
        getB().update(x);
    }

    @Override
    public void setC(IData x) {
        IRegister r = getC();
        if(r == null) {
            this.C = Register.makeRegister();
        }
        getC().update(x);
    }

    @Override
    public void setD(IData x) {
        IRegister r = getD();
        if(r == null) {
            this.D = Register.makeRegister();
        }
        getD().update(x);
    }

    @Override
    public void setE(IData x) {
        IRegister r = getE();
        if(r == null) {
            this.E = Register.makeRegister();
        }
        getE().update(x);
    }

    @Override
    public void setH(IData x) {
        IRegister r = getH();
        if(r == null) {
            this.H = Register.makeRegister();
        }
        getH().update(x);
    }

    @Override
    public void setL(IData x) {
        IRegister r = getL();
        if(r == null) {
            this.L = Register.makeRegister();
        }
        getL().update(x);
    }

    @Override
    public void setSP(IAddress x) {
        SP = x;
    }

    @Override
    public void setIP(IAddress x) {
        IP = x;
    }

    @Override
    public IRegister getA() {
        return A;
    }

    @Override
    public IRegister getB() {
        return B;
    }

    @Override
    public IRegister getC() {
        return C;
    }

    @Override
    public IRegister getD() {
        return D;
    }

    @Override
    public IRegister getE() {
        return E;
    }

    @Override
    public IRegister getH() {
        return H;
    }

    public IRegister getL() {
        return L;
    }

    @Override
    public IAddress getSP() {
        return SP;
    }

    @Override
    public IAddress getIP() {
        return IP;
    }

    @Override
    public Flags getFlags() {
        return this.flags;
    }

    @Override
    public void nextInstructionPointer() {
        int x = getIP().intValue();
        if (x < 16383) {
            x++;
            IAddress nextAddress = Address.from(x);
            setIP(nextAddress);
        } else {
            throw newException("IP exceeding 3FFF (16383)");
        }
    }

    @Override
    public void execute(String op) {
        try {
            executor.getClass().getMethod("_"+op).invoke(executor);
            InstructionExecuted instructionExecuted = new InstructionExecuted();
            instructionExecuted.setInstruction(Data.from(op));
            instructionExecuted.setNextAddress(getIP());
            this.getInstructionExecuted$().onNext(instructionExecuted);
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            onError$.onNext(newException(e.getMessage()));
        }
    }

    @Override
    public void setS(Integer x) {
        if (x < 2 && x > -1) {
            flags.setFlag(Flag.S, x);
        }
        // @TODO: Handel if not the case
    }

    @Override
    public void setZ(Integer x) {
        if (x < 2 && x > -1) {
            flags.setFlag(Flag.Z, x);
        }
        // @TODO: Handel if not the case
    }

    @Override
    public void setAc(Integer x) {
        if (x < 2 && x > -1) {
            flags.setFlag(Flag.Ac, x);
        }
        // @TODO: Handel if not the case
    }

    @Override
    public void setP(Integer x) {
        if (x < 2 && x > -1) {
            flags.setFlag(Flag.P, x);
        }
        // @TODO: Handel if not the case
    }

    @Override
    public void setCy(Integer x) {
        if (x < 2 && x > -1) {
            flags.setFlag(Flag.Cy, x);
        }
        // @TODO: Handel if not the case
    }

    public Integer getS() {
        return flags.getFlag(Flag.S);
    }

    public Integer getZ() {
        return flags.getFlag(Flag.Z);
    }

    public Integer getCy() {
        return flags.getFlag(Flag.Cy);
    }

    public Integer getP() {
        return flags.getFlag(Flag.P);
    }

    public Integer getAc() {
        return flags.getFlag(Flag.Ac);
    }

    private RuntimeException newException(String message) {
        return new RuntimeException(message);
    }

    @Override
    public IData getData(IAddress address) {
        return memory.getData(address.intValue());
    }

    @Override
    public IData getData(Integer i) {
        return memory.getData(i);
    }

    @Override
    public void setData(IAddress address, IData data) {
        memory.setData(address, data);
    }

    @Override
    public void setData(Integer address, String data) {
        memory.setData(address, data);
    }

    @Override
    public IData getM() {
        return getData(getHlAddress());
    }

    @Override
    public void setM(IData x) {
        setData(getHlAddress(), x);
    }

    private IAddress getHlAddress() {
        IData localH, localL;
        localH = getH();
        localL = getL();
        return Address.from(localH, localL);
    }

    @Override
    public IData getDataAtIP() {
        return getData(getIP());
    }

    @Override
    public void updateFlags(IFlags flags) {
        Set<Flag> keys = flags.getKeys();
        for(Flag k: keys) {
            switch (k) {
                case P: {
                    setP(flags.getFlag(Flag.P));
                    break;
                }
                case S: {
                    setS(flags.getFlag(Flag.S));
                    break;
                }
                case Ac: {
                    setAc(flags.getFlag(Flag.Ac));
                    break;
                }
                case Cy: {
                    setCy(flags.getFlag(Flag.Cy));
                    break;
                }
                case Z: {
                    setZ(flags.getFlag(Flag.Z));
                    break;
                }
            }
        }
    }

    @Override
    public void decrementSP() {
        int x = getSP().intValue();
        x--;
        setSP(Address.from(x));
    }

    @Override
    public void incrementSP() {
        int x = getSP().intValue();
        x++;
        setSP(Address.from(x));
    }

}
