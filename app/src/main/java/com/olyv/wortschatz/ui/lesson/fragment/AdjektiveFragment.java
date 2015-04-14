package com.olyv.wortschatz.ui.lesson.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.olyv.wortschatz.lesson.AnswerChecker;
import com.olyv.wortschatz.lesson.items.Adjektive;
import com.olyv.wortschatz.ui.R;
import com.olyv.wortschatz.ui.lesson.LessonActivity;

public class AdjektiveFragment extends Fragment
{
    //tags for flags
    public static final String IS_ANSWERED = "isItemAnswered";
    private static final String ANSWER = "answerWithComment";
    private static final String IS_CORRECT_ANSWER = "isAnswerCorrect";

    //UI components
    private TextView adjektiveView;
    private Button submitAnswerButton;
    private EditText translationText;
    private TextView correctAnswer;
    private TextView wrongAnswer;

    //flags and saved answer
    private boolean isItemAnswered = false;
    private boolean isCorrectAnswer = false;
    private String comment;

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);

        if (savedInstanceState != null)
        {
            comment = savedInstanceState.getString(ANSWER);
            isItemAnswered = savedInstanceState.getBoolean(IS_ANSWERED);

            if(isItemAnswered)
            {
                isCorrectAnswer = savedInstanceState.getBoolean(IS_CORRECT_ANSWER);

                setVisibleAnswer(isCorrectAnswer, comment);
            }
            else
            {
                translationText.setVisibility(View.VISIBLE);
                submitAnswerButton.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState)    //when rotated
    {
        super.onSaveInstanceState(outState);

        outState.putBoolean(IS_ANSWERED, isItemAnswered);
        outState.putString(ANSWER, comment);
        outState.putBoolean(IS_CORRECT_ANSWER, isCorrectAnswer);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.item_adjektiv, container, false);

        final Adjektive currentLessonItem = getArguments().getParcelable(LessonActivity.ADJEKTIVE_TAG);
        isItemAnswered = getArguments().getBoolean(IS_ANSWERED);

        adjektiveView = (TextView) rootView.findViewById(R.id.adjektive);
        submitAnswerButton = (Button) rootView.findViewById(R.id.submitAnswerBtn);
        translationText = (EditText) rootView.findViewById(R.id.enteredAdjeltiveTranslation);
        correctAnswer = (TextView) rootView.findViewById(R.id.correctTranslation);
        correctAnswer.setVisibility(View.GONE);
        wrongAnswer = (TextView) rootView.findViewById(R.id.inCorrectTranslation);
        wrongAnswer.setVisibility(View.GONE);

        adjektiveView.setText(((Adjektive) currentLessonItem).getWord().toString());

        if (!isItemAnswered)
        {
            submitAnswerButton.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    Adjektive answerAdjektive = new Adjektive().
                            setTranslation(translationText.getText().toString().trim());

                    isCorrectAnswer = AnswerChecker.isAdjektiveCorrect((Adjektive) currentLessonItem, answerAdjektive);

                    String correctAnswer = AnswerChecker.getStringAnswerFromAdjektive(currentLessonItem);
                    if (isCorrectAnswer)
                    {
                        comment = String.format(getString(R.string.correct_answer_assertion), correctAnswer);
                    }
                    else
                    {
                        String enteredAnswer = AnswerChecker.getStringAnswerFromAdjektive(answerAdjektive);

                        comment = String.format(getString(R.string.your_answer), enteredAnswer)
                                + String.format(getString(R.string.correct_answer), correctAnswer);
                    }

                    setVisibleAnswer(isCorrectAnswer, comment);

                    isItemAnswered = true;
                    LessonActivity.answeredItemsCounter ++;

                    ((LessonActivity)getActivity()).showResultsDialog();
                }
            });
        }
        return rootView;
    }

    //hides UI components and display answer
    private void setVisibleAnswer(boolean isCorrect, String answer)
    {
        if (isCorrect)
        {
            LessonActivity.correctAnswersCounter ++;
            correctAnswer.setVisibility(View.VISIBLE);
            correctAnswer.setText(answer);
        } else
        {
            wrongAnswer.setVisibility(View.VISIBLE);
            wrongAnswer.setText(answer);
        }
        submitAnswerButton.setVisibility(View.GONE);
        translationText.setVisibility(View.GONE);
    }
}