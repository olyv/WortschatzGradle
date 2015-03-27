package com.olyv.wortschatz.ui.editor;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Uri;
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
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

public class AddNewItemActivity extends BaseEditor
{
    private LinearLayout verbEditor;
    private LinearLayout nounEditor;
    private LinearLayout adjektiveEditor;
    private Spinner selectType;

    private String verbType;
    private String nounType;
    private String adjektiveType;

    private static final String LOG_TAG = "AddNewItemActivityLog";

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

        ArrayList<String> wordTypeSpinnerArray = new ArrayList<String>();
        wordTypeSpinnerArray.add(verbType);
        wordTypeSpinnerArray.add(nounType);
        wordTypeSpinnerArray.add(adjektiveType);

        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this, R.layout.spinner_item, wordTypeSpinnerArray);
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

        initializeTranslator();

        translateButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //check if user has internet connection
                ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                if ((cm.getActiveNetworkInfo() == null))
                {
                    translatedWord.setText(getString(R.string.check_connection_message));
                    return;
                }

                String result;
                try
                {
                    String targetLanguage = Locale.getDefault().getLanguage();

                    Log.e(LOG_TAG, "language is " + targetLanguage);

                    if (!targetLanguage.equals("ru") && !targetLanguage.equals("ua"))
                    {
                        targetLanguage = "en";
                    }

                    if ( !isEmptyField(wordForTranslation) )
                    {
                        String wordToTranslate = wordForTranslation.getText().toString().trim();
                        result = new TranslatorTask().execute("de", targetLanguage, wordToTranslate).get();

                        if (result == null)
                        {
                            result = getApplicationContext().getString(R.string.no_results_from_search);
                        }

                        translatedWord.setText(result);
                    }
                }
                catch (InterruptedException e)
                {
                    Log.e(LOG_TAG, "InterruptedException while executing AsyncTask");
                }
                catch (ExecutionException e)
                {
                    Log.e(LOG_TAG, "ExecutionException while executing AsyncTask");
                }
            }
        });
    }

    private class TranslatorTask extends AsyncTask<String, Void, String>
    {
        private static final String LOG_TAG = "TranslateWordTask";

        private String getTranslation(String json) throws JSONException {
            //translate("de", "ru", "Hund", "json");
            JSONObject object = new JSONObject(json);
            JSONArray array = object.getJSONArray("tuc");

            if (array.length() == 0)
            {
                Log.i(LOG_TAG, "No results found for search by " + json);
                return null;
            }

            String result = "";
            for (int i = 0; i < array.length(); i++)
            {
                JSONObject currentTuc = (JSONObject) array.get(i);
                JSONObject phrase = null;
                try
                {
                    phrase = currentTuc.getJSONObject("phrase");
                    result += phrase.getString("text") + "\n";

                }
                catch (JSONException e)
                {
                    Log.e(LOG_TAG, "org.json.JSONException: No value for phrase " + phrase);
                }
            }
            return result;
        }

        OkHttpClient client = new OkHttpClient();

        private String getURL(String fromLanguage, String destLanguage, String phrase)
        {
            Uri.Builder builder = new Uri.Builder();
            builder.scheme("https")
                    .authority("glosbe.com")
                    .appendPath("gapi")
                    .appendPath("translate")
                    .appendQueryParameter("from", fromLanguage)
                    .appendQueryParameter("dest", destLanguage)
                    .appendQueryParameter("phrase", phrase)
                    .appendQueryParameter("format", "json");
            return builder.build().toString();
        }

        String doGetRequest(String url) throws IOException
        {
            Request request = new Request.Builder().url(url).build();
            Response response = client.newCall(request).execute();
            return response.body().string();
        }

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            loadingIndicator.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... params)
        {
            String path = getURL(params[0], params[1], params[2]);//"de", "ru", "Hund");

            try
            {
                String response = doGetRequest(path);
                return getTranslation(response);
            }
            catch (IOException e)
            {
                Log.e(LOG_TAG, "IOException while performing HTTP request");
            }
            catch (JSONException e)
            {
                Log.e(LOG_TAG, "org.json.JSONException while performing HTTP request");
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s)
        {
            super.onPostExecute(s);
            loadingIndicator.setVisibility(View.GONE);
        }
    }

    private class InsertItemTask extends AsyncTask<LessonItemI, Integer, Void>
    {
        private LessonItemsHelper lessonHelper;

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
    }
}
