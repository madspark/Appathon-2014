package lt.vadovauk.readingexpert.app;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import com.loopj.android.http.JsonHttpResponseHandler;
import lt.vadovauk.readingexpert.app.common.NetworkClient;
import lt.vadovauk.readingexpert.app.domain.Question;
import lt.vadovauk.readingexpert.app.helper.DataHelper;
import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class QuizActivity extends Activity implements CrosswordFragment.OnCorrectListener {

    private static final int FRAGMENT_COUNT = 5;

    private Fragment[] mFragments;
    private int mCurrentFragment;
    private ArrayList<Question> questions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        mFragments = new Fragment[FRAGMENT_COUNT];
        mFragments[0] = CrosswordFragment.newInstance("What is Harry's last name?", "Potter");
        mFragments[1] = QuizFragment.newInstance("What is Harry's last name?", "Potter");
        mFragments[2] = CrosswordFragment.newInstance("What is Harry's last name?", "Potter");
        mFragments[3] = QuizFragment.newInstance("What is Harry's last name?", "Potter");
        mFragments[4] = CrosswordFragment.newInstance("What is Harry's last name?", "Potter");

        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, mFragments[0])
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.quiz, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        } else if (id == R.id.action_hint) {
            Toast.makeText(this, "Cia turetu veikt hint TTS", Toast.LENGTH_SHORT);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCorrect() {
        mCurrentFragment++;
        if (mCurrentFragment < FRAGMENT_COUNT) {
            getFragmentManager().beginTransaction()
                    .replace(R.id.container, mFragments[mCurrentFragment])
                    .commit();
        } else {
            finish();
        }
    }

    private void getQuestions() {
        NetworkClient.get("/stories/get_all_questions", null, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                super.onSuccess(statusCode, headers, response);
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject storyJSON = response.getJSONObject(i);
                        int apiid = Integer.parseInt(storyJSON.getString("id"));
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


            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
            }
        });
    }
}
