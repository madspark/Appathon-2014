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

    private static final int SELECTION_ZERO = 0;
    private static final int SELECTION_ONE = 1;
    private static final int SELECTION_HORIZONTAL = 2;
    private static final int SELECTION_VERTICAL = 3;
    private static final int SELECTION_DIAGONAL = 4;

    private static final int initialColor = Color.parseColor("#939393");
    private static final int selectedColor = Color.parseColor("#3F51B5");

    private static final String TAG = "CrosswordGridAdapter";
    private Context mContext;
    private char[] mLetters;
    private boolean[] mCorrect;
    private TextView[] mViews;
    private Random mRand;
    private int mSelection;
    private int mPrevPosition;

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
            textView.setBackgroundColor(initialColor);
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

    public boolean checkCorrect() {
        mSelection = SELECTION_ZERO;
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
                    for (int i = 0; i < GRID_SIZE * GRID_SIZE; i++) {
                        mViews[i].setBackgroundColor(initialColor);
                    }
                }
            }, 500);
        }
        return correct;
    }

    public void select(int i) {
        if (mSelection == SELECTION_ZERO) {
            mSelection = SELECTION_ONE;
            setSelected(i);
        } else if (mSelection == SELECTION_ONE && !isSelected(i)) {
            if (getX(i) == getX(mPrevPosition)) {
                mSelection = SELECTION_HORIZONTAL;
                setSelected(i);
            } else if (getY(i) == getY(mPrevPosition)) {
                mSelection = SELECTION_VERTICAL;
                setSelected(i);
            } else {
                mSelection = SELECTION_DIAGONAL;
                setSelected(i);
            }
        } else if (mSelection == SELECTION_HORIZONTAL
                && getX(i) == getX(mPrevPosition)
                && (getY(i) == getY(mPrevPosition) + 1 || getY(i) == getY(mPrevPosition) - 1)) {
            setSelected(i);
        } else if (mSelection == SELECTION_VERTICAL
                && getY(i) == getY(mPrevPosition)
                && (getX(i) == getX(mPrevPosition) + 1 || getX(i) == getX(mPrevPosition) - 1)) {
            setSelected(i);
        } else if (mSelection == SELECTION_DIAGONAL
                && (getX(i) == getX(mPrevPosition) + 1 && getY(i) == getY(mPrevPosition) + 1
                || getX(i) == getX(mPrevPosition) - 1 && getY(i) == getY(mPrevPosition) - 1)) {
            setSelected(i);
        }
    }

    private void setSelected(int i) {
        mPrevPosition = i;
        mViews[i].setBackgroundColor(selectedColor);
    }

    private boolean isSelected(int i) {
        return ((ColorDrawable) mViews[i].getBackground()).getColor() == selectedColor;
    }

    private static int getX(int i) {
        return i / GRID_SIZE;
    }

    private static int getY(int i) {
        return i % GRID_SIZE;
    }
}
