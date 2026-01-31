package com.amanvishnani.sim8085;

import org.junit.jupiter.api.Test;
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
}
