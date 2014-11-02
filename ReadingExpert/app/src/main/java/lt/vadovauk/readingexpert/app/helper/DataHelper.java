package lt.vadovauk.readingexpert.app.helper;

import android.widget.TextView;

import java.util.ArrayList;
import java.util.StringTokenizer;

import lt.vadovauk.readingexpert.app.domain.UserResult;

public class DataHelper {


    public static ArrayList<String> getLines(String content, TextView tv) {


        content = content.replaceAll("\\.(?=[\\da-zA-Z])",". ");
        content = content.replaceAll("!(?=[\\da-zA-Z])","! ");
        content = content.replaceAll("\\?(?=[\\da-zA-Z])","? ");
        //StringTokenizer st = new StringTokenizer(content, " ");
        String[] words = content.split(" ");
        String line = "";
        ArrayList<String> lines = new ArrayList<String>();
        for(String i : words){
            if(i.endsWith(".") || i.endsWith("?") || i.endsWith("!")){
                line += " " + i;
                lines.add(line.trim());
                line = "";
            }
            else {
                line += " " + i;
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
