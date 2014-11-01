package lt.vadovauk.readingexpert.app;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class CrosswordGridAdapter extends BaseAdapter {

    public static final int GRID_SIZE = 6;
    private static final int DIRECTION_HORIZONTAL = 0;
    private static final int DIRECTION_VERTICAL = 1;
    private static final int DIRECTION_DIAGONAL_LOWER = 2;
    private static final int DIRECTION_DIAGONAL_UPPER = 3;
    private static String TAG = "CrosswordGridAdapter";

    private Context mContext;
    private char[] mLetters;
    private boolean[] mCorrect;
    private TextView[] mViews;
    private Random mRand;

    public CrosswordGridAdapter(Context context, String word) {
        mContext = context;

        if (word.length() > GRID_SIZE) {
            Log.e(TAG, "Word length greater than crossword grid size");
        }

        mLetters = new char[GRID_SIZE * GRID_SIZE];
        mCorrect = new boolean[GRID_SIZE * GRID_SIZE];
        mViews = new TextView[GRID_SIZE * GRID_SIZE];

        mRand = new Random();
        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                setLetter(i, j, (char) ('A' + mRand.nextInt(26)), false);
            }
        }

        int direction = mRand.nextInt(4);
        if (direction == DIRECTION_HORIZONTAL) {
            int k = mRand.nextInt(GRID_SIZE);
            int start;
            if (GRID_SIZE - word.length() == 0) {
                start = 0;
            } else {
                start = mRand.nextInt(GRID_SIZE - word.length());
            }
            for (int i = 0; i < word.length(); i++) {
                setLetter(k, i + start, word.charAt(i), true);
            }
        } else if (direction == DIRECTION_VERTICAL) {
            int k = mRand.nextInt(GRID_SIZE);
            int start;
            if (GRID_SIZE - word.length() == 0) {
                start = 0;
            } else {
                start = mRand.nextInt(GRID_SIZE - word.length());
            }
            for (int i = 0; i < word.length(); i++) {
                setLetter(i + start, k, word.charAt(i), true);
            }
        } else if (direction == DIRECTION_DIAGONAL_LOWER) {
            int diagonal = mRand.nextInt(GRID_SIZE - word.length() + 1);
            int start = mRand.nextInt(GRID_SIZE - diagonal - word.length() + 1);
            for (int i = 0; i < word.length(); i++) {
                setLetter(start + i, diagonal + i, word.charAt(i), true);
            }
        } else if (direction == DIRECTION_DIAGONAL_UPPER) {
            int diagonal = mRand.nextInt(GRID_SIZE - word.length() + 1);
            int start = mRand.nextInt(GRID_SIZE - diagonal - word.length() + 1);
            for (int i = 0; i < word.length(); i++) {
                setLetter(diagonal + i, start + i, word.charAt(i), true);
            }
        }

        for (int i = 0; i < GRID_SIZE * GRID_SIZE; i++) {
            TextView textView = new TextView(mContext);
            textView.setHeight(150);
            textView.setTextSize(40);
            textView.setGravity(Gravity.CENTER);
            textView.setBackgroundColor(Color.BLUE);
            textView.setText(String.valueOf(mLetters[i]));
            mViews[i] = textView;
        }
    }

    private void setLetter(int i, int j, char letter, boolean correct) {
        int index = i * GRID_SIZE + j;
        mLetters[index] = letter;
        mCorrect[index] = correct;
    }

    @Override
    public int getCount() {
        return mLetters.length;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView textView;
        if (convertView == null) {  // if it's not recycled, initialize some attributes
            textView = mViews[position];
        } else {
            textView = (TextView) convertView;
        }
        return textView;
    }

    public TextView getTextView(int position) {
        return mViews[position];
    }

    public void checkCorrect() {
        boolean correct = true;
        for (int i = 0; i < GRID_SIZE * GRID_SIZE; i++) {
            TextView textView = (TextView) mViews[i];
            if (mCorrect[i] != (((ColorDrawable) textView.getBackground()).getColor() == Color.GREEN)) {
                correct = false;
            }
        }
        if (correct) {
            Toast.makeText(mContext, "Correct!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(mContext, "Incorrect!", Toast.LENGTH_SHORT).show();
        }
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < GRID_SIZE * GRID_SIZE; i++) {
                    mViews[i].setBackgroundColor(Color.BLUE);
                }
            }
        }, 1000);
    }
}
