package com.svc2uk.readingexpert;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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


import com.svc2uk.readingexpert.adapter.GridViewAdapter;
import com.svc2uk.readingexpert.common.NetworkClient;
import com.svc2uk.readingexpert.domain.Story;
import com.svc2uk.readingexpert.domain.UserResult;
import com.svc2uk.readingexpert.helper.DataHelper;
import com.svc2uk.readingexpert.helper.StorageHelper;

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
                ArrayList<UserResult> results = StorageHelper.readUserResults(context);
                int previous = (position == 0) ? 0 : position - 1;
                if (position == 0 || DataHelper.contains(results, stories.get(previous).getApiId())) {
                    float rating = 0;
                    if (DataHelper.contains(results, stories.get(position).getApiId())) {
                        rating = results.get(position).bestResult;
                    }

                    Intent intent = new Intent(MainActivity.this, PreReadActivity.class);
                    intent.putExtra("story", stories.get(position));
                    intent.putExtra("rating", rating);

                    startActivity(intent);
                } else {
                    Toast.makeText(context, "You need to complete other levels first", Toast.LENGTH_SHORT).show();
                }
            }
        });

        getStories();
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

    @Override
    protected void onResume() {
        super.onResume();
        if (mGridView != null)
            mGridView.setAdapter(new GridViewAdapter(MainActivity.this, stories));
    }
}
