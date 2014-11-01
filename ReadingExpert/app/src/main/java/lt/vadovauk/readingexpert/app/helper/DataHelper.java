package lt.vadovauk.readingexpert.app.helper;

import java.util.ArrayList;

public class DataHelper {
    private static final int maxNumberOfChars = 30;


    public static ArrayList<String> getLines(String content) {

        ArrayList<String> lines = new ArrayList<String>();

        int previousBreak = 0;
        int nextBreak = previousBreak + maxNumberOfChars;
        while(nextBreak+maxNumberOfChars<content.length()) {
            nextBreak = previousBreak + maxNumberOfChars;
            while (content.charAt(nextBreak) != ' ') {
                nextBreak--;
            }
            String line = content.substring(previousBreak, nextBreak);
            previousBreak = nextBreak;
            lines.add(line);
        }
        return lines;
    }
}
