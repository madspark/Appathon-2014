package lt.vadovauk.readingexpert.app.helper;

import android.widget.TextView;

import java.util.ArrayList;
import java.util.StringTokenizer;

import lt.vadovauk.readingexpert.app.domain.UserResult;

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
            if (!st.hasMoreTokens()) {
                lines.add(line);
            }
        }

        return lines;
    }


    public static ArrayList<String> getOtherAns(String otherAnswers) {
        ArrayList<String> otherAns = new ArrayList<String>();

        StringTokenizer st = new StringTokenizer(otherAnswers, ",");
        while (st.hasMoreTokens()) {
            otherAns.add(st.nextToken());
        }
        return otherAns;
    }

    public static boolean contains(ArrayList<UserResult> results, int storyId) {

        for (int i = 0; i < results.size(); i++) {
            if (results.get(i).storyId == storyId) {
                return true;
            }
        }
        return false;
    }
}
