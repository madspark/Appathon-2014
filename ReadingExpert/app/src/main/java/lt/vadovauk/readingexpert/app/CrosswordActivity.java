package lt.vadovauk.readingexpert.app;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.GridView;
import android.widget.TextView;

public class CrosswordActivity extends Activity {

    private String mQuestion;
    private String mAnswer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crossword);

        mQuestion = "What is Harry's last name?";
        mAnswer = "Potter";

        TextView questionView = (TextView) findViewById(R.id.questionTextView);
        questionView.setText(mQuestion);

        final GridView grid = (GridView) findViewById(R.id.crosswordGrid);
        final CrosswordGridAdapter adapter = new CrosswordGridAdapter(this, mAnswer.toUpperCase());
        grid.setAdapter(adapter);
        grid.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent e) {
                int action = e.getAction();
                if (action == MotionEvent.ACTION_MOVE) {
                    int position = grid.pointToPosition((int) e.getX(), (int) e.getY());
                    if (position != GridView.INVALID_POSITION) {
                        TextView textView = adapter.getTextView(position);
                        textView.setBackgroundColor(Color.GREEN);
                    }
                } else if (action == MotionEvent.ACTION_UP) {
                    adapter.checkCorrect();
                }
                return true;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.crossword, menu);
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
}
