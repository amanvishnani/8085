package com.amanvishnani.sim8085.domain.Impl;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class OpcodeTest {

    @Test
    void testCreateOpcode() {
        Opcode opcode = Opcode.createOpcode("00", 1, "NOP");
        assertEquals("NOP", opcode.getInstruction());
        assertEquals("00", opcode.getOpcodeData().hexValue());
        assertEquals(1, opcode.getOpcodeLength());
    }

    @Test
    void testEqualsAndHashCode() {
        Opcode opcode1 = Opcode.createOpcode("00", 1, "NOP");
        Opcode opcode2 = Opcode.createOpcode("00", 1, "NOP");
        Opcode opcode3 = Opcode.createOpcode("01", 1, "LXI B");
        
        assertEquals(opcode1, opcode2); // same opcode data, opcode length, and instruction
        assertNotEquals(opcode1, opcode3); // different opcode data
        assertNotEquals(opcode1, null); // not equal to null
        assertNotEquals(opcode1, new Object()); // not equal to different object type
        assertEquals(opcode1.hashCode(), opcode2.hashCode()); // equal hash codes for equal objects
    }
}
