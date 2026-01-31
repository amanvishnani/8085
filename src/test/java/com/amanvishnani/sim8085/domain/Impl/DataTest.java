package com.amanvishnani.sim8085.domain.Impl;

import com.amanvishnani.sim8085.domain.Flag;
import com.amanvishnani.sim8085.domain.IOperationResult;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DataTest {

    @Test
    void hexValue_zero() {
        var da = Data.from(0);
        Assertions.assertEquals("00", da.hexValue());
    }

    @Test
    void hexValue_255() {
        var da = Data.from(255);
        Assertions.assertEquals("FF", da.hexValue());
    }

    @Test
    void hexValue_256() {
        var da = Data.from(256);
        Assertions.assertEquals("00", da.hexValue());
    }

    @Test
    void intValue_zero() {
        var da = Data.from("00");
        Assertions.assertEquals(0, da.intValue());
    }

    @Test
    void intValue_255() {
        var da = Data.from("FF");
        Assertions.assertEquals(255, da.intValue());
    }

    @Test
    void add() {
        var da1 = Data.from("00");
        var da2 = Data.from("FF");

        var da3 = da1.add(da2);
        assertEquals("FF", da3.getData().hexValue());
        assertEquals(0, da3.getFlags().getFlag(Flag.Cy));
        assertEquals(0, da3.getFlags().getFlag(Flag.Ac));
        assertEquals(1, da3.getFlags().getFlag(Flag.S));
        assertEquals(1, da3.getFlags().getFlag(Flag.P));
        assertEquals(0, da3.getFlags().getFlag(Flag.Z));
    }

    @Test
    void add_carry() {
        var da1 = Data.from("01");
        var da2 = Data.from("FF");

        var da3 = da1.add(da2);
        assertEquals(da3.getData().hexValue(), "00");
        assertEquals(1, da3.getFlags().getFlag(Flag.Cy));
        assertEquals(1, da3.getFlags().getFlag(Flag.Ac));
        assertEquals(0, da3.getFlags().getFlag(Flag.S));
        assertEquals(1, da3.getFlags().getFlag(Flag.Z));
    }

    @Test
    void getLSB() {
        var da1 = Data.from("11");
        assertEquals(1, da1.getLSB().intValue());
    }

    @Test
    void getLSB_15() {
        var da1 = Data.from("2F");
        assertEquals(15, da1.getLSB().intValue());
    }

    @Test
    void testAdd_withCarry() {
        var da1 = Data.from("2F");
        var da2 = Data.from("02");
        var op = da1.add(da2, 1);
        assertEquals("32", op.getData().hexValue());
        assertEquals(0, op.getFlags().getFlag(Flag.Cy));
        assertEquals(1, op.getFlags().getFlag(Flag.Ac));
        assertEquals(0, op.getFlags().getFlag(Flag.S));
        assertEquals(0, op.getFlags().getFlag(Flag.P));
        assertEquals(0, op.getFlags().getFlag(Flag.Z));
    }

    @Test
    void minus() {
    }

    @Test
    void toRegister() {
    }

    @Test
    void testToString() {
        var da = Data.from(15);
        Assertions.assertTrue(da.toString().contains("0F"));
    }

    @Test
    void from() {
        Assertions.assertNotNull(Data.from("0"));
        Assertions.assertNotNull(Data.from("F"));
        Assertions.assertNotNull(Data.from("FF"));
        Assertions.assertNotNull(Data.from("FFF"));
    }

    @Test
    void testFrom() {
        Assertions.assertNotNull(Data.from(0));
        Assertions.assertNotNull(Data.from(12));
        Assertions.assertNotNull(Data.from(1000));
        Assertions.assertEquals(Data.from(255), Data.from("FF"));
    }

    @Test
    void getDataValue() {
        var d = Data.from(10);
        assertEquals(d.getDataValue(), "0A");
    }

    @Test
    void setDataValue() {
        var d = Data.from(10);
        d.setDataValue("0B");
        assertEquals(d.getDataValue(), "0B");
    }

    @Test
    void hashCodeTest() {
        var d1 = Data.from(10);
        var d2 = Data.from("0A");
        assertEquals(d1.hashCode(), d2.hashCode());
    }

    @Test
    void testMinus() {
        var d1 = Data.from(10);
        var d2 = Data.from("0A");
        IOperationResult r = d1.minus(d2);
        assertEquals(r.getData().intValue(), 0);
    }

    @Test
    void testMinus_ZFlag() {
        var d1 = Data.from(10);
        var d2 = Data.from("0A");
        IOperationResult r = d1.minus(d2);
        assertEquals(1, r.getFlags().getFlag(Flag.Z));
    }

    @Test
    void testMinus_S_P_Flag() {
        var d1 = Data.from(10);
        var d2 = Data.from("0D");
        IOperationResult r = d1.minus(d2);
        assertEquals(1, r.getFlags().getFlag(Flag.S));
        assertEquals(1, r.getFlags().getFlag(Flag.P));
    }
}