/**
 *
 * @author Aman
 */
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class re {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
       int count =0;
       Pattern p = Pattern.compile("\\W");
       Matcher m = p.matcher("abb8aababa");
       while (m.find())
       {
           System.out.println("Starts at "+m.start()+" and Ends at "+ (m.end()-1)+ "..."+m.group());
           count++;
       }
       System.out.println("The Number of occurences:"+count);
    }
    
}
