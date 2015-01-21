package com.olyv.wortschatz.ui.editor;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;
import com.olyv.wortschatz.lesson.DatabaseHelper;
import com.olyv.wortschatz.lesson.LessonItemsHelper;
import com.olyv.wortschatz.lesson.items.Adjektive;
import com.olyv.wortschatz.lesson.items.LessonItemI;
import com.olyv.wortschatz.lesson.items.Noun;
import com.olyv.wortschatz.lesson.items.Verb;
import com.olyv.wortschatz.ui.R;

import java.util.ArrayList;

public class AddNewItemActivity extends BaseEditor
{
    private LinearLayout verbEditor;
    private LinearLayout nounEditor;
    private LinearLayout adjektiveEditor;
    private Spinner selectType;

    private String verbType;
    private String nounType;
    private String adjektiveType;

    public static final String LOG_TAG = "AddNewItemActivityLog";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        verbType = getString(R.string.verb_item);
        nounType = getString(R.string.noun_item);
        adjektiveType = getString(R.string.adjektive_item);

        setContentView(R.layout.add_word);

        verbEditor = (LinearLayout) findViewById(R.id.editorVerb);
        nounEditor = (LinearLayout) findViewById(R.id.editorNoun);
        adjektiveEditor = (LinearLayout) findViewById(R.id.editorAdjektive);
        selectType = (Spinner) findViewById(R.id.selectTypeSpinner);

        ArrayList<String> spinnerArray = new ArrayList<String>();
        spinnerArray.add(verbType);
        spinnerArray.add(nounType);
        spinnerArray.add(adjektiveType);

        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this, R.layout.spinner_item, spinnerArray);
        selectType.setAdapter(spinnerArrayAdapter);

        selectType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                String selectedType = selectType.getSelectedItem().toString();
                if (selectedType.equals(verbType))
                {
                    verbEditor.setVisibility(View.VISIBLE);
                    nounEditor.setVisibility(View.GONE);
                    adjektiveEditor.setVisibility(View.GONE);

                    initializeVerbEditorUI();
                    save = (Button) verbEditor.findViewById(R.id.saveBtn);
                    save.setOnClickListener(new View.OnClickListener()
                    {
                        @Override
                        public void onClick(View v)
                        {
                            Verb newVerb = getEnteredVerb(new Verb());

                            if (newVerb != null)
                            {
                                Log.i(LOG_TAG, "inserting new verb " + verb.getText());
                                InsertItemTask task = new InsertItemTask();
                                task.execute(newVerb);
                                finish();
                            }
                        }
                    });

                }
                else if (selectedType.equals(nounType))
                {
                    verbEditor.setVisibility(View.GONE);
                    nounEditor.setVisibility(View.VISIBLE);
                    adjektiveEditor.setVisibility(View.GONE);

                    initializeNounEditorUI();
                    save = (Button) nounEditor.findViewById(R.id.saveBtn);
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
                                finish();
                            }
                        }
                    });
                }
                else if (selectedType.equals(adjektiveType))
                {
                    verbEditor.setVisibility(View.GONE);
                    nounEditor.setVisibility(View.GONE);
                    adjektiveEditor.setVisibility(View.VISIBLE);

                    initializeAdjektiveUI();
                    save = (Button) adjektiveEditor.findViewById(R.id.saveBtn);
                    save.setOnClickListener(new View.OnClickListener()
                    {
                        @Override
                        public void onClick(View v)
                        {
                            Adjektive newAdjektive = getEnteredAdjektive(new Adjektive());

                            if (newAdjektive != null)
                            {
                                Log.i(LOG_TAG, "inserting new Adjektive " + adjektive.getText());
                                InsertItemTask task = new InsertItemTask();
                                task.execute(newAdjektive);
                                finish();
                            }
                        }
                    });
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {
                // TODO Auto-generated method stub
            }
        });
    }

    private class InsertItemTask extends AsyncTask<LessonItemI, Integer, Void>
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
                lessonHelper.insertNewVerb(databaseHelper, (Verb) item);
            }
            if (item instanceof Noun)
            {
                lessonHelper.insertNewNoun(databaseHelper, (Noun) item);
            }
            if (item instanceof Adjektive)
            {
                lessonHelper.insertNewAdjektive(databaseHelper, (Adjektive) item);
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
