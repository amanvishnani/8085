
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
