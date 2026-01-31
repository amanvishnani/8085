package com.amanvishnani.sim8085.domain.Impl;

import com.amanvishnani.sim8085.domain.IAddress;
import com.amanvishnani.sim8085.domain.IData;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MemoryRowTest {

    @Test
    void testInitialization() {
        MemoryRow row = MemoryRow.from(0x1234);
        assertEquals("1234", row.getAddress().hexValue());
        assertEquals("00", row.getData().hexValue());
    }

    @Test
    void testSetGetData() {
        MemoryRow row = MemoryRow.from(0x0000);
        IData newData = Data.from("FF");
        row.setData(newData);
        assertEquals("FF", row.getData().hexValue());
        assertEquals(newData, row.getData());
    }

    @Test
    void testSetGetAddress() {
        MemoryRow row = MemoryRow.from(0x0000);
        IAddress newAddress = Address.from(0xFFFF);
        row.setAddress(newAddress);
        assertEquals("FFFF", row.getAddress().hexValue());
        assertEquals(newAddress, row.getAddress());
    }
}
