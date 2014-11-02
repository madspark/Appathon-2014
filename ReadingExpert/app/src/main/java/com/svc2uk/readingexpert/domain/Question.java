package com.svc2uk.readingexpert.domain;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Arrays;

import com.svc2uk.readingexpert.data.DbContract;
import com.svc2uk.readingexpert.data.DbHelper;

public class Question {
    private int id;
    private int storyId;
    private String question;
    private String answer;
    ArrayList<String> otherAnswers;

    public ArrayList<String> getOtherAnswers() {
        return otherAnswers;
    }

    public void setOtherAnswers(ArrayList<String> otherAnswers) {
        this.otherAnswers = otherAnswers;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStoryId() {
        return storyId;
    }

    public void setStoryId(int storyId) {
        this.storyId = storyId;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public Question(int id, int storyId, String question, String answer, ArrayList<String> otherAnswers) {
        this.id = id;
        this.storyId = storyId;
        this.question = question;
        this.answer = answer;
        this.otherAnswers = otherAnswers;
    }

    public void insertIntoDb(Context context){
        ContentValues cv = new ContentValues();
        cv.put(DbContract.Question.COLUMN_STORYID, storyId);
        cv.put(DbContract.Question.COLUMN_CONTENT, question);
        cv.put(DbContract.Question.COLUMN_CORRECT, answer);
        String other = "";
        for(int i =0 ;i < otherAnswers.size(); i++){
            if(i != 0)other += ",";
            other += otherAnswers.get(i);
        }
        cv.put(DbContract.Question.COLUMN_OTHER, other);

        DbHelper helper = new DbHelper(context);
        SQLiteDatabase db = helper.getWritableDatabase();
        db.insert(DbContract.Question.TABLE_NAME, null, cv);
        db.close();
    }

    public static Question[] getQuestions(int storyapiId, Context context){

        DbHelper helper = new DbHelper(context);
        SQLiteDatabase db = helper.getReadableDatabase();

        String question;
        String correct;
        ArrayList<String> others;

        Cursor c = db.query(DbContract.Question.TABLE_NAME, null,
                DbContract.Question.COLUMN_STORYID + " = ?",
                new String[] {Integer.toString(storyapiId)}, null, null, null);

        int cursorSize = c.getPosition();
        Question[] qs = new Question[cursorSize];
        c.moveToFirst();
        for(int i = 0; i < cursorSize; i++){
            question = c.getString(c.getColumnIndex(DbContract.Question.COLUMN_CONTENT));
            correct = c.getString(c.getColumnIndex(DbContract.Question.COLUMN_CORRECT));
            String ot = c.getString(c.getColumnIndex(DbContract.Question.COLUMN_OTHER));
            others = new ArrayList<String>(Arrays.asList(ot.split(",")));
            int id = c.getInt(c.getColumnIndex(DbContract.Question.COLUMN_ID));

            qs[i] = new Question(id, storyapiId, question, correct, others);
            c.moveToNext();
        }

        return qs;
    }
}
