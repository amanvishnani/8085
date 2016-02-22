
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
    public Pattern px[] = new Pattern[71];
    String data ="[0-9A-F]{4}(H)?";
    
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
        
        
        
        
    }
    public static void main(String[] args) {
        zero0 obj= new zero0();
        Scanner s = new Scanner(System.in);
        long StartTime = System.currentTimeMillis();
        String R ="[BCDE]";
        String abcd;
        obj.initializePatt();
        
        System.out.println("Enter a String:");
        abcd = s.nextLine();
        abcd = abcd.toUpperCase();
        
        int foundPatt = obj.findI(abcd);
        System.out.println(foundPatt);
        System.out.println( obj.findOpcode(foundPatt));
        
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
                
                
            default:
                return "Not Found at "+Integer.toString(x);
               
        }
    }
    
    
        
    }