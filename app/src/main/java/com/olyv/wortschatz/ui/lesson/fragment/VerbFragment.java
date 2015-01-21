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
import com.olyv.wortschatz.lesson.items.Verb;
import com.olyv.wortschatz.ui.R;
import com.olyv.wortschatz.ui.lesson.LessonActivity;

public class VerbFragment extends Fragment
{
    public static final String LOG_TAG = "VerbFragment";

    TextView verbView;
    TextView translationView;
    RadioGroup auxVerbRadioGroup;
    Button submitAnswerButton;
    EditText partizipText;

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

        Log.i(LOG_TAG, "Verb UI initialized");

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

                boolean correct = AnswerChecker.isVerbCorrect((Verb) currentLessonItem, answerVerb);
                String comment = AnswerChecker.getVerbComment(correct, (Verb) currentLessonItem, answerVerb);

                partizipText.setEnabled(false);
                partizipText.setSingleLine(false);

                if (correct)
                {
                    partizipText.setTextColor(Color.WHITE);
                    partizipText.setBackgroundResource(R.drawable.edit_text_correct);
                } else
                {
                    partizipText.setTextColor(Color.BLACK);
                    partizipText.setBackgroundResource(R.drawable.edit_text_error);
                }
                partizipText.setText(comment);

                submitAnswerButton.setVisibility(View.GONE);
                auxVerbRadioGroup.setVisibility(View.GONE);

                Log.i(LOG_TAG, "Submit Verb button clicked initialized");
            }
        });

        return rootView;
    }
}