package lt.vadovauk.readingexpert.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import lt.vadovauk.readingexpert.app.domain.Story;

public class PreReadActivity extends Activity {
    Story story;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pre_read);

        story = (Story) getIntent().getSerializableExtra("story");

        ImageView storyImg = (ImageView) findViewById(R.id.story_img);
        Picasso.with(this).load(story.getImageSource()).into(storyImg);

        TextView storyTitleTxt = (TextView) findViewById(R.id.story_title_txt);
        storyTitleTxt.setText(story.getTitle());

        Button proceedBtn = (Button) findViewById(R.id.proceed_btn);
        proceedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PreReadActivity.this, ReadActivity.class);
                intent.putExtra("content", story.getContent());
                intent.putExtra("id", story.getApiId());

                startActivity(intent);
                finish();
            }
        });

        TextView storyDescription = (TextView) findViewById(R.id.story_description_txt);
        storyDescription.setText(story.getDescription());

    }
}
