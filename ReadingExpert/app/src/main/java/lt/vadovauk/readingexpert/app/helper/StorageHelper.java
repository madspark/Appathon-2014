package lt.vadovauk.readingexpert.app.helper;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;

public class StorageHelper {

    public static void addLevel(Context context, int newLevel) {
        SharedPreferences prefs = context.getSharedPreferences(
                "lt.vadovauk.readingexpert.app", Context.MODE_PRIVATE);

        ArrayList<Integer> levels = readLevels(context);
        if (!levels.contains(newLevel)) {
            levels.add(newLevel);
        }
        String doneList = "";
        for (int i = 0; i < levels.size(); i++) {
            doneList += (levels.get(i));
        }
        prefs.edit().putString("DoneList", doneList).apply();
    }


    public static ArrayList<Integer> readLevels(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(
                "lt.vadovauk.readingexpert.app", Context.MODE_PRIVATE);
        String str = prefs.getString("DoneList", "");
        return DataHelper.GetIntegers(str);
    }
}