package lt.vadovauk.readingexpert.app;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;


public class QuizFragment extends Fragment {

    private static final String ARG_QUESTION = "question";
    private static final String ARG_ANSWERS = "answers";

    private String mQuestion;
    private ArrayList<String> mAnswers;

    private CrosswordFragment.OnResultListener mListener;

    public static QuizFragment newInstance(String question, ArrayList<String> answers) {
        QuizFragment fragment = new QuizFragment();
        Bundle args = new Bundle();
        args.putString(ARG_QUESTION, question);
        args.putStringArrayList(ARG_ANSWERS, answers);
        fragment.setArguments(args);
        return fragment;
    }

    public QuizFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mQuestion = getArguments().getString(ARG_QUESTION);
            mAnswers = getArguments().getStringArrayList(ARG_ANSWERS);
            for (int i = 0; i < mAnswers.size(); i++) {
                mAnswers.set(i, mAnswers.get(i).toUpperCase());
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_quiz, container, false);

        TextView questionTextView = (TextView) v.findViewById(R.id.question_text);
        questionTextView.setText(mQuestion);

        final EditText answerEditText = (EditText) v.findViewById(R.id.answer_edit);

        answerEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_DONE) {
                    checkClick();
                    return true;
                }
                return false;
            }
        });
        final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        answerEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, final boolean hasFocus) {
                answerEditText.post(new Runnable() {
                    @Override
                    public void run() {
                        if (hasFocus) {
                            imm.showSoftInput(answerEditText, 0);
                        } else {
                            imm.hideSoftInputFromWindow(answerEditText.getWindowToken(), 0);
                        }
                    }
                });
            }
        });
        answerEditText.requestFocus();

        Button checkButton = (Button) v.findViewById(R.id.check_button);
        checkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkClick();
            }
        });

        return v;
    }

    private void checkClick() {
        final EditText answerEditText = (EditText) getView().findViewById(R.id.answer_edit);

        boolean correct = mAnswers.contains(answerEditText.getText().toString().toUpperCase());
        mListener.playSound(correct);
        if (correct) {
            mListener.onCorrect();
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mListener = (CrosswordFragment.OnResultListener) activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
}
