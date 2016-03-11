
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

    public static void main(String[] args) {
       
        String[] memory = new String[65536];
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
