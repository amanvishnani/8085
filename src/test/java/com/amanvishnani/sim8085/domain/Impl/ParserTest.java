package com.amanvishnani.sim8085.domain.Impl;

import com.amanvishnani.sim8085.domain.IData;
import com.amanvishnani.sim8085.domain.IOpcode;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ParserTest {

    private final Parser parser = new Parser();

    @Test
    void getLineInstructions() {
        String code = """
                MVI A, 05H
                MOV B, A
                STA 5000H
                LABEL: HLT
                """;
        List<String> instructions = parser.getLineInstructions(code);
        assertEquals(4, instructions.size());
        assertTrue(instructions.contains("LABEL: HLT"));
    }

    @Test
    void getOpcode() {
        IOpcode opcode = parser.getOpcode("AMAN:  JMP  LABEL");
        IOpcode opcode1 = Opcode.createOpcode("C3", 3, "JMP LABEL");
        assertEquals(opcode1, opcode);

        assertThrows(RuntimeException.class, () -> parser.getOpcode("HELLO"));
    }

    @Test
    void extractLabel() {
        String instruction = "AMAN:  JMP  LABEL";
        Assertions.assertEquals("AMAN", parser.extractLabel(instruction));
        instruction = "AMAN  :  JMP  LABEL";
        Assertions.assertEquals("AMAN", parser.extractLabel(instruction));
        instruction = "AMAN  :  HLT";
        Assertions.assertEquals("AMAN", parser.extractLabel(instruction));
        instruction = "HLT";
        Assertions.assertEquals("", parser.extractLabel(instruction));
    }

    @Test
    void extractData() {
        IData _05H = Data.from("05");
        String instruction = "AMAN:  MVI A,05H";
        Assertions.assertEquals(_05H, parser.extractData(instruction));
        instruction = "AMAN  :  MVI A , 05H";
        Assertions.assertEquals(_05H, parser.extractData(instruction));
        instruction = "AMAN  :  HLT";
        assertNull(parser.extractData(instruction));
        instruction = "MOV A,B";
        assertNull(parser.extractData(instruction));
    }

    @Test
    void extractOperandLabel() {
        IOpcode opcode = Opcode.createOpcode("0C", 3, "AMAN:  JMP  LABEL");
        Assertions.assertEquals("LABEL", parser.extractOperandLabel(opcode));
        opcode = Opcode.createOpcode("0C", 1, "HLT");
        Assertions.assertEquals("", parser.extractOperandLabel(opcode));
    }

    @Test
    void extractOperandAddress() {
        Assertions.assertEquals("4000", parser.extractOperandAddress("STA 4000H").hexValue());
        Assertions.assertNull(parser.extractOperandAddress("STA AMAN"));
        Assertions.assertNull(parser.extractOperandAddress("STA ABH"));
        Assertions.assertEquals("4000", parser.extractOperandAddress("LBL : STA 4000").hexValue());
    }
}