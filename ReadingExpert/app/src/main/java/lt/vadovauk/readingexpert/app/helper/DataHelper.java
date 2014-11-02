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


    public static ArrayList<Integer> GetIntegers (String doneStories){
        ArrayList<Integer> ListOfIntegers = new ArrayList<Integer>();
        StringTokenizer tokenizer = new StringTokenizer(doneStories, ",");
        while (tokenizer.hasMoreTokens()){
            ListOfIntegers.add(Integer.parseInt(tokenizer.nextToken()));
        }
        return ListOfIntegers;
    }

    public static ArrayList<String> getOtherAns(String otherAnswers) {
        ArrayList<String> otherAns = new ArrayList<String>();

        StringTokenizer st = new StringTokenizer(otherAnswers, ",");
        while (st.hasMoreTokens()) {
            otherAns.add(st.nextToken());
        }
        return otherAns;
    }

}
