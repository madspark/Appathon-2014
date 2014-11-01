package lt.vadovauk.readingexpert.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import java.util.ArrayList;

import lt.vadovauk.readingexpert.app.adapter.GridViewAdapter;
import lt.vadovauk.readingexpert.app.domain.Story;


public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ArrayList<Story> stories = new ArrayList<Story>();
        Story testStory = new Story("asdasd", "description", 4, "once upon a time", " imageSource", false);
        stories.add(testStory);
        stories.add(testStory);
        stories.add(testStory);
        GridView mGridView = (GridView) findViewById(R.id.gridView);
        mGridView.setAdapter(new GridViewAdapter(MainActivity.this, stories));
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, PreReadActivity.class);
                startActivity(intent);
            }
        });
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
        } else if (id == R.id.action_crossword_test) {
            Intent intent = new Intent(this, CrosswordActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}
