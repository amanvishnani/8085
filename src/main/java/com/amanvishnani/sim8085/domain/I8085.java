package com.amanvishnani.sim8085.domain;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import com.amanvishnani.sim8085.domain.Impl.Flags;
import com.amanvishnani.sim8085.domain.Impl.InstructionExecuted;
import com.amanvishnani.sim8085.domain.Impl.InstructionRow;
import io.reactivex.rxjava3.subjects.PublishSubject;

import java.util.ArrayList;

/**
 *
 * @author Aman Vishnani
 */
public interface I8085 {
    void nextInstructionPointer();

    IRegister getA();
    IRegister getB();
    IRegister getC();
    IRegister getD();
    IRegister getE();
    IRegister getH();
    IRegister getL();
    IData getM();
    Flags getFlags();
    void setA(IData x);
    void setB(IData x);
    void setC(IData x);
    void setD(IData x);
    void setE(IData x);
    void setH(IData x);
    void setL(IData x);
    void setM(IData x);

    void execute(String op);

    void setSP(IAddress x);
    void setIP(IAddress x);
    IAddress getSP();
    IAddress getIP();

    void setS(Integer x);
    void setZ(Integer x);
    void setAc(Integer x);
    void setP(Integer x);
    void setCy(Integer x);
    Integer getS();
    Integer getZ();
    Integer getP();
    Integer getCy();
    Integer getAc();

    IData getData(Integer i);
    IData getData(IAddress i);

    void setData(IAddress i, IData data);
    void setData(Integer i, String data);

    PublishSubject<InstructionExecuted> getInstructionExecuted$();
    PublishSubject<RuntimeException> getOnError$();
    void initialize();
    IData getDataAtIP();
    void updateFlags(IFlags flags);
    void decrementSP();
    void incrementSP();

    IMemory getMemory();

    ArrayList<InstructionRow> compile(String code);
}
