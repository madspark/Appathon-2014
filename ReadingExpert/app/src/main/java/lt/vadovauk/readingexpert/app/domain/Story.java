package lt.vadovauk.readingexpert.app.domain;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import lt.vadovauk.readingexpert.app.data.DbContract;
import lt.vadovauk.readingexpert.app.data.DbHelper;

public class Story {
    private String title;
    private String description;
    private int difficulty; //wpm
    private String content;
    private String imageSource;
    private boolean done;

    public Story(String title, String description, int difficulty,
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
        ContentValues cv = new ContentValues();
        cv.put(DbContract.Story.COLUMN_TITLE, title);
        cv.put(DbContract.Story.COLUMN_DIFF, difficulty);
        cv.put(DbContract.Story.COLUMN_DONE, false); // Assume story is not completed at the point of insertion
        cv.put(DbContract.Story.COLUMN_IMG, imageSource);
        cv.put(DbContract.Story.COLUMN_CONTENT, content);
        cv.put(DbContract.Story.COLUMN_DESC, description);

        DbHelper helper = new DbHelper(context);
        SQLiteDatabase db = helper.getWritableDatabase();
        db.insert(DbContract.Story.TABLE_NAME, null, cv);
        db.close();
    }

    public static Story getStory(int id, Context context) {

        DbHelper helper = new DbHelper(context);
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor c = db.query(DbContract.Story.TABLE_NAME, null,
                DbContract.Story.COLUMN_ID + "= ?",
                new String[]{Integer.toString(id)}, null, null, null);

        c.moveToFirst();

        String title;
        String description;
        int difficulty;
        String content;
        String imageSource;
        boolean done;

        title = c.getString(c.getColumnIndex(DbContract.Story.COLUMN_TITLE));
        description = c.getString(c.getColumnIndex(DbContract.Story.COLUMN_DESC));
        difficulty = c.getInt(c.getColumnIndex(DbContract.Story.COLUMN_DIFF));
        content = c.getString(c.getColumnIndex(DbContract.Story.COLUMN_CONTENT));
        imageSource = c.getString(c.getColumnIndex(DbContract.Story.COLUMN_IMG));
        done = 1 == c.getInt(c.getColumnIndex(DbContract.Story.COLUMN_DONE));

        return new Story(title, description, difficulty, content, imageSource);

    }


}
