import java.util.*;
public class six {

    public static void main(String[] args) {
        String[] me={"Aman","Vishnani"};
        String[] myfriends ={"Darshan","Sarje","Akshay","Pokalwar"};
        
        List<String> list1 = new LinkedList<String>();
        List<String> list2 = new LinkedList<String>();
        
        for(String x: me)
            list1.add(x);
        for(String y: myfriends)
            list2.add(y);
        
        list1.addAll(list2);
        list2=null;
        printMe(list1);
        removeStuff(list1,2,3);
        reverseMe(list1);
    }
    //Print me method
    private static void printMe(List<String> h){
            for(String b : h)
                System.out.printf("%s",b);
            System.out.println();
           
    }
    
    //removeStuff Method
    private static void removeStuff(List<String> l, int from, int to)
    {
        l.subList(from,to).clear();
    }
    
    //reverseMe

    private static void reverseMe(List<String> l)
    {
        ListIterator<String> aman = l.listIterator(l.size());
        while(aman.hasPrevious())
            System.out.printf("%s", aman.previous());
        
    }
}
