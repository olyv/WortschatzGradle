package com.olyv.wortschatz.ui.editor.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;

import com.olyv.wortschatz.lesson.items.LessonItemI;
import com.olyv.wortschatz.lesson.items.Noun;
import com.olyv.wortschatz.lesson.items.Verb;
import com.olyv.wortschatz.ui.R;
import com.olyv.wortschatz.ui.manager.LessonItemsManagerActivity;

public class NounEditorFragment extends BaseFragment
{
    private static final String LOG_TAG = "NounEditorFragment";
    private EditText noun;
    private Button save;
    private EditText plural;
    private RadioGroup article;
    private EditText translation;
    private LessonItemI nounToEdit;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {

        try
        {
            nounToEdit = getArguments().getParcelable(LessonItemsManagerActivity.EDITED_ITEM);
        }
        catch (NullPointerException e)
        {
            Log.i(LOG_TAG, "fragment is in Add New Item Mode");
        }

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

        if (nounToEdit != null)
        {
            noun.setText(nounToEdit.getWord());
            plural.setText(((Noun) nounToEdit).getPlural());
            translation.setText(nounToEdit.getTranslation());
            if (((Noun) nounToEdit).getArticle().equals(Noun.ARTICLE_DAS))
            {
                article.check(R.id.articleDas);
            }
            else if (((Noun) nounToEdit).getArticle().equals(Noun.ARTICLE_DER))
            {
                article.check(R.id.articleDer);
            }
            else if (((Noun) nounToEdit).getArticle().equals(Noun.ARTICLE_DIE))
            {
                article.check(R.id.articleDie);
            }
        }

        save.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Noun newNoun = (nounToEdit != null)? getEnteredNoun(new Noun()) : getEnteredNoun((Noun) nounToEdit);

                if (newNoun != null)
                {
                    if (nounToEdit != null)
                    {
                        UpdateItemTask task = new UpdateItemTask();
                        task.execute(newNoun);
                        getActivity().setResult(getActivity().RESULT_OK);
                        Log.i(LOG_TAG, "updating noun " + newNoun.getWord());
                    }
                    else
                    {
                        InsertItemTask task = new InsertItemTask();
                        task.execute(newNoun);
                        Log.i(LOG_TAG, "inserting new noun " + newNoun.getWord());
                    }
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
