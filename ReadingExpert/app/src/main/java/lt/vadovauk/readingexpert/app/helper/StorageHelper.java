package lt.vadovauk.readingexpert.app.helper;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

import lt.vadovauk.readingexpert.app.domain.UserResult;

public class StorageHelper {
    private static SharedPreferences prefs;
    private static final String PREFS_NAME = "lt.vadovauk.readingexpert.app";
    private static final String USER_RESULTS = "user_results";

    public static void addLevel(Context context, UserResult result) {
        prefs = context.getSharedPreferences(
                PREFS_NAME, Context.MODE_PRIVATE);

        ArrayList<UserResult> results = readUserResults(context);
        boolean exists = false;
        for (int i = 0; i < results.size(); i++) {
            if (results.get(i).storyId == result.storyId) {
                results.get(i).bestResult = result.bestResult;
                exists = true;
            }
        }
        if (exists) {
            results.add(result);
        }

        writeUserResults(context, results);
    }

    public static ArrayList<UserResult> readUserResults(Context context) {

        prefs = context.getSharedPreferences(
                PREFS_NAME, Context.MODE_PRIVATE);
        String json = prefs.getString(USER_RESULTS, null);
        if (json != null) {
            Gson gson = new Gson();
            return gson.fromJson(json, new TypeToken<ArrayList<UserResult>>() {
            }.getType());
        } else
            return new ArrayList<UserResult>();
    }

    public static void writeUserResults(Context context, ArrayList<UserResult> results) {
        Gson gson = new Gson();
        String json = gson.toJson(results);
        prefs = context.getSharedPreferences(
                PREFS_NAME, Context.MODE_PRIVATE);

        SharedPreferences.Editor e = prefs.edit();
        e.putString(USER_RESULTS, json);
        e.commit();
    }
}