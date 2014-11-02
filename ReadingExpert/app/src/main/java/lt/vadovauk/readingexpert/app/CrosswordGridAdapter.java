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

import java.util.Random;

public class CrosswordGridAdapter extends BaseAdapter {

    public static final int GRID_SIZE = 6;

    private static final int DIRECTION_HORIZONTAL = 0;
    private static final int DIRECTION_VERTICAL = 1;
    private static final int DIRECTION_DIAGONAL_LOWER = 2;
    private static final int DIRECTION_DIAGONAL_UPPER = 3;

    private static final int initialColor = Color.parseColor("#8591D5");
    private static final int selectedColor = Color.parseColor("#3F51B5");

    private static final String TAG = "CrosswordGridAdapter";
    private Context mContext;
    private char[] mLetters;
    private boolean[] mCorrect;
    private TextView[] mViews;
    private Random mRand;
    private int mInitX;
    private int mInitY;

    public CrosswordGridAdapter(Context context, String word) {
        mContext = context;

        if (word.length() > GRID_SIZE) {
            Log.e(TAG, "Word length greater than crossword grid size");
        }

        mLetters = new char[GRID_SIZE * GRID_SIZE];
        mCorrect = new boolean[GRID_SIZE * GRID_SIZE];
        mViews = new TextView[GRID_SIZE * GRID_SIZE];
        mInitX = -1;

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
            //textView.setHeight(150);
            textView.setTextSize(40);
            textView.setGravity(Gravity.CENTER);
            textView.setBackgroundColor(initialColor);
            textView.setTextColor(Color.WHITE);
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
        return mViews[position];
    }

    public boolean checkCorrect() {
        mInitX = -1;
        boolean correct = true;
        for (int i = 0; i < GRID_SIZE * GRID_SIZE; i++) {
            if (mCorrect[i] != isSelected(i)) {
                correct = false;
            }
        }
        if (!correct) {
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    clearSelected();
                }
            }, 500);
        }
        return correct;
    }

    public void select(int pos) {
        int x = pos / GRID_SIZE;
        int y = pos % GRID_SIZE;

        if (mInitX == -1) {
            mInitX = x;
            mInitY = y;
            setSelected(x, y);
            return;
        }

        for (int j = 0; j < GRID_SIZE; j++) {
            if (x == mInitX + j && y == mInitY) {
                clearSelected();
                for (int k = 0; k <= j; k++) {
                    setSelected(mInitX + k, y);
                }
                return;
            }
            if (x == mInitX && y == mInitY + j) {
                clearSelected();
                for (int k = 0; k <= j; k++) {
                    setSelected(x, mInitY + k);
                }
                return;
            }
            if (x == mInitX + j && y == mInitY + j) {
                clearSelected();
                for (int k = 0; k <= j; k++) {
                    setSelected(mInitX + k, mInitY + k);
                }
                return;
            }
        }

        setSelected(x, y);
    }

    private void setSelected(int x, int y) {
        mViews[GRID_SIZE * x + y].setBackgroundColor(selectedColor);
    }

    private void clearSelected() {
        for (int i = 0; i < GRID_SIZE * GRID_SIZE; i++) {
            mViews[i].setBackgroundColor(initialColor);
        }
    }

    private boolean isSelected(int i) {
        return ((ColorDrawable) mViews[i].getBackground()).getColor() == selectedColor;
    }
}
