
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
public class datastructure {
    public static int LABEL=0,OPCODE=1,HEXC=2,LP;
    public static String[] memory = new String[65536];
    public static String[][] map = new String[16384][3];
    public String A,B,C,D,E,H,L,SP,IP;
    int S,Z,Ac,P,Cy;
    int execute(String op)
{
    switch(s)
	{
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
            _39();
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
            _F3();
            break;
	case "FB":
            _FB();
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
			_20();
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
			_30();
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
			_DB();
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
			_D3();
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
    return 1;
}
    public static String int2addr(int x)
    {
        String temp = Integer.toHexString(x);
        temp = temp.toUpperCase();
        if(temp.length()==1)
        {
            temp = "000".concat(temp);
            return temp;
        }
        else if(temp.length()==2)
        {
            temp = "00".concat(temp);
            return temp;
        }
        else if(temp.length()==3)
        {
            temp = "0".concat(temp);
            return temp;
        }
        else 
        {return temp;}
    }
    public int SetA(String x)
    {
        A=x;
        return 1;
    }
    public int SetB(String x)
    {
        B=x;
        return 1;
    }
    public int SetC(String x)
    {
        C=x;
        return 1;
    }
    public int SetD(String x)
    {
        D=x;
        return 1;
    }
    public int SetE(String x)
    {
        E=x;
        return 1;
    }
    public int SetH(String x)
    {
        H=x;
        return 1;
    }
    public int SetL(String x)
    {
        L=x;
        return 1;
    }
    public int SetSP(String x)
    {
        SP=x;
        return 1;
    }
    public int SetIP(String x)
    {
        IP=x;
        return 1;
    }
    public String getA()
    {
        return A;
    }
    public String getB()
    {
        return B;
    }
    public String getC()
    {
        return C;
    }
    public String getD()
    {
        return D;
    }
    public String getE()
    {
        return E;
    }
    public String getH()
    {
        return H;
    }
    public String getL()
    {
        return L;
    }
    public String getSP()
    {
        return SP;
    }
    public String getIP()
    {
        return IP;
    }
    public int SetS(int x)
    {
        if(x<2 || x>-1)
        {
            S=x;
            return 1;    
        }
        return -1;   
    }
        public int SetZ(int x)
    {
        if(x<2 || x>-1)
        {
            Z=x;
            return 1;    
        }
        return -1;   
    }

    public int SetAc(int x)
    {
        if(x<2 || x>-1)
        {
            Ac=x;
            return 1;    
        }
        return -1;   
    }
    public int SetP(int x)
    {
        if(x<2 || x>-1)
        {
            P=x;
            return 1;    
        }
        return -1;   
    }
    public int SetCy(int x)
    {
        if(x<2 || x>-1)
        {
            Cy=x;
            return 1;    
        }
        return -1;   
    }
    public int getS()
    {
        return S;
    }
    public int getZ()
    {
        return Z;
    }
    public int getCy()
    {
        return Cy;
    }
    public int getP()
    {
        return P;
    }
    public int getAc()
    {
        return Ac;
    }
    public String getData(String x)
    {
        String data="nan";
        if (x.length()>4)
        {
            System.out.println("Greater than FFFF");
            return "NaN";
        }
        else
        {
            try{
                int d=Integer.parseInt(x, 16);
                data = memory[d];
                return data;
            }catch(Exception e){System.out.println(e+"\n String not a Hex Number");}
        }
        return data;
        
    }
    public int setData(String address,String data)
    {
        try{
            if (data.length()!=2)
                {
                    System.out.println("Length != 2");
                    return -1;
                }
                memory[Integer.parseInt(address, 16)]=data;
                
            }catch(Exception e){System.out.println(e+"\n String not a Hex Number");}
        return 1;
    }
    
    public String getM()
    {
        String localH,localL;
        localH= getH();
        localL= getL();
        String temp = localH.concat(localL);
        return (getData(temp));
    }
    public int SetM(String x)
    {
        String localH,localL;
        localH= getH();
        localL= getL();
        String temp = localH.concat(localL);
        setData(temp,x);
        return 1;
    }
    
    public String ExtractData(String x)
    {
        Pattern p = Pattern.compile("[0-9A-F]{2}");
        Matcher m = p.matcher(x);
        if(m.find())
        {
        String temp = m.group();
        temp = temp.substring(0, 2);
        return temp;
        }
        return "";
    }
    
    public String ExtractAddress(String x)
    {
        Pattern p = Pattern.compile(" [0-9A-F]{4}");
        Matcher m = p.matcher(x);
        if(m.find())
        {
        String temp = m.group();
        temp = temp.substring(1, 5);
        return temp;
        }
        return "";
    }
        
    public String Trimmer(String x)
    {
        x = x.trim().replaceAll(" +"," ");
        return x;
    }
    public String ExtractLabel (String x)
    {
        int d =x.indexOf(":");
        if(-1!=d) {
            
            return x.substring(0, d);
        }
        else {
            return "";
        }
    }
    public String getLabel(String x)
    {
        int d = x.indexOf(":");
        x = x.substring(d+1, x.length()).trim();
        d= x.indexOf(" ");
        x = x.substring(d+1, x.length()).trim();
        return x;
    }
    
    public String extractOpcode(String S)
    {
        return S.substring(S.indexOf(":")+1, S.length());
    }
    public int OpcodeLength(String s)
{
	switch(s)
	{
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
    
    public int MyLoader(String[] x)
    {
        for(int i=0; i<x.length; i++)
        {
           
        }
        return 0;
    }
    public static void main(String[] args) {
       
       
        Scanner x = new Scanner(System.in);
        /*
        int d=-1;
        String data=null;
        for(int i=0;i<65536;i++)
        {
            memory[i]= "00";
        }
        String input =x.nextLine();
        if (input.length()>4)
        {
            System.out.println("Greater than FFFF");
        }
        else
        {
            try{
                data =memory[d= Integer.parseInt(input, 16)];
                if (data.length()!=2)
                {
                    System.out.println("Length != 2");
                }
            }catch(Exception e){System.out.println(e+"\n String not a Hex Number");}
        }
        
        System.out.println(data);
        System.out.print(d);
        */
        datastructure obj = new datastructure();
        String b=null;
        System.out.println(obj.extractOpcode("MVI D , 05H"));
        //System.out.println("Found ="+obj.ExtractAddress("LXIH 0525H"));
        String l =obj.getLabel("Label: Mov A,B");
        System.out.println("-"+l+"-");
    }
    
}
