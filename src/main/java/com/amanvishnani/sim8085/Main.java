package com.amanvishnani.sim8085;


import com.amanvishnani.sim8085.domain.*;
import com.amanvishnani.sim8085.domain.Impl.*;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.subjects.PublishSubject;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

@SpringBootApplication
public class Main extends javax.swing.JFrame implements IView, I8085, IExecutor {

    private PublishSubject<InstructionExecuted> instructionExecuted$;
    private PublishSubject<RuntimeException> onError$;

    public static IMemory memory = Memory.makeMemory();
    public IRegister A, B, C, D, E, H, L;
    public IAddress IP, SP;
    static Flags flags = Flags.newInstance();
    public static int LABEL = 0, OPCODE = 1, MEM = 1, SYMPTR, SYMPTR1, oldIP;
    public static String[][] ST = new String[100][2];
    public static String[][] ST1 = new String[100][2];
    public static String[][] map = new String[16384][2];
    public Matcher m;
    public Pattern[] patterns = new Pattern[246];
    public final String data = "[0-9A-F]{2}(H)?";
    public final String label = "[A-Za-z]{3}[A-Za-z]*";
    public final String addr = "[0-9A-F]{4}(H)?";
    public final String space = "( )*";
    public final String space1 = "( )+";
    private Disposable instructionExecutedSub = Disposable.disposed();
    private Disposable errorSub = Disposable.disposed();

    public PublishSubject<RuntimeException> getOnError$() {
        return onError$;
    }

    public void setOnError$(PublishSubject<RuntimeException> onError$) {
        this.onError$ = onError$;
    }

    @Override
    public void updateViewFlags() {
        jS.setText(flags.getFlag(Flag.S).toString());
        jCy.setText(flags.getFlag(Flag.Cy).toString());
        jZ.setText(flags.getFlag(Flag.Z).toString());
        jAc.setText(flags.getFlag(Flag.Ac).toString());
        jP.setText(flags.getFlag(Flag.P).toString());
    }

    @Override
    public void updateViewRegisters() {
        jA.setText(getA().hexValue());
        jB.setText(getB().hexValue());
        jC.setText(getC().hexValue());
        jD.setText(getD().hexValue());
        jE.setText(getE().hexValue());
        jH.setText(getH().hexValue());
        jL.setText(getL().hexValue());
    }

    @Override
    public void updateViewPointers() {
        jIP.setText(getIP().hexValue());
        jSP.setText(getSP().hexValue());
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

    @Override
    public void nextInstructionPointer() {
        int x = getIP().intValue();
        if (x < 16383) {
            x++;
            IAddress nextAddress = Address.from(x);
            setIP(nextAddress);
        } else {
            throw newException("IP exceeding 3FFF (16383)");
        }
    }

    String[] getLineInstructions() {
        int i = 0, k = 0, j;
        String code = code_av.getText().toUpperCase();
        String[] code_token = code.split("\\n");
        String[] temp = new String[code_token.length];

        for (j = 0; j < code_token.length; j++) {
            code_token[j] = Trimmer(code_token[j]);
            if (0 != code_token[j].length()) {
                temp[i] = code_token[j];
                i++;
            } else {
                k++;
            }
        }
        String[] temp2 = new String[temp.length - k];
        for (j = 0; j < temp.length - k; j++) {
            temp2[j] = temp[j];
        }
        return temp2;
    }

    public void iniMap() {
        for (int i = 0; i < 16383; i++) {
            map[i][LABEL] = "";
            map[i][OPCODE] = "";
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
                    obj[1] = map[i][LABEL];
                    obj[2] = map[i][OPCODE];
                    obj[3] = memory.getHexData(i);
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
                    obj[1] = memory.getHexData(i);
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
                    obj[1] = memory.getHexData(i);
                    x.addRow(obj);
                }
            }
        } catch (Exception NumberfException) {
            JOptionPane.showMessageDialog(this, "{Make sure given Data Segment Head is a Hex Number} ");
        }
    }

    @Override
    public void execute(String op) {
        switch (op) {
            case "8F":
                _8F();
                break;
            case "88":
                _88();
                break;
            case "89":
                _89();
                break;
            case "8A":
                _8A();
                break;
            case "8B":
                _8B();
                break;
            case "8C":
                _8C();
                break;
            case "8D":
                _8D();
                break;
            case "8E":
                _8E();
                break;
            case "87":
                _87();
                break;
            case "80":
                _80();
                break;
            case "81":
                _81();
                break;
            case "82":
                _82();
                break;
            case "83":
                _83();
                break;
            case "84":
                _84();
                break;
            case "85":
                _85();
                break;
            case "86":
                _86();
                break;
            case "A7":
                _A7();
                break;
            case "A0":
                _A0();
                break;
            case "A1":
                _A1();
                break;
            case "A2":
                _A2();
                break;
            case "A3":
                _A3();
                break;
            case "A4":
                _A4();
                break;
            case "A5":
                _A5();
                break;
            case "A6":
                _A6();
                break;
            case "2F":
                _2F();
                break;
            case "3F":
                _3F();
                break;
            case "BF":
                _BF();
                break;
            case "B8":
                _B8();
                break;
            case "B9":
                _B9();
                break;
            case "BA":
                _BA();
                break;
            case "BB":
                _BB();
                break;
            case "BC":
                _BC();
                break;
            case "BD":
                _BD();
                break;
            case "BE":
                _BE();
                break;
            case "27":
                _27();
                break;
            case "09":
                _09();
                break;
            case "19":
                _19();
                break;
            case "29":
                _29();
                break;
            case "39":
                //_39();
                break;
            case "3D":
                _3D();
                break;
            case "05":
                _05();
                break;
            case "0D":
                _0D();
                break;
            case "15":
                _15();
                break;
            case "1D":
                _1D();
                break;
            case "25":
                _25();
                break;
            case "2D":
                _2D();
                break;
            case "35":
                _35();
                break;
            case "0B":
                _0B();
                break;
            case "1B":
                _1B();
                break;
            case "2B":
                _2B();
                break;
            case "3B":
                _3B();
                break;
            case "F3":
                //_F3();
                break;
            case "FB":
                //_FB();
                break;
            case "76":
                _76();
                break;
            case "3C":
                _3C();
                break;
            case "04":
                _04();
                break;
            case "0C":
                _0C();
                break;
            case "14":
                _14();
                break;
            case "1C":
                _1C();
                break;
            case "24":
                _24();
                break;
            case "2C":
                _2C();
                break;
            case "34":
                _34();
                break;
            case "03":
                _03();
                break;
            case "13":
                _13();
                break;
            case "23":
                _23();
                break;
            case "33":
                _33();
                break;
            case "0A":
                _0A();
                break;
            case "1A":
                _1A();
                break;
            case "7F":
                _7F();
                break;
            case "78":
                _78();
                break;
            case "79":
                _79();
                break;
            case "7A":
                _7A();
                break;
            case "7B":
                _7B();
                break;
            case "7C":
                _7C();
                break;
            case "7D":
                _7D();
                break;
            case "7E":
                _7E();
                break;
            case "47":
                _47();
                break;
            case "40":
                _40();
                break;
            case "41":
                _41();
                break;
            case "42":
                _42();
                break;
            case "43":
                _43();
                break;
            case "44":
                _44();
                break;
            case "45":
                _45();
                break;
            case "46":
                _46();
                break;
            case "4F":
                _4F();
                break;
            case "48":
                _48();
                break;
            case "49":
                _49();
                break;
            case "4A":
                _4A();
                break;
            case "4B":
                _4B();
                break;
            case "4C":
                _4C();
                break;
            case "4D":
                _4D();
                break;
            case "4E":
                _4E();
                break;
            case "57":
                _57();
                break;
            case "50":
                _50();
                break;
            case "51":
                _51();
                break;
            case "52":
                _52();
                break;
            case "53":
                _53();
                break;
            case "54":
                _54();
                break;
            case "55":
                _55();
                break;
            case "56":
                _56();
                break;
            case "5F":
                _5F();
                break;
            case "58":
                _58();
                break;
            case "59":
                _59();
                break;
            case "5A":
                _5A();
                break;
            case "5B":
                _5B();
                break;
            case "5C":
                _5C();
                break;
            case "5D":
                _5D();
                break;
            case "5E":
                _5E();
                break;
            case "67":
                _67();
                break;
            case "60":
                _60();
                break;
            case "61":
                _61();
                break;
            case "62":
                _62();
                break;
            case "63":
                _63();
                break;
            case "64":
                _64();
                break;
            case "65":
                _65();
                break;
            case "66":
                _66();
                break;
            case "6F":
                _6F();
                break;
            case "68":
                _68();
                break;
            case "69":
                _69();
                break;
            case "6A":
                _6A();
                break;
            case "6B":
                _6B();
                break;
            case "6C":
                _6C();
                break;
            case "6D":
                _6D();
                break;
            case "6E":
                _6E();
                break;
            case "77":
                _77();
                break;
            case "70":
                _70();
                break;
            case "71":
                _71();
                break;
            case "72":
                _72();
                break;
            case "73":
                _73();
                break;
            case "74":
                _74();
                break;
            case "75":
                _75();
                break;
            case "00":
                _00();
                break;
            case "B7":
                _B7();
                break;
            case "B0":
                _B0();
                break;
            case "B1":
                _B1();
                break;
            case "B2":
                _B2();
                break;
            case "B3":
                _B3();
                break;
            case "B4":
                _B4();
                break;
            case "B5":
                _B5();
                break;
            case "B6":
                _B6();
                break;
            case "E9":
                _E9();
                break;
            case "C1":
                _C1();
                break;
            case "C0":
                _C0();
                break;
            case "D1":
                _D1();
                break;
            case "E1":
                _E1();
                break;
            case "F1":
                _F1();
                break;
            case "C5":
                _C5();
                break;
            case "D5":
                _D5();
                break;
            case "E5":
                _E5();
                break;
            case "F5":
                _F5();
                break;
            case "17":
                _17();
                break;
            case "1F":
                _1F();
                break;
            case "D8":
                _D8();
                break;
            case "C9":
                _C9();
                break;
            case "20":
                //_20();
                break;
            case "07":
                _07();
                break;
            case "F8":
                _F8();
                break;
            case "D0":
                _D0();
                break;
            case "F0":
                _F0();
                break;
            case "E8":
                _E8();
                break;
            case "E0":
                _E0();
                break;
            case "0F":
                _0F();
                break;
            case "C7":
                _C7();
                break;
            case "CF":
                _CF();
                break;
            case "D7":
                _D7();
                break;
            case "DF":
                _DF();
                break;
            case "E7":
                _E7();
                break;
            case "EF":
                _EF();
                break;
            case "F7":
                _F7();
                break;
            case "FF":
                _FF();
                break;
            case "C8":
                _C8();
                break;
            case "9F":
                _9F();
                break;
            case "98":
                _98();
                break;
            case "99":
                _99();
                break;
            case "9A":
                _9A();
                break;
            case "9B":
                _9B();
                break;
            case "9C":
                _9C();
                break;
            case "9D":
                _9D();
                break;
            case "9E":
                _9E();
                break;
            case "30":
                //	_30();
                break;
            case "F9":
                _F9();
                break;
            case "02":
                _02();
                break;
            case "12":
                _12();
                break;
            case "37":
                _37();
                break;
            case "97":
                _97();
                break;
            case "90":
                _90();
                break;
            case "91":
                _91();
                break;
            case "92":
                _92();
                break;
            case "93":
                _93();
                break;
            case "94":
                _94();
                break;
            case "95":
                _95();
                break;
            case "96":
                _96();
                break;
            case "EB":
                _EB();
                break;
            case "AF":
                _AF();
                break;
            case "A8":
                _A8();
                break;
            case "A9":
                _A9();
                break;
            case "AA":
                _AA();
                break;
            case "AB":
                _AB();
                break;
            case "AC":
                _AC();
                break;
            case "AD":
                _AD();
                break;
            case "AE":
                _AE();
                break;
            case "E3":
                _E3();
                break;
            case "CE":
                _CE();
                break;
            case "C6":
                _C6();
                break;
            case "E6":
                _E6();
                break;
            case "FE":
                _FE();
                break;
            case "DB":
                //	_DB();
                break;
            case "3E":
                _3E();
                break;
            case "06":
                _06();
                break;
            case "0E":
                _0E();
                break;
            case "16":
                _16();
                break;
            case "1E":
                _1E();
                break;
            case "26":
                _26();
                break;
            case "2E":
                _2E();
                break;
            case "36":
                _36();
                break;
            case "F6":
                _F6();
                break;
            case "D3":
                //	_D3();
                break;
            case "DE":
                _DE();
                break;
            case "D6":
                _D6();
                break;
            case "EE":
                _EE();
                break;
            case "CD":
                _CD();
                break;
            case "DC":
                _DC();
                break;
            case "FC":
                _FC();
                break;
            case "D4":
                _D4();
                break;
            case "C4":
                _C4();
                break;
            case "F4":
                _F4();
                break;
            case "EC":
                _EC();
                break;
            case "E4":
                _E4();
                break;
            case "CC":
                _CC();
                break;
            case "DA":
                _DA();
                break;
            case "FA":
                _FA();
                break;
            case "C3":
                _C3();
                break;
            case "D2":
                _D2();
                break;
            case "C2":
                _C2();
                break;
            case "F2":
                _F2();
                break;
            case "EA":
                _EA();
                break;
            case "E2":
                _E2();
                break;
            case "CA":
                _CA();
                break;
            case "3A":
                _3A();
                break;
            case "2A":
                _2A();
                break;
            case "01":
                _01();
                break;
            case "11":
                _11();
                break;
            case "21":
                _21();
                break;
            case "31":
                _31();
                break;
            case "22":
                _22();
                break;
            case "32":
                _32();
                break;

        }
        InstructionExecuted instructionExecuted = new InstructionExecuted();
        instructionExecuted.setInstruction(Data.from(op));
        instructionExecuted.setNextAddress(getIP());
        this.getInstructionExecuted$().onNext(instructionExecuted);
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
                    setData(address, data);
                    System.out.println(localval.substring(2, 4) + " is set at " + localmem);
                    int x = Integer.parseInt(localmem, 16);
                    x++;
                    localmem = Address.from(x).hexValue();
                    address = Address.from(localmem);
                    data = Data.from(localval.substring(0, 2));
                    setData(address, data);
                }
            }

        }
    }

    public void PassOne(String[] x) {
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
                String a2 = findOpcode(a1);
                int a3 = OpcodeLength(a2);
                memory.setData(LP, a2);
                String l = ExtractLabel(s);
                map[LP][LABEL] = l;
                map[LP][OPCODE] = m.group();
//                System.out.println(map[LP][OPCODE]+" for "+i);
                if (l.length() != 0) {
                    for (int g = 0; g < SYMPTR1; g++) {
                        if (ST1[g][LABEL].equalsIgnoreCase(l)) {
                            JOptionPane.showMessageDialog(this, l + " already Exists at " + ST1[g][MEM] + " Terminating rest of the process");
                            flag = 1;
                        }
                    }
                    if (flag == 0) {
                        ST1[SYMPTR1][LABEL] = l;
                        //System.out.println("Breakpoint");
                        ST1[SYMPTR1][MEM] = Address.from(LP).hexValue();
                        SYMPTR1++;
                    }
                }
                if (a3 == 1) {
                    LP = LP + 1;
                } else if (a3 == 2) {
                    LP = LP + 1;
                    memory.setData(LP, ExtractData(s));
                    map[LP][OPCODE] = "";
                    map[LP][LABEL] = "";
                    LP = LP + 1;
                } else if (a3 == 3) {
                    l = extractAddress(s);

                    if (l.length() != 0) {
                        LP = LP + 1;
                        map[LP][OPCODE] = "";
                        map[LP][LABEL] = "";
                        memory.setData(LP, l.substring(2, 4));
                        LP = LP + 1;
                        map[LP][OPCODE] = "";
                        map[LP][LABEL] = "";
                        memory.setData(LP, l.substring(0, 2));
                        LP = LP + 1;
                    } else {
                        l = getLabel(s);

                        LP = LP + 1;
                        ST[SYMPTR][LABEL] = l;
                        ST[SYMPTR][MEM] = Address.from(LP).hexValue();
                        SYMPTR++;
                        map[LP][OPCODE] = "";
                        map[LP][LABEL] = "";
                        LP = LP + 1;
                        map[LP][OPCODE] = "";
                        map[LP][LABEL] = "";
                        LP = LP + 1;
                    }
                }

            } else {
                JOptionPane.showMessageDialog(this, "Instruction " + s + " NOT Valid\n Skipping invalid Instruction");
            }
        }
    }


    @Override
    public void setA(IData x) {
        IRegister a = getA();
        if(a == null) {
            this.A = Register.makeRegister();
        }
        getA().update(x);
    }

    @Override
    public void setB(IData x) {
        IRegister r = getB();
        if(r == null) {
            this.B = Register.makeRegister();
        }
        getB().update(x);
    }

    @Override
    public void setC(IData x) {
        IRegister r = getC();
        if(r == null) {
            this.C = Register.makeRegister();
        }
        getC().update(x);
    }

    @Override
    public void setD(IData x) {
        IRegister r = getD();
        if(r == null) {
            this.D = Register.makeRegister();
        }
        getD().update(x);
    }

    @Override
    public void setE(IData x) {
        IRegister r = getE();
        if(r == null) {
            this.E = Register.makeRegister();
        }
        getE().update(x);
    }

    @Override
    public void setH(IData x) {
        IRegister r = getH();
        if(r == null) {
            this.H = Register.makeRegister();
        }
        getH().update(x);
    }

    @Override
    public void setL(IData x) {
        IRegister r = getL();
        if(r == null) {
            this.L = Register.makeRegister();
        }
        getL().update(x);
    }

    @Override
    public void setSP(IAddress x) {
        SP = x;
    }

    @Override
    public void setIP(IAddress x) {
        IP = x;
        updateCodeTable();
    }

    @Override
    public IRegister getA() {
        return A;
    }

    @Override
    public IRegister getB() {
        return B;
    }

    @Override
    public IRegister getC() {
        return C;
    }

    @Override
    public IRegister getD() {
        return D;
    }

    @Override
    public IRegister getE() {
        return E;
    }

    @Override
    public IRegister getH() {
        return H;
    }

    public IRegister getL() {
        return L;
    }

    @Override
    public IAddress getSP() {
        return SP;
    }

    @Override
    public IAddress getIP() {
        return IP;
    }

    @Override
    public void setS(Integer x) {
        if (x < 2 && x > -1) {
            flags.setFlag(Flag.S, x);
        }
        // @TODO: Handel if not the case
    }

    @Override
    public void setZ(Integer x) {
        if (x < 2 && x > -1) {
            flags.setFlag(Flag.Z, x);
        }
        // @TODO: Handel if not the case
    }

    @Override
    public void setAc(Integer x) {
        if (x < 2 && x > -1) {
            flags.setFlag(Flag.Ac, x);
        }
        // @TODO: Handel if not the case
    }

    @Override
    public void setP(Integer x) {
        if (x < 2 && x > -1) {
            flags.setFlag(Flag.P, x);
        }
        // @TODO: Handel if not the case
    }

    @Override
    public void setCy(Integer x) {
        if (x < 2 && x > -1) {
            flags.setFlag(Flag.Cy, x);
        }
        // @TODO: Handel if not the case
    }

    public Integer getS() {
        return flags.getFlag(Flag.S);
    }

    public Integer getZ() {
        return flags.getFlag(Flag.Z);
    }

    public Integer getCy() {
        return flags.getFlag(Flag.Cy);
    }

    public Integer getP() {
        return flags.getFlag(Flag.P);
    }

    public Integer getAc() {
        return flags.getFlag(Flag.Ac);
    }

    public IData getData(IAddress address) {
        return memory.getData(address.intValue());
    }

    public void setData(IAddress address, IData data) {
        memory.setData(address, data);
    }

    public IData getM() {
        return getData(getHlAddress());
    }

    public void setM(IData x) {
        setData(getHlAddress(), x);
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

    public IAddress getHlAddress() {
        IData localH, localL;
        localH = getH();
        localL = getL();
        return Address.from(localH, localL);
    }

    /* MOV INSTRUCTIONS */
    public void _7F() {
        setA(getA());
        nextInstructionPointer();
    }

    public void _78() {
        setA(getB());
        nextInstructionPointer();
    }

    public void _79() {
        setA(getC());
        nextInstructionPointer();
    }

    public void _7A() {
        setA(getD());
        nextInstructionPointer();
    }

    public void _7B() {
        setA(getE());
        nextInstructionPointer();
    }

    public void _7C() {
        setA(getH());
        nextInstructionPointer();
    }

    public void _7D() {
        setA(getL());
        nextInstructionPointer();
    }

    public void _7E() {
        setA(getM());
        nextInstructionPointer();
    }

    public void _47() {
        setB(getA());
        nextInstructionPointer();
    }

    public void _40() {
        setB(getB());
        nextInstructionPointer();
    }

    public void _41() {
        setB(getC());
        nextInstructionPointer();
    }

    public void _42() {
        setB(getD());
        nextInstructionPointer();
    }

    public void _43() {
        setB(getE());
        nextInstructionPointer();
    }

    public void _44() {
        setB(getH());
        nextInstructionPointer();
    }

    public void _45() {
        setB(getL());
        nextInstructionPointer();
    }

    public void _46() {
        setB(getM());
        nextInstructionPointer();
    }

    public void _4F() {
        setC(getA());
        nextInstructionPointer();
    }

    public void _48() {
        setC(getB());
        nextInstructionPointer();
    }

    public void _49() {
        setC(getC());
        nextInstructionPointer();
    }

    public void _4A() {
        setC(getD());
        nextInstructionPointer();
    }

    public void _4B() {
        setC(getE());
        nextInstructionPointer();
    }

    public void _4C() {
        setC(getH());
        nextInstructionPointer();
    }

    public void _4D() {
        setC(getL());
        nextInstructionPointer();
    }

    public void _4E() {
        setC(getM());
        nextInstructionPointer();
    }

    public void _57() {
        setD(getA());
        nextInstructionPointer();
    }

    public void _50() {
        setD(getB());
        nextInstructionPointer();
    }

    public void _51() {
        setD(getC());
        nextInstructionPointer();
    }

    public void _52() {
        setD(getD());
        nextInstructionPointer();
    }

    public void _53() {
        setD(getE());
        nextInstructionPointer();
    }

    public void _54() {
        setD(getH());
        nextInstructionPointer();
    }

    public void _55() {
        setD(getL());
        nextInstructionPointer();
    }

    public void _56() {
        setD(getM());
        nextInstructionPointer();
    }

    public void _5F() {
        setE(getA());
        nextInstructionPointer();
    }

    public void _58() {
        setE(getB());
        nextInstructionPointer();
    }

    public void _59() {
        setE(getC());
        nextInstructionPointer();
    }

    public void _5A() {
        setE(getD());
        nextInstructionPointer();
    }

    public void _5B() {
        setE(getE());
        nextInstructionPointer();
    }

    public void _5C() {
        setE(getH());
        nextInstructionPointer();
    }

    public void _5D() {
        setE(getL());
        nextInstructionPointer();
    }

    public void _5E() {
        setE(getM());
        nextInstructionPointer();
    }

    public void _67() {
        setH(getA());
        nextInstructionPointer();
    }

    public void _60() {
        setH(getB());
        nextInstructionPointer();
    }

    public void _61() {
        setH(getC());
        nextInstructionPointer();
    }

    public void _62() {
        setH(getD());
        nextInstructionPointer();
    }

    public void _63() {
        setH(getE());
        nextInstructionPointer();
    }

    public void _64() {
        setH(getH());
        nextInstructionPointer();
    }

    public void _65() {
        setH(getL());
        nextInstructionPointer();
    }

    public void _66() {
        setH(getM());
        nextInstructionPointer();
    }

    public void _6F() {
        setL(getA());
        nextInstructionPointer();
    }

    public void _68() {
        setL(getB());
        nextInstructionPointer();
    }

    public void _69() {
        setL(getC());
        nextInstructionPointer();
    }

    public void _6A() {
        setL(getD());
        nextInstructionPointer();
    }

    public void _6B() {
        setL(getE());
        nextInstructionPointer();
    }

    public void _6C() {
        setL(getH());
        nextInstructionPointer();
    }

    public void _6D() {
        setL(getL());
        nextInstructionPointer();
    }

    public void _6E() {
        setL(getM());
        nextInstructionPointer();
    }

    public void _77() {
        setM(getA());
        nextInstructionPointer();
    }

    public void _70() {
        setM(getB());
        nextInstructionPointer();
    }

    public void _71() {
        setM(getC());
        nextInstructionPointer();
    }

    public void _72() {
        setM(getD());
        nextInstructionPointer();
    }

    public void _73() {
        setM(getE());
        nextInstructionPointer();
    }

    public void _74() {
        setM(getH());
        nextInstructionPointer();
    }

    public void _75() {
        setM(getL());
        nextInstructionPointer();
    }

    public void _76() {
        jStep.setEnabled(false);
    }

    private IData getDataAtIP() {
        return getData(getIP());
    }
    
    public void _3E() {
        nextInstructionPointer();
        setA(getDataAtIP());
        nextInstructionPointer();
    }

    public void _06() {
        nextInstructionPointer();
        setB(getDataAtIP());
        nextInstructionPointer();
    }

    public void _0E() {
        nextInstructionPointer();
        setC(getDataAtIP());
        nextInstructionPointer();
    }

    public void _16() {
        nextInstructionPointer();
        setD(getDataAtIP());
        nextInstructionPointer();
    }

    public void _1E() {
        nextInstructionPointer();
        setE(getDataAtIP());
        nextInstructionPointer();
    }

    public void _26() {
        nextInstructionPointer();
        setH(getDataAtIP());
        nextInstructionPointer();
    }

    public void _2E() {
        nextInstructionPointer();
        setL(getDataAtIP());
        nextInstructionPointer();
    }

    public void _36() {
        nextInstructionPointer();
        setM(getDataAtIP());
        nextInstructionPointer();
    }

    public String Trimmer(String x) {
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
        x = Trimmer(x.substring(d + 1));
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
                System.out.print("Invalied opcode");
        }
        return 0;
    }

    public int run_code_index = 0;

    /**
     * Creates new form test1
     */
    public Main() {
        initComponents();
        initializePatt();
        initializeDomain();
        jStep.setEnabled(false);
    }

    public PublishSubject<InstructionExecuted> getInstructionExecuted$() {
        return instructionExecuted$;
    }

    public void setInstructionExecuted$(PublishSubject<InstructionExecuted> instructionExecuted$) {
        this.instructionExecuted$ = instructionExecuted$;
    }

    private void subscribeInstructionPointer() {
        this.instructionExecutedSub.dispose();
        this.instructionExecutedSub = this.getInstructionExecuted$()
            .subscribe( instructionExecuted -> {
                System.out.println("Debug new Address="+instructionExecuted.getNextAddress().hexValue());
                updateView();
            });

        this.errorSub.dispose();
        this.errorSub = getOnError$()
                .subscribe(err -> {
                    JOptionPane.showMessageDialog(this, err.getMessage());
                });
    }

    private void initializeDomain() {
        setA(IData.ZERO);
        setB(IData.ZERO);
        setC(IData.ZERO);
        setD(IData.ZERO);
        setE(IData.ZERO);
        setH(IData.ZERO);
        setL(IData.ZERO);
        setCy(0);
        setAc(0);
        setS(0);
        setZ(0);
        setP(0);
        setIP(IAddress.ZERO);
        setSP(IAddress.FFFF);
        setInstructionExecuted$(PublishSubject.create());
        setOnError$(PublishSubject.create());
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
        oldIP = 0;
        jStep.setEnabled(true);
        initializeDomain();
        subscribeInstructionPointer();
        iniMap();
        String[] instructions = getLineInstructions();
        PassOne(instructions);
        PassTwo();
        String test123[] = memory.getSlice(0, 20).getHexDataArray();
        run_code.setListData(test123);
        run_code_index = 0;
        System.out.println("Symbol Table for Where to Load\nLABEL\tMEMORY");
        for (int e = 0; e < 10; e++) {
            System.out.println(ST[e][LABEL] + "\t" + ST[e][MEM]);
        }
        System.out.println("Symbol Table for Symbol and their LOACTIONS\nLABEL\tMEMORY");
        for (int e = 0; e < 10; e++) {
            System.out.println(ST1[e][LABEL] + "\t" + ST1[e][MEM]);
        }

        updateCodeTable();

    }//GEN-LAST:event_jButton1ActionPerformed

    private void jStepActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jStepActionPerformed
        // TODO add your handling code here:
        int nip = getIP().intValue();
        execute(getDataAtIP().hexValue());
        nip = nip - Address.from(CodeHead.getText()).intValue();

        CodeTable.addRowSelectionInterval(nip, nip);
        if (oldIP != nip) {
            CodeTable.removeRowSelectionInterval(oldIP, oldIP);
        }
        oldIP = nip;
        // run_code.setSelectedIndex(run_code_index);
        // run_code_index++;
        // System.out.println(run_code.getSelectedValue());
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
                    setData(Address.from(p), Data.from(q));
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
                setData(Address.from(p), Data.from(q));
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
    //ADD A

    public void _87() {
        IOperationResult r = getA().add(getA());
        updateFlags(r.getFlags());
        setA(r.getData());
        nextInstructionPointer();
    }

    private void updateFlags(IFlags flags) {
        Set<Flag> keys = flags.getKeys();
        for(Flag k: keys) {
            switch (k) {
                case P: {
                    setP(flags.getFlag(Flag.P));
                    break;
                }
                case S: {
                    setS(flags.getFlag(Flag.S));
                    break;
                }
                case Ac: {
                    setAc(flags.getFlag(Flag.Ac));
                    break;
                }
                case Cy: {
                    setCy(flags.getFlag(Flag.Cy));
                    break;
                }
                case Z: {
                    setZ(flags.getFlag(Flag.Z));
                    break;
                }
            }
        }
    }
//ADD B

    public void _80() {
        IOperationResult r = getA().add(getB());
        updateFlags(r.getFlags());
        setA(r.getData());
        nextInstructionPointer();
    }
//ADD C

    public void _81() {
        IOperationResult r = getA().add(getC());
        updateFlags(r.getFlags());
        setA(r.getData());
        nextInstructionPointer();
    }
//ADD D

    public void _82() {
        IOperationResult r = getA().add(getD());
        updateFlags(r.getFlags());
        setA(r.getData());
        nextInstructionPointer();
    }
//ADD E

    public void _83() {
        IOperationResult r = getA().add(getE());
        updateFlags(r.getFlags());
        setA(r.getData());
        nextInstructionPointer();
    }
//ADD H

    public void _84() {
        IOperationResult r = getA().add(getH());
        updateFlags(r.getFlags());
        setA(r.getData());
        nextInstructionPointer();
    }
//ADD L

    public void _85() {
        IOperationResult r = getA().add(getL());
        updateFlags(r.getFlags());
        setA(r.getData());
        nextInstructionPointer();
    }
//ADD M

    public void _86() {
        IOperationResult r = getA().add(getM());
        updateFlags(r.getFlags());
        setA(r.getData());
        nextInstructionPointer();
    }
//ADI data

    public void _C6() {
        nextInstructionPointer();
        IAddress InstPtr = getIP();
        IOperationResult r = getA().add(memory.getData(InstPtr.intValue()));
        updateFlags(r.getFlags());
        setA(r.getData());
        nextInstructionPointer();
    }

//ADC A
    public void _8F() {
        IOperationResult r = getA().add(getA(), getCy());
        updateFlags(r.getFlags());
        setA(r.getData());
        nextInstructionPointer();
    }
//ADC B

    public void _88() {
        IOperationResult r = getA().add(getB(), getCy());
        updateFlags(r.getFlags());
        setA(r.getData());
        nextInstructionPointer();
    }
//ADC C

    public void _89() {
        IOperationResult r = getA().add(getC(), getCy());
        updateFlags(r.getFlags());
        setA(r.getData());
        nextInstructionPointer();
    }
//ADC D

    public void _8A() {
        IOperationResult r = getA().add(getD(), getCy());
        updateFlags(r.getFlags());
        setA(r.getData());
        nextInstructionPointer();
    }
//ADC E

    public void _8B() {
        IOperationResult r = getA().add(getD(), getCy());
        updateFlags(r.getFlags());
        setA(r.getData());
        nextInstructionPointer();
    }
//ADC H

    public void _8C() {
        IOperationResult r = getA().add(getH(), getCy());
        updateFlags(r.getFlags());
        setA(r.getData());
        nextInstructionPointer();
    }
//ADC L

    public void _8D() {
        IOperationResult r = getA().add(getL(), getCy());
        updateFlags(r.getFlags());
        setA(r.getData());
        nextInstructionPointer();
    }
//ADC M

    public void _8E() {
        IOperationResult r = getA().add(getM(), getCy());
        updateFlags(r.getFlags());
        setA(r.getData());
        nextInstructionPointer();
    }
//ACI

    public void _CE() {
        IOperationResult r = getA().add(getDataAtIP(), getCy());
        updateFlags(r.getFlags());
        setA(r.getData());
        nextInstructionPointer();
    }

    void sub(IData r) {
        IOperationResult operationResult = getA().minus(r);
        IFlags flags = operationResult.getFlags();
        IData result = operationResult.getData();
        updateFlags(flags);
        setA(result);
        nextInstructionPointer();
    }
//SUB A

    public void _97() {
        this.sub(getA());
    }
//SUB B

    public void _90() {
        this.sub(getB());
    }
//SUB C

    public void _91() {
        this.sub(getC());
    }
//SUB D

    public void _92() {
        this.sub(getD());
    }
//SUB E

    public void _93() {
        this.sub(getE());
    }
//SUB H

    public void _94() {
        this.sub(getH());
    }
//SUB L

    public void _95() {
        this.sub(getL());
    }
//SUB M

    public void _96() {
        this.sub(getM());
    }
//SUI Data

    public void _D6() {
        IData r = getDataAtIP();
        this.sub(r);
    }
//SBB A

    public void _9F() {
        int r1, r2, r3;
        r1 = getA().intValue();
        r2 = getA().intValue();
        int r4;
        r4 = getCy();
        r3 = r1 - r2 - r4;
        if (r3 < 0) {
            r3 *= -1;
            setS(1);
        } else {
            setS(0);
        }
        if (r3 == 0) {
            setZ(1);
        } else {
            setZ(0);
        }
        if (r3 > 255) {
            setCy(1);
        } else {
            setCy(0);
        }
        String temp = Integer.toHexString(r3);
        setA(Data.from(temp));
        PARITY();
        nextInstructionPointer();
    }
//SBB B

    public void _98() {
        int r1, r2, r3;
        r1 = getA().intValue();
        r2 = getB().intValue();
        int r4;
        r4 = getCy();
        r3 = r1 - r2 - r4;
        if (r3 < 0) {
            r3 *= -1;
            setS(1);
        } else {
            setS(0);
        }
        if (r3 == 0) {
            setZ(1);
        } else {
            setZ(0);
        }
        if (r3 > 255) {
            setCy(1);
        } else {
            setCy(0);
        }
        String temp = Integer.toHexString(r3);
        setA(Data.from(temp));
        PARITY();
        nextInstructionPointer();
    }
//SBB C

    public void _99() {
        int r1, r2, r3;
        r1 = getA().intValue();
        r2 = getC().intValue();
        int r4;
        r4 = getCy();
        r3 = r1 - r2 - r4;
        if (r3 < 0) {
            r3 *= -1;
            setS(1);
        } else {
            setS(0);
        }
        if (r3 == 0) {
            setZ(1);
        } else {
            setZ(0);
        }
        if (r3 > 255) {
            setCy(1);
        } else {
            setCy(0);
        }
        String temp = Integer.toHexString(r3);
        setA(Data.from(temp));
        PARITY();
        nextInstructionPointer();
    }
//SBB D

    public void _9A() {
        int r1, r2, r3;
        r1 = getA().intValue();
        r2 = getD().intValue();
        int r4;
        r4 = getCy();
        r3 = r1 - r2 - r4;
        if (r3 < 0) {
            r3 *= -1;
            setS(1);
        } else {
            setS(0);
        }
        if (r3 == 0) {
            setZ(1);
        } else {
            setZ(0);
        }
        if (r3 > 255) {
            setCy(1);
        } else {
            setCy(0);
        }
        String temp = Integer.toHexString(r3);
        setA(Data.from(temp));
        PARITY();
        nextInstructionPointer();
    }
//SBB E

    public void _9B() {
        int r1, r2, r3;
        r1 = getA().intValue();
        r2 = getE().intValue();
        int r4;
        r4 = getCy();
        r3 = r1 - r2 - r4;
        if (r3 < 0) {
            r3 *= -1;
            setS(1);
        } else {
            setS(0);
        }
        if (r3 == 0) {
            setZ(1);
        } else {
            setZ(0);
        }
        if (r3 > 255) {
            setCy(1);
        } else {
            setCy(0);
        }
        String temp = Integer.toHexString(r3);
        setA(Data.from(temp));
        PARITY();
        nextInstructionPointer();
    }
//SBB H

    public void _9C() {
        int r1, r2, r3;
        r1 = getA().intValue();
        r2 = getH().intValue();
        int r4;
        r4 = getCy();
        r3 = r1 - r2 - r4;
        if (r3 < 0) {
            r3 *= -1;
            setS(1);
        } else {
            setS(0);
        }
        if (r3 == 0) {
            setZ(1);
        } else {
            setZ(0);
        }
        if (r3 > 255) {
            setCy(1);
        } else {
            setCy(0);
        }
        String temp = Integer.toHexString(r3);
        setA(Data.from(temp));
        PARITY();
        nextInstructionPointer();
    }
//SBB L

    public void _9D() {
        int r1, r2, r3;
        r1 = getA().intValue();
        r2 = getL().intValue();
        int r4;
        r4 = getCy();
        r3 = r1 - r2 - r4;
        if (r3 < 0) {
            r3 *= -1;
            setS(1);
        } else {
            setS(0);
        }
        if (r3 == 0) {
            setZ(1);
        } else {
            setZ(0);
        }
        if (r3 > 255) {
            setCy(1);
        } else {
            setCy(0);
        }
        String temp = Integer.toHexString(r3);
        setA(Data.from(temp));
        PARITY();
        nextInstructionPointer();
    }
//SBB M

    public void _9E() {
        int r1, r2, r3;
        r1 = getA().intValue();
        r2 = getM().intValue();
        int r4;
        r4 = getCy();
        r3 = r1 - r2 - r4;
        if (r3 < 0) {
            r3 *= -1;
            setS(1);
        } else {
            setS(0);
        }
        if (r3 == 0) {
            setZ(1);
        } else {
            setZ(0);
        }
        if (r3 > 255) {
            setCy(1);
        } else {
            setCy(0);
        }
        String temp = Integer.toHexString(r3);
        setA(Data.from(temp));
        PARITY();
        nextInstructionPointer();
    }
//SBI

    public void _DE() {
        int r1, r2, r3;
        r1 = getA().intValue();
        nextInstructionPointer();
        r2 = getDataAtIP().intValue();
        int r4;
        r4 = getCy();
        r3 = r1 - r2 - r4;
        if (r3 < 0) {
            r3 *= -1;
            setS(1);
        } else {
            setS(0);
        }
        if (r3 == 0) {
            setZ(1);
        } else {
            setZ(0);
        }
        if (r3 > 255) {
            setCy(1);
        } else {
            setCy(0);
        }
        String temp = Integer.toHexString(r3);
        setA(Data.from(temp));
        PARITY();
        nextInstructionPointer();
    }

//INR A
    public void _3C() {
        int r;
        r = getA().intValue();
        r++;
        String temp = Integer.toHexString(r);
        setA(Data.from(temp));
        nextInstructionPointer();
    }
//INR B

    public void _04() {
        int r;
        r = getB().intValue();
        r++;
        String temp = Integer.toHexString(r);
        setB(Data.from(temp));
        nextInstructionPointer();
    }
//INR C

    public void _0C() {
        int r;
        r = getC().intValue();
        r++;
        String temp = Integer.toHexString(r);
        setC(Data.from(temp));
        nextInstructionPointer();
    }
//INR D

    public void _14() {
        int r;
        r = getD().intValue();
        r++;
        String temp = Integer.toHexString(r);
        setD(Data.from(temp));
        nextInstructionPointer();
    }
//INR E

    public void _1C() {
        int r;
        r = getE().intValue();
        r++;
        String temp = Integer.toHexString(r);
        setE(Data.from(temp));
        nextInstructionPointer();
    }
//INR H

    public void _24() {
        int r;
        r = getH().intValue();
        r++;
        String temp = Integer.toHexString(r);
        setH(Data.from(temp));
        nextInstructionPointer();
    }
//INR L

    public void _2C() {
        int r;
        r = getL().intValue();
        r++;
        String temp = Integer.toHexString(r);
        setL(Data.from(temp));
        nextInstructionPointer();
    }
//INR M

    public void _34() {
        int r;
        r = getM().intValue();
        r++;
        String temp = Integer.toHexString(r);
        setM(Data.from(temp));
        nextInstructionPointer();
    }
//CMP A

    public void _BF() {
        int r1, r2;
        r1 = getA().intValue();
        r2 = getA().intValue();
        if (r1 < r2) {
            setCy(1);
        }
        if (r1 == r2) {
            setZ(1);
        }
        if (r1 > r2) {
            setZ(0);
            setCy(0);
        }
        nextInstructionPointer();
    }

//CMP B
    public void _B8() {
        int r1, r2;
        r1 = getA().intValue();
        r2 = getB().intValue();
        if (r1 < r2) {
            setCy(1);
        }
        if (r1 == r2) {
            setZ(1);
        }
        if (r1 > r2) {
            setZ(0);
            setCy(0);
        }
        nextInstructionPointer();
    }
//CMP C

    public void _B9() {
        int r1, r2;
        r1 = getA().intValue();
        r2 = getC().intValue();
        if (r1 < r2) {
            setCy(1);
        }
        if (r1 == r2) {
            setZ(1);
        }
        if (r1 > r2) {
            setZ(0);
            setCy(0);
        }
        nextInstructionPointer();
    }
//CMP D

    public void _BA() {
        int r1, r2;
        r1 = getA().intValue();
        r2 = getD().intValue();
        if (r1 < r2) {
            setCy(1);
        }
        if (r1 == r2) {
            setZ(1);
        }
        if (r1 > r2) {
            setZ(0);
            setCy(0);
        }
        nextInstructionPointer();
    }
//CMP E

    public void _BB() {
        int r1, r2;
        r1 = getA().intValue();
        r2 = getE().intValue();
        if (r1 < r2) {
            setCy(1);
        }
        if (r1 == r2) {
            setZ(1);
        }
        if (r1 > r2) {
            setZ(0);
            setCy(0);
        }
        nextInstructionPointer();
    }
//CMP H

    public void _BC() {
        int r1, r2;
        r1 = getA().intValue();
        r2 = getH().intValue();
        if (r1 < r2) {
            setCy(1);
        }
        if (r1 == r2) {
            setZ(1);
        }
        if (r1 > r2) {
            setZ(0);
            setCy(0);
        }
        nextInstructionPointer();
    }
//CMP L

    public void _BD() {
        int r1, r2;
        r1 = getA().intValue();
        r2 = getL().intValue();
        if (r1 < r2) {
            setCy(1);
        }
        if (r1 == r2) {
            setZ(1);
        }
        if (r1 > r2) {
            setZ(0);
            setCy(0);
        }
        nextInstructionPointer();
    }

    public void _BE() {
        int r1, r2;
        r1 = getA().intValue();
        r2 = getM().intValue();
        if (r1 < r2) {
            setCy(1);
        }
        if (r1 == r2) {
            setZ(1);
        }
        if (r1 > r2) {
            setZ(0);
            setCy(0);
        }
        nextInstructionPointer();
    }
//DCR A

    public void _3D() {
        int r;
        r = getA().intValue();
        r--;
        if (r == 0) {
            setZ(1);
        }
        String temp = Integer.toHexString(r);
        setA(Data.from(temp));
        nextInstructionPointer();
    }
//DCR B

    public void _05() {
        int r;
        r = getB().intValue();
        r--;
        if (r == 0) {
            setZ(1);
        }
        String temp = Integer.toHexString(r);
        setB(Data.from(temp));
        nextInstructionPointer();
    }
//DCR C

    public void _0D() {
        int r;
        r = getC().intValue();
        r--;
        if (r == 0) {
            setZ(1);
        }
        String temp = Integer.toHexString(r);
        setC(Data.from(temp));
        nextInstructionPointer();
    }
//DCR D

    public void _15() {
        int r;
        r = getD().intValue();
        r--;
        if (r == 0) {
            setZ(1);
        }
        String temp = Integer.toHexString(r);
        setD(Data.from(temp));
        nextInstructionPointer();
    }
//DCR E

    public void _1D() {
        int r;
        r = getE().intValue();
        r--;
        if (r == 0) {
            setZ(1);
        }
        String temp = Integer.toHexString(r);
        setE(Data.from(temp));
        nextInstructionPointer();
    }
//DCR H

    public void _25() {
        int r;
        r = getH().intValue();
        r--;
        if (r == 0) {
            setZ(1);
        }
        String temp = Integer.toHexString(r);
        setH(Data.from(temp));
        nextInstructionPointer();
    }
//DCR L

    public void _2D() {
        int r;
        r = getL().intValue();
        r--;
        if (r == 0) {
            setZ(1);
        }
        String temp = Integer.toHexString(r);
        setL(Data.from(temp));
        nextInstructionPointer();
    }
//DCR M

    public void _35() {
        int r;
        r = getM().intValue();
        r--;
        if (r == 0) {
            setZ(1);
        }
        String temp = Integer.toHexString(r);
        setM(Data.from(temp));
        nextInstructionPointer();
    }
//XCHG

    public void _EB() {
        IData r1, r2;
        r1 = getD();
        r2 = getE();
        setD(getH());
        setE(getL());
        setH(r1);
        setL(r2);
        nextInstructionPointer();
    }

    private void dadOperation(IData leftRegister, IData rightRegister) {
        IAddress r1, r2;
        r1 = Address.from(leftRegister, rightRegister);
        r2 = Address.from(getH(), getL());

        int sum = r1.intValue() + r2.intValue();
        if (sum > 65535) {
            setCy(1);
        }

        IAddress temp = Address.from(sum);
        IData t1 = temp.getLSB();
        IData t2 = temp.getMSB();
        setL(t1);
        setH(t2);
        nextInstructionPointer();
    }

    //DAD B
    public void _09() {
        this.dadOperation(getB(), getC());
    }
//DAD D

    public void _19() {
        this.dadOperation(getD(), getE());
    }

    //DAD H
    public void _29() {
        this.dadOperation(getH(), getL());
    }
//ANA A

    public void _A7() {
        int r1, r2;
        r1 = getA().intValue();
        r2 = getA().intValue();
        int s = r1 & r2;
        String temp = Integer.toHexString(s);
        setA(Data.from(temp));
        if (s > 127) {
            setS(1);
        }
        if (s == 0) {
            setZ(1);
        }
        nextInstructionPointer();
    }
//ANA B

    public void _A0() {
        int r1, r2;
        r1 = getA().intValue();
        r2 = getB().intValue();
        int s = r1 & r2;
        String temp = Integer.toHexString(s);
        setA(Data.from(temp));
        if (s > 127) {
            setS(1);
        }
        if (s == 0) {
            setZ(1);
        }
        nextInstructionPointer();
    }
//ANA C

    public void _A1() {
        int r1, r2;
        r1 = getA().intValue();
        r2 = getC().intValue();
        int s = r1 & r2;
        String temp = Integer.toHexString(s);
        setA(Data.from(temp));
        if (s > 127) {
            setS(1);
        }
        if (s == 0) {
            setZ(1);
        }
        nextInstructionPointer();
    }
//ANA D

    public void _A2() {
        int r1, r2;
        r1 = getA().intValue();
        r2 = getD().intValue();
        int s = r1 & r2;
        String temp = Integer.toHexString(s);
        setA(Data.from(temp));
        if (s > 127) {
            setS(1);
        }
        if (s == 0) {
            setZ(1);
        }
        nextInstructionPointer();
    }
//ANA E

    public void _A3() {
        int r1, r2;
        r1 = getA().intValue();
        r2 = getE().intValue();
        int s = r1 & r2;
        String temp = Integer.toHexString(s);
        setA(Data.from(temp));
        if (s > 127) {
            setS(1);
        }
        if (s == 0) {
            setZ(1);
        }
        nextInstructionPointer();
    }
//ANA H

    public void _A4() {
        int r1, r2;
        r1 = getA().intValue();
        r2 = getH().intValue();
        int s = r1 & r2;
        String temp = Integer.toHexString(s);
        setA(Data.from(temp));
        if (s > 127) {
            setS(1);
        }
        if (s == 0) {
            setZ(1);
        }
        nextInstructionPointer();
    }
//ANA L

    public void _A5() {
        int r1, r2;
        r1 = getA().intValue();
        r2 = getL().intValue();
        int s = r1 & r2;
        String temp = Integer.toHexString(s);
        setA(Data.from(temp));
        if (s > 127) {
            setS(1);
        }
        if (s == 0) {
            setZ(1);
        }
        nextInstructionPointer();
    }
//ANA M

    public void _A6() {
        int r1, r2;
        r1 = getA().intValue();
        r2 = getM().intValue();
        int s = r1 & r2;
        String temp = Integer.toHexString(s);
        setA(Data.from(temp));
        if (s > 127) {
            setS(1);
        }
        if (s == 0) {
            setZ(1);
        }
        nextInstructionPointer();
    }
//ANI data

    public void _E6() {
        int r1, r2;
        r1 = getA().intValue();
        r2 = getM().intValue();
        int s = r1 & r2;
        String temp = Integer.toHexString(s);
        setA(Data.from(temp));
        if (s > 127) {
            setS(1);
        }
        if (s == 0) {
            setZ(1);
        }
        nextInstructionPointer();
    }
//ORA A

    public void _B7() {
        int r1, r2;
        r1 = getA().intValue();
        nextInstructionPointer();
        r2 = getDataAtIP().intValue();
        int s = r1 | r2;
        String temp = Integer.toHexString(s);
        setA(Data.from(temp));
        if (s > 127) {
            setS(1);
        }
        if (s == 0) {
            setZ(1);
        }
        nextInstructionPointer();
    }
//ORA B

    public void _B0() {
        int r1, r2;
        r1 = getA().intValue();
        r2 = getB().intValue();
        int s = r1 | r2;
        String temp = Integer.toHexString(s);
        setA(Data.from(temp));
        if (s > 127) {
            setS(1);
        }
        if (s == 0) {
            setZ(1);
        }
        nextInstructionPointer();
    }
//ORA C

    public void _B1() {
        int r1, r2;
        r1 = getA().intValue();
        r2 = getC().intValue();
        int s = r1 | r2;
        String temp = Integer.toHexString(s);
        setA(Data.from(temp));
        if (s > 127) {
            setS(1);
        }
        if (s == 0) {
            setZ(1);
        }
        nextInstructionPointer();
    }
//ORA D

    public void _B2() {
        int r1, r2;
        r1 = getA().intValue();
        r2 = getD().intValue();
        int s = r1 | r2;
        String temp = Integer.toHexString(s);
        setA(Data.from(temp));
        if (s > 127) {
            setS(1);
        }
        if (s == 0) {
            setZ(1);
        }
        nextInstructionPointer();
    }
//ORA E

    public void _B3() {
        int r1, r2;
        r1 = getA().intValue();
        r2 = getE().intValue();
        int s = r1 | r2;
        String temp = Integer.toHexString(s);
        setA(Data.from(temp));
        if (s > 127) {
            setS(1);
        }
        if (s == 0) {
            setZ(1);
        }
        nextInstructionPointer();
    }
//ORA H

    public void _B4() {
        int r1, r2;
        r1 = getA().intValue();
        r2 = getH().intValue();
        int s = r1 | r2;
        String temp = Integer.toHexString(s);
        setA(Data.from(temp));
        if (s > 127) {
            setS(1);
        }
        if (s == 0) {
            setZ(1);
        }
        nextInstructionPointer();
    }
//ORA L

    public void _B5() {
        int r1, r2;
        r1 = getA().intValue();
        r2 = getL().intValue();
        int s = r1 | r2;
        String temp = Integer.toHexString(s);
        setA(Data.from(temp));
        if (s > 127) {
            setS(1);
        }
        if (s == 0) {
            setZ(1);
        }
        nextInstructionPointer();
    }
//ORA M

    public void _B6() {
        int r1, r2;
        r1 = getA().intValue();
        r2 = getM().intValue();
        int s = r1 | r2;
        String temp = Integer.toHexString(s);
        setA(Data.from(temp));
        if (s > 127) {
            setS(1);
        }
        if (s == 0) {
            setZ(1);
        }
        nextInstructionPointer();
    }
//ORI

    public void _F6() {
        int r1, r2;
        r1 = getA().intValue();
        nextInstructionPointer();
        r2 = getDataAtIP().intValue();
        int s = r1 | r2;
        String temp = Integer.toHexString(s);
        setA(Data.from(temp));
        if (s > 127) {
            setS(1);
        }
        if (s == 0) {
            setZ(1);
        }
        nextInstructionPointer();
    }

//XRA A
    public void _AF() {
        int r1, r2;
        r1 = getA().intValue();
        r2 = getA().intValue();
        int s = r1 ^ r2;
        String temp = Integer.toHexString(s);
        setA(Data.from(temp));
        if (s > 127) {
            setS(1);
        }
        if (s == 0) {
            setZ(1);
        }
        nextInstructionPointer();
    }
//XRA B

    public void _A8() {
        int r1, r2;
        r1 = getA().intValue();
        r2 = getB().intValue();
        int s = r1 ^ r2;
        String temp = Integer.toHexString(s);
        setA(Data.from(temp));
        if (s > 127) {
            setS(1);
        }
        if (s == 0) {
            setZ(1);
        }
        nextInstructionPointer();
    }
//XRA C

    public void _A9() {
        int r1, r2;
        r1 = getA().intValue();
        r2 = getC().intValue();
        int s = r1 ^ r2;
        String temp = Integer.toHexString(s);
        setA(Data.from(temp));
        if (s > 127) {
            setS(1);
        }
        if (s == 0) {
            setZ(1);
        }
        nextInstructionPointer();
    }
//XRA D

    public void _AA() {
        int r1, r2;
        r1 = getA().intValue();
        r2 = getD().intValue();
        int s = r1 ^ r2;
        String temp = Integer.toHexString(s);
        setA(Data.from(temp));
        if (s > 127) {
            setS(1);
        }
        if (s == 0) {
            setZ(1);
        }
        nextInstructionPointer();
    }
//XRA E

    public void _AB() {
        int r1, r2;
        r1 = getA().intValue();
        r2 = getE().intValue();
        int s = r1 ^ r2;
        String temp = Integer.toHexString(s);
        setA(Data.from(temp));
        if (s > 127) {
            setS(1);
        }
        if (s == 0) {
            setZ(1);
        }
        nextInstructionPointer();
    }
//XRA H

    public void _AC() {
        int r1, r2;
        r1 = getA().intValue();
        r2 = getH().intValue();
        int s = r1 ^ r2;
        String temp = Integer.toHexString(s);
        setA(Data.from(temp));
        if (s > 127) {
            setS(1);
        }
        if (s == 0) {
            setZ(1);
        }
        nextInstructionPointer();
    }
//XRA L

    public void _AD() {
        int r1, r2;
        r1 = getA().intValue();
        r2 = getL().intValue();
        int s = r1 ^ r2;
        String temp = Integer.toHexString(s);
        setA(Data.from(temp));
        if (s > 127) {
            setS(1);
        }
        if (s == 0) {
            setZ(1);
        }
        nextInstructionPointer();
    }
//XRA M

    public void _AE() {
        int r1, r2;
        r1 = getA().intValue();
        r2 = getM().intValue();
        int s = r1 ^ r2;
        String temp = Integer.toHexString(s);
        setA(Data.from(temp));
        if (s > 127) {
            setS(1);
        }
        if (s == 0) {
            setZ(1);
        }
        nextInstructionPointer();
    }
//XRI data

    public void _EE() {
        int r1, r2;
        r1 = getA().intValue();
        nextInstructionPointer();
        r2 = getDataAtIP().intValue();
        int s = r1 ^ r2;
        String temp = Integer.toHexString(s);
        setA(Data.from(temp));
        if (s > 127) {
            setS(1);
        }
        if (s == 0) {
            setZ(1);
        }
        nextInstructionPointer();
    }
//STC 

    public void _37() {
        setCy(1);
        nextInstructionPointer();
    }
//CALL LABEL

    public void _CD() {
        nextInstructionPointer();
        String s1 = getDataAtIP().hexValue();
        nextInstructionPointer();
        String s2 = getDataAtIP().hexValue();
        nextInstructionPointer();
        String s3 = getIP().hexValue();
        IData t1 = getIP().getMSB();
        IData t2 = getIP().getLSB();
        setData(getSP(), t2);
        decrementSP();
        setData(getSP(), t1);
        decrementSP();
        setIP(Address.from(s2 + s1));
    }
//RET

    public void _C9() {
        incrementSP();
        String s1 = getData(getSP()).hexValue();
        incrementSP();
        String s2 = getData(getSP()).hexValue();
        setIP(Address.from(s1 + s2));
    }
//JMP

    public void _C3() {
        nextInstructionPointer();
        String s1 = getDataAtIP().hexValue();
        nextInstructionPointer();
        String s2 = getDataAtIP().hexValue();
        setIP(Address.from(s2 + s1));
    }

    /**
     * *************** JUMP PSW    ***************
     */

//JC Cy=1 _DA()
    public void _DA() {
        if (getCy() == 1) {
            _C3();
        } else {
            nextInstructionPointer();
            nextInstructionPointer();
            nextInstructionPointer();
        }
    }
//JNC Cy=0 _D2()

    public void _D2() {
        if (getCy() == 0) {
            _C3();
        } else {
            nextInstructionPointer();
            nextInstructionPointer();
            nextInstructionPointer();
        }
    }
//JP S=0 _F2()

    public void _F2() {
        if (getS() == 0) {
            _C3();
        } else {
            nextInstructionPointer();
            nextInstructionPointer();
            nextInstructionPointer();
        }
    }
//JM S=1 _FA()

    public void _FA() {
        if (getS() == 1) {
            _C3();
        } else {
            nextInstructionPointer();
            nextInstructionPointer();
            nextInstructionPointer();
        }
    }
//JZ Z=1 _CA()

    public void _CA() {
        if (getZ() == 1) {
            _C3();
        } else {
            nextInstructionPointer();
            nextInstructionPointer();
            nextInstructionPointer();
        }
    }
//JNZ Z=0 _C2()

    public void _C2() {
        if (getZ() == 0) {
            _C3();
        } else {
            nextInstructionPointer();
            nextInstructionPointer();
            nextInstructionPointer();
        }
    }
//JPE P=1 _EA()

    public void _EA() {
        if (getP() == 1) {
            _C3();
        } else {
            nextInstructionPointer();
            nextInstructionPointer();
            nextInstructionPointer();
        }
    }
//JPO P=0 _E2()

    public void _E2() {
        if (getP() == 0) {
            _C3();
        } else {
            nextInstructionPointer();
            nextInstructionPointer();
            nextInstructionPointer();
        }
    }

    /**
     * *************** RETURN PSW ****************
     */
//RC
    public void _D8() {
        if (getCy() == 1) {
            _C9();
        }
    }
//RNC

    public void _D0() {
        if (getCy() == 0) {
            _C9();
        }
    }
//RP

    public void _F0() {
        if (getS() == 0) {
            _C9();
        }
    }
//RM

    public void _F8() {
        if (getS() == 1) {
            _C9();
        }
    }
//RZ

    public void _C8() {
        if (getZ() == 1) {
            _C9();
        }
    }
//RNZ

    public void _C0() {
        if (getZ() == 0) {
            _C9();
        }
    }
//RPE

    public void _E8() {
        if (getP() == 1) {
            _C9();
        }
    }
//RPO

    public void _E0() {
        if (getP() == 0) {
            _C9();
        }
    }

    /**
     * ****************** CALL PSW  ********************
     */
//CC Cy=1 _DC()
    public void _DC() {
        if (getCy() == 1) {
            _CD();
        }
    }
//CNC Cy=0 _D4()

    public void _D4() {
        if (getCy() == 0) {
            _CD();
        }
    }
//CP S=0 _F4()

    public void _F4() {
        if (getS() == 0) {
            _CD();
        }
    }
//CM S=1 _FC()

    public void _FC() {
        if (getS() == 1) {
            _CD();
        }
    }
//CZ Z=1 _CC()

    public void _CC() {
        if (getZ() == 1) {
            _CD();
        }
    }
//CNZ Z=0 _C4()

    public void _C4() {
        if (getZ() == 0) {
            _CD();
        }
    }
//CPE P=1 _EC()

    public void _EC() {
        if (getP() == 1) {
            _CD();
        }
    }
//CPO P=0 _E4()

    public void _E4() {
        if (getP() == 1) {
            _CD();
        }
    }

    /**
     * **********************************
     */

//NOP
    public void _00() {
        nextInstructionPointer();
    }
//LDA

    public void _3A() {
        nextInstructionPointer();
        IData s1 = getDataAtIP();
        nextInstructionPointer();
        IData s2 = getDataAtIP();
        nextInstructionPointer();

        setA(getData(Address.from(s2, s1)));
    }

//LDAX B
    public void _0A() {
        IAddress addr = Address.from(getB(), getC());
        IData data = memory.getData(addr.intValue());
        setA(data);
        nextInstructionPointer();
    }
//LDAX D

    public void _1A() {
        IAddress addr = Address.from(getD(), getE());
        IData data = memory.getData(addr.intValue());
        setA(data);
        nextInstructionPointer();
    }
//LXI B _01()

    public void _01() {
        nextInstructionPointer();
        setC(getDataAtIP());
        nextInstructionPointer();
        setB(getDataAtIP());
        nextInstructionPointer();
    }
//LXI D _11()

    public void _11() {
        nextInstructionPointer();
        setE(getDataAtIP());
        nextInstructionPointer();
        setD(getDataAtIP());
        nextInstructionPointer();
    }
//LXI H _21()

    public void _21() {
        nextInstructionPointer();
        setL(getDataAtIP());
        nextInstructionPointer();
        setH(getDataAtIP());
        nextInstructionPointer();
    }
//LXI SP _31()

    public void _31() {
        nextInstructionPointer();
        String s1 = getDataAtIP().hexValue();
        nextInstructionPointer();
        String s2 = getDataAtIP().hexValue();
        setSP(Address.from(s2 + s1));
        nextInstructionPointer();
    }
//LHLD

    public void _2A() {
        nextInstructionPointer();
        IData d1 = getDataAtIP();
        nextInstructionPointer();
        IData d2 = getDataAtIP();
        IAddress x = Address.from(d2, d1);
        setL(getData(x));
        IAddress nextAddr = Address.from(x.intValue() + 1);
        setH(getData(nextAddr));
        nextInstructionPointer();
    }
//STA 

    public void _32() {
        nextInstructionPointer();
        IData s1 = getDataAtIP();
        nextInstructionPointer();
        IData s2 = getDataAtIP();
        setData(Address.from(s2, s1), getA());
        nextInstructionPointer();
    }
//STAX B

    public void _02() {
        IData s1 = getB();
        IData s2 = getC();
        setData(Address.from(s1, s2), getA());
        nextInstructionPointer();
    }
//STAX D

    public void _12() {
        IData s1 = getD();
        IData s2 = getE();
        setData(Address.from(s1, s2), getA());
        nextInstructionPointer();
    }
//SHLD

    public void _22() {
        nextInstructionPointer();
        IData s1 = getDataAtIP();
        nextInstructionPointer();
        IData s2 = getDataAtIP();
        IAddress x = Address.from(s2, s1);
        setData(x, getL());
        IAddress nextAddr = Address.from(x.intValue() + 1);
        setData(nextAddr, getH());
        nextInstructionPointer();
    }

    void decrementSP() {
        int x = getSP().intValue();
        x--;
        setSP(Address.from(x));
    }

    void incrementSP() {
        int x = getSP().intValue();
        x++;
        setSP(Address.from(x));
    }
//RST 0

    public void _C7() {
        setIP(IAddress.ZERO);
    }
//RST 1

    public void _CF() {
        setIP(Address.from("0008"));
    }
//RST 2

    public void _D7() {
        setIP(Address.from("0010"));
    }
//RST 3

    public void _DF() {
        setIP(Address.from("0018"));
    }
//RST 4

    public void _E7() {
        setIP(Address.from("0020"));
    }
//RST 5

    public void _EF() {
        setIP(Address.from("0028"));
    }
//RST 6

    public void _F7() {
        setIP(Address.from("0030"));
    }
//RST 7

    public void _FF() {
        setIP(Address.from("0030"));
    }
//RLC 

    public void _07() {
        int r1 = getA().intValue();
        int x = r1 << 1;
        int y = x / 255;
        if (y == 1) {
            x = x % 256;
            setCy(1);
            x = x | 1;
        }
        if (y == 0) {
            setCy(0);
        }
        setA(Data.from(x));
    }
//RRC

    public void _0F() {
        int r1 = getA().intValue();
        int z = r1 & 1;
        int x = r1 >> 1;
        if (z == 1) {
            setCy(1);
            x = x | 128;
        }
        if (z == 0) {
            setCy(0);
        }
        setA(Data.from(x));
    }
//RAL

    public void _17() {
        int r1 = getA().intValue();
        int x = r1 << 1;
        int y = x / 256;
        if (getCy() == 1) {
            x = x | 1;
        }
        x = x & 255;
        setCy(y);
        setA(Data.from(x));
        nextInstructionPointer();
    }
//SPHL

    public void _F9() {
        setSP(Address.from(getH(), getL()));
        nextInstructionPointer();
    }
//XTHL

    public void _E3() {
        setData(getSP(), getL());
        decrementSP();
        setData(getSP(), getH());
        decrementSP();
        nextInstructionPointer();
    }

//PUSH B
    public void _C5() {
        setData(getSP(), getB());
        decrementSP();
        setData(getSP(), getC());
        decrementSP();
        nextInstructionPointer();
    }
//PUSH D

    public void _D5() {
        setData(getSP(), getD());
        decrementSP();
        setData(getSP(), getE());
        decrementSP();
        nextInstructionPointer();
    }
//PUSH H

    public void _E5() {
        setData(getSP(), getH());
        decrementSP();
        setData(getSP(), getL());
        decrementSP();
        nextInstructionPointer();
    }
//POP B

    public void _C1() {
        incrementSP();
        setC(getData(getSP()));
        incrementSP();
        setB(getData(getSP()));
        nextInstructionPointer();
    }
//POP D

    public void _D1() {
        incrementSP();
        setE(getData(getSP()));
        incrementSP();
        setD(getData(getSP()));
        nextInstructionPointer();
    }
//POP H

    public void _E1() {
        incrementSP();
        setL(getData(getSP()));
        incrementSP();
        setH(getData(getSP()));
        nextInstructionPointer();
    }
//POP PSW

    public void _F1() {
        incrementSP();
        int y = getData(getSP()).intValue();
        if ((y & 128) == 128) {
            setS(1);
        }
        if ((y & 64) == 64) {
            setZ(1);
        }
        if ((y & 16) == 16) {
            setAc(1);
        }
        if ((y & 4) == 4) {
            setP(1);
        }
        if ((y & 1) == 1) {
            setCy(1);
        }

        nextInstructionPointer();
        //JOptionPane.showMessageDialog(this, "[WIP - Work In Progress] POP PSW");
    }
//PUSH PSW

    public void _F5() {
        int x = 0;
        if (getS() == 1) {
            x = x | 128;
        }
        if (getZ() == 1) {
            x = x | 64;
        }
        if (getAc() == 1) {
            x = x | 16;
        }
        if (getP() == 1) {
            x = x | 4;
        }
        if (getCy() == 1) {
            x = x | 1;
        }
        setData(getSP(), Data.from(x));
        decrementSP();
        setS(0);
        setZ(0);
        setAc(0);
        setP(0);
        setCy(0);
        nextInstructionPointer();
        //JOptionPane.showMessageDialog(this, "[WIP - Work In Progress] PUSH PSW");
    }
//INX B

    public void _03() {
        IData s1 = getB();
        IData s2 = getC();
        int x = Address.from(s1, s2).intValue();
        x++;
        IAddress s3 = Address.from(x);
        setB(s3.getMSB());
        setC(s3.getLSB());
        nextInstructionPointer();
    }
//INX D

    public void _13() {

        IData s1 = getD();
        IData s2 = getE();
        int x = Address.from(s1, s2).intValue();
        x++;
        IAddress s3 = Address.from(x);
        setD(s3.getMSB());
        setE(s3.getLSB());
        nextInstructionPointer();
    }
//INX H

    public void _23() {
        IData s1 = getH();
        IData s2 = getL();
        int x = Address.from(s1, s2).intValue();
        x++;
        IAddress s3 = Address.from(x);
        setH(s3.getMSB());
        setL(s3.getLSB());
        nextInstructionPointer();
    }
//INX SP

    public void _33() {
        incrementSP();
        nextInstructionPointer();
    }
//DCX B

    public void _0B() {
        IData s1 = getB();
        IData s2 = getC();
        int x = Address.from(s1, s2).intValue();
        x--;
        if (x < 0) {
            x = x * (-1);
        }
        IAddress s3 = Address.from(x);
        setB(s3.getMSB());
        setC(s3.getLSB());
        nextInstructionPointer();
    }
//DCX D

    public void _1B() {
        IData s1 = getD();
        IData s2 = getE();
        int x = Address.from(s1, s2).intValue();
        x--;
        if (x < 0) {
            x = x * (-1);
        }
        IAddress s3 = Address.from(x);
        setD(s3.getMSB());
        setE(s3.getLSB());
        nextInstructionPointer();
    }
//DCX H

    public void _2B() {
        IData s1 = getH();
        IData s2 = getL();
        int x = Address.from(s1, s2).intValue();
        x--;
        if (x < 0) {
            x = x * (-1);
        }
        IAddress s3 = Address.from(x);
        setH(s3.getMSB());
        setL(s3.getLSB());
        nextInstructionPointer();
    }
//DCX SP

    public void _3B() {
        decrementSP();
        nextInstructionPointer();
    }
//CMA

    public void _2F() {
        int r1;
        r1 = getA().intValue();
        r1 = ~r1;
        String temp = Integer.toHexString(r1);

        setA( Data.from(temp.substring((temp.length() - 2))) );
        nextInstructionPointer();
    }
//CMC 

    public void _3F() {
        if (getCy() == 1) {
            setCy(0);
        } else {
            setCy(1);
        }
        nextInstructionPointer();
    }
//DAA

    public void _27() {
        int s = getA().intValue();
        String temp = Integer.toString(s);
        setA(Data.from(temp.substring((temp.length() - 2))));
        if (s == 0) {
            setZ(1);
        }
        if (s > 99) {
            setCy(1);
        }
        if (s > 999) {
            setAc(1);
        }
        if (s < 0) {
            setS(1);
        }

        PARITY();
        nextInstructionPointer();
    }
//PCHL

    public void _E9() {
        setIP(getHlAddress());
        nextInstructionPointer();
    }
//CPI data

    public void _FE() {
        int r1, r2;
        r1 = getA().intValue();
        nextInstructionPointer();
        r2 = getDataAtIP().intValue();
        if (r1 < r2) {
            setCy(1);
        }
        if (r1 == r2) {
            setZ(1);
        }
        if (r1 > r2) {
            setZ(0);
            setCy(0);
        }
        nextInstructionPointer();
    }
//RAR _1F

    public void _1F() {
        int x, y;
        x = getA().intValue();
        y = x % 2;
        x = x >> 1;
        if (getCy() == 1) {
            x = x | 128;
        }
        setCy(y);
        setA(Data.from(x));
        nextInstructionPointer();
    }

    public void PARITY() {
        int x, counter = 0;
        x = getA().intValue();
        String s = Integer.toBinaryString(x);
        char[] c = s.toCharArray();
        for (int i = 0; i < s.length(); i++) {
            if (c[i] == '1') {
                counter++;
            }
        }
        if (counter % 2 == 0) {
            setP(1);
        } else {
            setP(0);
        }

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
