import java.util.*;
public class four {

    public static void main(String[] args) {
        String[] name ={"Aman","Anisa","Mumtaz","Hyderali"};
        List<String> list1 = new ArrayList<String>();
        for(String x: name)
        {
            list1.add(x);
        }
        String[] parent ={"Mumtaz","Hyderali"};
        List<String> list2 =new ArrayList<String>();
        for(String y: parent)
            list2.add(y);
        for(int i=0; i<list2.size();i++)
            System.out.printf("%s", list2.get(i));

    }
    
}
