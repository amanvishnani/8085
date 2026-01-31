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
            "'MVI M, FF', '36'",
            "'PUSH B', 'C5'",
            "'PUSH D', 'D5'",
            "'PUSH H', 'E5'",
            "'PUSH PSW', 'F5'",
            "'POP B', 'C1'",
            "'POP D', 'D1'",
            "'POP H', 'E1'",
            "'POP PSW', 'F1'",
            "'JMP 1000H', 'C3'",
            "'JZ 1000H', 'CA'",
            "'JNZ 1000H', 'C2'",
            "'JC 1000H', 'DA'",
            "'JNC 1000H', 'D2'",
            "'JP 1000H', 'F2'",
            "'JM 1000H', 'FA'",
            "'JPE 1000H', 'EA'",
            "'JPO 1000H', 'E2'",
            "'CALL 1000H', 'CD'",
            "'CZ 1000H', 'CC'",
            "'CNZ 1000H', 'C4'",
            "'CC 1000H', 'DC'",
            "'CNC 1000H', 'D4'",
            "'CP 1000H', 'F4'",
            "'CM 1000H', 'FC'",
            "'CPE 1000H', 'EC'",
            "'CPO 1000H', 'E4'",
            "'ADI 10H', 'C6'",
            "'ACI 10H', 'CE'",
            "'SUI 10H', 'D6'",
            "'SBI 10H', 'DE'",
            "'ANI 10H', 'E6'",
            "'XRI 10H', 'EE'",
            "'ORI 10H', 'F6'",
            "'CPI 10H', 'FE'",
            "'CMA', '2F'",
            "'CMC', '3F'",
            "'DAA', '27'",
            "'DI', 'F3'",
            "'EI', 'FB'",
            "'HLT', '76'",
            "'NOP', '00'",
            "'PCHL', 'E9'",
            "'RAL', '17'",
            "'RAR', '1F'",
            "'RET', 'C9'",
            "'RIM', '20'",
            "'RLC', '07'",
            "'RRC', '0F'",
            "'SIM', '30'",
            "'STC', '37'",
            "'XCHG', 'EB'",
            "'XTHL', 'E3'",
            "'SPHL', 'F9'",
            "'LHLD 2000H', '2A'",
            "'SHLD 2000H', '22'",
            "'LXI B, 1000H', '01'",
            "'LXI D, 1000H', '11'",
            "'LXI H, 1000H', '21'",
            "'LXI SP, 1000H', '31'"
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
        assertThrows(RuntimeException.class, () -> parser.getOpcode("LXI A, 1000H"));
        assertThrows(RuntimeException.class, () -> parser.getOpcode("LXI B")); // Missing operand
        assertThrows(RuntimeException.class, () -> parser.getOpcode("PUSH A"));
        assertThrows(RuntimeException.class, () -> parser.getOpcode("POP C"));
        assertThrows(RuntimeException.class, () -> parser.getOpcode("LXI X, 0000H"));
        assertThrows(RuntimeException.class, () -> parser.getOpcode("ADD X"));
    }
}
