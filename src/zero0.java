
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
    
    void initializePatt()
    {
        px[0] = Pattern.compile("MOVB,C");
        px[1] = Pattern.compile("MOVB,D");
        px[2] = Pattern.compile("MOVB,E");
        px[3] = Pattern.compile("MOVB,H");
        px[4] = Pattern.compile("MOVB,L");
        px[5] = Pattern.compile("MOVB,B");
        px[6] = Pattern.compile("MOVC,B");
        px[7] = Pattern.compile("MOVC,C");
        px[8] = Pattern.compile("MOVC,D");
        px[9] = Pattern.compile("MOVC,E");
        px[10] = Pattern.compile("MOVC,H");
        px[11] = Pattern.compile("MOVC,L");
        px[12] = Pattern.compile("MOVD,B");
        px[13] = Pattern.compile("MOVD,C");
        px[14] = Pattern.compile("MOVD,D");
        px[15] = Pattern.compile("MOVD,E");
        px[16] = Pattern.compile("MOVD,H");
        px[17] = Pattern.compile("MOVD,L");
        px[18] = Pattern.compile("MOVE,B");
        px[19] = Pattern.compile("MOVE,C");
        px[20] = Pattern.compile("MOVE,D");
        px[21] = Pattern.compile("MOVE,E");
        px[22] = Pattern.compile("MOVE,H");
        px[23] = Pattern.compile("MOVE,L");
        px[24] = Pattern.compile("MOVH,B");
        px[25] = Pattern.compile("MOVH,C");
        px[26] = Pattern.compile("MOVH,D");
        px[27] = Pattern.compile("MOVH,E");
        px[28] = Pattern.compile("MOVH,H");
        px[29] = Pattern.compile("MOVH,L");
        px[30] = Pattern.compile("MOVL,B");
        px[31] = Pattern.compile("MOVL,C");
        px[32] = Pattern.compile("MOVL,D");
        px[33] = Pattern.compile("MOVL,E");
        px[34] = Pattern.compile("MOVL,H");
        px[35] = Pattern.compile("MOVL,L");
        px[36] = Pattern.compile("MOVA,A");
        px[37] = Pattern.compile("MOVA,B");
        px[38] = Pattern.compile("MOVA,C");
        px[39] = Pattern.compile("MOVA,D");
        px[40] = Pattern.compile("MOVA,E");
        px[41] = Pattern.compile("MOVA,H");
        px[42] = Pattern.compile("MOVA,L");
        px[43] = Pattern.compile("MOVB,A");
        px[44] = Pattern.compile("MOVC,A");
        px[45] = Pattern.compile("MOVD,A");
        px[46] = Pattern.compile("MOVE,A");
        px[47] = Pattern.compile("MOVH,A");
        px[48] = Pattern.compile("MOVL,A");
        px[49] = Pattern.compile("MOVA,M");
        px[50] = Pattern.compile("MOVA,M");
        px[50] = Pattern.compile("MOVB,M");
        px[51] = Pattern.compile("MOVC,M");
        px[52] = Pattern.compile("MOVD,M");
        px[53] = Pattern.compile("MOVE,M");
        px[54] = Pattern.compile("MOVH,M");
        px[55] = Pattern.compile("MOVL,M");
        px[56] = Pattern.compile("MOVM,A");
        px[57] = Pattern.compile("MOVM,B");
        px[58] = Pattern.compile("MOVM,C");
        px[59] = Pattern.compile("MOVM,D");
        px[60] = Pattern.compile("MOVM,E");
        px[61] = Pattern.compile("MOVM,H");
        px[62] = Pattern.compile("MOVM,L");
        px[63] = Pattern.compile("MVIA,"+data);
        px[64] = Pattern.compile("MVIB,"+data);
        px[65] = Pattern.compile("MVIC,"+data);
        px[66] = Pattern.compile("MVID,"+data);
        px[67] = Pattern.compile("MVIE,"+data);
        px[68] = Pattern.compile("MVIH,"+data);
        px[69] = Pattern.compile("MVIL,"+data);
        px[70] = Pattern.compile("MVIM,"+data);
        px[71] = Pattern.compile("ACI"+data);
        px[72] = Pattern.compile("ADCA");
        px[73] = Pattern.compile("ADCB");
        px[74] = Pattern.compile("ADCC");
        px[75] = Pattern.compile("ADCD");
        px[76] = Pattern.compile("ADCE");
        px[77] = Pattern.compile("ADCH");
        px[78] = Pattern.compile("ADCL");
        px[79] = Pattern.compile("ADCM");
        px[80] = Pattern.compile("ADDA");
        px[81] = Pattern.compile("ADDB");
        px[82] = Pattern.compile("ADDC");
        px[83] = Pattern.compile("ADDD");
        px[84] = Pattern.compile("ADDE");
        px[85] = Pattern.compile("ADDH");
        px[86] = Pattern.compile("ADDL");
        px[87] = Pattern.compile("ADDM");
        px[88] = Pattern.compile("ADI"+data);
        px[89] = Pattern.compile("ANAA");
        px[90] = Pattern.compile("ANAB");
        px[91] = Pattern.compile("ANAC");
        px[92] = Pattern.compile("ANAD");
        px[93] = Pattern.compile("ANAE");
        px[94] = Pattern.compile("ANAH");
        px[95] = Pattern.compile("ANAL");
        px[96] = Pattern.compile("ANAM");
        px[97] = Pattern.compile("ANI"+data);
        px[98] = Pattern.compile("CALL"+label);
        px[99] = Pattern.compile("CC"+label);
        px[100] = Pattern.compile("CM"+label);
        px[101] = Pattern.compile("CMA");
        px[102] = Pattern.compile("CMC");
        px[103] = Pattern.compile("CMPA");
        px[104] = Pattern.compile("CMPB");
        px[105] = Pattern.compile("CMPC");
        px[106] = Pattern.compile("CMPD");
        px[107] = Pattern.compile("CMPE");
        px[108] = Pattern.compile("CMPH");
        px[109] = Pattern.compile("CMPL");
        px[110] = Pattern.compile("CMPM");
        px[111] = Pattern.compile("CNC"+label);
        px[112] = Pattern.compile("CNZ"+label);
        px[113] = Pattern.compile("CPE"+label);
        px[114] = Pattern.compile("CPO"+label);
        px[115] = Pattern.compile("CPI"+data);
        px[116] = Pattern.compile("CP"+label);
        px[117] = Pattern.compile("CZ"+label);
        px[118] = Pattern.compile("DAA");
        px[119] = Pattern.compile("DADB");
        px[120] = Pattern.compile("DADD");
        px[121] = Pattern.compile("DADH");
        px[122] = Pattern.compile("DADSP");
        px[123] = Pattern.compile("DCRA");
        px[124] = Pattern.compile("DCRB");
        px[125] = Pattern.compile("DCRC");
        px[126] = Pattern.compile("DCRD");
        px[127] = Pattern.compile("DCRE");
        px[128] = Pattern.compile("DCRH");
        px[129] = Pattern.compile("DCRL");
        px[130] = Pattern.compile("DCRM");
        px[131] = Pattern.compile("DCXB");
        px[132] = Pattern.compile("DCXD");
        px[133] = Pattern.compile("DCXH");
        px[134] = Pattern.compile("DCXSP");
        px[135] = Pattern.compile("DI");
        px[136] = Pattern.compile("EI");
        px[137] = Pattern.compile("HLT");
        px[138] = Pattern.compile("IN"+data);
        px[139] = Pattern.compile("INRA");
        px[140] = Pattern.compile("INRB");
        px[141] = Pattern.compile("INRC");
        px[142] = Pattern.compile("INRD");
        px[143] = Pattern.compile("INRE");
        px[144] = Pattern.compile("INRH");
        px[145] = Pattern.compile("INRL");
        px[146] = Pattern.compile("INRM");
        px[147] = Pattern.compile("INXB");
        px[148] = Pattern.compile("INXD");
        px[149] = Pattern.compile("INXH");
        px[150] = Pattern.compile("INXSP");
        px[151] = Pattern.compile("JC"+label);
        px[152] = Pattern.compile("JMP"+label);
        px[153] = Pattern.compile("JM"+label);
        px[154] = Pattern.compile("JNC"+label);
        px[155] = Pattern.compile("JNZ"+label);
        px[156] = Pattern.compile("JPO"+label);
        px[157] = Pattern.compile("JPE"+label);
        px[158] = Pattern.compile("JP"+label);
        px[159] = Pattern.compile("JZ"+label);
        px[160] = Pattern.compile("LDA"+addr);
        px[161] = Pattern.compile("LDAXB");
        px[162] = Pattern.compile("LDAXD");
        px[163] = Pattern.compile("LHLD"+addr);
        px[164] = Pattern.compile("LXIB");
        px[165] = Pattern.compile("LXID");
        px[166] = Pattern.compile("LXIH");
        px[167] = Pattern.compile("LXISP");
        px[168] = Pattern.compile("NOP");
        px[169] = Pattern.compile("ORAA");
        px[170] = Pattern.compile("ORAB");
        px[171] = Pattern.compile("ORAC");
        px[172] = Pattern.compile("ORAD");
        px[173] = Pattern.compile("ORAE");
        px[174] = Pattern.compile("ORAH");
        px[175] = Pattern.compile("ORAL");
        px[176] = Pattern.compile("ORAM");
        px[177] = Pattern.compile("ORI"+data);
        px[178] = Pattern.compile("OUT"+data);
        px[179] = Pattern.compile("PCHL");
        px[180] = Pattern.compile("POPB");
        px[181] = Pattern.compile("POPD");
        px[182] = Pattern.compile("POPH");
        px[183] = Pattern.compile("POPPSW");
        px[184] = Pattern.compile("PUSHB");
        px[185] = Pattern.compile("PUSHD");
        px[186] = Pattern.compile("PUSHH");
        px[187] = Pattern.compile("PUSHPSW");
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
        px[201] = Pattern.compile("RST0");
        px[202] = Pattern.compile("RST1");
        px[203] = Pattern.compile("RST2");
        px[204] = Pattern.compile("RST3");
        px[205] = Pattern.compile("RST4");
        px[206] = Pattern.compile("RST5");
        px[207] = Pattern.compile("RST6");
        px[208] = Pattern.compile("RST7");
        px[209] = Pattern.compile("RZ");
        px[210] = Pattern.compile("SBBA");
        px[211] = Pattern.compile("SBBB");
        px[212] = Pattern.compile("SBBC");
        px[213] = Pattern.compile("SBBD");
        px[214] = Pattern.compile("SBBE");
        px[215] = Pattern.compile("SBBH");
        px[216] = Pattern.compile("SBBL");
        px[217] = Pattern.compile("SBBM");
        px[218] = Pattern.compile("SBI"+data);
        px[219] = Pattern.compile("SHLD"+addr);
        px[220] = Pattern.compile("SIM");
        px[221] = Pattern.compile("SPHL");
        px[222] = Pattern.compile("STA"+addr);
        px[223] = Pattern.compile("STAXB");
        px[224] = Pattern.compile("STAXD");
        px[225] = Pattern.compile("STC");
        px[226] = Pattern.compile("SUBA");
        px[227] = Pattern.compile("SUBB");
        px[228] = Pattern.compile("SUBC");
        px[229] = Pattern.compile("SUBD");
        px[230] = Pattern.compile("SUBE");
        px[231] = Pattern.compile("SUBH");
        px[232] = Pattern.compile("SUBL");
        px[233] = Pattern.compile("SUBM");
        px[234] = Pattern.compile("SUI"+data);
        px[235] = Pattern.compile("XCHD");
        px[236] = Pattern.compile("XRAA");
        px[237] = Pattern.compile("XRAB");
        px[238] = Pattern.compile("XRAC");
        px[239] = Pattern.compile("XRAD");
        px[240] = Pattern.compile("XRAE");
        px[241] = Pattern.compile("XRAH");
        px[242] = Pattern.compile("XRAL");
        px[243] = Pattern.compile("XRAM");
        px[244] = Pattern.compile("XRI"+data);
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