public class one {

    public static void main(String[] args) {
        String[] hello ={"Aman" , "Akshay", "Darshan"};
        for (String s: hello)
        {
            if(s.startsWith("A"))
            {
                System.out.println(s);
            }
        }
        for (String s: hello)
        {
            if(s.endsWith("n"))
            {
                System.out.println(s);
            }
        }
    }
    
}
