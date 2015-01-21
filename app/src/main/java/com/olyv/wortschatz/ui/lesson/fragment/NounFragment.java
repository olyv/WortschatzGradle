package com.olyv.wortschatz.ui.lesson.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import com.olyv.wortschatz.lesson.AnswerChecker;
import com.olyv.wortschatz.lesson.items.Noun;
import com.olyv.wortschatz.lesson.items.Verb;
import com.olyv.wortschatz.ui.R;
import com.olyv.wortschatz.ui.lesson.LessonActivity;

public class NounFragment extends Fragment
{
    private static final String LOG_TAG = "NounFragment";

    TextView nounView;
    TextView translationView;
    RadioGroup articleRadioGroup;
    Button submitAnswerButton;
    EditText pluralText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.item_noun, container, false);

        final Noun currentLessonItem = getArguments().getParcelable(LessonActivity.NOUN_TAG);

        nounView = (TextView) rootView.findViewById(R.id.noun);
        translationView = (TextView) rootView.findViewById(R.id.nounTranslation);
        articleRadioGroup = (RadioGroup) rootView.findViewById(R.id.articleSelect);
        submitAnswerButton = (Button) rootView.findViewById(R.id.submitAnswerBtn);
        pluralText = (EditText) rootView.findViewById(R.id.nounPlural);

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

                boolean correct = AnswerChecker.isNounCorrect((Noun) currentLessonItem, answerNoun);
                String comment = AnswerChecker.getNounComment(correct, (Noun) currentLessonItem, answerNoun);

                pluralText.setEnabled(false);
                pluralText.setSingleLine(false);

                if (correct)
                {
                    pluralText.setTextColor(Color.WHITE);
                    pluralText.setBackgroundResource(R.drawable.edit_text_correct);
                } else
                {
                    pluralText.setTextColor(Color.BLACK);
                    pluralText.setBackgroundResource(R.drawable.edit_text_error);
                }
                pluralText.setText(comment);

                submitAnswerButton.setVisibility(View.GONE);
                articleRadioGroup.setVisibility(View.GONE);

                Log.i(LOG_TAG, "Submit Noun button clicked initialized");
            }
        });

        return rootView;
    }
}