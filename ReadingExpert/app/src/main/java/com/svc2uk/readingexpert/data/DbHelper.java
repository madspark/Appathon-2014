package com.svc2uk.readingexpert.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by antanas on 14.11.1.
 */

public class DbHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;

    public static final String DATABASE_NAME = "entries.db";

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        final String SQL_CREATE_ENTRIES_TABLE = "CREATE TABLE " + DbContract.Story.TABLE_NAME +
                " (" +
                DbContract.Story.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                DbContract.Story.COLUMN_DIFF + " INTEGER NOT NULL, " +
                DbContract.Story.COLUMN_IMG + " TEXT, " +
                DbContract.Story.COLUMN_CONTENT + " TEXT NOT NULL, " +
                DbContract.Story.COLUMN_TITLE + " TEXT NOT NULL, " +
                DbContract.Story.COLUMN_DONE + " BOOL, " +
                DbContract.Story.COLUMN_DESC + " TEXT, " +
                DbContract.Story.COLUMN_APIID + " INTEGER NOT NULL, " +
                "UNIQUE (" + DbContract.Story.COLUMN_ID + ") ON CONFLICT REPLACE );"
                +
                "CREATE TABLE " + DbContract.Question.TABLE_NAME + " (" +
                DbContract.Question.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                DbContract.Question.COLUMN_CONTENT + " TEXT NOT NULL, " +
                DbContract.Question.COLUMN_CORRECT + " TEXT NOT NULL, " +
                DbContract.Question.COLUMN_OTHER + " TEXT NOT NULL, " +
                DbContract.Question.COLUMN_STORYID + " INTEGER NOT NULL, " +
                "UNIQUE (" + DbContract.Question.COLUMN_ID + ") ON CONFLICT REPLACE );";

        sqLiteDatabase.execSQL(SQL_CREATE_ENTRIES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) {
        //Fires when DATABASE_VERSION changes
        //Delete the entries table
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + DbContract.Story.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + DbContract.Question.TABLE_NAME);
        onCreate(sqLiteDatabase);

    }
}


