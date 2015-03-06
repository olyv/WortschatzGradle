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
import com.olyv.wortschatz.lesson.items.Noun;
import com.olyv.wortschatz.ui.R;
import com.olyv.wortschatz.ui.lesson.LessonActivity;

public class NounFragment extends Fragment
{
    //tags for flags
    public static final String IS_ANSWERED = "isItemAnswered";
    private static final String CORRECT_ANSWER_VISIBLE = "isCorrectAnswerVisible";
    private static final String WRONG_ANSWER_VISIBLE = "isWrongAnswerVisible";
    private static final String SUBMIT_BUTTON_VISIBLE = "isButtonVisible";
    private static final String ARTICLE_VISIBLE = "isArticleRadioGroupVisible";
    private static final String ANSWER = "answerWithComment";
    private static final String IS_CORRECT_ANSWER = "isAnswerCorrect";

    //UI components
    private TextView nounView;
    private TextView translationView;
    private RadioGroup articleRadioGroup;
    private Button submitAnswerButton;
    private EditText pluralText;
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
                pluralText.setVisibility(View.VISIBLE);
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
        outState.putBoolean(SUBMIT_BUTTON_VISIBLE, pluralText.getVisibility() == View.VISIBLE);
        outState.putBoolean(ARTICLE_VISIBLE, articleRadioGroup.getVisibility() == View.VISIBLE);
        outState.putBoolean(IS_ANSWERED, isItemAnswered);
        outState.putString(ANSWER, comment);
        outState.putBoolean(IS_CORRECT_ANSWER, isCorrectAnswer);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.item_noun, container, false);

        final Noun currentLessonItem = getArguments().getParcelable(LessonActivity.NOUN_TAG);
        isItemAnswered = getArguments().getBoolean(IS_ANSWERED);

        nounView = (TextView) rootView.findViewById(R.id.noun);
        translationView = (TextView) rootView.findViewById(R.id.nounTranslation);
        articleRadioGroup = (RadioGroup) rootView.findViewById(R.id.articleSelect);
        submitAnswerButton = (Button) rootView.findViewById(R.id.submitAnswerBtn);
        pluralText = (EditText) rootView.findViewById(R.id.nounPlural);
        correctAnswer = (TextView) rootView.findViewById(R.id.correctPlural);
        correctAnswer.setVisibility(View.GONE);
        wrongAnswer = (TextView) rootView.findViewById(R.id.inCorrectPlural);
        wrongAnswer.setVisibility(View.GONE);

        nounView.setText(((Noun) currentLessonItem).getWord().toString());
        translationView.setText(((Noun) currentLessonItem).getTranslation().toString());

        submitAnswerButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String articleFromAnswer = null;

                switch (articleRadioGroup.getCheckedRadioButtonId())
                {
                    case R.id.articleDer:
                        articleFromAnswer = Noun.ARTICLE_DER;
                        break;
                    case R.id.articleDie:
                        articleFromAnswer = Noun.ARTICLE_DIE;
                        break;
                    case R.id.articleDas:
                        articleFromAnswer = Noun.ARTICLE_DAS;
                        break;
                }

                Noun answerNoun = new Noun()
                        .setArticle(articleFromAnswer)
                        .setPlural(pluralText.getText().toString().trim());

                isCorrectAnswer = AnswerChecker.isNounCorrect((Noun) currentLessonItem, answerNoun);

                String correctAnswer = currentLessonItem.getArticle() + " " + currentLessonItem.getPlural();
                if (isCorrectAnswer)
                {
                    comment = String.format(getString(R.string.correct_answer_assertion), correctAnswer);
                }
                else
                {
                    String enteredAnswer = answerNoun.getArticle() + " " + answerNoun.getPlural();

                    comment = String.format(getString(R.string.your_answer), enteredAnswer)
                            + String.format(getString(R.string.correct_answer), correctAnswer);
                }

                setVisibleAnswer(isCorrectAnswer, comment);

                isItemAnswered = true;
                LessonActivity.answeredItemsCounter ++;

                ((LessonActivity)getActivity()).showResultsDialog();
            }
        });
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
        articleRadioGroup.setVisibility(View.GONE);
        submitAnswerButton.setVisibility(View.GONE);
        pluralText.setVisibility(View.GONE);
    }
}