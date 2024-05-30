import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
public class ExtensibleHashTestDriver {
    public static void main(String[] args) {
        ExtensibleHashST<Integer, Integer> st1 = new ExtensibleHashST<>();

        int x = 0;
        while (st1.size() < 10) {
            st1.put(x, x+100);
            x += 4; }
        st1.detailInfo();

        Scanner sc = new Scanner(System.in);
        System.out.print("\n\n파일 이름? ");
        String fname = sc.next();
        sc.close();

        ExtensibleHashST<String, Integer> st2 = new ExtensibleHashST<>(8);
        try {
            sc = new Scanner(new File(fname));
            while (sc.hasNext()) {
                String word = sc.next();
                if (word.length() < 8) continue;
                if (!st2.contains(word)) st2.put(word, 1);
                else st2.put(word, st2.get(word) + 1);
            }

            String maxKey = "";
            int maxValue = 0;
            for (String word : st2.keys())
                if (st2.get(word) > maxValue) {
                    maxValue = st2.get(word);
                    maxKey = word;
                }
            System.out.println(maxKey + " " + maxValue); st2.summaryInfo();
        } catch (FileNotFoundException e) { e.printStackTrace(); }
        if (sc != null)
            sc.close();
    }
}