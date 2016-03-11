
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Aman
 */
public class zero0 {

    public Matcher m ;
    public Pattern px[] = new Pattern[246];
    String data ="[0-9A-F]{2}(H)?";
    String label = "[A-Za-z]{3}[A-Za-z]*";
    String addr = "[0-9A-F]{4}(H)?";
    String space ="( )*";
    String space1 = "( )+";
    
    void initializePatt()
    {
        px[0] = Pattern.compile("MOV"+space1+"B"+space+","+space+"C");
        px[1] = Pattern.compile("MOV"+space1+"B"+space+","+space+"D");
        px[2] = Pattern.compile("MOV"+space1+"B"+space+","+space+"E");
        px[3] = Pattern.compile("MOV"+space1+"B"+space+","+space+"H");
        px[4] = Pattern.compile("MOV"+space1+"B"+space+","+space+"L");
        px[5] = Pattern.compile("MOV"+space1+"B"+space+","+space+"B");
        px[6] = Pattern.compile("MOV"+space1+"C"+space+","+space+"B");
        px[7] = Pattern.compile("MOV"+space1+"C"+space+","+space+"C");
        px[8] = Pattern.compile("MOV"+space1+"C"+space+","+space+"D");
        px[9] = Pattern.compile("MOV"+space1+"C"+space+","+space+"E");
        px[10] = Pattern.compile("MOV"+space1+"C"+space+","+space+"H");
        px[11] = Pattern.compile("MOV"+space1+"C"+space+","+space+"L");
        px[12] = Pattern.compile("MOV"+space1+"D"+space+","+space+"B");
        px[13] = Pattern.compile("MOV"+space1+"D"+space+","+space+"C");
        px[14] = Pattern.compile("MOV"+space1+"D"+space+","+space+"D");
        px[15] = Pattern.compile("MOV"+space1+"D"+space+","+space+"E");
        px[16] = Pattern.compile("MOV"+space1+"D"+space+","+space+"H");
        px[17] = Pattern.compile("MOV"+space1+"D"+space+","+space+"L");
        px[18] = Pattern.compile("MOV"+space1+"E"+space+","+space+"B");
        px[19] = Pattern.compile("MOV"+space1+"E"+space+","+space+"C");
        px[20] = Pattern.compile("MOV"+space1+"E"+space+","+space+"D");
        px[21] = Pattern.compile("MOV"+space1+"E"+space+","+space+"E");
        px[22] = Pattern.compile("MOV"+space1+"E"+space+","+space+"H");
        px[23] = Pattern.compile("MOV"+space1+"E"+space+","+space+"L");
        px[24] = Pattern.compile("MOV"+space1+"H"+space+","+space+"B");
        px[25] = Pattern.compile("MOV"+space1+"H"+space+","+space+"C");
        px[26] = Pattern.compile("MOV"+space1+"H"+space+","+space+"D");
        px[27] = Pattern.compile("MOV"+space1+"H"+space+","+space+"E");
        px[28] = Pattern.compile("MOV"+space1+"H"+space+","+space+"H");
        px[29] = Pattern.compile("MOV"+space1+"H"+space+","+space+"L");
        px[30] = Pattern.compile("MOV"+space1+"L"+space+","+space+"B");
        px[31] = Pattern.compile("MOV"+space1+"L"+space+","+space+"C");
        px[32] = Pattern.compile("MOV"+space1+"L"+space+","+space+"D");
        px[33] = Pattern.compile("MOV"+space1+"L"+space+","+space+"E");
        px[34] = Pattern.compile("MOV"+space1+"L"+space+","+space+"H");
        px[35] = Pattern.compile("MOV"+space1+"L"+space+","+space+"L");
        px[36] = Pattern.compile("MOV"+space1+"A"+space+","+space+"A");
        px[37] = Pattern.compile("MOV"+space1+"A"+space+","+space+"B");
        px[38] = Pattern.compile("MOV"+space1+"A"+space+","+space+"C");
        px[39] = Pattern.compile("MOV"+space1+"A"+space+","+space+"D");
        px[40] = Pattern.compile("MOV"+space1+"A"+space+","+space+"E");
        px[41] = Pattern.compile("MOV"+space1+"A"+space+","+space+"H");
        px[42] = Pattern.compile("MOV"+space1+"A"+space+","+space+"L");
        px[43] = Pattern.compile("MOV"+space1+"B"+space+","+space+"A");
        px[44] = Pattern.compile("MOV"+space1+"C"+space+","+space+"A");
        px[45] = Pattern.compile("MOV"+space1+"D"+space+","+space+"A");
        px[46] = Pattern.compile("MOV"+space1+"E"+space+","+space+"A");
        px[47] = Pattern.compile("MOV"+space1+"H"+space+","+space+"A");
        px[48] = Pattern.compile("MOV"+space1+"L"+space+","+space+"A");
        px[49] = Pattern.compile("MOV"+space1+"A"+space+","+space+"M");
        px[50] = Pattern.compile("MOV"+space1+"A"+space+","+space+"M");
        px[50] = Pattern.compile("MOV"+space1+"B"+space+","+space+"M");
        px[51] = Pattern.compile("MOV"+space1+"C"+space+","+space+"M");
        px[52] = Pattern.compile("MOV"+space1+"D"+space+","+space+"M");
        px[53] = Pattern.compile("MOV"+space1+"E"+space+","+space+"M");
        px[54] = Pattern.compile("MOV"+space1+"H"+space+","+space+"M");
        px[55] = Pattern.compile("MOV"+space1+"L"+space+","+space+"M");
        px[56] = Pattern.compile("MOV"+space1+"M"+space+","+space+"A");
        px[57] = Pattern.compile("MOV"+space1+"M"+space+","+space+"B");
        px[58] = Pattern.compile("MOV"+space1+"M"+space+","+space+"C");
        px[59] = Pattern.compile("MOV"+space1+"M"+space+","+space+"D");
        px[60] = Pattern.compile("MOV"+space1+"M"+space+","+space+"E");
        px[61] = Pattern.compile("MOV"+space1+"M"+space+","+space+"H");
        px[62] = Pattern.compile("MOV"+space1+"M"+space+","+space+"L");
        px[63] = Pattern.compile("MVI"+space1+"A"+space+","+space+data);
        px[64] = Pattern.compile("MVI"+space1+"B"+space+","+space+data);
        px[65] = Pattern.compile("MVI"+space1+"C"+space+","+space+data);
        px[66] = Pattern.compile("MVI"+space1+"D"+space+","+space+data);
        px[67] = Pattern.compile("MVI"+space1+"E"+space+","+space+data);
        px[68] = Pattern.compile("MVI"+space1+"H"+space+","+space+data);
        px[69] = Pattern.compile("MVI"+space1+"L"+space+","+space+data);
        px[70] = Pattern.compile("MVI"+space1+"M"+space+","+space+data);
        px[71] = Pattern.compile("ACI"+space1+data);
        px[72] = Pattern.compile("ADC"+space1+"A");
        px[73] = Pattern.compile("ADC"+space1+"B");
        px[74] = Pattern.compile("ADC"+space1+"C");
        px[75] = Pattern.compile("ADC"+space1+"D");
        px[76] = Pattern.compile("ADC"+space1+"E");
        px[77] = Pattern.compile("ADC"+space1+"H");
        px[78] = Pattern.compile("ADC"+space1+"L");
        px[79] = Pattern.compile("ADC"+space1+"M");
        px[80] = Pattern.compile("ADD"+space1+"A");
        px[81] = Pattern.compile("ADD"+space1+"B");
        px[82] = Pattern.compile("ADD"+space1+"C");
        px[83] = Pattern.compile("ADD"+space1+"D");
        px[84] = Pattern.compile("ADD"+space1+"E");
        px[85] = Pattern.compile("ADD"+space1+"H");
        px[86] = Pattern.compile("ADD"+space1+"L");
        px[87] = Pattern.compile("ADD"+space1+"M");
        px[88] = Pattern.compile("ADI"+space1+data);
        px[89] = Pattern.compile("ANA"+space1+"A");
        px[90] = Pattern.compile("ANA"+space1+"B");
        px[91] = Pattern.compile("ANA"+space1+"C");
        px[92] = Pattern.compile("ANA"+space1+"D");
        px[93] = Pattern.compile("ANA"+space1+"E");
        px[94] = Pattern.compile("ANA"+space1+"H");
        px[95] = Pattern.compile("ANA"+space1+"L");
        px[96] = Pattern.compile("ANA"+space1+"M");
        px[97] = Pattern.compile("ANI"+space1+data);
        px[98] = Pattern.compile("CALL"+space1+label);
        px[99] = Pattern.compile("CC"+space1+label);
        px[100] = Pattern.compile("CM"+space1+label);
        px[101] = Pattern.compile("CMA");
        px[102] = Pattern.compile("CMC");
        px[103] = Pattern.compile("CMP"+space1+"A");
        px[104] = Pattern.compile("CMP"+space1+"B");
        px[105] = Pattern.compile("CMP"+space1+"C");
        px[106] = Pattern.compile("CMP"+space1+"D");
        px[107] = Pattern.compile("CMP"+space1+"E");
        px[108] = Pattern.compile("CMP"+space1+"H");
        px[109] = Pattern.compile("CMP"+space1+"L");
        px[110] = Pattern.compile("CMP"+space1+"M");
        px[111] = Pattern.compile("CNC"+space1+label);
        px[112] = Pattern.compile("CNZ"+space1+label);
        px[113] = Pattern.compile("CPE"+space1+label);
        px[114] = Pattern.compile("CPO"+space1+label);
        px[115] = Pattern.compile("CPI"+space1+data);
        px[116] = Pattern.compile("CP"+space1+label);
        px[117] = Pattern.compile("CZ"+space1+label);
        px[118] = Pattern.compile("DAA");
        px[119] = Pattern.compile("DAD"+space1+"B");
        px[120] = Pattern.compile("DAD"+space1+"D");
        px[121] = Pattern.compile("DAD"+space1+"H");
        px[122] = Pattern.compile("DAD"+space1+"SP");
        px[123] = Pattern.compile("DCR"+space1+"A");
        px[124] = Pattern.compile("DCR"+space1+"B");
        px[125] = Pattern.compile("DCR"+space1+"C");
        px[126] = Pattern.compile("DCR"+space1+"D");
        px[127] = Pattern.compile("DCR"+space1+"E");
        px[128] = Pattern.compile("DCR"+space1+"H");
        px[129] = Pattern.compile("DCR"+space1+"L");
        px[130] = Pattern.compile("DCR"+space1+"M");
        px[131] = Pattern.compile("DCX"+space1+"B");
        px[132] = Pattern.compile("DCX"+space1+"D");
        px[133] = Pattern.compile("DCX"+space1+"H");
        px[134] = Pattern.compile("DCX"+space1+"SP");
        px[135] = Pattern.compile("DI");
        px[136] = Pattern.compile("EI");
        px[137] = Pattern.compile("HLT");
        px[138] = Pattern.compile("IN"+space1+data);
        px[139] = Pattern.compile("INR"+space1+"A");
        px[140] = Pattern.compile("INR"+space1+"B");
        px[141] = Pattern.compile("INR"+space1+"C");
        px[142] = Pattern.compile("INR"+space1+"D");
        px[143] = Pattern.compile("INR"+space1+"E");
        px[144] = Pattern.compile("INR"+space1+"H");
        px[145] = Pattern.compile("INR"+space1+"L");
        px[146] = Pattern.compile("INR"+space1+"M");
        px[147] = Pattern.compile("INX"+space1+"B");
        px[148] = Pattern.compile("INX"+space1+"D");
        px[149] = Pattern.compile("INX"+space1+"H");
        px[150] = Pattern.compile("INX"+space1+"SP");
        px[151] = Pattern.compile("JC"+space1+label);
        px[152] = Pattern.compile("JMP"+space1+label);
        px[153] = Pattern.compile("JM"+space1+label);
        px[154] = Pattern.compile("JNC"+space1+label);
        px[155] = Pattern.compile("JNZ"+space1+label);
        px[156] = Pattern.compile("JPO"+space1+label);
        px[157] = Pattern.compile("JPE"+space1+label);
        px[158] = Pattern.compile("JP"+space1+label);
        px[159] = Pattern.compile("JZ"+space1+label);
        px[160] = Pattern.compile("LDA"+space1+addr);
        px[161] = Pattern.compile("LDAX"+space1+"B");
        px[162] = Pattern.compile("LDAX"+space1+"D");
        px[163] = Pattern.compile("LHLD"+space1+addr);
        px[164] = Pattern.compile("LXI"+space1+"B");
        px[165] = Pattern.compile("LXI"+space1+"D");
        px[166] = Pattern.compile("LXI"+space1+"H");
        px[167] = Pattern.compile("LXI"+space1+"SP");
        px[168] = Pattern.compile("NOP");
        px[169] = Pattern.compile("ORA"+space1+"A");
        px[170] = Pattern.compile("ORA"+space1+"B");
        px[171] = Pattern.compile("ORA"+space1+"C");
        px[172] = Pattern.compile("ORA"+space1+"D");
        px[173] = Pattern.compile("ORA"+space1+"E");
        px[174] = Pattern.compile("ORA"+space1+"H");
        px[175] = Pattern.compile("ORA"+space1+"L");
        px[176] = Pattern.compile("ORA"+space1+"M");
        px[177] = Pattern.compile("ORI"+space1+data);
        px[178] = Pattern.compile("OUT"+space1+data);
        px[179] = Pattern.compile("PCHL");
        px[180] = Pattern.compile("POP"+space1+"B");
        px[181] = Pattern.compile("POP"+space1+"D");
        px[182] = Pattern.compile("POP"+space1+"H");
        px[183] = Pattern.compile("POP"+space1+"PSW");
        px[184] = Pattern.compile("PUSH"+space1+"B");
        px[185] = Pattern.compile("PUSH"+space1+"D");
        px[186] = Pattern.compile("PUSH"+space1+"H");
        px[187] = Pattern.compile("PUSH"+space1+"PSW");
        px[188] = Pattern.compile("RAL");
        px[189] = Pattern.compile("RAR");
        px[190] = Pattern.compile("RC");
        px[191] = Pattern.compile("RET");
        px[192] = Pattern.compile("RIM");
        px[193] = Pattern.compile("RLC");
        px[194] = Pattern.compile("RM");
        px[195] = Pattern.compile("RNC");
        px[196] = Pattern.compile("RNZ");
        px[197] = Pattern.compile("RP");
        px[198] = Pattern.compile("RPE");
        px[199] = Pattern.compile("RPO");
        px[200] = Pattern.compile("RRC");
        px[201] = Pattern.compile("RST"+space1+"0");
        px[202] = Pattern.compile("RST"+space1+"1");
        px[203] = Pattern.compile("RST"+space1+"2");
        px[204] = Pattern.compile("RST"+space1+"3");
        px[205] = Pattern.compile("RST"+space1+"4");
        px[206] = Pattern.compile("RST"+space1+"5");
        px[207] = Pattern.compile("RST"+space1+"6");
        px[208] = Pattern.compile("RST"+space1+"7");
        px[209] = Pattern.compile("RZ");
        px[210] = Pattern.compile("SBB"+space1+"A");
        px[211] = Pattern.compile("SBB"+space1+"B");
        px[212] = Pattern.compile("SBB"+space1+"C");
        px[213] = Pattern.compile("SBB"+space1+"D");
        px[214] = Pattern.compile("SBB"+space1+"E");
        px[215] = Pattern.compile("SBB"+space1+"H");
        px[216] = Pattern.compile("SBB"+space1+"L");
        px[217] = Pattern.compile("SBB"+space1+"M");
        px[218] = Pattern.compile("SBI"+space1+data);
        px[219] = Pattern.compile("SHLD"+space1+addr);
        px[220] = Pattern.compile("SIM");
        px[221] = Pattern.compile("SPHL");
        px[222] = Pattern.compile("STA"+space1+addr);
        px[223] = Pattern.compile("STAX"+space1+"B");
        px[224] = Pattern.compile("STAX"+space1+"D");
        px[225] = Pattern.compile("STC");
        px[226] = Pattern.compile("SUB"+space1+"A");
        px[227] = Pattern.compile("SUB"+space1+"B");
        px[228] = Pattern.compile("SUB"+space1+"C");
        px[229] = Pattern.compile("SUB"+space1+"D");
        px[230] = Pattern.compile("SUB"+space1+"E");
        px[231] = Pattern.compile("SUB"+space1+"H");
        px[232] = Pattern.compile("SUB"+space1+"L");
        px[233] = Pattern.compile("SUB"+space1+"M");
        px[234] = Pattern.compile("SUI"+space1+data);
        px[235] = Pattern.compile("XCHD");
        px[236] = Pattern.compile("XRA"+space1+"A");
        px[237] = Pattern.compile("XRA"+space1+"B");
        px[238] = Pattern.compile("XRA"+space1+"C");
        px[239] = Pattern.compile("XRA"+space1+"D");
        px[240] = Pattern.compile("XRA"+space1+"E");
        px[241] = Pattern.compile("XRA"+space1+"H");
        px[242] = Pattern.compile("XRA"+space1+"L");
        px[243] = Pattern.compile("XRA"+space1+"M");
        px[244] = Pattern.compile("XRI"+space1+data);
        px[245] = Pattern.compile("XTHL");
        
    }
    public static void main(String[] args) {
        int flag=1;
        zero0 obj= new zero0();
        Scanner s = new Scanner(System.in);
        long StartTime = System.currentTimeMillis();
        String R ="[BCDE]";
        String abcd="";
        obj.initializePatt();
        while(flag==1)
        {
            if(abcd.equals("STOP"))
            {
                flag=0;
            }
        System.out.println("Enter a String:");
        abcd = s.nextLine();
        abcd = abcd.toUpperCase();
        
        int foundPatt = obj.findI(abcd);
        System.out.println(foundPatt);
        System.out.println( obj.findOpcode(foundPatt));
        }
        /*if(m.find())
        {
            System.out.println("Found!\nStarts at "+m.start()+"\nEnds At "+m.end()+"Group: "+m.group());
        }
        else
        {
            System.out.println("Not Found!");
        }*/
        long StopTime = System.currentTimeMillis();
        System.out.print(StopTime-StartTime);
        }
    
    int findI(String str)
    {
        
        for(int i=0;i<px.length;i++)
        {
            m = px[i].matcher(str);
            if(m.find())
            {
                return i;
            }
        }
        return -1;
    }
    
    String findOpcode(int x)
    {
        switch(x)
        {
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
            case 15 :
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
                return "75";
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
            case 123 :
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
                return "Not Found at "+Integer.toString(x);
               
        }
    }
    
    
        
    }