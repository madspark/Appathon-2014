package lt.vadovauk.readingexpert.app.helper;

import android.content.Context;
import android.content.SharedPreferences;

public class StorageHelper {

    public static void writeLevels(Context context,  String doneList){
        SharedPreferences prefs = context.getSharedPreferences(
                "lt.vadovauk.readingexpert.app", Context.MODE_PRIVATE);

        prefs.edit().putString("DoneList", doneList).apply();
    };

    public static String readLevels(Context context){
        SharedPreferences prefs = context.getSharedPreferences(
                "lt.vadovauk.readingexpert.app", Context.MODE_PRIVATE);
        return prefs.getString("doneList", "");
    };


}