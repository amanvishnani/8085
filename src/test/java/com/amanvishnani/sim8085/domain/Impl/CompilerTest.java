package com.amanvishnani.sim8085.domain.Impl;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

class CompilerTest {

    @Test
    void compile() {
        Compiler compiler = new Compiler();
        ArrayList<InstructionRow> code = compiler.compile("MVI A, 0FH");
        InstructionRow row = InstructionRow.createInstructionRow(0);
        row.setData(Data.from("3E"));
        row.setInstruction("MVI A, 0FH");
        row.setAddress(Address.from("0000"));
        assertEquals(row, code.get(0));
    }
}