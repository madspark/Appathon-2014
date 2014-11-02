package lt.vadovauk.readingexpert.app;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.*;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class QuizFragment extends Fragment {

    private static final String ARG_QUESTION = "question";
    private static final String ARG_ANSWER = "answer";

    private String mQuestion;
    private String mAnswer;

    private CrosswordFragment.OnCorrectListener mListener;

    public static QuizFragment newInstance(String question, String answer) {
        QuizFragment fragment = new QuizFragment();
        Bundle args = new Bundle();
        args.putString(ARG_QUESTION, question);
        args.putString(ARG_ANSWER, answer);
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
            mAnswer = getArguments().getString(ARG_ANSWER);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_quiz, container, false);

        TextView questionTextView = (TextView) v.findViewById(R.id.question_text);
        questionTextView.setText(mQuestion);

        final EditText answerEditText = (EditText) v.findViewById(R.id.answer_edit);
        final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        answerEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_DONE) {
                    checkClick();
                }
                return false;
            }
        });
        answerEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, final boolean hasFocus) {
                answerEditText.post(new Runnable() {
                    @Override
                    public void run() {
                        if (hasFocus) {
                            imm.showSoftInput(answerEditText, InputMethodManager.SHOW_IMPLICIT);
                        } else {
                            imm.hideSoftInputFromWindow(answerEditText.getWindowToken(), 0);
                        }
                    }
                });
            }
        });

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
        if (answerEditText.getText().toString().toUpperCase().equals(mAnswer.toUpperCase())) {
            Toast.makeText(getActivity(), R.string.correct, Toast.LENGTH_SHORT).show();
            mListener.onCorrect();
        } else {
            Toast.makeText(getActivity(), R.string.incorrect, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mListener = (CrosswordFragment.OnCorrectListener) activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
}
