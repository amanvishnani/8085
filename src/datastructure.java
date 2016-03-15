
import java.util.Scanner;

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
    public static String[] memory = new String[65536];
    public String A,B,C,D,E,H,L,SP,IP;
    int S,Z,Ac,P,Cy;
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
        int d=-1;
        if (x.length()>4)
        {
            System.out.println("Greater than FFFF");
            return "NaN";
        }
        else
        {
            try{
                String data = memory[d= Integer.parseInt(x, 16)];
                if (data.length()!=2)
                {
                    System.out.println("Length != 2");
                    return "invalid";
                }
                return data;
            }catch(Exception e){System.out.println(e+"\n String not a Hex Number");}
        }
        return "NaN";
        
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
    public static void main(String[] args) {
       
       
        Scanner x = new Scanner(System.in);
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
    }
    
}
