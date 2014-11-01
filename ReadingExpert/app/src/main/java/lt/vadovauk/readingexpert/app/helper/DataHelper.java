package lt.vadovauk.readingexpert.app.helper;

import java.util.ArrayList;
import java.util.StringTokenizer;

public class DataHelper {
    private static final int maxNumberOfChars = 30;


    public static ArrayList<String> getLines(String content) {

        StringTokenizer st = new StringTokenizer(content, " ");
        ArrayList<String> lines = new ArrayList<String>();
        String line = "";
        while (st.hasMoreTokens()) {
            String word = st.nextToken();

            if ((line + " " +
                    word).length() > maxNumberOfChars) {
                lines.add(line);
                line = word;
            } else {
                line += " " + word;
            }
        }

        return lines;
    }
}
