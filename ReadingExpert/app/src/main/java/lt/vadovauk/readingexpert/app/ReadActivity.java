package lt.vadovauk.readingexpert.app;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

import lt.vadovauk.readingexpert.app.helper.DataHelper;

public class ReadActivity extends Activity {
    String content = "Once upon a time there was a lion who ...";
    int line = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read);

        final TextView readLineTxt = (TextView) findViewById(R.id.read_line_txt);

        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        readLineTxt.setText(DataHelper.getLine(line, content));
                    }
                });
            }
        }, 0, 1000);
    }
}
