package com.olyv.wortschatz.ui.editor;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import com.olyv.wortschatz.lesson.DatabaseHelper;
import com.olyv.wortschatz.lesson.items.Adjektive;
import com.olyv.wortschatz.lesson.items.Noun;
import com.olyv.wortschatz.lesson.items.Verb;
import com.olyv.wortschatz.ui.R;

public class BaseEditor extends Activity
{
    protected DatabaseHelper databaseHelper = null;

    //verb UI elements and fields
    protected EditText verb;
    protected EditText partizip;
    protected RadioGroup auxverb;

    protected String selectedAuxVerb;

    //noun UI elements
    protected EditText noun;
    protected EditText plural;
    protected RadioGroup article;

    //adjektive UI elements
    protected EditText adjektive;

    // common UI elements
    protected Button save;
    protected EditText translation;

    protected String selectedArticle;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    protected void initializeVerbEditorUI()
    {
        verb = (EditText) findViewById(R.id.verb);
        partizip = (EditText) findViewById(R.id.verbPartizip);
        auxverb = (RadioGroup) findViewById(R.id.auxverbSelect);
        translation = (EditText) findViewById(R.id.verbTranslation);
        save = (Button) findViewById(R.id.saveBtn);
    }

    protected void initializeNounEditorUI()
    {
        noun = (EditText) findViewById(R.id.noun);
        plural = (EditText) findViewById(R.id.nounPlural);
        article = (RadioGroup) findViewById(R.id.articleSelect);
        translation = (EditText) findViewById(R.id.nounTranslation);
        save = (Button) findViewById(R.id.saveBtn);
    }

    protected void initializeAdjektiveUI()
    {
        adjektive = (EditText) findViewById(R.id.adjektive);
        translation = (EditText) findViewById(R.id.adjektiveTranslation);
        save = (Button) findViewById(R.id.saveBtn);
    }

    private String getSelectedAuxVerb()
    {
        switch (auxverb.getCheckedRadioButtonId())
        {
            case R.id.auxverbHat:
                selectedAuxVerb = Verb.AUXVERB_HAT;
                break;
            case R.id.auxverbIst:
                selectedAuxVerb = Verb.AUXVERB_IST;
                break;
        }
        return selectedAuxVerb;
    }

    protected Verb getEnteredVerb(Verb verbItem)
    {
        if (isEmptyField(verb))
        {
            verb.setError(getString(R.string.enter_verb_hint));
            return null;
        }
        else
        {
            verbItem.setWord(verb.getText().toString().trim());
        }
        if (isEmptyField(translation))
        {
            translation.setError(getString(R.string.enter_translation_hint));
            return null;
        }
        else
        {
            verbItem.setTranslation(translation.getText().toString().trim());
        }
        if (isEmptyField(partizip))
        {
            partizip.setError(getString(R.string.verb_partizip_hint));
            return null;
        }
        else
        {
            verbItem.setPartizip(partizip.getText().toString().trim());
        }
        verbItem.setAuxverb(getSelectedAuxVerb());

        return verbItem;
    }

    //noun helper methods
    protected String getSelectedArticle()
    {
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

    protected Noun getEnteredNoun(Noun nounItem)
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

    //adjektive helpers methods
    protected Adjektive getEnteredAdjektive(Adjektive adjektiveItem)
    {
        if (isEmptyField(adjektive))
        {
            adjektive.setError(getString(R.string.enter_adjektive_hint));
            return null;
        }
        else
        {
            adjektiveItem.setWord(adjektive.getText().toString().trim());
        }
        if (isEmptyField(translation))
        {
            translation.setError(getString(R.string.enter_translation_hint));
            return null;
        }
        else
        {
            adjektiveItem.setTranslation(translation.getText().toString().trim());
        }
        return adjektiveItem;
    }

    private boolean isEmptyField(EditText field)
    {
        return field.getText().toString().trim().matches("");
    }
}