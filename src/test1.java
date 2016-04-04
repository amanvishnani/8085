import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;


public class test1 extends javax.swing.JFrame {

    public static String[] memory = new String[65536];
    public static String A,B,C,D,E,H,L,SP,IP;
    static int S,Z,Ac,P,Cy;
    public static int LABEL=0,OPCODE=1,MEM=1,SYMPTR,SYMPTR1,oldIP;
    public static String[][] ST = new String[100][2];
    public static String[][] ST1 = new String[100][2];
    public static String[][] map = new String[16384][2];
    public Matcher m ;
    public Pattern px[] = new Pattern[246];
    public String data ="[0-9A-F]{2}(H)?";
    public String label = "[A-Za-z]{3}[A-Za-z]*";
    public String addr = "[0-9A-F]{4}(H)?";
    public String space ="( )*";
    public String space1 = "( )+";
    
    
    void IncIP()
    {
        int x = hex2int(getIP());
        if(x<16383)
        {
            int ch =hex2int(CodeHead.getText());
            
            if(x>=(ch+20))
            {
                CodeHead.setText(int2addr(ch+1));
                refreshCode();
            }
            x++;
            SetIP(int2addr(x));
        }
        else
        {
            JOptionPane.showMessageDialog(this, "IP exceeding 3FFF (16383)");
        }
    }
    
    String getLSB(String S)
    {
        if(S.length()==2)
        {
            return S.substring(1, 2);
        }
        else if(S.length()==4)
        {
            return S.substring(2, 4);
        }
        else
        {
            return "";
        }
    }
    
    String getMSB(String S)
    {
        if(S.length()==2)
        {
            return S.substring(0, 1);
        }
        else if(S.length()==4)
        {
            return S.substring(0, 2);
        }
        else
        {
            return "";
        }
    }
    
    int hex2int(String S)
    {
        try{
            return Integer.parseInt(S, 16);
        }catch(Exception NumException){JOptionPane.showMessageDialog(this, "Problem with hex to int converssion , HEX="+S);}
        return -1;
    }
    
    String[] getMyCode()
    {
        int i=0,k=0,j=0;
        String code = code_av.getText().toUpperCase();
        code_token = code.split("\\n");
        String[] temp= new String[code_token.length];
        
        for (j=0;j<code_token.length;j++)
        {
            code_token[j]=Trimmer(code_token[j]);
            if(0!=code_token[j].length())
            {
                temp[i]=code_token[j];
                i++;
            }
            else
            {
                k++;
            }
        }
        String[] temp2 = new String[temp.length-k];
        for(j=0;j<temp.length-k;j++)
        {
            temp2[j]=temp[j];
        }
        return temp2;
    }
    
    public void iniMap()
    {
        for(int i=0;i<65536;i++)
        {
            memory[i]="00";
        }
        for(int i=0;i<16383;i++)
        {
            map[i][LABEL]="";
            map[i][OPCODE]="";
        }
        SetA("00");
        SetB("00");
        SetC("00");
        SetD("00");
        SetE("00");
        SetH("00");
        SetL("00");
        SetCy(0);
        SetAc(0);
        SetS(0);
        SetZ(0);
        SetP(0);
        SetIP("0000");
        SetSP("FFFF");
    }
    
    void refreshCode()
    {
        String S1 = CodeHead.getText();
        int codehead ;
        try{
            if(S1.length()!=0)
            {
                codehead = Integer.parseInt(S1, 16);
            }
            else
            {
                codehead = 0;
            }
           if(codehead<0 || codehead>16383)
           {
               JOptionPane.showMessageDialog(this, "{Make sure given Code Segment Head is in Specified Range} ");
           }
           else
           {
           DefaultTableModel x = (DefaultTableModel) CodeTable.getModel();
           x.setRowCount(0);
           Object obj[] = new Object[4];
           int START=0, END=20;
            if(codehead<=16383 && codehead>16363)
            {
                START =16364;
                END = 16384;
            }
            else 
            {
                START = codehead;
                END = codehead+20;
            }
            for(int i =START ; i<END ; i++ )
                {
                    obj[0]=int2addr(i);
                    obj[1]=map[i][LABEL];
                    obj[2]=map[i][OPCODE];
                    obj[3]=memory[i];
                    x.addRow(obj);
                }
           }
        }catch(Exception NumberfException)
            {
                JOptionPane.showMessageDialog(this, "{Make sure given Code Segment Head is a Hex Number} ");
            }
    }
    
    void refreshData()
    {
        String S1 = DataHead.getText();
        int datahead ;
        try{
            if(S1.length()!=0)
            {
                datahead = Integer.parseInt(S1, 16);
            }
            else
            {
                datahead = 16384;
            }
           if(datahead<16384 || datahead>57343)
           {
               JOptionPane.showMessageDialog(this, "{Make sure given Code Segment Head is in Specified Range} ");
           }
           else
           {
           DefaultTableModel x = (DefaultTableModel) DataTable.getModel();
           x.setRowCount(0);
           Object obj[] = new Object[4];
           int START=57323, END=57343;
            if(datahead<=57343 && datahead>57323)
            {
                START =57324;
                END = 57344;
            }
            else 
            {
                START = datahead;
                END = datahead+20;
            }
            for(int i =START ; i<END ; i++ )
                {
                    obj[0]=int2addr(i);
                    obj[1]=memory[i];
                    x.addRow(obj);
                }
           }
        }catch(Exception NumberfException)
            {
                JOptionPane.showMessageDialog(this, "{Make sure given Data Segment Head is a Hex Number} ");
            }
    }
    
    void refreshStack()
    {
        String S1 = StackHead.getText();
        int stackhead ;
        try{
            if(S1.length()!=0)
            {
                stackhead = Integer.parseInt(S1, 16);
            }
            else
            {
                stackhead = 57344;
            }
           if(stackhead<57344 || stackhead>65535)
           {
               JOptionPane.showMessageDialog(this, "{Make sure given Stack Segment Head is in Specified Range} ");
           }
           else
           {
           DefaultTableModel x = (DefaultTableModel) StackTable.getModel();
           x.setRowCount(0);
           Object obj[] = new Object[4];
           int START=57344, END=57364;
            if(stackhead<=65535 && stackhead>65515)
            {
                START =65516;
                END = 65536;
            }
            else 
            {
                START = stackhead;
                END = stackhead+20;
            }
            for(int i =START ; i<END ; i++ )
                {
                    obj[0]=int2addr(i);
                    obj[1]=memory[i];
                    x.addRow(obj);
                }
           }
        }catch(Exception NumberfException)
            {
                JOptionPane.showMessageDialog(this, "{Make sure given Data Segment Head is a Hex Number} ");
            }
    }
    
    public void Pass2()
    {
        for(int i=0;i<SYMPTR;i++)
        {
            String localLabel,localmem;
            localLabel=ST[i][LABEL].trim();
            localmem = ST[i][MEM];
            for(int j=0;j<SYMPTR1;j++)
            {
                ST1[j][LABEL] = ST1[j][LABEL].trim();
                if(ST1[j][LABEL].equals(localLabel))
                {
                    String localval= ST1[j][MEM];
                    setData(localmem, localval.substring(2, 4));
                    System.out.println(localval.substring(2, 4)+" is set at "+localmem);
                    int x = Integer.parseInt(localmem, 16);
                    x++;
                    localmem = int2addr(x);
                    setData(localmem, localval.substring(0, 2));
                    System.out.println(localval.substring(0, 2)+" is set at "+localmem);
                }
            }
            
        }
    }
    
    public String extractOpcode(String S)
    {
        return S.substring(S.indexOf(":")+1, S.length());
    }
    
    public int Pass1(String[] x)
    {
        int LP=0;
        ST = null;
        ST = new String[100][2];
        SYMPTR=0;
        ST1 = null;
        ST1 = new String[100][2];
        SYMPTR1=0;
        for(int i=0; i<x.length; i++)
        {
            int a1 = findI(x[i]);
            if(a1>=0)
            {
                int flag=0;
                String a2 = findOpcode(a1);
                int a3 = OpcodeLength(a2);
                memory[LP] = a2;
                String l =ExtractLabel(x[i]);
                map[LP][LABEL] = l;
                map[LP][OPCODE] = extractOpcode(x[i]);
//                System.out.println(map[LP][OPCODE]+" for "+i);
                if(l.length()!=0)
                    {
                        for(int g=0;g<SYMPTR1;g++)
                        {
                            if(ST1[g][LABEL].equalsIgnoreCase(l))
                            {
                                JOptionPane.showMessageDialog(this, l+" already Exists at "+ST1[g][MEM]+" Terminating rest of the process");
                                flag=1;
                            }
                        }
                        if(flag==0)
                        {
                        ST1[SYMPTR1][LABEL] = l;
                        //System.out.println("Breakpoint");
                        ST1[SYMPTR1][MEM] = int2addr(LP);
                        SYMPTR1++;
                        }
                    }
                if(a3==1)
                {
                    LP = LP+1;
                }
                else if(a3 == 2)
                {
                    LP = LP + 1;
                    memory[LP] = ExtractData(x[i]);
                    map[LP][OPCODE]="";
                    map[LP][LABEL]="";
                    LP = LP + 1;
                }
                else if(a3 == 3)
                {
                    l = ExtractAddress(x[i]);
                    
                    if(l.length()!=0)
                    {        
                        LP = LP +1;
                        map[LP][OPCODE]="";
                        map[LP][LABEL]="";
                        memory[LP]=l.substring(2, 4);
                        LP = LP +1;
                        map[LP][OPCODE]="";
                        map[LP][LABEL]="";
                        memory[LP]=l.substring(0, 2);
                        LP = LP +1;
                    }
                    else 
                    {
                        l = getLabel(x[i]);
                        
                        LP = LP +1;
                        ST[SYMPTR][LABEL]=l;
                        ST[SYMPTR][MEM]=int2addr(LP);
                        SYMPTR++;
                        map[LP][OPCODE]="";
                        map[LP][LABEL]="";
                        LP = LP + 1;
                        map[LP][OPCODE]="";
                        map[LP][LABEL]="";
                        LP = LP + 1;
                    }
                }
                
            }
            else
            {
                JOptionPane.showMessageDialog(this, "Instruction "+x[i]+" NOT Valid\n Skipping invalid Instruction");
            }
        }
        return 0;
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
        jA.setText(x);
        return 1;
    }
    public int SetB(String x)
    {
        B=x;
        jB.setText(x);
        return 1;
    }
    public int SetC(String x)
    {
        C=x;
        jC.setText(x);
        return 1;
    }
    public int SetD(String x)
    {
        D=x;
        jD.setText(x);
        return 1;
    }
    public int SetE(String x)
    {
        E=x;
        jE.setText(x);
        return 1;
    }
    public int SetH(String x)
    {
        H=x;
        jH.setText(x);
        return 1;
    }
    public int SetL(String x)
    {
        L=x;
        jL.setText(x);
        return 1;
    }
    public int SetSP(String x)
    {
        SP=x;
        jSP.setText(x);
        return 1;
    }
    public int SetIP(String x)
    {
        IP=x;
        jIP.setText(x);
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
            jS.setText(Integer.toString(x));
            return 1;    
        }
        return -1;   
    }
        public int SetZ(int x)
    {
        if(x<2 || x>-1)
        {
            Z=x;
            jZ.setText(Integer.toString(x));
            return 1;    
        }
        return -1;   
    }

    public int SetAc(int x)
    {
        if(x<2 || x>-1)
        {
            Ac=x;
            jAc.setText(Integer.toString(x));
            return 1;    
        }
        return -1;   
    }
    public int SetP(int x)
    {
        if(x<2 || x>-1)
        {
            P=x;
            jP.setText(Integer.toString(x));
            return 1;    
        }
        return -1;   
    }
    public int SetCy(int x)
    {
        if(x<2 || x>-1)
        {
            Cy=x;
            jCy.setText(Integer.toString(x));
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
        String data1="NaN";
        if (x.length()>4)
        {
            System.out.println("Greater than FFFF");
            return "NaN";
        }
        else
        {
            try{
                int d=Integer.parseInt(x, 16);
                data1 = memory[d];
                return data1;
            }catch(Exception e){System.out.println(e+"\n String not a Hex Number");}
        }
        return data1;      
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
        x = x.substring(x.indexOf(","),x.length());
        x = x.substring(x.indexOf(" ")+1,x.length());
        Pattern p = Pattern.compile("[0-9A-F]{2}");
        Matcher m1 = p.matcher(x);
        if(m1.find())
        {
        String temp = m1.group();
        temp = temp.substring(0, 2);
        return temp;
        }
        return "";
    }
    
    public String ExtractAddress(String x)
    {
        Pattern p = Pattern.compile(" [0-9A-F]{4}");
        Matcher m1 = p.matcher(x);
        if(m1.find())
        {
        String temp = m1.group();
        temp = temp.substring(1, 5);
        return temp;
        }
        return "";
    }
    
    /* MOV INSTRUCTIONS */
void _7F()
{
	SetA(getA());
}
void _78()
{
	SetA(getB());
}
void _79()
{
	SetA(getC());
}
void _7A()
{
	SetA(getD());
}
void _7B()
{
	SetA(getE());
}
void _7C()
{
	SetA(getH());
}
void _7D()
{
	SetA(getL());
}
void _7E()
{
	SetA(getM());
}
void _47()
{
	SetB(getA());
}
void _40()
{
	SetB(getB());
}
void _41()
{
	SetB(getC());
}
void _42()
{
	SetB(getD());
}
void _43()
{
	SetB(getE());
}
void _44()
{
	SetB(getH());
}
void _45()
{
	SetB(getL());
}
void _46()
{
	SetB(getM());
}
void _4F()
{
	SetC(getA());
}
void _48()
{
	SetC(getB());
}
void _49()
{
	SetC(getC());
}
void _4A()
{
	SetC(getD());
}
void _4B()
{
	SetC(getE());
}
void _4C()
{
	SetC(getH());
}
void _4D()
{ 
	SetC(getL());
}
void _4E()
{ 
	SetC(getM());
}
void _57()
{
	SetD(getA());
}
void _50()
{
	SetD(getB());
}
void _51()
{
	SetD(getC());
}
void _52()
{
	SetD(getD());
}
void _53()
{
	SetD(getE());
}
void _54()
{
	SetD(getH());
}
void _55()
{
	SetD(getL());
}
void _56()
{
	SetD(getM());
}
void _5F()
{
	SetE(getA());
}
void _58()
{
	SetE(getB());
}
void _59()
{
	SetE(getC());
}
void _5A()
{
	SetE(getD());
}
void _5B()
{
	SetE(getE());
}
void _5C()
{
	SetE(getH());
}
void _5D()
{
	SetE(getL());
}
void _5E()
{
	SetE(getM());
}
void _67()
{
	SetH(getA());
}
void _60()
{
	SetH(getB());
}
void _61()
{
	SetH(getC());
}
void _62()
{
	SetH(getD());
}
void _63()
{
	SetH(getE());
}
void _64()
{
	SetH(getH());
}
void _65()
{
	SetH(getL());
}
void _66()
{
	SetH(getM());
}
void _6F()
{
	SetL(getA());
}
void _68()
{
	SetL(getB());
}
void _69()
{
	SetL(getC());
}
void _6A()
{
	SetL(getD());
}
void _6B()
{
	SetL(getE());
}
void _6C()
{
	SetL(getH());
}
void _6D()
{
	SetL(getL());
}
void _6E()
{
	SetL(getM());
}
void _77()
{
	SetM(getA());
}
void _70()
{
	SetM(getB());
}
void _71()
{
	SetM(getC());
}
void _72()
{
	SetM(getD());
}
void _73()
{
	SetM(getE());
}
void _74()
{
	SetM(getH());
}
void _75()
{
	SetM(getL());
}
void _76()
{
	SetM(getM());
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
        x = Trimmer(x.substring(d+1, x.length()));
        d= x.indexOf(" ");
        x = x.substring(d+1, x.length()).trim();
        if(x.length()==0 || x==null)
        {
            JOptionPane.showMessageDialog(this, "Label Not found");
            return "";
        }
        else
        return x;
    }

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
    
    String[] code_token;
    public int run_code_index=0;
    /**
     * Creates new form test1
     */
    public test1() {
        initComponents();
        initializePatt();
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
        jButton2 = new javax.swing.JButton();
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
        jScrollPane1.setViewportView(code_av);

        jButton1.setText("LOAD >");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        code_here.setText("Type Your Code Here:");

        jScrollPane2.setViewportView(run_code);

        jButton2.setText("1 Step >");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
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

        jS.setText("0");
        jS.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jSActionPerformed(evt);
            }
        });

        jZ.setText("0");

        jAc.setText("0");

        jP.setText("0");

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
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
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

        jButton5.setText("Set");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
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
            Class[] types = new Class [] {
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
        jScrollPane4.setViewportView(DataTable);

        jLabel20.setText("Data Space ranges from 4000 to DFFF");

        jLabel21.setText("Display Memory From :");

        DataHead.setText("4000");
        DataHead.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                DataHeadActionPerformed(evt);
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

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 426, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel20)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel21)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(DataHead, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel27)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton4))
                    .addComponent(jLabel22))
                .addGap(0, 0, Short.MAX_VALUE))
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
                    .addComponent(jLabel27))
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
            .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 422, Short.MAX_VALUE)
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
                        .addGap(0, 110, Short.MAX_VALUE))))
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
                    .addComponent(jButton2)
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
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 18, Short.MAX_VALUE)
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
                .addGap(27, 743, Short.MAX_VALUE))
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
                    .addComponent(jButton2))
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jTabbedPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 463, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(21, 21, 21))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        oldIP=0;
        iniMap();
        int SetB = SetB("01");
        int SetC = SetC("02");
        SetH("03");
        SetL("04");
        String _09;
        _09 = _09();
        Pass1(getMyCode());
        Pass2();
        String test123[];
        test123 = new String[20];
        System.arraycopy(memory, 0, test123, 0, 20);
        run_code.setListData(test123);
        run_code_index=0;
        System.out.println("Symbol Table for Where to Load\nLABEL\tMEMORY");
        for(int e=0; e<10; e++)
        {
            System.out.println(ST[e][LABEL]+"\t"+ST[e][MEM]);
        }
        System.out.println("Symbol Table for Symbol and their LOACTIONS\nLABEL\tMEMORY");
        for(int e=0; e<10; e++)
        {
            System.out.println(ST1[e][LABEL]+"\t"+ST1[e][MEM]);
        }
        
        refreshCode();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        int nip= hex2int(getIP());
        IncIP();
        nip = nip - hex2int(CodeHead.getText());

        CodeTable.addRowSelectionInterval(nip, nip);
        if(oldIP!=nip)
            CodeTable.removeRowSelectionInterval(oldIP, oldIP);
        oldIP=nip;
       // run_code.setSelectedIndex(run_code_index);
       // run_code_index++;
       // System.out.println(run_code.getSelectedValue());
    }//GEN-LAST:event_jButton2ActionPerformed

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
        refreshCode();
    }//GEN-LAST:event_jButton5ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
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
            java.util.logging.Logger.getLogger(test1.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(test1.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(test1.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(test1.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                test1 t1 = new test1();
                t1.setVisible(true);
                        
                
            }
        });
    }
    //ADD A
String _87()
{
    int r1,r2;
    r1 = hex2int(getA());
    r2 = hex2int(getA());
    if((r1+r2)>255)
        {SetCy(1);}
    if( (hex2int(getLSB(getA()))+hex2int(getLSB(getA()))) > 15)
    {
        SetAc(1);
    }

    String temp = Integer.toHexString(r1+r2);
        switch (temp.length()) {
            case 1:
                temp= "0"+temp;
                SetA(temp);
                return "0";
            case 2:
                SetA(temp);
                return "0";
            default:
                SetA(temp.substring((temp.length() - 2),temp.length()));
                break;
        }
 return "0";
}
//ADD B
String _80()
{
    int r1,r2;
    r1 = hex2int(getA());
    r2 = hex2int(getB());
    if((r1+r2)>255)
        {SetCy(1);}
    if( (hex2int(getLSB(getA()))+hex2int(getLSB(getB()))) > 15)
    {
        SetAc(1);
    }

    String temp = Integer.toHexString(r1+r2);
            switch (temp.length()) {
            case 1:
                temp= "0"+temp;
                SetA(temp);
                return "0";
            case 2:
                SetA(temp);
                return "0";
            default:
                SetA(temp.substring((temp.length() - 2),temp.length()));
                break;
        }
 return "0";

}
//ADD C
String _81()
{
    int r1,r2;
    r1 = hex2int(getA());
    r2 = hex2int(getC());
    if((r1+r2)>255)
        {SetCy(1);}
    if( (hex2int(getLSB(getA()))+hex2int(getLSB(getC()))) > 15)
    {
        SetAc(1);
    }

    String temp = Integer.toHexString(r1+r2);
        switch (temp.length()) {
            case 1:
                temp= "0"+temp;
                SetA(temp);
                return "0";
            case 2:
                SetA(temp);
                return "0";
            default:
                SetA(temp.substring((temp.length() - 2),temp.length()));
                break;
        }
 return "0";
}
//ADD D
String _82()
{
    int r1,r2;
    r1 = hex2int(getA());
    r2 = hex2int(getD());
    if((r1+r2)>255)
        {SetCy(1);}
    if( (hex2int(getLSB(getA()))+hex2int(getLSB(getD()))) > 15)
    {
        SetAc(1);
    }

    String temp = Integer.toHexString(r1+r2);
        switch (temp.length()) {
            case 1:
                temp= "0"+temp;
                SetA(temp);
                return "0";
            case 2:
                SetA(temp);
                return "0";
            default:
                SetA(temp.substring((temp.length() - 2),temp.length()));
                break;
        }
 return "0";
}
//ADD E
String _83()
{
    int r1,r2;
    r1 = hex2int(getA());
    r2 = hex2int(getE());
    if((r1+r2)>255)
        {SetCy(1);}
    if( (hex2int(getLSB(getA()))+hex2int(getLSB(getE()))) > 15)
    {
        SetAc(1);
    }

    String temp = Integer.toHexString(r1+r2);
        switch (temp.length()) {
            case 1:
                temp= "0"+temp;
                SetA(temp);
                return "0";
            case 2:
                SetA(temp);
                return "0";
            default:
                SetA(temp.substring((temp.length() - 2),temp.length()));
                break;
        }
 return "0";
}
//ADD H
String _84()
{
    int r1,r2;
    r1 = hex2int(getA());
    r2 = hex2int(getH());
    if((r1+r2)>255)
        {SetCy(1);}
    if( (hex2int(getLSB(getA()))+hex2int(getLSB(getH()))) > 15)
    {
        SetAc(1);
    }
        String temp = Integer.toHexString(r1+r2);
        switch (temp.length()) {
            case 1:
                temp= "0"+temp;
                SetA(temp);
                return "0";
            case 2:
                SetA(temp);
                return "0";
            default:
                SetA(temp.substring((temp.length() - 2),temp.length()));
                break;
        }
 return "0";
}
//ADD L
String _85()
{
    int r1,r2;
    r1 = hex2int(getA());
    r2 = hex2int(getL());
    if((r1+r2)>255)
        {SetCy(1);}
    if( (hex2int(getLSB(getA()))+hex2int(getLSB(getL()))) > 15)
    {
        SetAc(1);
    }

    String temp = Integer.toHexString(r1+r2);
        switch (temp.length()) {
            case 1:
                temp= "0"+temp;
                SetA(temp);
                return "0";
            case 2:
                SetA(temp);
                return "0";
            default:
                SetA(temp.substring((temp.length() - 2),temp.length()));
                break;
        }
 return "0";
}
//ADD M
String _86()
{
    int r1,r2;
    r1 = hex2int(getA());
    r2 = hex2int(getM());
    if((r1+r2)>255)
        {SetCy(1);}
    if( (hex2int(getLSB(getA()))+hex2int(getLSB(getM()))) > 15)
    {
        SetAc(1);
    }

    String temp = Integer.toHexString(r1+r2);
        switch (temp.length()) {
            case 1:
                temp= "0"+temp;
                SetA(temp);
                return "0";
            case 2:
                SetA(temp);
                return "0";
            default:
                SetA(temp.substring((temp.length() - 2),temp.length()));
                break;
        }
 return "0";
}
//ADC A
String _8F()
{
   int r1,r2,r3;
    r1 = hex2int(getA());
    r2 = hex2int(getA());
    if((r1+r2+getCy())>255)
        {SetCy(1);}
    if( (hex2int(getLSB(getA()))+hex2int(getLSB(getA()))+getCy()) > 15)
    {
        SetAc(1);
    }
    r3=r1+r2;
    if(getCy()==1)
    {
        r3++;
    }
    String temp = Integer.toHexString(r3);
        switch (temp.length()) {
            case 1:
                temp= "0"+temp;
                SetA(temp);
                return "0";
            case 2:
                SetA(temp);
                return "0";
            default:
                SetA(temp.substring((temp.length() - 2),temp.length()));
                break;
        }
 return "0";

}
//ADC B
String _88()
{
    int r1,r2,r3;
    r1 = hex2int(getA());
    r2 = hex2int(getB());
    if((r1+r2+getCy())>31)
        {SetCy(1);}
    if( (hex2int(getLSB(getA()))+hex2int(getLSB(getB()))+getCy()) > 15)
    {
        SetAc(1);
    }
    r3=r1+r2;
    if(getCy()==1)
    {
        r3++;
    }
    String temp = Integer.toHexString(r3);
        switch (temp.length()) {
            case 1:
                temp= "0"+temp;
                SetA(temp);
                return "0";
            case 2:
                SetA(temp);
                return "0";
            default:
                SetA(temp.substring((temp.length() - 2),temp.length()));
                break;
        }
 return "0";

}
//ADC C
String _89()
{
    int r1,r2,r3;
    r1 = hex2int(getA());
    r2 = hex2int(getC());
    if((r1+r2+getCy())>31)
        {SetCy(1);}
    if( (hex2int(getLSB(getA()))+hex2int(getLSB(getC()))+getCy()) > 15)
    {
        SetAc(1);
    }
    r3=r1+r2;
    if(getCy()==1)
    {
        r3++;
    }
    String temp = Integer.toHexString(r3);
            switch (temp.length()) {
            case 1:
                temp= "0"+temp;
                SetA(temp);
                return "0";
            case 2:
                SetA(temp);
                return "0";
            default:
                SetA(temp.substring((temp.length() - 2),temp.length()));
                break;
        }
 return "0";

}
//ADC D
String _8A()
{
    int r1,r2,r3;
    r1 = hex2int(getA());
    r2 = hex2int(getD());
    if((r1+r2+getCy())>31)
        {SetCy(1);}
    if( (hex2int(getLSB(getA()))+hex2int(getLSB(getD()))+getCy()) > 15)
    {
        SetAc(1);
    }
    r3=r1+r2;
    if(getCy()==1)
    {
        r3++;
    }
    String temp = Integer.toHexString(r3);
        switch (temp.length()) {
            case 1:
                temp= "0"+temp;
                SetA(temp);
                return "0";
            case 2:
                SetA(temp);
                return "0";
            default:
                SetA(temp.substring((temp.length() - 2),temp.length()));
                break;
        }
 return "0";

}
//ADC E
String _8B()
{
    int r1,r2,r3;
    r1 = hex2int(getA());
    r2 = hex2int(getE());
    if((r1+r2+getCy())>31)
        {SetCy(1);}
    if( (hex2int(getLSB(getA()))+hex2int(getLSB(getE()))+getCy()) > 15)
    {
        SetAc(1);
    }
    r3=r1+r2;
    if(getCy()==1)
    {
        r3++;
    }
    String temp = Integer.toHexString(r3);
        switch (temp.length()) {
            case 1:
                temp= "0"+temp;
                SetA(temp);
                return "0";
            case 2:
                SetA(temp);
                return "0";
            default:
                SetA(temp.substring((temp.length() - 2),temp.length()));
                break;
        }
 return "0";

}
//ADC H
String _8C()
{
    int r1,r2,r3;
    r1 = hex2int(getA());
    r2 = hex2int(getH());
    if((r1+r2+getCy())>31)
        {SetCy(1);}
    if( (hex2int(getLSB(getA()))+hex2int(getLSB(getH()))+getCy()) > 15)
    {
        SetAc(1);
    }
    r3=r1+r2;
    if(getCy()==1)
    {
        r3++;
    }
    String temp = Integer.toHexString(r3);
        switch (temp.length()) {
            case 1:
                temp= "0"+temp;
                SetA(temp);
                return "0";
            case 2:
                SetA(temp);
                return "0";
            default:
                SetA(temp.substring((temp.length() - 2),temp.length()));
                break;
        }
 return "0";

}
//ADC L
String _8D()
{
    int r1,r2,r3;
    r1 = hex2int(getA());
    r2 = hex2int(getL());
    if((r1+r2+getCy())>31)
        {SetCy(1);}
    if( (hex2int(getLSB(getA()))+hex2int(getLSB(getL()))+getCy()) > 15)
    {
        SetAc(1);
    }
    r3=r1+r2;
    if(getCy()==1)
    {
        r3++;
    }
    String temp = Integer.toHexString(r3);
        switch (temp.length()) {
            case 1:
                temp= "0"+temp;
                SetA(temp);
                return "0";
            case 2:
                SetA(temp);
                return "0";
            default:
                SetA(temp.substring((temp.length() - 2),temp.length()));
                break;
        }
 return "0";

}
String _8E()
{
    int r1,r2,r3;
    r1 = hex2int(getA());
    r2 = hex2int(getM());
    if((r1+r2+getCy())>31)
        {SetCy(1);}
    if( (hex2int(getLSB(getA()))+hex2int(getLSB(getM()))+getCy()) > 15)
    {
        SetAc(1);
    }
    r3=r1+r2;
    if(getCy()==1)
    {
        r3++;
    }
    String temp = Integer.toHexString(r3);
        switch (temp.length()) {
            case 1:
                temp= "0"+temp;
                SetA(temp);
                return "0";
            case 2:
                SetA(temp);
                return "0";
            default:
                SetA(temp.substring((temp.length() - 2),temp.length()));
                break;
        }
 return "0";

}
//SUB A
String _97()
{
    int r1,r2,r3;
    r1 = hex2int(getA());
    r2 = hex2int(getA());
    r3=r1-r2;
    if (r3<0)
    {
        r3*=-1;
        SetS(1);
    } 
    else SetS(0);
    if(r3==0)
    {
        SetZ(1);
    }
    else SetZ(0);
    String temp = Integer.toHexString(r3);
    SetA(temp);
    return "0";
}
//SUB B
String _90()
{
        int r1,r2,r3;
    r1 = hex2int(getA());
    r2 = hex2int(getB());
    r3=r1-r2;
    if (r3<0)
    {
        SetS(1);
    } 
    else SetS(0);
    if(r3==0)
    {
        SetZ(1);
    }
    else SetZ(0);
    String temp = Integer.toHexString(r3);
    SetA(temp);
    return"0";

}
//SUB C
String _91()
{
        int r1,r2,r3;
    r1 = hex2int(getA());
    r2 = hex2int(getC());
    r3=r1-r2;
    if (r3<0)
    {
        SetS(1);
    } 
    else SetS(0);
    if(r3==0)
    {
        SetZ(1);
    }
    else SetZ(0);
    String temp = Integer.toHexString(r3);
    SetA(temp);
    return"0";

}
//SUB D
String _92()
{
        int r1,r2,r3;
    r1 = hex2int(getA());
    r2 = hex2int(getD());
    r3=r1-r2;
    if (r3<0)
    {
        SetS(1);
    } 
    else SetS(0);
    if(r3==0)
    {
        SetZ(1);
    }
    else SetZ(0);
    String temp = Integer.toHexString(r3);
    SetA(temp);
    return"0";

}
//SUB E
String _93()
{
        int r1,r2,r3;
    r1 = hex2int(getA());
    r2 = hex2int(getE());
    r3=r1-r2;
    if (r3<0)
    {
        SetS(1);
    } 
    else SetS(0);
    if(r3==0)
    {
        SetZ(1);
    }
    else SetZ(0);
    String temp = Integer.toHexString(r3);
    SetA(temp);
    return"0";

}
//SUB H
String _94()
{
        int r1,r2,r3;
    r1 = hex2int(getA());
    r2 = hex2int(getH());
    r3=r1-r2;
    if (r3<0)
    {
        SetS(1);
    } 
    else SetS(0);
    if(r3==0)
    {
        SetZ(1);
    }
    else SetZ(0);
    String temp = Integer.toHexString(r3);
    SetA(temp);
    return"0";

}
//SUB L
String _95()
{
        int r1,r2,r3;
    r1 = hex2int(getA());
    r2 = hex2int(getL());
    r3=r1-r2;
    if (r3<0)
    {
        SetS(1);
    } 
    else SetS(0);
    if(r3==0)
    {
        SetZ(1);
    }
    else SetZ(0);
    String temp = Integer.toHexString(r3);
    SetA(temp);
    return"0";

}
//SUB M
String _96()
{
        int r1,r2,r3;
    r1 = hex2int(getA());
    r2 = hex2int(getM());
    r3=r1-r2;
    if (r3<0)
    {
        SetS(1);
    } 
    else SetS(0);
    if(r3==0)
    {
        SetZ(1);
    }
    else SetZ(0);
    String temp = Integer.toHexString(r3);
    SetA(temp);
    return"0";

}
//INR A
String _3C()
{
    int r;
    r=hex2int(getA());
    r++;
    String temp = Integer.toHexString(r);
    SetA(temp);
    return "0";
}
//INR B
String _04()
{
    int r;
    r=hex2int(getB());
    r++;
    String temp = Integer.toHexString(r);
    SetB(temp);
    return "0";

}
//INR C
String _0C()
{
    int r;
    r=hex2int(getC());
    r++;
    String temp = Integer.toHexString(r);
    SetC(temp);
    return "0";

}
//INR D
String _14()
{
    int r;
    r=hex2int(getD());
    r++;
    String temp = Integer.toHexString(r);
    SetD(temp);
    return "0";

}
//INT E
String _1C()
{
    int r;
    r=hex2int(getE());
    r++;
    String temp = Integer.toHexString(r);
    SetE(temp);
    return "0";

}
//INR H
String _24()
{
    int r;
    r=hex2int(getH());
    r++;
    String temp = Integer.toHexString(r);
    SetH(temp);
    return "0";

}
//INR L
String _2C()
{
    int r;
    r=hex2int(getL());
    r++;
    String temp = Integer.toHexString(r);
    SetL(temp);
    return "0";

}
//INR M
String _34()
{
    int r;
    r=hex2int(getM());
    r++;
    String temp = Integer.toHexString(r);
    SetM(temp);
    return "0";

}
//CMP A
String _BF()
{
    int r1,r2;
    r1=hex2int(getA());
    r2=hex2int(getA());
    if(r1<r2)
        SetCy(1);
    if(r1==r2)
        SetZ(1);
    if(r1>r2)
    {
        SetZ(0);
        SetCy(0);
    }
    return "0";
}
//CMP B
String _B8()
{
    int r1,r2;
    r1=hex2int(getA());
    r2=hex2int(getB());
    if(r1<r2)
        SetCy(1);
    if(r1==r2)
        SetZ(1);
    if(r1>r2)
    {
        SetZ(0);
        SetCy(0);
    }
    return "0";

}
//CMP C
String _B9()
{
    int r1,r2;
    r1=hex2int(getA());
    r2=hex2int(getC());
    if(r1<r2)
        SetCy(1);
    if(r1==r2)
        SetZ(1);
    if(r1>r2)
    {
        SetZ(0);
        SetCy(0);
    }
    return "0";

}
//CMP D
String _BA()
{
    int r1,r2;
    r1=hex2int(getA());
    r2=hex2int(getD());
    if(r1<r2)
        SetCy(1);
    if(r1==r2)
        SetZ(1);
    if(r1>r2)
    {
        SetZ(0);
        SetCy(0);
    }
    return "0";

}
//CMP E
String _BB()
{
    int r1,r2;
    r1=hex2int(getA());
    r2=hex2int(getE());
    if(r1<r2)
        SetCy(1);
    if(r1==r2)
        SetZ(1);
    if(r1>r2)
    {
        SetZ(0);
        SetCy(0);
    }
    return "0";

}
//CMP H
String _BC()
{
    int r1,r2;
    r1=hex2int(getA());
    r2=hex2int(getH());
    if(r1<r2)
        SetCy(1);
    if(r1==r2)
        SetZ(1);
    if(r1>r2)
    {
        SetZ(0);
        SetCy(0);
    }
    return "0";

}
//CMP L
String _BD()
{
    int r1,r2;
    r1=hex2int(getA());
    r2=hex2int(getL());
    if(r1<r2)
        SetCy(1);
    if(r1==r2)
        SetZ(1);
    if(r1>r2)
    {
        SetZ(0);
        SetCy(0);
    }
    return "0";

}
String _BE()
{
    int r1,r2;
    r1=hex2int(getA());
    r2=hex2int(getM());
    if(r1<r2)
        SetCy(1);
    if(r1==r2)
        SetZ(1);
    if(r1>r2)
    {
        SetZ(0);
        SetCy(0);
    }
    return "0";

}
//DCR A
String _3D()
{
    int r;
    r=hex2int(getA());
    r--;
    if(r==0)
        SetZ(1);
    String temp = Integer.toHexString(r);
    SetA(temp);
    return "0";
}
//DCR B
String _05()
{
    int r;
    r=hex2int(getB());
    r--;
    if(r==0)
        SetZ(1);
    String temp = Integer.toHexString(r);
    SetB(temp);
    return "0";

}
//DCR C
String _0D()
{
    int r;
    r=hex2int(getC());
    r--;
    if(r==0)
        SetZ(1);
    String temp = Integer.toHexString(r);
    SetC(temp);
    return "0";

}
//DCR D
String _15()
{
    int r;
    r=hex2int(getD());
    r--;
    if(r==0)
        SetZ(1);
    String temp = Integer.toHexString(r);
    SetD(temp);
    return "0";

}
//DCR E
String _1D()
{
    int r;
    r=hex2int(getE());
    r--;
    if(r==0)
        SetZ(1);
    String temp = Integer.toHexString(r);
    SetE(temp);
    return "0";
}
//DCR H
String _25()
{
    int r;
    r=hex2int(getH());
    r--;
    if(r==0)
        SetZ(1);
    String temp = Integer.toHexString(r);
    SetH(temp);
    return "0";
}
//DCR L
String _2D()
{
    int r;
    r=hex2int(getL());
    r--;
    if(r==0)
        SetZ(1);
    String temp = Integer.toHexString(r);
    SetL(temp);
    return "0";

}
//DCR M
String _35()
{
    int r;
    r=hex2int(getM());
    r--;
    if(r==0)
        SetZ(1);
    String temp = Integer.toHexString(r);
    SetM(temp);
    return "0";
}
//XCHG
String _EB()
{
    String r1,r2;
    r1=getD();
    r2=getE();
    SetD(getH());
    SetE(getL());
    SetH(r1);
    SetL(r2);
    return "0";
}
//DAD B
String _09()
 {
     int r1,r2;
     r1=hex2int(getB().concat(getC()));
     r2=hex2int(getH().concat(getL()));
     if((r1+r2<65535))
         SetCy(1);
     String temp=Integer.toHexString(r1+r2);
     String t1 = temp.substring(temp.length()-2,temp.length());
     String t2 =  temp.substring(temp.length()-4,temp.length()-2);
     SetL(t1);
     SetH(t2);
     return "0";
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
    private javax.swing.JTabbedPane jTabbedPane2;
    private javax.swing.JTextField jTextField18;
    private javax.swing.JTextField jZ;
    private javax.swing.JList run_code;
    // End of variables declaration//GEN-END:variables
}
