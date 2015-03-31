package com.olyv.wortschatz.ui.editor.fragment;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.olyv.wortschatz.ui.R;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

public class TranslatorFragment extends BaseFragment
{
    private static final String LOG_TAG = "TranslatorFragment";
    private static final String GOTTEN_TRANSLATION = "TranslatedWordText";
    private static final String WORD_TO_TRANSLATE = "WordToTranslateTExt";
    private ProgressBar loadingIndicator;
    private TextView translatedWord;
    private Button translateButton;
    private EditText wordForTranslation;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.translator_fragment, container, false);

        wordForTranslation = (EditText) view.findViewById(R.id.enteredWord);
        translateButton = (Button) view.findViewById(R.id.translateButton);
        translatedWord = (TextView) view.findViewById(R.id.translatedWord);
        loadingIndicator = (ProgressBar) view.findViewById(R.id.loadingIndicator);

        translateButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //check if user has internet connection
                ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
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
                            result = getActivity().getApplicationContext().getString(R.string.no_results_from_search);
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

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);

        if (savedInstanceState != null)
        {
            wordForTranslation.setText(savedInstanceState.getString(WORD_TO_TRANSLATE));
            translatedWord.setText(savedInstanceState.getString(GOTTEN_TRANSLATION));
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);
        outState.putString(WORD_TO_TRANSLATE, wordForTranslation.getText().toString());
        outState.putString(GOTTEN_TRANSLATION, translatedWord.getText().toString());
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
            OkHttpClient client = new OkHttpClient();
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
}
