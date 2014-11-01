package lt.vadovauk.readingexpert.app.domain;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.io.Serializable;
import java.util.ArrayList;

import lt.vadovauk.readingexpert.app.data.DbContract;
import lt.vadovauk.readingexpert.app.data.DbHelper;


public class Story implements Serializable{
    private int apiid;
    private String title;
    private String description;
    private int difficulty; //wpm
    private String content;
    private String imageSource;
    private boolean done;

    public Story(int apiid, String title, String description, int difficulty,
                 String content, String imageSource) {
        this.title = title;
        this.description = description;
        this.difficulty = difficulty;
        this.content = content;
        this.imageSource = imageSource;
        this.done = false;
    }

    public String getContent() {
        return content;
    }

    public String getImageSource() {
        return imageSource;
    }

    public void setImageSource(String source) {
        this.imageSource = source;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void insertIntoDb(Context context) {

        //check if the story is already in the database
        DbHelper helper = new DbHelper(context);
        SQLiteDatabase db = helper.getReadableDatabase();

        Cursor c = db.query(DbContract.Story.TABLE_NAME, null, DbContract.Story.COLUMN_APIID + " = ? ",
                new String[] {Integer.toString(apiid)}, null, null, null);

        if(c.moveToFirst()){
            db.close();
            return;
        }

        db = helper.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(DbContract.Story.COLUMN_APIID, apiid);
        cv.put(DbContract.Story.COLUMN_TITLE, title);
        cv.put(DbContract.Story.COLUMN_DIFF, difficulty);
        cv.put(DbContract.Story.COLUMN_DONE, false); // Assume story is not completed at the point of insertion
        cv.put(DbContract.Story.COLUMN_IMG, imageSource);
        cv.put(DbContract.Story.COLUMN_CONTENT, content);
        cv.put(DbContract.Story.COLUMN_DESC, description);


        db.insert(DbContract.Story.TABLE_NAME, null, cv);
        db.close();
    }

    public static Story getStory(int apiid, Context context) {

        DbHelper helper = new DbHelper(context);
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor c = db.query(DbContract.Story.TABLE_NAME, null,
                DbContract.Story.COLUMN_APIID + " = ?",
                new String[]{Integer.toString(apiid)}, null, null, null);

        c.moveToFirst();

        //int id;
        String title;
        String description;
        int difficulty;
        String content;
        String imageSource;
        boolean done;

        //id = c.getInt(c.getColumnIndex(DbContract.Story.COLUMN_ID));
        title = c.getString(c.getColumnIndex(DbContract.Story.COLUMN_TITLE));
        description = c.getString(c.getColumnIndex(DbContract.Story.COLUMN_DESC));
        difficulty = c.getInt(c.getColumnIndex(DbContract.Story.COLUMN_DIFF));
        content = c.getString(c.getColumnIndex(DbContract.Story.COLUMN_CONTENT));
        imageSource = c.getString(c.getColumnIndex(DbContract.Story.COLUMN_IMG));
        done = 1 == c.getInt(c.getColumnIndex(DbContract.Story.COLUMN_DONE));

        return new Story(apiid, title, description, difficulty, content, imageSource);

    }

    public static ArrayList<Story> getStories(Context context){

        ArrayList<Story> stories = new ArrayList<Story>();

        DbHelper helper = new DbHelper(context);
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor c = db.query(DbContract.Story.TABLE_NAME, null, null, null, null, null, null);

        int size = c.getCount();
        c.moveToFirst();

        for(int i = 0; i< size; i++){

            int apiId;
            String title;
            String description;
            int difficulty;
            String content;
            String imageSource;
            boolean done;

            apiId = c.getInt(c.getColumnIndex(DbContract.Story.COLUMN_APIID));
            title = c.getString(c.getColumnIndex(DbContract.Story.COLUMN_TITLE));
            description = c.getString(c.getColumnIndex(DbContract.Story.COLUMN_DESC));
            difficulty = c.getInt(c.getColumnIndex(DbContract.Story.COLUMN_DIFF));
            content = c.getString(c.getColumnIndex(DbContract.Story.COLUMN_CONTENT));
            imageSource = c.getString(c.getColumnIndex(DbContract.Story.COLUMN_IMG));
            done = 1 == c.getInt(c.getColumnIndex(DbContract.Story.COLUMN_DONE));

            stories.add(new Story(apiId, title, description, difficulty, content, imageSource));
            c.moveToNext();

        }
        return stories;
    }


}
