package com.svc2uk.readingexpert;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.svc2uk.readingexpert.common.NetworkClient;
import com.svc2uk.readingexpert.domain.Question;
import com.svc2uk.readingexpert.domain.Story;
import com.svc2uk.readingexpert.helper.DataHelper;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;


public class QuizActivity extends Activity implements CrosswordFragment.OnResultListener {

    private static final int QUESTION_COUNT = 5;

    private ArrayList<Question> mCrosswordQuestions;
    private ArrayList<Question> mQuizQuestions;
    private int mCurrentQuestionIndex;
    private String mCurrentAnswer;
    private TextToSpeech mTTS;
    private Story mStory;
    int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
        ResultActivity.rating = 5;

        mTTS = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    int result = mTTS.setLanguage(Locale.US);
                    mTTS.setSpeechRate(0.7f);
                    if (result == TextToSpeech.LANG_MISSING_DATA
                            || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                        Log.e("TTS", "TTS language is not supported");
                    }
                } else {
                    Log.e("TTS", "TTS initialization failed");
                }
            }
        });

        mStory = (Story) getIntent().getSerializableExtra("story");
        id = getIntent().getIntExtra("id", 0);
        getQuestions(id, true);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.quiz, menu);
        return true;
    }

    private void speakOut() {
        mTTS.speak(mCurrentAnswer, TextToSpeech.QUEUE_FLUSH, null);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_hint) {
            ResultActivity.rating = (float) (ResultActivity.rating - 0.5);
            speakOut();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void playSound(boolean correct) {
        MediaPlayer mPlayer = MediaPlayer.create(this, correct ? R.raw.right : R.raw.wrong);
        mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mPlayer.start();
    }

    @Override
    public void onCorrect() {
        if (mCurrentQuestionIndex < QUESTION_COUNT) {
            Fragment fragment;
            if (mCurrentQuestionIndex % 2 == 0) {
                Question question;
                if (mQuizQuestions.isEmpty()) {
                    question = mCrosswordQuestions.get(mCurrentQuestionIndex);
                } else {
                    question = mCrosswordQuestions.get(mCurrentQuestionIndex / 2);
                }
                mCurrentAnswer = question.getAnswer();
                fragment = CrosswordFragment.newInstance(question.getQuestion(), mCurrentAnswer);
            } else {
                Question question;
                if (mQuizQuestions.isEmpty()) {
                    question = mCrosswordQuestions.get(mCurrentQuestionIndex);
                } else {
                    question = mQuizQuestions.get(mCurrentQuestionIndex / 2);
                }
                ArrayList<String> answers = question.getOtherAnswers();
                mCurrentAnswer = question.getAnswer();
                answers.add(mCurrentAnswer);
                fragment = QuizFragment.newInstance(question.getQuestion(), answers);
            }
            getFragmentManager().beginTransaction()
                    .replace(R.id.container, fragment)
                    .commit();
            mCurrentQuestionIndex++;
        } else {
            Intent intent = new Intent(QuizActivity.this, ResultActivity.class);
            intent.putExtra("id", id);
            intent.putExtra("story", mStory);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void onDestroy() {
        if (mTTS != null) {
            mTTS.stop();
            mTTS.shutdown();
        }
        super.onDestroy();
    }

    private void getQuestions(final int id, final boolean forCrossword) {
        RequestParams rp = new RequestParams();
        rp.add("id", "" + id);
        String path;
        if (forCrossword) {
            path = "/stories/crossword_questions_by_id";
        } else {
            path = "/stories/questions_by_id";
        }
        final ArrayList<Question> questions = new ArrayList<Question>();
        NetworkClient.get(path, rp, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                super.onSuccess(statusCode, headers, response);
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject storyJSON = response.getJSONObject(i);
                        int apiid = Integer.parseInt(storyJSON.getString("Qid"));
                        int storyId = Integer.parseInt(storyJSON.getString("story_id"));
                        String questionContent = storyJSON.getString("Qcontent");
                        String correctAnswer = storyJSON.getString("correct_answer");
                        String otherAnswers = storyJSON.getString("other_answers");

                        ArrayList<String> otherAns = DataHelper.getOtherAns(otherAnswers);

                        Question question = new Question(apiid, storyId, questionContent, correctAnswer, otherAns);
                        questions.add(question);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                Collections.shuffle(questions);
                if (forCrossword) {
                    mCrosswordQuestions = questions;
                    getQuestions(id, false);
                } else {
                    mQuizQuestions = questions;
                    onCorrect();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
            }
        });
    }
}
