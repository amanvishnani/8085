package com.amanvishnani.sim8085.domain;

import com.amanvishnani.sim8085.domain.Impl.*;
import com.amanvishnani.sim8085.domain.Impl.Compiler;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.function.Consumer;

public class Simulator implements I8085 {

    private final Flags flags;
    private IRegister A, B, C, D, E, H, L;
    private IAddress IP, SP;
    private final IMemory memory = Memory.makeMemory();
    private Set<Consumer<InstructionExecuted>> IESubscription;
    private Set<Consumer<RuntimeException>> errorSubscription;

    private Compiler compiler;

    private final IExecutor executor;
    private final Map<String, Runnable> instructionMap = new HashMap<>();

    public Simulator() {
        flags = Flags.newInstance();
        A = Register.makeRegister();
        B = Register.makeRegister();
        C = Register.makeRegister();
        D = Register.makeRegister();
        E = Register.makeRegister();
        H = Register.makeRegister();
        L = Register.makeRegister();
        this.initialize();
        executor = Executor.createExecutor(this);
        this.initializeInstructionMap();
    }

    private void initializeInstructionMap() {
        for (Method method : executor.getClass().getMethods()) {
            if (method.getName().startsWith("_")) {
                String op = method.getName().substring(1);
                instructionMap.put(op, () -> {
                    try {
                        method.invoke(executor);
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        throw new RuntimeException("Error executing instruction " + op, e);
                    }
                });
            }
        }
    }

    @Override
    public ArrayList<InstructionRow> compile(String code) {
        var rows = compiler.compile(code);
        var rowSize = rows.size();
        for (int i = 0; i < rowSize; i++) {
            setData(i, rows.get(i).getData().hexValue());
        }
        return rows;
    }

    @Override
    public void onInstructionExecuted(Consumer<InstructionExecuted> callback) {
        IESubscription.add(callback);
    }

    @Override
    public void onError(Consumer<RuntimeException> callback) {
        errorSubscription.add(callback);
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
        this.compiler = new Compiler();
        this.IESubscription = new HashSet<>();
        this.errorSubscription = new HashSet<>();
    }

    @Override
    public void setA(IData x) {
        A.update(x);
    }

    @Override
    public void setB(IData x) {
        B.update(x);
    }

    @Override
    public void setC(IData x) {
        C.update(x);
    }

    @Override
    public void setD(IData x) {
        D.update(x);
    }

    @Override
    public void setE(IData x) {
        E.update(x);
    }

    @Override
    public void setH(IData x) {
        H.update(x);
    }

    @Override
    public void setL(IData x) {
        L.update(x);
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
        Runnable runnable = instructionMap.get(op);
        if (runnable != null) {
            runnable.run();
            InstructionExecuted instructionExecuted = new InstructionExecuted();
            instructionExecuted.setInstruction(Data.from(op));
            instructionExecuted.setNextAddress(getIP());
            for (var ie : IESubscription) {
                ie.accept(instructionExecuted);
            }
        } else {
            for (var sub : errorSubscription) {
                sub.accept(newException("NoSuchMethodException: _" + op));
            }
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
        for (Flag k : keys) {
            switch (k) {
                case P -> setP(flags.getFlag(Flag.P));
                case S -> setS(flags.getFlag(Flag.S));
                case Ac -> setAc(flags.getFlag(Flag.Ac));
                case Cy -> setCy(flags.getFlag(Flag.Cy));
                case Z -> setZ(flags.getFlag(Flag.Z));
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
