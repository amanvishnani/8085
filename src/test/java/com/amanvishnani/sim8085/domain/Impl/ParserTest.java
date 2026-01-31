package com.amanvishnani.sim8085.domain.Impl;

import com.amanvishnani.sim8085.domain.IData;
import com.amanvishnani.sim8085.domain.IOpcode;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ParserTest {

    private final Parser parser = new Parser();

    @Test
    void getLineInstructions() {
        String code = "  MVI A, 05H \n\n MOV B, A \n  LABEL: HLT ";
        List<String> instructions = parser.getLineInstructions(code);
        assertEquals(3, instructions.size());
        assertEquals("MVI A, 05H", instructions.get(0));
        assertEquals("LABEL: HLT", instructions.get(2));
    }

    @ParameterizedTest
    @CsvSource({
            "'MOV A, B', '78'",
            "'MOV B, C', '41'",
            "'MOV C, D', '4A'",
            "'MOV D, E', '53'",
            "'MOV E, H', '5C'",
            "'MOV H, L', '65'",
            "'MOV L, M', '6E'",
            "'MOV M, A', '77'",
            "'ADD B', '80'",
            "'ADC C', '89'",
            "'SUB D', '92'",
            "'SBB E', '9B'",
            "'ANA H', 'A4'",
            "'XRA L', 'AD'",
            "'ORA M', 'B6'",
            "'CMP A', 'BF'",
            "'INR A', '3C'",
            "'DCR B', '05'",
            "'MVI A, 05', '3E'",
            "'MVI M, FF', '36'"
    })
    void testOpcodeResolution(String instruction, String expectedHex) {
        IOpcode opcode = parser.getOpcode(instruction);
        assertEquals(expectedHex, opcode.getOpcodeData().hexValue());
    }

    @ParameterizedTest
    @CsvSource({
            "'JMP 1234H', 'C3', 3",
            "'LDA 4000H', '3A', 3",
            "'STA 5000H', '32', 3",
            "'LXI H, 1000H', '21', 3",
            "'LXI SP, FFFFH', '31', 3",
            "'ADI 10H', 'C6', 2",
            "'CPI 00H', 'FE', 2",
            "'HLT', '76', 1",
            "'NOP', '00', 1",
            "'XCHG', 'EB', 1"
    })
    void testOpcodeLengthAndHex(String instruction, String expectedHex, int expectedLength) {
        IOpcode opcode = parser.getOpcode(instruction);
        assertEquals(expectedHex, opcode.getOpcodeData().hexValue());
        assertEquals(expectedLength, opcode.getOpcodeLength());
    }

    @Test
    void testCaseInsensitivityAndSpacing() {
        assertEquals("78", parser.getOpcode("mov a,   b").getOpcodeData().hexValue());
        assertEquals("78", parser.getOpcode("  MOV   A  ,   B  ").getOpcodeData().hexValue());
        assertEquals("3E", parser.getOpcode("mvi a, 10h").getOpcodeData().hexValue());
    }

    @Test
    void testLabelExtraction() {
        assertEquals("START", parser.extractLabel("START: MOV A, B"));
        assertEquals("LOOP", parser.extractLabel("  LOOP  : ADD C"));
        assertEquals("", parser.extractLabel("MOV A, B"));
    }

    @Test
    void testDataExtraction() {
        assertEquals("05", parser.extractData("MVI A, 05H").hexValue());
        assertEquals("FF", parser.extractData("MVI B, FF").hexValue());
        assertEquals("10", parser.extractData("ADI 10H").hexValue());
        assertEquals("05", parser.extractData("ADI 5H").hexValue());
        assertNull(parser.extractData("HLT"));
    }

    @Test
    void testAddressExtraction() {
        assertEquals("1234", parser.extractOperandAddress("JMP 1234H").hexValue());
        assertEquals("4000", parser.extractOperandAddress("STA 4000").hexValue());
        assertEquals("0100", parser.extractOperandAddress("LXI H, 100H").hexValue());
        assertNull(parser.extractOperandAddress("MOV A, B"));
    }

    @Test
    void testOperandLabelExtraction() {
        IOpcode opcode = parser.getOpcode("JMP TARGET");
        assertEquals("TARGET", parser.extractOperandLabel(opcode));

        opcode = parser.getOpcode("JZ LOOP_1");
        assertEquals("LOOP_1", parser.extractOperandLabel(opcode));

        opcode = parser.getOpcode("HLT");
        assertEquals("", parser.extractOperandLabel(opcode));

        opcode = parser.getOpcode("JMP 1000H");
        assertEquals("", parser.extractOperandLabel(opcode));
    }

    @Test
    void testInvalidInstructions() {
        assertThrows(RuntimeException.class, () -> parser.getOpcode("INVALID A, B"));
        assertThrows(RuntimeException.class, () -> parser.getOpcode("MOV X, Y"));
        assertThrows(RuntimeException.class, () -> parser.getOpcode("LXI A, 1000")); // A is not valid for LXI
    }
}
