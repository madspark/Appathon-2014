package lt.vadovauk.readingexpert.app;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import lt.vadovauk.readingexpert.app.adapter.GridViewAdapter;
import lt.vadovauk.readingexpert.app.common.NetworkClient;
import lt.vadovauk.readingexpert.app.domain.Story;
import lt.vadovauk.readingexpert.app.helper.DataHelper;
import lt.vadovauk.readingexpert.app.helper.StorageHelper;

public class MainActivity extends Activity {
    Context context = MainActivity.this;
    ArrayList<Story> stories = new ArrayList<Story>();
    GridView mGridView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mGridView = (GridView) findViewById(R.id.gridView);
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String doneLevels = StorageHelper.readLevels(context);
                ArrayList<Integer> done = DataHelper.GetIntegers(doneLevels);
                if (position == 0 || done.contains(stories.get(position).getApiId())) {
                    Intent intent = new Intent(MainActivity.this, PreReadActivity.class);
                    intent.putExtra("story", stories.get(position));
                    intent.putExtra("id", "" + id);
                    startActivity(intent);
                } else {
                    Toast.makeText(context, "You need to complete other levels first", Toast.LENGTH_SHORT).show();
                }
            }
        });

        getStories();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        } else if (id == R.id.action_quiz) {
            Intent intent = new Intent(MainActivity.this, QuizActivity.class);
            intent.putExtra("id", "6");
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void getStories() {

        NetworkClient.get("/stories/get_all_stories", null, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                super.onSuccess(statusCode, headers, response);
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject storyJSON = response.getJSONObject(i);
                        int apiid = Integer.parseInt(storyJSON.getString("id"));
                        int difficulty = Integer.parseInt(storyJSON.getString("difficulty"));
                        String content = storyJSON.getString("content");
                        String title = storyJSON.getString("title");
                        String description = storyJSON.getString("description");
                        String imageUrl = NetworkClient.BASE_URL + storyJSON.getString("imagesource");

                        Story story = new Story(apiid, title, description, difficulty, content, imageUrl);
                        stories.add(story);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                mGridView.setAdapter(new GridViewAdapter(MainActivity.this, stories));
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
            }
        });
    }
}
