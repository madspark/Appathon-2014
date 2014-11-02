package lt.vadovauk.readingexpert.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v7.widget.CardView;
import android.text.Html;
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

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import lt.vadovauk.readingexpert.app.common.NetworkClient;
import lt.vadovauk.readingexpert.app.domain.Story;
import lt.vadovauk.readingexpert.app.helper.DataHelper;

public class ReadActivity extends Activity {

    final static int DELAY = 5000; //milliseconds
    int adjusted_delay;


    Story story;
    int line = 0;
    TextView readLineTxt1;
    TextView tvDefTitle;
    TextView tvDefBody;
    ArrayList<String> lines;
    Button bPrevious;
    Button bPause;
    boolean isPaused = false;
    TimerTask timerTask;
    Timer timer;
    ProgressBar progressBar;
    CardView cardView;
    int id;
    private TextToSpeech mTTS;

    @Override
    protected void onPause() {
        super.onPause();
        finish();
        timerTask.cancel();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read);

        story = (Story) getIntent().getSerializableExtra("story");
        int level = story.getApiId();
        id = story.getApiId();

        adjusted_delay = 5000 - (level - 1) * 500;
        getActionBar().setTitle(story.getTitle());

        readLineTxt1 = (TextView) findViewById(R.id.read_line_txt1);
        bPrevious = (Button) findViewById(R.id.previous_btn);
        bPause = (Button) findViewById(R.id.pause_btn);
        tvDefTitle = (TextView) findViewById(R.id.tv_def_title);
        tvDefBody = (TextView) findViewById(R.id.tv_def_body);
        cardView = (CardView) findViewById(R.id.card);

        lines = DataHelper.getLines(story.getContent(), readLineTxt1);

        timer = new Timer();
        timerTask = generateTask();
        timer.scheduleAtFixedRate(timerTask, 0, adjusted_delay);

        bPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isPaused) { //already paused, button should resume
                    timerTask = generateTask();
                    timer.scheduleAtFixedRate(timerTask, 0, adjusted_delay);
                    isPaused = false;
                    bPause.setText("Pause");
                } else {
                    timerTask.cancel();
                    isPaused = true;
                    bPause.setText("Play");
                }
            }
        });

        Button nextBtn = (Button) findViewById(R.id.next_button);
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                incrementLine();
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
                if (progressBar.getProgress() > 0) {
                    progressBar.setProgress(line * 100 / lines.size());
                }
            }
        });

        mTTS = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    int result = mTTS.setLanguage(Locale.US);
                    mTTS.setSpeechRate(0.7f);
                    if (result == TextToSpeech.LANG_MISSING_DATA
                            || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                        Log.e("TTS", "TTS language is not supported");
                    }
                } else {
                    Log.e("TTS", "TTS initialization failed");
                }
            }
        });
    }

    @Override
    public void onDestroy() {
        if (mTTS != null) {
            mTTS.stop();
            mTTS.shutdown();
        }
        super.onDestroy();
    }

    private TimerTask generateTask() {
        return new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        incrementLine();
                    }
                });
            }
        };
    }

    private void incrementLine() {
        if (line < lines.size()) {
            init(readLineTxt1, lines.get(line));
            line++;
            if (progressBar.getProgress() < 100) {
                progressBar.setProgress(line * 100 / lines.size());
            }
        } else {
            Intent intent = new Intent(ReadActivity.this, QuizActivity.class);
            intent.putExtra("id", id);
            intent.putExtra("story", story);
            startActivity(intent);
            finish();
            timerTask.cancel();
        }
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
                //Log.d("tapped on:", mWord);
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

    public void onSpeechClick(View v) {
        mTTS.speak(((TextView) cardView.findViewById(R.id.tv_def_title)).getText().toString(),
                TextToSpeech.QUEUE_FLUSH, null);
    }

    private void getDefinition(final String mWord) {
        RequestParams rp = new RequestParams();
        rp.add("word", mWord);
        NetworkClient.get("/definitions/get_definition", rp, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    String tip = response.getString("definition");
                    tvDefTitle.setText(Html.fromHtml("<u><i>" + mWord + "</i></u>"));
                    tvDefBody.setText(tip);
                    cardView.setVisibility(View.VISIBLE);

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
