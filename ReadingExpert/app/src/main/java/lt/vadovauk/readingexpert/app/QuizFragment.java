package lt.vadovauk.readingexpert.app;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

        Button checkButton = (Button) v.findViewById(R.id.check_button);
        checkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (answerEditText.getText().toString().toUpperCase().equals(mAnswer.toUpperCase())) {
                    mListener.onCorrect();
                } else {
                    Toast.makeText(getActivity(), R.string.incorrect, Toast.LENGTH_SHORT).show();
                }
            }
        });

        return v;
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
