package lt.vadovauk.readingexpert.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import lt.vadovauk.readingexpert.app.domain.Story;

public class PreReadActivity extends Activity {
    Story story;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pre_read);

        story = (Story) getIntent().getSerializableExtra("story");

        TextView storyTitleTxt = (TextView) findViewById(R.id.story_title_txt);
        storyTitleTxt.setText(story.getTitle());

        Button proceedBtn = (Button) findViewById(R.id.proceed_btn);
        proceedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PreReadActivity.this, ReadActivity.class);
                intent.putExtra("content", story.getContent());
                startActivity(intent);
                finish();
            }
        });
    }
}
