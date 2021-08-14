package com.amanvishnani.sim8085.domain.Impl;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

class CompilerTest {

    @Test
    void compile() {
        Compiler compiler = new Compiler();
        var codeString = """
                MVI A,0FH
                MVI B,12H
                SUB B
                JM NEXT
                STA 4000H
                NEXT:CALL SBROUT
                HLT
                
                SBROUT:STA 4005H
                RET
                """;
        ArrayList<InstructionRow> code = compiler.compile(codeString);

        for (int i = 0; i < code.size(); i++) {
            var addr = code.get(i).getAddress().intValue();
            assertEquals(i, addr);
        }

        InstructionRow row;
        int i;

        i = 0;
        row = InstructionRow.createInstructionRow(i);
        row.setData(Data.from("3E"));
        row.setInstruction("MVI A,0FH");
        assertEquals(row, code.get(i));

        i = 1;
        row = InstructionRow.createInstructionRow(i);
        row.setData(Data.from("0F"));
        assertEquals(row, code.get(i));

        i = 2;
        row = InstructionRow.createInstructionRow(i);
        row.setData(Data.from("06"));
        row.setInstruction("MVI B,12H");
        assertEquals(row, code.get(i));

        i = 3;
        row = InstructionRow.createInstructionRow(i);
        row.setData(Data.from("12"));
        assertEquals(row, code.get(i));

        i = 4;
        row = InstructionRow.createInstructionRow(i);
        row.setData(Data.from("90"));
        row.setInstruction("SUB B");
        assertEquals(row, code.get(i));

        i = 5;
        row = InstructionRow.createInstructionRow(i);
        row.setData(Data.from("FA"));
        row.setInstruction("JM NEXT");
        assertEquals(row, code.get(i));

        i = 6;
        row = InstructionRow.createInstructionRow(i);
        row.setData(Data.from("0B"));
        assertEquals(row, code.get(i));

        i = 7;
        row = InstructionRow.createInstructionRow(i);
        row.setData(Data.from("00"));
        assertEquals(row, code.get(i));


        row = InstructionRow.createInstructionRow(11);
        row.setData(Data.from("CD"));
        row.setLabel("NEXT");
        row.setInstruction("CALL SBROUT");
        assertEquals(row, code.get(11));

        row = InstructionRow.createInstructionRow(12);
        row.setData(Data.from("0F"));
        assertEquals(row, code.get(12));


    }
}