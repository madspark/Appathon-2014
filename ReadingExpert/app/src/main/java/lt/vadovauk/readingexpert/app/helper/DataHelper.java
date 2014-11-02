package lt.vadovauk.readingexpert.app.helper;

import android.widget.TextView;

import java.util.ArrayList;
import java.util.StringTokenizer;

public class DataHelper {
    private static final int maxNumberOfChars = 50;


    public static ArrayList<String> getLines(String content, TextView tv) {

        StringTokenizer st = new StringTokenizer(content, " ");
        ArrayList<String> lines = new ArrayList<String>();
        String line = "";
        while (st.hasMoreTokens()) {
            String word = st.nextToken();

            if ((line + " " + word).length() > maxNumberOfChars) {
                lines.add(line);
                line = word;
            } else {
                line += " " + word;
            }
            if(!st.hasMoreTokens()){
                lines.add(line);
            }
        }

        return lines;
    }


}
