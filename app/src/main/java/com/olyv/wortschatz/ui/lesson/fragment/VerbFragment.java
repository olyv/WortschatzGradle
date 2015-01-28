package com.olyv.wortschatz.ui.lesson.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import com.olyv.wortschatz.lesson.AnswerChecker;
import com.olyv.wortschatz.lesson.items.Verb;
import com.olyv.wortschatz.ui.R;
import com.olyv.wortschatz.ui.lesson.LessonActivity;

public class VerbFragment extends Fragment
{
    //tags for flags
    public static final String IS_ANSWERED = "isItemAnswered";
    private static final String CORRECT_ANSWER_VISIBLE = "isCorrectAnswerVisible";
    private static final String WRONG_ANSWER_VISIBLE = "isWrongAnswerVisible";
    private static final String SUBMIT_BUTTON_VISIBLE = "isButtonVisible";
    private static final String AUXVERB_VISIBLE = "isArticleRadioGroupVisible";
    private static final String ANSWER = "answerWithComment";
    private static final String IS_CORRECT_ANSWER = "isAnswerCorrect";

    //UI components
    private TextView verbView;
    private TextView translationView;
    private RadioGroup auxVerbRadioGroup;
    private Button submitAnswerButton;
    private EditText partizipText;
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
                partizipText.setVisibility(View.VISIBLE);
                submitAnswerButton.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState)    //when rotated
    {
        super.onSaveInstanceState(outState);

        outState.putBoolean(CORRECT_ANSWER_VISIBLE, correctAnswer.getVisibility() == View.VISIBLE);
        outState.putBoolean(WRONG_ANSWER_VISIBLE, wrongAnswer.getVisibility() == View.VISIBLE);
        outState.putBoolean(SUBMIT_BUTTON_VISIBLE, submitAnswerButton.getVisibility() == View.VISIBLE);
        outState.putBoolean(SUBMIT_BUTTON_VISIBLE, partizipText.getVisibility() == View.VISIBLE);
        outState.putBoolean(AUXVERB_VISIBLE, auxVerbRadioGroup.getVisibility() == View.VISIBLE);
        outState.putBoolean(IS_ANSWERED, isItemAnswered);
        outState.putString(ANSWER, comment);
        outState.putBoolean(IS_CORRECT_ANSWER, isCorrectAnswer);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.item_verb, container, false);

        final Verb currentLessonItem = getArguments().getParcelable(LessonActivity.VERB_TAG);

        verbView = (TextView) rootView.findViewById(R.id.verb);
        translationView = (TextView) rootView.findViewById(R.id.verbTranslation);
        auxVerbRadioGroup = (RadioGroup) rootView.findViewById(R.id.auxverbSelect);
        submitAnswerButton = (Button) rootView.findViewById(R.id.submitAnswerBtn);
        partizipText = (EditText) rootView.findViewById(R.id.enteredVerbPartizip);
        correctAnswer = (TextView) rootView.findViewById(R.id.correctPartizip);
        correctAnswer.setVisibility(View.GONE);
        wrongAnswer = (TextView) rootView.findViewById(R.id.inCorrectPartizip);
        wrongAnswer.setVisibility(View.GONE);

        verbView.setText(((Verb) currentLessonItem).getWord().toString());
        translationView.setText(((Verb) currentLessonItem).getTranslation().toString());

        submitAnswerButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String auxverbFromAnswer = null;

                switch (auxVerbRadioGroup.getCheckedRadioButtonId())
                {
                    case R.id.auxverbHat:
                        auxverbFromAnswer = Verb.AUXVERB_HAT;
                        break;
                    case R.id.auxverbIst:
                        auxverbFromAnswer = Verb.AUXVERB_IST;
                        break;
                }

                Verb answerVerb = new Verb()
                        .setAuxverb(auxverbFromAnswer)
                        .setPartizip(partizipText.getText().toString().trim());

                isCorrectAnswer = AnswerChecker.isVerbCorrect((Verb) currentLessonItem, answerVerb);
                comment = AnswerChecker.getVerbComment(isCorrectAnswer, (Verb) currentLessonItem, answerVerb);

                setVisibleAnswer(isCorrectAnswer, comment);

                isItemAnswered = true;
                LessonActivity.answeredItemsCounter ++;

                ((LessonActivity)getActivity()).showResultsDialog();
            }
        });
        return rootView;
    }

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
        auxVerbRadioGroup.setVisibility(View.GONE);
        submitAnswerButton.setVisibility(View.GONE);
        partizipText.setVisibility(View.GONE);
    }
}