package com.amanvishnani.sim8085.domain.Impl;

import com.amanvishnani.sim8085.domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SimulatorTest {

    private Simulator simulator;

    @BeforeEach
    void setUp() {
        simulator = new Simulator();
        simulator.initialize();
    }

    @Test
    void testInitialization() {
        assertEquals("00", simulator.getA().hexValue());
        assertEquals("0000", simulator.getIP().hexValue());
        assertEquals("FFFF", simulator.getSP().hexValue());
        assertEquals(0, simulator.getCy());
        assertEquals(0, simulator.getZ());
    }

    @Test
    void testRegisterUpdates() {
        simulator.setA(Data.from("0A"));
        simulator.setB(Data.from("0B"));
        simulator.setC(Data.from("0C"));
        simulator.setD(Data.from("0D"));
        simulator.setE(Data.from("0E"));
        simulator.setH(Data.from("0F"));
        simulator.setL(Data.from("10"));

        assertEquals("0A", simulator.getA().hexValue());
        assertEquals("0B", simulator.getB().hexValue());
        assertEquals("0C", simulator.getC().hexValue());
        assertEquals("0D", simulator.getD().hexValue());
        assertEquals("0E", simulator.getE().hexValue());
        assertEquals("0F", simulator.getH().hexValue());
        assertEquals("10", simulator.getL().hexValue());
    }

    @Test
    void testFlagUpdates() {
        simulator.setCy(1);
        simulator.setZ(1);
        simulator.setS(1);
        simulator.setP(1);
        simulator.setAc(1);

        assertEquals(1, simulator.getCy());
        assertEquals(1, simulator.getZ());
        assertEquals(1, simulator.getS());
        assertEquals(1, simulator.getP());
        assertEquals(1, simulator.getAc());
    }

    @Test
    void testMemoryOperations() {
        IAddress addr = Address.from("4000");
        IData data = Data.from("FF");
        simulator.setData(addr, data);
        assertEquals("FF", simulator.getData(addr).hexValue());

        simulator.setData(0x5000, "AA");
        assertEquals("AA", simulator.getData(0x5000).hexValue());
    }

    @Test
    void testMemoryIndirectOperations() {
        simulator.setH(Data.from("40"));
        simulator.setL(Data.from("00"));
        simulator.setM(Data.from("CC"));

        assertEquals("CC", simulator.getM().hexValue());
        assertEquals("CC", simulator.getData(Address.from("4000")).hexValue());
    }

    @Test
    void testSPandIPManipulation() {
        simulator.setIP(Address.from("1000"));
        simulator.nextInstructionPointer();
        assertEquals("1001", simulator.getIP().hexValue());

        simulator.setSP(Address.from("F000"));
        simulator.decrementSP();
        assertEquals("EFFF", simulator.getSP().hexValue());
        simulator.incrementSP();
        assertEquals("F000", simulator.getSP().hexValue());
    }

    @Test
    void testExecuteNOP() {
        // NOP opcode is 00
        simulator.setIP(Address.from("1000"));
        simulator.execute("00");
        assertEquals("1001", simulator.getIP().hexValue());
    }

    @Test
    void testExecuteMOV_A_B() {
        simulator.setB(Data.from("55"));
        // MOV A, B opcode is 78
        simulator.execute("78");
        assertEquals("55", simulator.getA().hexValue());
    }

    @Test
    void testNextInstructionPointerRange() {
        simulator.setIP(Address.from(16382));
        simulator.nextInstructionPointer();
        assertEquals(16383, simulator.getIP().intValue());

        assertThrows(RuntimeException.class, () -> simulator.nextInstructionPointer());
    }

    @Test
    void testOnInstructionExecuted() {
        boolean[] executed = { false };
        simulator.onInstructionExecuted(ie -> {
            executed[0] = true;
            assertEquals("00", ie.getInstruction().hexValue());
        });
        simulator.execute("00");
        assertTrue(executed[0]);
    }

    @Test
    void testOnError() {
        boolean[] errorCalled = { false };
        simulator.onError(e -> {
            errorCalled[0] = true;
            assertTrue(e.getMessage().contains("NoSuchMethodException"));
        });
        simulator.execute("INVALID");
        assertTrue(errorCalled[0]);
    }

    @Test
    void testUpdateFlags() {
        Flags f = Flags.newInstance();
        f.setFlag(Flag.Cy, 1);
        f.setFlag(Flag.Z, 1);

        simulator.updateFlags(f);
        assertEquals(1, simulator.getCy());
        assertEquals(1, simulator.getZ());
        assertEquals(0, simulator.getS());
    }

    @Test
    void testCompileAndExecute() {
        String code = "MVI A, 10H\nMVI B, 20H\nADD B\nHLT";
        var rows = simulator.compile(code);
        assertNotNull(rows);
        assertFalse(rows.isEmpty());

        // Manual execution sequence
        simulator.setIP(Address.from(0));

        // The compile method loads data into memory from address 0, 1, 2...
        // But wait, compile implementation in Simulator:
        // for (int i = 0; i < rowSize; i++) {
        // setData(i, rows.get(i).getData().hexValue());
        // }
        // This is a bit simplistic as it doesn't account for instruction length
        // but it's what's currently there.

        simulator.execute(simulator.getDataAtIP().hexValue()); // MVI A (3E)
        assertEquals("10", simulator.getA().hexValue());

        simulator.execute(simulator.getDataAtIP().hexValue()); // MVI B (06)
        assertEquals("20", simulator.getB().hexValue());

        simulator.execute(simulator.getDataAtIP().hexValue()); // ADD B (80)
        assertEquals("30", simulator.getA().hexValue());
    }
}
