package com.olyv.wortschatz.ui.editor;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;

import com.olyv.wortschatz.lesson.items.Noun;
import com.olyv.wortschatz.ui.R;

public class NounEditorFragment extends BaseFragment
{
    private static final String LOG_TAG = "NounEditorFragment";
    private EditText noun;
    private Button save;
    private EditText plural;
    private RadioGroup article;
    private EditText translation;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.noun_editor_fragment, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);

        noun = (EditText) getActivity().findViewById(R.id.noun);
        plural = (EditText) getActivity().findViewById(R.id.nounPlural);
        article = (RadioGroup) getActivity().findViewById(R.id.articleSelect);
        translation = (EditText) getActivity().findViewById(R.id.nounTranslation);
        save = (Button) getActivity().findViewById(R.id.saveBtn);

        save.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Noun newNoun = getEnteredNoun(new Noun());

                if (newNoun != null)
                {
                    Log.i(LOG_TAG, "inserting new noun " + noun.getText());
                    InsertItemTask task = new InsertItemTask();
                    task.execute(newNoun);
                    getActivity().finish();
                }
            }
        });
    }

    private Noun getEnteredNoun(Noun nounItem)
    {
        if (isEmptyField(noun))
        {
            noun.setError(getString(R.string.enter_noun_hint));
            return null;
        }
        else
        {
            nounItem.setWord(noun.getText().toString().trim());
        }
        if (isEmptyField(translation))
        {
            translation.setError(getString(R.string.enter_translation_hint));
            return null;
        }
        else
        {
            nounItem.setTranslation(translation.getText().toString().trim());
        }
        if (isEmptyField(plural))
        {
            plural.setError(getString(R.string.noun_plural_hint));
        }
        else
        {
            nounItem.setPlural(plural.getText().toString().trim());
        }
        nounItem.setArticle(getSelectedArticle());

        return nounItem;
    }

    private String getSelectedArticle()
    {
            String selectedArticle = null;
        switch (article.getCheckedRadioButtonId())
        {
            case R.id.articleDer:
                selectedArticle = Noun.ARTICLE_DER;
                break;
            case R.id.articleDie:
                selectedArticle = Noun.ARTICLE_DIE;
                break;
            case R.id.articleDas:
                selectedArticle = Noun.ARTICLE_DAS;
                break;
        }
        return selectedArticle;
    }
}
