package lt.vadovauk.readingexpert.app;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import lt.vadovauk.readingexpert.app.common.NetworkClient;
import lt.vadovauk.readingexpert.app.domain.Question;


public class QuestionActivity extends Activity {

    ArrayList<Question> questions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);

        questions = new ArrayList<Question>();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.question, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void getQuestions() {

        {
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

                            Question question = new Question(apiid, storyId, questionContent, correctAnswer, null);

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
}
