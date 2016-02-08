
import java.util.*;


public class eight {
    public static void main(String[] args) {

        String[] fruits ={"Apple","Oranges","Pineapple"};
        LinkedList<String> thelist = new LinkedList<String>(Arrays.asList(fruits));
        
        thelist.add("Promoganet"); 
        thelist.addFirst("FirstFruit");
        
        //convert back to an arry
        fruits =thelist.toArray(new String[thelist.size()]);
        
        for(String x: fruits)
            System.out.printf("%s", x);
    }
    
}
