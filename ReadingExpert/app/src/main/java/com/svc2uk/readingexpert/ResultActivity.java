package com.svc2uk.readingexpert;

import android.app.Activity;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;

import com.squareup.picasso.Picasso;

import com.svc2uk.readingexpert.domain.Story;
import com.svc2uk.readingexpert.domain.UserResult;
import com.svc2uk.readingexpert.helper.StorageHelper;


public class ResultActivity extends Activity {

    public static float rating = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        Story story = (Story) getIntent().getSerializableExtra("story");
        ImageView storyImg = (ImageView) findViewById(R.id.story_img);
        Picasso.with(this).load(story.getImageSource()).into(storyImg);

        RatingBar ratingBar = (RatingBar) findViewById(R.id.rating_bar);
        ratingBar.setRating(rating);

        Button backToStories = (Button) findViewById(R.id.back_to_stories);
        backToStories.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        int id = getIntent().getIntExtra("id", 0);
        StorageHelper.addLevel(this, new UserResult(id, true, rating));

        MediaPlayer mPlayer = MediaPlayer.create(this, R.raw.finish);
        mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mPlayer.start();
    }
}
