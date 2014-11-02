package lt.vadovauk.readingexpert.app;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import lt.vadovauk.readingexpert.app.common.NetworkClient;
import lt.vadovauk.readingexpert.app.domain.Question;
import lt.vadovauk.readingexpert.app.helper.DataHelper;
import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.*;


public class QuizActivity extends Activity implements CrosswordFragment.OnCorrectListener {

    private static final int QUESTION_COUNT = 5;

    private ArrayList<Question> mQuestions;
    private int mQuestionId;
    private String mCurrentQuestion;
    private String mCurrentAnswer;
    private TextToSpeech mTTS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        mTTS = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    int result = mTTS.setLanguage(Locale.US);
                    //tts.setPitch(5);
                    //tts.setSpeechRate(2);
                    if (result == TextToSpeech.LANG_MISSING_DATA
                            || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                        Log.e("TTS", "TTS language is not supported");
                    }
                } else {
                    Log.e("TTS", "TTS initialization failed");
                }
            }
        });

        mQuestions = new ArrayList<Question>();
        getQuestions(getIntent().getStringExtra("id"));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
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
            speakOut();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCorrect() {
        if (mQuestionId < QUESTION_COUNT) {
            Fragment fragment;
            Question question = mQuestions.get(mQuestionId);
            mCurrentQuestion = question.getQuestion();
            mCurrentAnswer = question.getAnswer();
            if (mQuestionId % 2 == 0) {
                fragment = CrosswordFragment.newInstance(mCurrentQuestion, mCurrentAnswer);
            } else {
                fragment = QuizFragment.newInstance(mCurrentQuestion, mCurrentAnswer);
            }
            getFragmentManager().beginTransaction()
                    .replace(R.id.container, fragment)
                    .commit();
            mQuestionId++;
        } else {
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

    private void getQuestions(String id) {
        RequestParams rp = new RequestParams();
        rp.add("id", id);
        NetworkClient.get("/stories/get_", rp, new JsonHttpResponseHandler() {

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
                        mQuestions.add(question);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                Collections.shuffle(mQuestions);
                onCorrect();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
            }
        });
    }
}
