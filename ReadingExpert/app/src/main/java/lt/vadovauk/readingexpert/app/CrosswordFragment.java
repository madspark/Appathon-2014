package lt.vadovauk.readingexpert.app;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;


public class CrosswordFragment extends Fragment {

    private static final String ARG_QUESTION = "question";
    private static final String ARG_ANSWER = "answer";

    private String mQuestion;
    private String mAnswer;

    private OnCorrectListener mListener;

    public static CrosswordFragment newInstance(String question, String answer) {
        CrosswordFragment fragment = new CrosswordFragment();
        Bundle args = new Bundle();
        args.putString(ARG_QUESTION, question);
        args.putString(ARG_ANSWER, answer);
        fragment.setArguments(args);
        return fragment;
    }

    public CrosswordFragment() {
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
        View v = inflater.inflate(R.layout.fragment_crossword, container, false);

        final Button nextButton = (Button) v.findViewById(R.id.next_button);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onCorrect();
            }
        });

        TextView questionView = (TextView) v.findViewById(R.id.question_text_view);
        questionView.setText(mQuestion);

        final GridView grid = (GridView) v.findViewById(R.id.crossword_grid);
        final CrosswordGridAdapter adapter = new CrosswordGridAdapter(getActivity(), mAnswer.toUpperCase());
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
                        Toast.makeText(getActivity(), R.string.correct, Toast.LENGTH_SHORT).show();
                        nextButton.setVisibility(Button.VISIBLE);
                    } else {
                        Toast.makeText(getActivity(), R.string.incorrect, Toast.LENGTH_SHORT).show();
                    }
                }
                return true;
            }
        });

        return v;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mListener = (OnCorrectListener) activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnCorrectListener {
        public void onCorrect();
    }

}
