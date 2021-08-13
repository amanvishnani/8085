package com.amanvishnani.sim8085;


import com.amanvishnani.sim8085.domain.*;
import com.amanvishnani.sim8085.domain.Impl.*;
import io.reactivex.rxjava3.disposables.Disposable;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

@SpringBootApplication
public class Main extends javax.swing.JFrame implements IView {

    private I8085 simulator = new Simulator();

    public static int LABEL = 0, MEM = 1, SYMPTR, SYMPTR1, oldIP;
    public static String[][] ST = new String[100][2];
    public static String[][] ST1 = new String[100][2];
    private Instruction[] instructionMap = new InstructionImpl[16384];
    public Matcher m;
    public Pattern[] patterns = new Pattern[246];
    public final String data = "[0-9A-F]{2}(H)?";
    public final String label = "[A-Za-z]{3}[A-Za-z]*";
    public final String addr = "[0-9A-F]{4}(H)?";
    public final String space = "( )*";
    public final String space1 = "( )+";
    private Disposable instructionExecutedSub = Disposable.disposed();
    private Disposable errorSub = Disposable.disposed();

    @Override
    public void updateViewFlags() {
        Flags flags = simulator.getFlags();
        jS.setText(flags.getFlag(Flag.S).toString());
        jCy.setText(flags.getFlag(Flag.Cy).toString());
        jZ.setText(flags.getFlag(Flag.Z).toString());
        jAc.setText(flags.getFlag(Flag.Ac).toString());
        jP.setText(flags.getFlag(Flag.P).toString());
    }

    @Override
    public void updateViewRegisters() {
        jA.setText(simulator.getA().hexValue());
        jB.setText(simulator.getB().hexValue());
        jC.setText(simulator.getC().hexValue());
        jD.setText(simulator.getD().hexValue());
        jE.setText(simulator.getE().hexValue());
        jH.setText(simulator.getH().hexValue());
        jL.setText(simulator.getL().hexValue());
    }

    @Override
    public void updateViewPointers() {
        jIP.setText(simulator.getIP().hexValue());
        jSP.setText(simulator.getSP().hexValue());
    }

    @Override
    public void updateView() {
        updateViewFlags();
        updateViewRegisters();
        updateViewPointers();
        updateCodeTable();
    }

    private RuntimeException newException(String message) {
        return new RuntimeException(message);
    }

    public void loadInstructions() {
        simulator.initialize();
        subscribeInstructionPointer();

        initializeInstructionMap();
        oldIP = 0;
        String code = code_av.getText().toUpperCase();
        List<String> instructions = getLineInstructions(code);
        PassOne(instructions);
        PassTwo();
        String test123[] = simulator.getMemory().getSlice(0, 20).getHexDataArray();
        run_code.setListData(test123);
        run_code_index = 0;

        jStep.setEnabled(true);
        updateCodeTable();
    }

    List<String> getLineInstructions(String code) {
        String[] code_token = code.split("\\n");
        List<String> temp= new ArrayList<>();

        for (int j = 0; j < code_token.length; j++) {
            code_token[j] = trimSpaces(code_token[j]);
            if (0 != code_token[j].length()) {
                temp.add(code_token[j]);
            }
        }
        return temp;
    }

    public void initializeInstructionMap() {
        for (int i = 0; i < 16383; i++) {
            instructionMap[i] = InstructionImpl.newInstructionImpl();
        }
    }

    void updateCodeTable() {
        String S1 = CodeHead.getText();
        int codehead;
        try {
            if (S1.length() != 0) {
                codehead = Integer.parseInt(S1, 16);
            } else {
                codehead = 0;
            }
            if (codehead < 0 || codehead > 16383) {
                JOptionPane.showMessageDialog(this, "{Make sure given Code Segment Head is in Specified Range} ");
            } else {
                DefaultTableModel x = (DefaultTableModel) CodeTable.getModel();
                x.setRowCount(0);
                String[] obj = new String[4];
                int START, END;
                START = codehead;
                END = codehead + 20;
                for (int i = START; i < END; i++) {
                    obj[0] = Address.from(i).hexValue();
                    obj[1] = instructionMap[i].getLabel();
                    obj[2] = instructionMap[i].getOpcode();
                    obj[3] = simulator.getData(i).hexValue();
                    x.addRow(obj);
                }
            }
        } catch (Exception NumberfException) {
            JOptionPane.showMessageDialog(this, "{Make sure given Code Segment Head is a Hex Number} ");
        }
    }

    void refreshData() {
        String S1 = DataHead.getText();
        int datahead;
        try {
            if (S1.length() != 0) {
                datahead = Integer.parseInt(S1, 16);
            } else {
                datahead = 16384;
            }
            if (datahead < 16384 || datahead > 57343) {
                JOptionPane.showMessageDialog(this, "{Make sure given Code Segment Head is in Specified Range} ");
            } else {
                DefaultTableModel x = (DefaultTableModel) DataTable.getModel();
                x.setRowCount(0);
                Object[] obj = new Object[4];
                int START, END;
                START = datahead;
                END = datahead + 20;
                for (int i = START; i < END; i++) {
                    obj[0] = Address.from(i).hexValue();
                    obj[1] = simulator.getData(i).hexValue();
                    x.addRow(obj);
                }
            }
        } catch (Exception NumberfException) {
            JOptionPane.showMessageDialog(this, "{Make sure given Data Segment Head is a Hex Number} ");
        }
    }

    void refreshStack() {
        String S1 = StackHead.getText();
        int stackhead;
        try {
            if (S1.length() != 0) {
                stackhead = Integer.parseInt(S1, 16);
            } else {
                stackhead = 57344;
            }
            if (stackhead < 57344 || stackhead > 65535) {
                JOptionPane.showMessageDialog(this, "{Make sure given Stack Segment Head is in Specified Range} ");
            } else {
                DefaultTableModel x = (DefaultTableModel) StackTable.getModel();
                x.setRowCount(0);
                Object[] obj = new Object[4];
                int END = stackhead + 20;
                for (int i = stackhead; i < END; i++) {
                    obj[0] = Address.from(i).hexValue();
                    obj[1] = simulator.getData(i).hexValue();
                    x.addRow(obj);
                }
            }
        } catch (Exception NumberfException) {
            JOptionPane.showMessageDialog(this, "{Make sure given Data Segment Head is a Hex Number} ");
        }
    }

    public void PassTwo() {
        for (int i = 0; i < SYMPTR; i++) {
            String localLabel, localmem;
            localLabel = ST[i][LABEL].trim();
            localmem = ST[i][MEM];
            for (int j = 0; j < SYMPTR1; j++) {
                ST1[j][LABEL] = ST1[j][LABEL].trim();
                if (ST1[j][LABEL].equals(localLabel)) {
                    String localval = ST1[j][MEM];
                    IAddress address = Address.from(localmem);
                    IData data = Data.from(localval.substring(2, 4));
                    simulator.setData(address, data);
                    int x = Integer.parseInt(localmem, 16);
                    x++;
                    localmem = Address.from(x).hexValue();
                    address = Address.from(localmem);
                    data = Data.from(localval.substring(0, 2));
                    simulator.setData(address, data);
                }
            }

        }
    }

    public void PassOne(List<String> x) {
        int LP = 0;
        ST = null;
        ST = new String[100][2];
        SYMPTR = 0;
        ST1 = null;
        ST1 = new String[100][2];
        SYMPTR1 = 0;
        for (String s : x) {
            int a1 = findI(s);
            if (a1 >= 0) {
                int flag = 0;
                String opcode = findOpcode(a1);
                int opcodeLength = OpcodeLength(opcode);
                simulator.setData(LP, opcode);
                String l = ExtractLabel(s);
                instructionMap[LP].setLabel(l);
                instructionMap[LP].setOpcode(m.group());
                if (!l.isEmpty()) {
                    for (int g = 0; g < SYMPTR1; g++) {
                        if (ST1[g][LABEL].equalsIgnoreCase(l)) {
                            // JOptionPane.showMessageDialog(this, l + " already Exists at " + ST1[g][MEM] + " Terminating rest of the process");
                            flag = 1;
                        }
                    }
                    if (flag == 0) {
                        ST1[SYMPTR1][LABEL] = l;
                        ST1[SYMPTR1][MEM] = Address.from(LP).hexValue();
                        SYMPTR1++;
                    }
                }
                if (opcodeLength == 1) {
                    LP = LP + 1;
                } else if (opcodeLength == 2) {
                    LP = LP + 1;
                    simulator.setData(LP, ExtractData(s));
                    instructionMap[LP].setOpcode("");
                    instructionMap[LP].setLabel("");
                    LP = LP + 1;
                } else if (opcodeLength == 3) {
                    l = extractAddress(s);

                    if (l.length() != 0) {
                        LP = LP + 1;
                        instructionMap[LP].setOpcode("");
                        instructionMap[LP].setLabel("");
                        simulator.setData(LP, l.substring(2, 4));
                        LP = LP + 1;
                        instructionMap[LP].setOpcode("");
                        instructionMap[LP].setLabel("");
                        simulator.setData(LP, l.substring(0, 2));
                        LP = LP + 1;
                    } else {
                        l = getLabel(s);

                        LP = LP + 1;
                        ST[SYMPTR][LABEL] = l;
                        ST[SYMPTR][MEM] = Address.from(LP).hexValue();
                        SYMPTR++;
                        instructionMap[LP].setOpcode("");
                        instructionMap[LP].setLabel("");
                        LP = LP + 1;
                        instructionMap[LP].setOpcode("");
                        instructionMap[LP].setLabel("");
                        LP = LP + 1;
                    }
                }

            } else {
                JOptionPane.showMessageDialog(this, "Instruction " + s + " NOT Valid\n Skipping invalid Instruction");
            }
        }
    }

    public String ExtractData(String x) {
        x = x.substring(x.indexOf(":") + 1);
        x = x.trim();
        x = x.substring(x.indexOf(",") + 1);
        x = x.trim();
        x = x.substring(x.indexOf(" ") + 1);
        x = x.trim();
        Pattern p = Pattern.compile("[0-9A-F]{2}");
        Matcher m1 = p.matcher(x);
        if (m1.find()) {
            String temp = m1.group();
            temp = temp.substring(0, 2);
            return temp;
        }
        return "";
    }

    public String extractAddress(String x) {
        Pattern p = Pattern.compile("[0-9A-F]{4}");
        Matcher m1 = p.matcher(x);
        if (m1.find()) {
            return m1.group();
        }
        return "";
    }



    public void _76() {
        jStep.setEnabled(false);
    }

    public String trimSpaces(String x) {
        x = x.trim().replaceAll(" +", " ");
        return x;
    }

    public String ExtractLabel(String x) {
        int d = x.indexOf(":");
        if (-1 != d) {

            return x.substring(0, d);
        } else {
            return "";
        }
    }

    public String getLabel(String x) {
        int d = x.indexOf(":");
        x = trimSpaces(x.substring(d + 1));
        d = x.indexOf(" ");
        x = x.substring(d + 1).trim();
        if (x.length() == 0) {
            JOptionPane.showMessageDialog(this, "Label Not found");
            return "";
        } else {
            return x;
        }
    }

    void initializePatt() {
        patterns[0] = Pattern.compile("MOV" + space1 + "B" + space + "," + space + "C");
        patterns[1] = Pattern.compile("MOV" + space1 + "B" + space + "," + space + "D");
        patterns[2] = Pattern.compile("MOV" + space1 + "B" + space + "," + space + "E");
        patterns[3] = Pattern.compile("MOV" + space1 + "B" + space + "," + space + "H");
        patterns[4] = Pattern.compile("MOV" + space1 + "B" + space + "," + space + "L");
        patterns[5] = Pattern.compile("MOV" + space1 + "B" + space + "," + space + "B");
        patterns[6] = Pattern.compile("MOV" + space1 + "C" + space + "," + space + "B");
        patterns[7] = Pattern.compile("MOV" + space1 + "C" + space + "," + space + "C");
        patterns[8] = Pattern.compile("MOV" + space1 + "C" + space + "," + space + "D");
        patterns[9] = Pattern.compile("MOV" + space1 + "C" + space + "," + space + "E");
        patterns[10] = Pattern.compile("MOV" + space1 + "C" + space + "," + space + "H");
        patterns[11] = Pattern.compile("MOV" + space1 + "C" + space + "," + space + "L");
        patterns[12] = Pattern.compile("MOV" + space1 + "D" + space + "," + space + "B");
        patterns[13] = Pattern.compile("MOV" + space1 + "D" + space + "," + space + "C");
        patterns[14] = Pattern.compile("MOV" + space1 + "D" + space + "," + space + "D");
        patterns[15] = Pattern.compile("MOV" + space1 + "D" + space + "," + space + "E");
        patterns[16] = Pattern.compile("MOV" + space1 + "D" + space + "," + space + "H");
        patterns[17] = Pattern.compile("MOV" + space1 + "D" + space + "," + space + "L");
        patterns[18] = Pattern.compile("MOV" + space1 + "E" + space + "," + space + "B");
        patterns[19] = Pattern.compile("MOV" + space1 + "E" + space + "," + space + "C");
        patterns[20] = Pattern.compile("MOV" + space1 + "E" + space + "," + space + "D");
        patterns[21] = Pattern.compile("MOV" + space1 + "E" + space + "," + space + "E");
        patterns[22] = Pattern.compile("MOV" + space1 + "E" + space + "," + space + "H");
        patterns[23] = Pattern.compile("MOV" + space1 + "E" + space + "," + space + "L");
        patterns[24] = Pattern.compile("MOV" + space1 + "H" + space + "," + space + "B");
        patterns[25] = Pattern.compile("MOV" + space1 + "H" + space + "," + space + "C");
        patterns[26] = Pattern.compile("MOV" + space1 + "H" + space + "," + space + "D");
        patterns[27] = Pattern.compile("MOV" + space1 + "H" + space + "," + space + "E");
        patterns[28] = Pattern.compile("MOV" + space1 + "H" + space + "," + space + "H");
        patterns[29] = Pattern.compile("MOV" + space1 + "H" + space + "," + space + "L");
        patterns[30] = Pattern.compile("MOV" + space1 + "L" + space + "," + space + "B");
        patterns[31] = Pattern.compile("MOV" + space1 + "L" + space + "," + space + "C");
        patterns[32] = Pattern.compile("MOV" + space1 + "L" + space + "," + space + "D");
        patterns[33] = Pattern.compile("MOV" + space1 + "L" + space + "," + space + "E");
        patterns[34] = Pattern.compile("MOV" + space1 + "L" + space + "," + space + "H");
        patterns[35] = Pattern.compile("MOV" + space1 + "L" + space + "," + space + "L");
        patterns[36] = Pattern.compile("MOV" + space1 + "A" + space + "," + space + "A");
        patterns[37] = Pattern.compile("MOV" + space1 + "A" + space + "," + space + "B");
        patterns[38] = Pattern.compile("MOV" + space1 + "A" + space + "," + space + "C");
        patterns[39] = Pattern.compile("MOV" + space1 + "A" + space + "," + space + "D");
        patterns[40] = Pattern.compile("MOV" + space1 + "A" + space + "," + space + "E");
        patterns[41] = Pattern.compile("MOV" + space1 + "A" + space + "," + space + "H");
        patterns[42] = Pattern.compile("MOV" + space1 + "A" + space + "," + space + "L");
        patterns[43] = Pattern.compile("MOV" + space1 + "B" + space + "," + space + "A");
        patterns[44] = Pattern.compile("MOV" + space1 + "C" + space + "," + space + "A");
        patterns[45] = Pattern.compile("MOV" + space1 + "D" + space + "," + space + "A");
        patterns[46] = Pattern.compile("MOV" + space1 + "E" + space + "," + space + "A");
        patterns[47] = Pattern.compile("MOV" + space1 + "H" + space + "," + space + "A");
        patterns[48] = Pattern.compile("MOV" + space1 + "L" + space + "," + space + "A");
        patterns[49] = Pattern.compile("MOV" + space1 + "A" + space + "," + space + "M");
        patterns[50] = Pattern.compile("MOV" + space1 + "B" + space + "," + space + "M");
        patterns[51] = Pattern.compile("MOV" + space1 + "C" + space + "," + space + "M");
        patterns[52] = Pattern.compile("MOV" + space1 + "D" + space + "," + space + "M");
        patterns[53] = Pattern.compile("MOV" + space1 + "E" + space + "," + space + "M");
        patterns[54] = Pattern.compile("MOV" + space1 + "H" + space + "," + space + "M");
        patterns[55] = Pattern.compile("MOV" + space1 + "L" + space + "," + space + "M");
        patterns[56] = Pattern.compile("MOV" + space1 + "M" + space + "," + space + "A");
        patterns[57] = Pattern.compile("MOV" + space1 + "M" + space + "," + space + "B");
        patterns[58] = Pattern.compile("MOV" + space1 + "M" + space + "," + space + "C");
        patterns[59] = Pattern.compile("MOV" + space1 + "M" + space + "," + space + "D");
        patterns[60] = Pattern.compile("MOV" + space1 + "M" + space + "," + space + "E");
        patterns[61] = Pattern.compile("MOV" + space1 + "M" + space + "," + space + "H");
        patterns[62] = Pattern.compile("MOV" + space1 + "M" + space + "," + space + "L");
        patterns[63] = Pattern.compile("MVI" + space1 + "A" + space + "," + space + data);
        patterns[64] = Pattern.compile("MVI" + space1 + "B" + space + "," + space + data);
        patterns[65] = Pattern.compile("MVI" + space1 + "C" + space + "," + space + data);
        patterns[66] = Pattern.compile("MVI" + space1 + "D" + space + "," + space + data);
        patterns[67] = Pattern.compile("MVI" + space1 + "E" + space + "," + space + data);
        patterns[68] = Pattern.compile("MVI" + space1 + "H" + space + "," + space + data);
        patterns[69] = Pattern.compile("MVI" + space1 + "L" + space + "," + space + data);
        patterns[70] = Pattern.compile("MVI" + space1 + "M" + space + "," + space + data);
        patterns[71] = Pattern.compile("ACI" + space1 + data);
        patterns[72] = Pattern.compile("ADC" + space1 + "A");
        patterns[73] = Pattern.compile("ADC" + space1 + "B");
        patterns[74] = Pattern.compile("ADC" + space1 + "C");
        patterns[75] = Pattern.compile("ADC" + space1 + "D");
        patterns[76] = Pattern.compile("ADC" + space1 + "E");
        patterns[77] = Pattern.compile("ADC" + space1 + "H");
        patterns[78] = Pattern.compile("ADC" + space1 + "L");
        patterns[79] = Pattern.compile("ADC" + space1 + "M");
        patterns[80] = Pattern.compile("ADD" + space1 + "A");
        patterns[81] = Pattern.compile("ADD" + space1 + "B");
        patterns[82] = Pattern.compile("ADD" + space1 + "C");
        patterns[83] = Pattern.compile("ADD" + space1 + "D");
        patterns[84] = Pattern.compile("ADD" + space1 + "E");
        patterns[85] = Pattern.compile("ADD" + space1 + "H");
        patterns[86] = Pattern.compile("ADD" + space1 + "L");
        patterns[87] = Pattern.compile("ADD" + space1 + "M");
        patterns[88] = Pattern.compile("ADI" + space1 + data);
        patterns[89] = Pattern.compile("ANA" + space1 + "A");
        patterns[90] = Pattern.compile("ANA" + space1 + "B");
        patterns[91] = Pattern.compile("ANA" + space1 + "C");
        patterns[92] = Pattern.compile("ANA" + space1 + "D");
        patterns[93] = Pattern.compile("ANA" + space1 + "E");
        patterns[94] = Pattern.compile("ANA" + space1 + "H");
        patterns[95] = Pattern.compile("ANA" + space1 + "L");
        patterns[96] = Pattern.compile("ANA" + space1 + "M");
        patterns[97] = Pattern.compile("ANI" + space1 + data);
        patterns[98] = Pattern.compile("CALL" + space1 + label);
        patterns[99] = Pattern.compile("CC" + space1 + label);
        patterns[100] = Pattern.compile("CM" + space1 + label);
        patterns[101] = Pattern.compile("CMA");
        patterns[102] = Pattern.compile("CMC");
        patterns[103] = Pattern.compile("CMP" + space1 + "A");
        patterns[104] = Pattern.compile("CMP" + space1 + "B");
        patterns[105] = Pattern.compile("CMP" + space1 + "C");
        patterns[106] = Pattern.compile("CMP" + space1 + "D");
        patterns[107] = Pattern.compile("CMP" + space1 + "E");
        patterns[108] = Pattern.compile("CMP" + space1 + "H");
        patterns[109] = Pattern.compile("CMP" + space1 + "L");
        patterns[110] = Pattern.compile("CMP" + space1 + "M");
        patterns[111] = Pattern.compile("CNC" + space1 + label);
        patterns[112] = Pattern.compile("CNZ" + space1 + label);
        patterns[113] = Pattern.compile("CPE" + space1 + label);
        patterns[114] = Pattern.compile("CPO" + space1 + label);
        patterns[115] = Pattern.compile("CPI" + space1 + data);
        patterns[116] = Pattern.compile("CP" + space1 + label);
        patterns[117] = Pattern.compile("CZ" + space1 + label);
        patterns[118] = Pattern.compile("DAA");
        patterns[119] = Pattern.compile("DAD" + space1 + "B");
        patterns[120] = Pattern.compile("DAD" + space1 + "D");
        patterns[121] = Pattern.compile("DAD" + space1 + "H");
        patterns[122] = Pattern.compile("DAD" + space1 + "SP");
        patterns[123] = Pattern.compile("DCR" + space1 + "A");
        patterns[124] = Pattern.compile("DCR" + space1 + "B");
        patterns[125] = Pattern.compile("DCR" + space1 + "C");
        patterns[126] = Pattern.compile("DCR" + space1 + "D");
        patterns[127] = Pattern.compile("DCR" + space1 + "E");
        patterns[128] = Pattern.compile("DCR" + space1 + "H");
        patterns[129] = Pattern.compile("DCR" + space1 + "L");
        patterns[130] = Pattern.compile("DCR" + space1 + "M");
        patterns[131] = Pattern.compile("DCX" + space1 + "B");
        patterns[132] = Pattern.compile("DCX" + space1 + "D");
        patterns[133] = Pattern.compile("DCX" + space1 + "H");
        patterns[134] = Pattern.compile("DCX" + space1 + "SP");
        patterns[135] = Pattern.compile("DI");
        patterns[136] = Pattern.compile("EI");
        patterns[137] = Pattern.compile("HLT");
        patterns[138] = Pattern.compile("IN" + space1 + data);
        patterns[139] = Pattern.compile("INR" + space1 + "A");
        patterns[140] = Pattern.compile("INR" + space1 + "B");
        patterns[141] = Pattern.compile("INR" + space1 + "C");
        patterns[142] = Pattern.compile("INR" + space1 + "D");
        patterns[143] = Pattern.compile("INR" + space1 + "E");
        patterns[144] = Pattern.compile("INR" + space1 + "H");
        patterns[145] = Pattern.compile("INR" + space1 + "L");
        patterns[146] = Pattern.compile("INR" + space1 + "M");
        patterns[147] = Pattern.compile("INX" + space1 + "B");
        patterns[148] = Pattern.compile("INX" + space1 + "D");
        patterns[149] = Pattern.compile("INX" + space1 + "H");
        patterns[150] = Pattern.compile("INX" + space1 + "SP");
        patterns[151] = Pattern.compile("JC" + space1 + label);
        patterns[152] = Pattern.compile("JMP" + space1 + label);
        patterns[153] = Pattern.compile("JM" + space1 + label);
        patterns[154] = Pattern.compile("JNC" + space1 + label);
        patterns[155] = Pattern.compile("JNZ" + space1 + label);
        patterns[156] = Pattern.compile("JPO" + space1 + label);
        patterns[157] = Pattern.compile("JPE" + space1 + label);
        patterns[158] = Pattern.compile("JP" + space1 + label);
        patterns[159] = Pattern.compile("JZ" + space1 + label);
        patterns[160] = Pattern.compile("LDA" + space1 + addr);
        patterns[161] = Pattern.compile("LDAX" + space1 + "B");
        patterns[162] = Pattern.compile("LDAX" + space1 + "D");
        patterns[163] = Pattern.compile("LHLD" + space1 + addr);
        patterns[164] = Pattern.compile("LXI" + space1 + "B" + space + "," + space + addr);
        patterns[165] = Pattern.compile("LXI" + space1 + "D" + space + "," + space + addr);
        patterns[166] = Pattern.compile("LXI" + space1 + "H" + space + "," + space + addr);
        patterns[167] = Pattern.compile("LXI" + space1 + "SP" + space + "," + space + addr);
        patterns[168] = Pattern.compile("NOP");
        patterns[169] = Pattern.compile("ORA" + space1 + "A");
        patterns[170] = Pattern.compile("ORA" + space1 + "B");
        patterns[171] = Pattern.compile("ORA" + space1 + "C");
        patterns[172] = Pattern.compile("ORA" + space1 + "D");
        patterns[173] = Pattern.compile("ORA" + space1 + "E");
        patterns[174] = Pattern.compile("ORA" + space1 + "H");
        patterns[175] = Pattern.compile("ORA" + space1 + "L");
        patterns[176] = Pattern.compile("ORA" + space1 + "M");
        patterns[177] = Pattern.compile("ORI" + space1 + data);
        patterns[178] = Pattern.compile("OUT" + space1 + data);
        patterns[179] = Pattern.compile("PCHL");
        patterns[180] = Pattern.compile("POP" + space1 + "B");
        patterns[181] = Pattern.compile("POP" + space1 + "D");
        patterns[182] = Pattern.compile("POP" + space1 + "H");
        patterns[183] = Pattern.compile("POP" + space1 + "PSW");
        patterns[184] = Pattern.compile("PUSH" + space1 + "B");
        patterns[185] = Pattern.compile("PUSH" + space1 + "D");
        patterns[186] = Pattern.compile("PUSH" + space1 + "H");
        patterns[187] = Pattern.compile("PUSH" + space1 + "PSW");
        patterns[188] = Pattern.compile("RAL");
        patterns[189] = Pattern.compile("RAR");
        patterns[190] = Pattern.compile("RC");
        patterns[191] = Pattern.compile("RET");
        patterns[192] = Pattern.compile("RIM");
        patterns[193] = Pattern.compile("RLC");
        patterns[194] = Pattern.compile("RM");
        patterns[195] = Pattern.compile("RNC");
        patterns[196] = Pattern.compile("RNZ");
        patterns[197] = Pattern.compile("RP");
        patterns[198] = Pattern.compile("RPE");
        patterns[199] = Pattern.compile("RPO");
        patterns[200] = Pattern.compile("RRC");
        patterns[201] = Pattern.compile("RST" + space1 + "0");
        patterns[202] = Pattern.compile("RST" + space1 + "1");
        patterns[203] = Pattern.compile("RST" + space1 + "2");
        patterns[204] = Pattern.compile("RST" + space1 + "3");
        patterns[205] = Pattern.compile("RST" + space1 + "4");
        patterns[206] = Pattern.compile("RST" + space1 + "5");
        patterns[207] = Pattern.compile("RST" + space1 + "6");
        patterns[208] = Pattern.compile("RST" + space1 + "7");
        patterns[209] = Pattern.compile("RZ");
        patterns[210] = Pattern.compile("SBB" + space1 + "A");
        patterns[211] = Pattern.compile("SBB" + space1 + "B");
        patterns[212] = Pattern.compile("SBB" + space1 + "C");
        patterns[213] = Pattern.compile("SBB" + space1 + "D");
        patterns[214] = Pattern.compile("SBB" + space1 + "E");
        patterns[215] = Pattern.compile("SBB" + space1 + "H");
        patterns[216] = Pattern.compile("SBB" + space1 + "L");
        patterns[217] = Pattern.compile("SBB" + space1 + "M");
        patterns[218] = Pattern.compile("SBI" + space1 + data);
        patterns[219] = Pattern.compile("SHLD" + space1 + addr);
        patterns[220] = Pattern.compile("SIM");
        patterns[221] = Pattern.compile("SPHL");
        patterns[222] = Pattern.compile("STA" + space1 + addr);
        patterns[223] = Pattern.compile("STAX" + space1 + "B");
        patterns[224] = Pattern.compile("STAX" + space1 + "D");
        patterns[225] = Pattern.compile("STC");
        patterns[226] = Pattern.compile("SUB" + space1 + "A");
        patterns[227] = Pattern.compile("SUB" + space1 + "B");
        patterns[228] = Pattern.compile("SUB" + space1 + "C");
        patterns[229] = Pattern.compile("SUB" + space1 + "D");
        patterns[230] = Pattern.compile("SUB" + space1 + "E");
        patterns[231] = Pattern.compile("SUB" + space1 + "H");
        patterns[232] = Pattern.compile("SUB" + space1 + "L");
        patterns[233] = Pattern.compile("SUB" + space1 + "M");
        patterns[234] = Pattern.compile("SUI" + space1 + data);
        patterns[235] = Pattern.compile("XCHD");
        patterns[236] = Pattern.compile("XRA" + space1 + "A");
        patterns[237] = Pattern.compile("XRA" + space1 + "B");
        patterns[238] = Pattern.compile("XRA" + space1 + "C");
        patterns[239] = Pattern.compile("XRA" + space1 + "D");
        patterns[240] = Pattern.compile("XRA" + space1 + "E");
        patterns[241] = Pattern.compile("XRA" + space1 + "H");
        patterns[242] = Pattern.compile("XRA" + space1 + "L");
        patterns[243] = Pattern.compile("XRA" + space1 + "M");
        patterns[244] = Pattern.compile("XRI" + space1 + data);
        patterns[245] = Pattern.compile("XTHL");

    }

    int findI(String str) {

        for (int i = 0; i < patterns.length; i++) {
            m = patterns[i].matcher(str);
            if (m.find()) {
                return i;
            }
        }
        return -1;
    }

    String findOpcode(int x) {
        switch (x) {
            case -1:
                return "Not found";
            case 0:
                return "41";
            case 1:
                return "42";
            case 2:
                return "43";
            case 3:
                return "44";
            case 4:
                return "45";
            case 5:
                return "40";
            case 6:
                return "48";
            case 7:
                return "49";
            case 8:
                return "4A";
            case 9:
                return "4B";
            case 10:
                return "4C";
            case 11:
                return "4D";
            case 12:
                return "50";
            case 13:
                return "51";
            case 14:
                return "52";
            case 15:
                return "53";
            case 16:
                return "54";
            case 17:
                return "55";
            case 18:
                return "58";
            case 19:
                return "59";
            case 20:
                return "5A";
            case 21:
                return "5B";
            case 22:
                return "5C";
            case 23:
                return "5D";
            case 24:
                return "60";
            case 25:
                return "61";
            case 26:
                return "62";
            case 27:
                return "63";
            case 28:
                return "64";
            case 29:
                return "65";
            case 30:
                return "68";
            case 31:
                return "69";
            case 32:
                return "6A";
            case 33:
                return "6B";
            case 34:
                return "6C";
            case 35:
                return "6D";
            case 36:
                return "7F";
            case 37:
                return "78";
            case 38:
                return "79";
            case 39:
                return "7A";
            case 40:
                return "7B";
            case 41:
                return "7C";
            case 42:
                return "7D";
            case 43:
                return "47";
            case 44:
                return "4F";
            case 45:
                return "57";
            case 46:
                return "5F";
            case 47:
                return "67";
            case 48:
                return "6F";
            case 49:
                return "7E";
            case 50:
                return "46";
            case 51:
                return "4E";
            case 52:
                return "56";
            case 53:
                return "5E";
            case 54:
                return "66";
            case 55:
                return "6E";
            case 56:
                return "77";
            case 57:
                return "70";
            case 58:
                return "71";
            case 59:
                return "72";
            case 60:
                return "73";
            case 61:
                return "74";
            case 62:
                return "75";
            case 63:
                return "3E";
            case 64:
                return "06";
            case 65:
                return "0E";
            case 66:
                return "16";
            case 67:
                return "1E";
            case 68:
                return "26";
            case 69:
                return "2E";
            case 70:
                return "36";
            case 71:
                return "CE";
            case 72:
                return "8F";
            case 73:
                return "88";
            case 74:
                return "89";
            case 75:
                return "8A";
            case 76:
                return "8B";
            case 77:
                return "8C";
            case 78:
                return "8D";
            case 79:
                return "8E";
            case 80:
                return "87";
            case 81:
                return "80";
            case 82:
                return "81";
            case 83:
                return "82";
            case 84:
                return "83";
            case 85:
                return "84";
            case 86:
                return "85";
            case 87:
                return "86";
            case 88:
                return "C6";
            case 89:
                return "A7";
            case 90:
                return "A0";
            case 91:
                return "A1";
            case 92:
                return "A2";
            case 93:
                return "A3";
            case 94:
                return "A4";
            case 95:
                return "A5";
            case 96:
                return "A6";
            case 97:
                return "E6";
            case 98:
                return "CD";
            case 99:
                return "DC";
            case 100:
                return "FC";
            case 101:
                return "2F";
            case 102:
                return "3F";
            case 103:
                return "BF";
            case 104:
                return "B8";
            case 105:
                return "B9";
            case 106:
                return "BA";
            case 107:
                return "BB";
            case 108:
                return "BC";
            case 109:
                return "BD";
            case 110:
                return "BE";
            case 111:
                return "D4";
            case 112:
                return "C4";
            case 113:
                return "EC";
            case 114:
                return "E4";
            case 115:
                return "FE";
            case 116:
                return "F4";
            case 117:
                return "CC";
            case 118:
                return "27";
            case 119:
                return "09";
            case 120:
                return "19";
            case 121:
                return "29";
            case 122:
                return "39";
            case 123:
                return "3D";
            case 124:
                return "05";
            case 125:
                return "0D";
            case 126:
                return "15";
            case 127:
                return "1D";
            case 128:
                return "25";
            case 129:
                return "2D";
            case 130:
                return "35";
            case 131:
                return "0B";
            case 132:
                return "1B";
            case 133:
                return "2B";
            case 134:
                return "3B";
            case 135:
                return "F3";
            case 136:
                return "FB";
            case 137:
                return "76";
            case 138:
                return "DB";
            case 139:
                return "3C";
            case 140:
                return "04";
            case 141:
                return "0C";
            case 142:
                return "14";
            case 143:
                return "1C";
            case 144:
                return "24";
            case 145:
                return "2C";
            case 146:
                return "34";
            case 147:
                return "03";
            case 148:
                return "13";
            case 149:
                return "23";
            case 150:
                return "33";
            case 151:
                return "DA";
            case 152:
                return "C3";
            case 153:
                return "FA";
            case 154:
                return "D2";
            case 155:
                return "C2";
            case 156:
                return "E2";
            case 157:
                return "EA";
            case 158:
                return "F2";
            case 159:
                return "CA";
            case 160:
                return "3A";
            case 161:
                return "0A";
            case 162:
                return "1A";
            case 163:
                return "2A";
            case 164:
                return "01";
            case 165:
                return "11";
            case 166:
                return "21";
            case 167:
                return "31";
            case 168:
                return "00";
            case 169:
                return "B7";
            case 170:
                return "B0";
            case 171:
                return "B1";
            case 172:
                return "B2";
            case 173:
                return "B3";
            case 174:
                return "B4";
            case 175:
                return "B5";
            case 176:
                return "B6";
            case 177:
                return "F6";
            case 178:
                return "D3";
            case 179:
                return "E9";
            case 180:
                return "C1";
            case 181:
                return "D1";
            case 182:
                return "E1";
            case 183:
                return "F1";
            case 184:
                return "C5";
            case 185:
                return "D5";
            case 186:
                return "E5";
            case 187:
                return "F5";
            case 188:
                return "17";
            case 189:
                return "1F";
            case 190:
                return "D8";
            case 191:
                return "C9";
            case 192:
                return "20";
            case 193:
                return "07";
            case 194:
                return "F8";
            case 195:
                return "D0";
            case 196:
                return "C0";
            case 197:
                return "F0";
            case 198:
                return "E8";
            case 199:
                return "E0";
            case 200:
                return "0F";
            case 201:
                return "C7";
            case 202:
                return "CF";
            case 203:
                return "D7";
            case 204:
                return "DF";
            case 205:
                return "E7";
            case 206:
                return "EF";
            case 207:
                return "F7";
            case 208:
                return "FF";
            case 209:
                return "C8";
            case 210:
                return "9F";
            case 211:
                return "98";
            case 212:
                return "99";
            case 213:
                return "9A";
            case 214:
                return "9B";
            case 215:
                return "9C";
            case 216:
                return "9D";
            case 217:
                return "9E";
            case 218:
                return "DE";
            case 219:
                return "22";
            case 220:
                return "30";
            case 221:
                return "F9";
            case 222:
                return "32";
            case 223:
                return "02";
            case 224:
                return "12";
            case 225:
                return "37";
            case 226:
                return "97";
            case 227:
                return "90";
            case 228:
                return "91";
            case 229:
                return "92";
            case 230:
                return "93";
            case 231:
                return "94";
            case 232:
                return "95";
            case 233:
                return "96";
            case 234:
                return "D6";
            case 235:
                return "EB";
            case 236:
                return "AF";
            case 237:
                return "A8";
            case 238:
                return "A9";
            case 239:
                return "AA";
            case 240:
                return "AB";
            case 241:
                return "AC";
            case 242:
                return "AD";
            case 243:
                return "AE";
            case 244:
                return "EE";
            case 245:
                return "E3";

            default:
                return "Not Found at " + x;

        }
    }

    public int OpcodeLength(String s) {
        switch (s) {
            case "8F":
            case "88":
            case "89":
            case "8A":
            case "8B":
            case "8C":
            case "8D":
            case "8E":
            case "87":
            case "80":
            case "81":
            case "82":
            case "83":
            case "84":
            case "85":
            case "86":
            case "A7":
            case "A0":
            case "A1":
            case "A2":
            case "A3":
            case "A4":
            case "A5":
            case "A6":
            case "2F":
            case "3F":
            case "BF":
            case "B8":
            case "B9":
            case "BA":
            case "BB":
            case "BC":
            case "BD":
            case "BE":
            case "27":
            case "09":
            case "19":
            case "29":
            case "39":
            case "3D":
            case "05":
            case "0D":
            case "15":
            case "1D":
            case "25":
            case "2D":
            case "35":
            case "0B":
            case "1B":
            case "2B":
            case "3B":
            case "F3":
            case "FB":
            case "76":
            case "3C":
            case "04":
            case "0C":
            case "14":
            case "1C":
            case "24":
            case "2C":
            case "34":
            case "03":
            case "13":
            case "23":
            case "33":
            case "0A":
            case "1A":
            case "7F":
            case "78":
            case "79":
            case "7A":
            case "7B":
            case "7C":
            case "7D":
            case "7E":
            case "47":
            case "40":
            case "41":
            case "42":
            case "43":
            case "44":
            case "45":
            case "46":
            case "4F":
            case "48":
            case "49":
            case "4A":
            case "4B":
            case "4C":
            case "4D":
            case "4E":
            case "57":
            case "50":
            case "51":
            case "52":
            case "53":
            case "54":
            case "55":
            case "56":
            case "5F":
            case "58":
            case "59":
            case "5A":
            case "5B":
            case "5C":
            case "5D":
            case "5E":
            case "67":
            case "60":
            case "61":
            case "62":
            case "63":
            case "64":
            case "65":
            case "66":
            case "6F":
            case "68":
            case "69":
            case "6A":
            case "6B":
            case "6C":
            case "6D":
            case "6E":
            case "77":
            case "70":
            case "71":
            case "72":
            case "73":
            case "74":
            case "75":
            case "00":
            case "B7":
            case "B0":
            case "B1":
            case "B2":
            case "B3":
            case "B4":
            case "B5":
            case "B6":
            case "E9":
            case "C1":
            case "D1":
            case "E1":
            case "F1":
            case "C5":
            case "D5":
            case "E5":
            case "F5":
            case "17":
            case "1F":
            case "D8":
            case "C9":
            case "20":
            case "07":
            case "F8":
            case "D0":
            case "F0":
            case "E8":
            case "E0":
            case "0F":
            case "C7":
            case "CF":
            case "D7":
            case "DF":
            case "E7":
            case "EF":
            case "F7":
            case "FF":
            case "C8":
            case "9F":
            case "98":
            case "99":
            case "9A":
            case "9B":
            case "9C":
            case "9D":
            case "9E":
            case "30":
            case "F9":
            case "02":
            case "12":
            case "37":
            case "97":
            case "90":
            case "91":
            case "92":
            case "93":
            case "94":
            case "95":
            case "96":
            case "EB":
            case "AF":
            case "A8":
            case "A9":
            case "AA":
            case "AB":
            case "AC":
            case "AD":
            case "AE":
            case "E3":
                return 1;
            case "CE":
            case "C6":
            case "E6":
            case "FE":
            case "DB":
            case "3E":
            case "06":
            case "0E":
            case "16":
            case "1E":
            case "26":
            case "2E":
            case "36":
            case "F6":
            case "D3":
            case "DE":
            case "D6":
            case "EE":
                return 2;
            case "CD":
            case "DC":
            case "FC":
            case "D4":
            case "C4":
            case "F4":
            case "EC":
            case "E4":
            case "CC":
            case "DA":
            case "FA":
            case "C3":
            case "D2":
            case "C2":
            case "F2":
            case "EA":
            case "E2":
            case "CA":
            case "3A":
            case "2A":
            case "01":
            case "11":
            case "21":
            case "31":
            case "22":
            case "32":
                return 3;
            default:
                throw  newException("Invalid opcode");
        }
    }

    public int run_code_index = 0;

    /**
     * Creates new form test1
     */
    public Main() {
        initComponents();
        initializePatt();
        simulator.initialize();
        initializeInstructionMap();
        jStep.setEnabled(false);
    }

    private void subscribeInstructionPointer() {
        this.instructionExecutedSub.dispose();
        this.instructionExecutedSub = simulator.getInstructionExecuted$()
            .subscribe( instructionExecuted -> {
                if(instructionExecuted.getInstruction().hexValue().equals("76")) {
                    jStep.setEnabled(false);
                }
                updateView();
            });

        this.errorSub.dispose();
        this.errorSub = simulator.getOnError$()
                .subscribe(err -> {
                    JOptionPane.showMessageDialog(this, err.getMessage());
                });
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jFrame1 = new javax.swing.JFrame();
        jScrollPane1 = new javax.swing.JScrollPane();
        code_av = new javax.swing.JTextArea();
        jButton1 = new javax.swing.JButton();
        code_here = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        run_code = new javax.swing.JList();
        jStep = new javax.swing.JButton();
        jA = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jB = new javax.swing.JTextField();
        jC = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jD = new javax.swing.JTextField();
        jE = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jH = new javax.swing.JTextField();
        jL = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        jIP = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        jSP = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        jS = new javax.swing.JTextField();
        jZ = new javax.swing.JTextField();
        jAc = new javax.swing.JTextField();
        jP = new javax.swing.JTextField();
        jCy = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jTextField18 = new javax.swing.JTextField();
        jSeparator1 = new javax.swing.JSeparator();
        jSeparator2 = new javax.swing.JSeparator();
        jSeparator3 = new javax.swing.JSeparator();
        jSeparator4 = new javax.swing.JSeparator();
        jTabbedPane2 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        CodeTable = new javax.swing.JTable();
        jLabel23 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        CodeHead = new javax.swing.JTextField();
        jButton5 = new javax.swing.JButton();
        jLabel25 = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        DataTable = new javax.swing.JTable();
        jLabel20 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        DataHead = new javax.swing.JTextField();
        jButton4 = new javax.swing.JButton();
        jLabel22 = new javax.swing.JLabel();
        jLabel27 = new javax.swing.JLabel();
        jButton2 = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane5 = new javax.swing.JScrollPane();
        StackTable = new javax.swing.JTable();
        jLabel13 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        StackHead = new javax.swing.JTextField();
        jButton3 = new javax.swing.JButton();
        jLabel17 = new javax.swing.JLabel();
        jLabel28 = new javax.swing.JLabel();

        javax.swing.GroupLayout jFrame1Layout = new javax.swing.GroupLayout(jFrame1.getContentPane());
        jFrame1.getContentPane().setLayout(jFrame1Layout);
        jFrame1Layout.setHorizontalGroup(
            jFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        jFrame1Layout.setVerticalGroup(
            jFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Aman and Darshan's 8085 Simulator");
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        setResizable(false);

        code_av.setColumns(20);
        code_av.setRows(5);
        code_av.setText("MVI A,0FH\nMVI B,12H\nSUB B\nJM NEXT\nSTA 4000H\nNEXT:CALL SBROUT\nHLT\n\nSBROUT:STA 4005H\nRET");
        jScrollPane1.setViewportView(code_av);

        jButton1.setText("LOAD >");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        code_here.setText("Type Your Code Here:");

        jScrollPane2.setViewportView(run_code);

        jStep.setText("1 Step >");
        jStep.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jStepActionPerformed(evt);
            }
        });

        jA.setEditable(false);
        jA.setText("NULL");
        jA.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jAActionPerformed(evt);
            }
        });

        jLabel1.setText("Accumlator: ");

        jLabel2.setText("Parsed Instructions");

        jLabel3.setText("REG (B)");

        jLabel4.setText("REG (C)");

        jB.setEditable(false);
        jB.setText("NULL");
        jB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBActionPerformed(evt);
            }
        });

        jC.setEditable(false);
        jC.setText("NULL");
        jC.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCActionPerformed(evt);
            }
        });

        jLabel5.setText("REG (D)");

        jLabel6.setText("REG (E)");

        jD.setEditable(false);
        jD.setText("NULL");
        jD.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jDActionPerformed(evt);
            }
        });

        jE.setEditable(false);
        jE.setText("NULL");

        jLabel7.setText("REG (H)");

        jLabel8.setText("REG (L)");

        jH.setEditable(false);
        jH.setText("NULL");
        jH.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jHActionPerformed(evt);
            }
        });

        jL.setEditable(false);
        jL.setText("NULL");

        jLabel9.setText("Instruction Ptr.:");

        jIP.setText("0000H");
        jIP.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jIPActionPerformed(evt);
            }
        });

        jLabel10.setText("Stack Pointer:");

        jSP.setText("FFFFH");

        jLabel11.setText("S");

        jS.setEditable(false);
        jS.setText("0");
        jS.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jSActionPerformed(evt);
            }
        });

        jZ.setEditable(false);
        jZ.setText("0");

        jAc.setEditable(false);
        jAc.setText("0");

        jP.setEditable(false);
        jP.setText("0");

        jCy.setEditable(false);
        jCy.setText("0");

        jLabel12.setText("Z");

        jLabel14.setText("Ac");

        jLabel16.setText("P");

        jLabel18.setText("Cy");

        jLabel19.setText("Flags:");

        jTextField18.setText("00");

        jSeparator4.setOrientation(javax.swing.SwingConstants.VERTICAL);

        CodeTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Memory", "Label", "Operand", "HEX"
            }
        ) {
            final Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            final boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        CodeTable.setSelectionBackground(new java.awt.Color(255, 255, 51));
        CodeTable.setSelectionForeground(new java.awt.Color(0, 0, 0));
        CodeTable.setShowHorizontalLines(true);
        CodeTable.setShowVerticalLines(true);
        jScrollPane3.setViewportView(CodeTable);

        jLabel23.setText("Code Space ranges from 0000 to 3FFF");

        jLabel24.setText("Display Memory from :");

        CodeHead.setText("0000");
        CodeHead.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CodeHeadActionPerformed(evt);
            }
        });
        CodeHead.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                CodeHeadKeyPressed(evt);
            }
        });

        jButton5.setText("Set");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });
        jButton5.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jButton5KeyPressed(evt);
            }
        });

        jLabel25.setText("20 Max results will be displayed");

        jLabel26.setText("H");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel25)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel24)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(CodeHead, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(3, 3, 3)
                        .addComponent(jLabel26)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton5))
                    .addComponent(jLabel23)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 433, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addComponent(jLabel23)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel24)
                    .addComponent(CodeHead, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton5)
                    .addComponent(jLabel26))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel25)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 357, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jTabbedPane2.addTab("CODE", jPanel1);

        DataTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null}
            },
            new String [] {
                "Memory", "HEX"
            }
        ) {
            final Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, true
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        DataTable.setSelectionBackground(new java.awt.Color(255, 255, 0));
        DataTable.setSelectionForeground(new java.awt.Color(0, 0, 0));
        DataTable.setShowHorizontalLines(true);
        DataTable.setShowVerticalLines(true);
        DataTable.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                DataTableKeyPressed(evt);
            }
        });
        jScrollPane4.setViewportView(DataTable);

        jLabel20.setText("Data Space ranges from 4000 to DFFF");

        jLabel21.setText("Display Memory From :");

        DataHead.setText("4000");
        DataHead.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                DataHeadActionPerformed(evt);
            }
        });
        DataHead.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                DataHeadKeyPressed(evt);
            }
        });

        jButton4.setText("Set");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jLabel22.setText("20 Max results will be displayed");

        jLabel27.setText("H");

        jButton2.setText("Apply");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 426, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel20)
                    .addComponent(jLabel22))
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jLabel21)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(DataHead, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel27)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton2)
                .addGap(33, 33, 33))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addComponent(jLabel20)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel21)
                    .addComponent(DataHead, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton4)
                    .addComponent(jLabel27)
                    .addComponent(jButton2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel22)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 355, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jTabbedPane2.addTab("DATA", jPanel2);

        StackTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null}
            },
            new String [] {
                "Memory", "HEX"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        StackTable.setSelectionBackground(new java.awt.Color(255, 255, 0));
        StackTable.setSelectionForeground(new java.awt.Color(0, 0, 0));
        StackTable.setShowHorizontalLines(true);
        StackTable.setShowVerticalLines(true);
        jScrollPane5.setViewportView(StackTable);

        jLabel13.setText("Stack Space is from E000 to FFFF ");

        jLabel15.setText("Display Memory From :");

        StackHead.setText("FFFF");
        StackHead.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                StackHeadActionPerformed(evt);
            }
        });
        StackHead.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                StackHeadKeyPressed(evt);
            }
        });

        jButton3.setText("Set");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jLabel17.setText("20 Max results will be displayed");

        jLabel28.setText("H");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 425, Short.MAX_VALUE)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel17)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(jLabel15)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(StackHead, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel28)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton3))
                            .addComponent(jLabel13))
                        .addGap(0, 143, Short.MAX_VALUE))))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addComponent(jLabel13)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel15)
                    .addComponent(StackHead, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton3)
                    .addComponent(jLabel28))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel17)
                .addGap(13, 13, 13)
                .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 357, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jTabbedPane2.addTab("STACK", jPanel3);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(22, 22, 22)
                        .addComponent(jButton1))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jStep)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(42, 42, 42)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jSeparator2, javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jSeparator3, javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createSequentialGroup()
                                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(jLabel9, javax.swing.GroupLayout.Alignment.TRAILING)
                                                .addComponent(jLabel10, javax.swing.GroupLayout.Alignment.TRAILING))
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                .addComponent(jSP)
                                                .addComponent(jIP, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                        .addGroup(layout.createSequentialGroup()
                                            .addComponent(jLabel19)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                            .addComponent(jTextField18, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(layout.createSequentialGroup()
                                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(jLabel1)
                                                .addGroup(layout.createSequentialGroup()
                                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                        .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .addComponent(jB)
                                                        .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .addComponent(jD)
                                                        .addComponent(jLabel7, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .addComponent(jH, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                    .addGap(18, 18, 18)
                                                    .addComponent(jSeparator4, javax.swing.GroupLayout.PREFERRED_SIZE, 8, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                            .addGap(3, 3, 3)
                                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(jA, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                    .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                    .addComponent(jC, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                                    .addComponent(jE, javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addComponent(jLabel6, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                    .addComponent(jLabel8, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                    .addComponent(jL, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                                    .addComponent(jSeparator1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel11)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jS, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel12)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jZ, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel14)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jAc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 12, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel18)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jCy, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addGap(18, 18, 18)
                .addComponent(jTabbedPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 430, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(26, 26, 26))
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(code_here)
                .addGap(18, 18, 18)
                .addComponent(jLabel2)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jA, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3)
                            .addComponent(jLabel4))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jB, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jC, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel5)
                            .addComponent(jLabel6))
                        .addGap(20, 20, 20)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jD, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jE, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel7)
                            .addComponent(jLabel8))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jH, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jL, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(13, 13, 13))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jSeparator4, javax.swing.GroupLayout.PREFERRED_SIZE, 207, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)))
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(1, 1, 1)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(jIP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jSP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel10))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel19)
                    .addComponent(jTextField18, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(jS, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jAc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jZ, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel16)
                    .addComponent(jLabel18)
                    .addComponent(jCy, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel14))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(code_here)
                            .addComponent(jLabel2))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 448, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 448, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(jStep))
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jTabbedPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 463, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(21, 21, 21))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        this.loadInstructions();

    }//GEN-LAST:event_jButton1ActionPerformed

    private void jStepActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jStepActionPerformed
        // TODO add your handling code here:
        int nip = simulator.getIP().intValue();
        simulator.execute(simulator.getDataAtIP().hexValue());
        nip = nip - Address.from(CodeHead.getText()).intValue();

        CodeTable.addRowSelectionInterval(nip, nip);
        if (oldIP != nip) {
            CodeTable.removeRowSelectionInterval(oldIP, oldIP);
        }
        oldIP = nip;
    }//GEN-LAST:event_jStepActionPerformed

    private void jBActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jBActionPerformed

    private void jCActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCActionPerformed

    private void jDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jDActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jDActionPerformed

    private void jHActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jHActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jHActionPerformed

    private void jAActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jAActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jAActionPerformed

    private void jIPActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jIPActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jIPActionPerformed

    private void jSActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jSActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jSActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
        refreshStack();
    }//GEN-LAST:event_jButton3ActionPerformed

    private void StackHeadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_StackHeadActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_StackHeadActionPerformed

    private void DataHeadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_DataHeadActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_DataHeadActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // TODO add your handling code here:
        refreshData();
    }//GEN-LAST:event_jButton4ActionPerformed

    private void CodeHeadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CodeHeadActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_CodeHeadActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        // TODO add your handling code here:
        updateCodeTable();
    }//GEN-LAST:event_jButton5ActionPerformed

    private void DataTableKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_DataTableKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == evt.VK_ENTER) {
            for (int i = 0; i < 20; i++) {
                Object y = DataTable.getValueAt(i, 1);
                Object x = DataTable.getValueAt(i, 0);
                String q = y.toString().toUpperCase();
                String p = x.toString();
                Pattern ppp;
                ppp = Pattern.compile("^[A-Fa-f0-9]{2}$");
                Matcher mmm = ppp.matcher(q);
                if (!mmm.find()) {
                    JOptionPane.showMessageDialog(this, q + " is not a 1 Byte Hex digit");
                    DataTable.setValueAt("00", i, 1);
                } else {
                    simulator.setData(Address.from(p), Data.from(q));
                }
            }
        }
    }//GEN-LAST:event_DataTableKeyPressed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        for (int i = 0; i < 20; i++) {
            Object y = DataTable.getValueAt(i, 1);
            Object x = DataTable.getValueAt(i, 0);
            String q = y.toString().toUpperCase();
            String p = x.toString();
            Pattern ppp;
            ppp = Pattern.compile("^[A-Fa-f0-9]{2}$");
            Matcher mmm = ppp.matcher(q);
            if (!mmm.find()) {
                JOptionPane.showMessageDialog(this, q + " is not a 1 Byte Hex digit");
                DataTable.setValueAt("00", i, 1);
            } else {
                simulator.setData(Address.from(p), Data.from(q));
            }
        }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton5KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jButton5KeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == evt.VK_ENTER) {
            updateCodeTable();
        }
    }//GEN-LAST:event_jButton5KeyPressed

    private void CodeHeadKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_CodeHeadKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == evt.VK_ENTER) {
            updateCodeTable();
        }
    }//GEN-LAST:event_CodeHeadKeyPressed

    private void DataHeadKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_DataHeadKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == evt.VK_ENTER) {
            refreshData();
        }
    }//GEN-LAST:event_DataHeadKeyPressed

    private void StackHeadKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_StackHeadKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == evt.VK_ENTER) {
            refreshStack();
        }
    }//GEN-LAST:event_StackHeadKeyPressed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        SpringApplication.run(Main.class, args);
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                Main t1 = new Main();
                t1.setVisible(true);

            }
        });
    }











    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField CodeHead;
    private javax.swing.JTable CodeTable;
    private javax.swing.JTextField DataHead;
    private javax.swing.JTable DataTable;
    private javax.swing.JTextField StackHead;
    private javax.swing.JTable StackTable;
    private javax.swing.JTextArea code_av;
    private javax.swing.JLabel code_here;
    private javax.swing.JTextField jA;
    private javax.swing.JTextField jAc;
    private javax.swing.JTextField jB;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JTextField jC;
    private javax.swing.JTextField jCy;
    private javax.swing.JTextField jD;
    private javax.swing.JTextField jE;
    private javax.swing.JFrame jFrame1;
    private javax.swing.JTextField jH;
    private javax.swing.JTextField jIP;
    private javax.swing.JTextField jL;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JTextField jP;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JTextField jS;
    private javax.swing.JTextField jSP;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JButton jStep;
    private javax.swing.JTabbedPane jTabbedPane2;
    private javax.swing.JTextField jTextField18;
    private javax.swing.JTextField jZ;
    private javax.swing.JList run_code;
    // End of variables declaration//GEN-END:variables
}
