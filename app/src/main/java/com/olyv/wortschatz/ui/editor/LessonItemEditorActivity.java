package com.olyv.wortschatz.ui.editor;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import com.olyv.wortschatz.lesson.DatabaseHelper;
import com.olyv.wortschatz.lesson.LessonItemsHelper;
import com.olyv.wortschatz.lesson.items.Adjektive;
import com.olyv.wortschatz.lesson.items.LessonItemI;
import com.olyv.wortschatz.lesson.items.Noun;
import com.olyv.wortschatz.lesson.items.Verb;
import com.olyv.wortschatz.ui.R;
import com.olyv.wortschatz.ui.manager.LessonItemsManagerActivity;

public class LessonItemEditorActivity extends BaseEditor
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        Bundle extras = getIntent().getExtras();
        final LessonItemI item = extras.getParcelable(LessonItemsManagerActivity.EDITED_ITEM);

        if (item != null)
        {
            if (item instanceof Verb)
            {
                setContentView(R.layout.editor_item_verb);

                super.initializeVerbEditorUI();

                verb.setText(item.getWord());
                partizip.setText(((Verb) item).getPartizip());
                translation.setText(item.getTranslation());
                if (((Verb) item).getAuxverb().equals(Verb.AUXVERB_IST)) {
                    auxverb.check(R.id.auxverbIst);
                }
                else
                {
                    auxverb.check(R.id.auxverbHat);
                }

                save.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        Verb editedVerb = getEnteredVerb((Verb) item);

                        UpdateItemTask task = new UpdateItemTask();
                        task.execute(editedVerb);
                        setResult(RESULT_OK);
                        finish();
                    }
                });
            }
            if (item instanceof Noun)
            {
                setContentView(R.layout.editor_item_noun);

                super.initializeNounEditorUI();

                noun.setText(item.getWord());
                plural.setText(((Noun) item).getPlural());
                translation.setText(item.getTranslation());
                if (((Noun) item).getArticle().equals(Noun.ARTICLE_DAS))
                {
                    article.check(R.id.articleDas);
                }
                else if (((Noun) item).getArticle().equals(Noun.ARTICLE_DER))
                {
                    article.check(R.id.articleDer);
                }
                else if (((Noun) item).getArticle().equals(Noun.ARTICLE_DIE))
                {
                    article.check(R.id.articleDie);
                }

                getSelectedArticle();

                save.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        Noun editedNoun = getEnteredNoun((Noun) item);

                        UpdateItemTask task = new UpdateItemTask();
                        task.execute(editedNoun);
                        setResult(RESULT_OK);
                        finish();
                    }
                });
            }
            if (item instanceof Adjektive)
            {
                setContentView(R.layout.editor_item_adjektiv);

                initializeAdjektiveUI();

                adjektive.setText(item.getWord());
                translation.setText(item.getTranslation());

                save.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        Adjektive editedAdjektive = getEnteredAdjektive((Adjektive) item);

                        UpdateItemTask task = new UpdateItemTask();
                        task.execute(editedAdjektive);
                        setResult(RESULT_OK);
                        finish();
                    }
                });
            }
        }
    }

    private class UpdateItemTask extends AsyncTask<LessonItemI, Integer, Void>
    {
        private LessonItemsHelper lessonHelper;

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(LessonItemI... params)
        {
            if (databaseHelper == null)
            {
                databaseHelper = new DatabaseHelper(getApplicationContext());
            }

            LessonItemI item = params[0];    //http://stackoverflow.com/questions/17549042/android-asynctask-passing-a-single-string
            lessonHelper = new LessonItemsHelper();
            if (item instanceof Verb)
            {
                lessonHelper.updateVerb(databaseHelper, (Verb) item);
            }
            if (item instanceof Noun)
            {
                lessonHelper.updateNoun(databaseHelper, (Noun) item);
            }
            if (item instanceof Adjektive)
            {
                lessonHelper.updateAdjektive(databaseHelper, (Adjektive) item);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result)
        {
            super.onPostExecute(result);
        }
    }
}