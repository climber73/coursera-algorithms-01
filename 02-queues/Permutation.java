import edu.princeton.cs.algs4.StdIn;

public class Permutation {
    public static void main(String[] args) {
        int k = Integer.parseInt(args[0]);
        String s;
        int i = 0;
        Deque<String> d = new Deque<>();
        while ((s = StdIn.readString()) != null && i < k) {
            d.addLast(s);
            i++;
        }
        System.out.println("size=" + d.size());
        System.out.println("queue = " + d);
    }
}
