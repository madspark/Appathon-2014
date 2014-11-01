package lt.vadovauk.readingexpert.app;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

public class CrosswordActivity extends Activity {

    private String mQuestion;
    private String mAnswer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crossword);

        final Button nextButton = (Button) findViewById(R.id.next_button);

        mQuestion = "What is Harry's last name?";
        mAnswer = "Potter";

        TextView questionView = (TextView) findViewById(R.id.question_text_view);
        questionView.setText(mQuestion);

        final GridView grid = (GridView) findViewById(R.id.crossword_grid);
        final CrosswordGridAdapter adapter = new CrosswordGridAdapter(this, mAnswer.toUpperCase());
        grid.setAdapter(adapter);
        grid.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent e) {
                int action = e.getAction();
                if (action == MotionEvent.ACTION_MOVE) {
                    int position = grid.pointToPosition((int) e.getX(), (int) e.getY());
                    if (position != GridView.INVALID_POSITION) {
                        adapter.select(position);
                    }
                } else if (action == MotionEvent.ACTION_UP) {
                    if (adapter.checkCorrect()) {
                        Toast.makeText(CrosswordActivity.this, R.string.correct, Toast.LENGTH_SHORT).show();
                        nextButton.setVisibility(Button.VISIBLE);
                    } else {
                        Toast.makeText(CrosswordActivity.this, R.string.incorrect, Toast.LENGTH_SHORT).show();
                    }
                }
                return true;
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
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
