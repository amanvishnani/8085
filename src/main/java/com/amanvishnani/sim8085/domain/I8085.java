package com.amanvishnani.sim8085.domain;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

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
    void setA(IData x);
    void setB(IData x);
    void setC(IData x);
    void setD(IData x);
    void setE(IData x);
    void setH(IData x);
    void setL(IData x);

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
}
