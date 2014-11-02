package lt.vadovauk.readingexpert.app;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nhaarman.supertooltips.ToolTip;
import com.nhaarman.supertooltips.ToolTipRelativeLayout;
import com.nhaarman.supertooltips.ToolTipView;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import lt.vadovauk.readingexpert.app.common.NetworkClient;
import lt.vadovauk.readingexpert.app.helper.DataHelper;

public class ReadActivity extends Activity {

    final static int DELAY = 2000; //milliseconds

    String content = "Once upon a time there were three little pigs and the time came for them to leave home and seek their fortunes. Before they left, their mother told them \" Whatever you do , do it the best that you can because that's the way to get along in the world.";
    int line = 0;
    TextView readLineTxt1;
    ArrayList<String> lines;
    Button bPrevious;
    Button bPause;
    boolean isPaused = false;
    TimerTask timerTask;
    Timer timer;
    ProgressBar progressBar;
    String id;
    private ToolTipView myToolTipView;
    ToolTipRelativeLayout toolTipRelativeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read);


        content = getIntent().getStringExtra("content");
        id = getIntent().getStringExtra("iToolTipViewd");


        readLineTxt1 = (TextView) findViewById(R.id.read_line_txt1);
        bPrevious = (Button) findViewById(R.id.previous_btn);
        bPause = (Button) findViewById(R.id.pause_btn);


        lines = DataHelper.getLines(content, readLineTxt1);

        timer = new Timer();
        timerTask = generateTask();
        timer.scheduleAtFixedRate(timerTask, 0, DELAY);


        bPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isPaused) { //already paused, button should resume
                    timerTask = generateTask();
                    timer.scheduleAtFixedRate(timerTask, 0, DELAY);
                    isPaused = false;
                    bPause.setText("Pause");
                } else {
                    timerTask.cancel();
                    isPaused = true;
                    bPause.setText("Play");
                }
            }
        });

        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        bPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isPaused && line > 1) {
                    line -= 2;
                    init(readLineTxt1, lines.get(line));
                } else if (isPaused && line == 1) {
                    line--;
                    init(readLineTxt1, lines.get(line));
                } else if (line > 1) { // not paused, firstly pause, then show previous.
                    timerTask.cancel();
                    isPaused = true;
                    line -= 2;
                    init(readLineTxt1, lines.get(line));
                    bPause.setText("Play");
                } else if (line == 1) {
                    timerTask.cancel();
                    isPaused = true;
                    line--;
                    init(readLineTxt1, lines.get(line));
                    bPause.setText("Play");
                }
            }
        });

        toolTipRelativeLayout = (ToolTipRelativeLayout) findViewById(R.id.activity_main_tooltipRelativeLayout);

        showToolTip("tip");
    }

    private void showToolTip(String tip) {
        ToolTip toolTip = new ToolTip()
                .withText(tip)
                .withColor(Color.RED)
                .withShadow();
        myToolTipView = toolTipRelativeLayout.showToolTipForView(toolTip, findViewById(R.id.read_line_txt1));
    }

    private TimerTask generateTask() {
        return new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (line < lines.size()) {
                            init(readLineTxt1, lines.get(line));
                            line++;
                            if (progressBar.getProgress() < 100) {
                                progressBar.setProgress(line * 100 / lines.size());
                            }
                        } else {
                            Intent intent = new Intent(ReadActivity.this, QuizActivity.class);
                            intent.putExtra("id", id);
                            startActivity(intent);
                            timer.cancel();
                            finish();
                        }
                    }
                });
            }
        };
    }

    private void init(TextView tv, String text) {
        tv.setMovementMethod(LinkMovementMethod.getInstance());
        tv.setText(text, TextView.BufferType.SPANNABLE);
        Spannable spans = (Spannable) tv.getText();
        BreakIterator iterator = BreakIterator.getWordInstance(Locale.US);
        iterator.setText(text);
        int start = iterator.first();
        for (int end = iterator.next(); end != BreakIterator.DONE; start = end, end = iterator
                .next()) {
            String possibleWord = text.substring(start, end);
            if (Character.isLetterOrDigit(possibleWord.charAt(0))) {
                ClickableSpan clickSpan = getClickableSpan(possibleWord);
                spans.setSpan(clickSpan, start, end,
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
    }

    private ClickableSpan getClickableSpan(final String word) {
        return new ClickableSpan() {
            final String mWord;
            {
                mWord = word;
            }

            @Override
            public void onClick(View widget) {
                Log.d("tapped on:", mWord);
                getDefinition(mWord);
                if (isPaused) {
                    //already paused, do nothing
                } else {
                    timerTask.cancel();
                    isPaused = true;
                    bPause.setText("Play");
                }
            }

            public void updateDrawState(TextPaint ds) {
                //super.updateDrawState(ds);
            }
        };
    }

    private void getDefinition(String mWord) {
        RequestParams rp = new RequestParams();
        rp.add("word", mWord);
        NetworkClient.get("/definitions/get_definition", rp, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    String tip = response.getString("definition");
                    showToolTip(tip);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                super.onSuccess(statusCode, headers, response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
            }
        });

    }

}
