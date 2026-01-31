package com.amanvishnani.sim8085;

import com.amanvishnani.sim8085.domain.I8085;
import com.amanvishnani.sim8085.domain.Simulator;
import com.amanvishnani.sim8085.domain.Impl.Data;
import com.amanvishnani.sim8085.domain.Impl.InstructionRow;
import org.junit.jupiter.api.Test;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import static org.junit.jupiter.api.Assertions.*;

class MainHelperTest {

    @Test
    void testParseHexValid() {
        AtomicReference<String> error = new AtomicReference<>();
        MainHelper.ErrorReporter reporter = error::set;

        assertEquals(0x10, MainHelper.parseHex("10H", 0, 0, 0xFFFF, "Test", reporter));
        assertNull(error.get());

        assertEquals(0xFF, MainHelper.parseHex("FF", 0, 0, 0xFFFF, "Test", reporter));
        assertNull(error.get());

        assertEquals(0x0, MainHelper.parseHex("", 0, 0, 0xFFFF, "Test", reporter));
        assertNull(error.get());
    }

    @Test
    void testParseHexInvalidNumber() {
        AtomicReference<String> error = new AtomicReference<>();
        MainHelper.ErrorReporter reporter = error::set;

        assertEquals(-1, MainHelper.parseHex("G1", 0, 0, 0xFFFF, "Test", reporter));
        assertEquals("Make sure given Test is a Hex Number", error.get());
    }

    @Test
    void testParseHexOutOfRange() {
        AtomicReference<String> error = new AtomicReference<>();
        MainHelper.ErrorReporter reporter = error::set;

        assertEquals(-1, MainHelper.parseHex("100", 0, 0, 0xF, "Test", reporter));
        assertEquals("Make sure given Test is in range 0000 - 000F", error.get());
    }

    @Test
    void testGetMemoryTableData() {
        I8085 simulator = new Simulator();
        simulator.initialize();
        simulator.setData(0x4000, "FF");

        List<Object[]> data = MainHelper.getMemoryTableData(simulator, 0x4000, 0x4005);
        assertEquals(6, data.size());
        assertEquals("4000", data.get(0)[0]);
        assertEquals("FF", data.get(0)[1]);
        assertEquals("4001", data.get(1)[0]);
        assertEquals("00", data.get(1)[1]);
    }

    @Test
    void testGetCodeTableData() {
        List<InstructionRow> rows = new ArrayList<>();
        InstructionRow row = InstructionRow.createInstructionRow(0);
        row.setLabel("LABEL");
        row.setInstruction("MOV A,B");
        row.setData(Data.from("78"));
        rows.add(row);

        List<Object[]> data = MainHelper.getCodeTableData(rows, 0);
        assertEquals(1, data.size());
        assertEquals("0000", data.get(0)[0]);
        assertEquals("LABEL", data.get(0)[1]);
        assertEquals("MOV A,B", data.get(0)[2]);
        assertEquals("78", data.get(0)[3]);
    }

    @Test
    void testGetRegistersMap() {
        I8085 simulator = new Simulator();
        simulator.initialize();
        simulator.setA(Data.from("AA"));

        Map<String, String> regs = MainHelper.getRegistersMap(simulator);
        assertEquals("AA", regs.get("A"));
        assertEquals("00", regs.get("B"));
    }

    @Test
    void testGetFlagsMap() {
        I8085 simulator = new Simulator();
        simulator.initialize();
        Map<String, String> flags = MainHelper.getFlagsMap(simulator);
        assertEquals("0", flags.get("S"));
        assertEquals("0", flags.get("Z"));
    }
}
