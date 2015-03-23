package com.olyv.wortschatz.translator;

import android.net.Uri;
import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.util.Log;

import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;


public class Client extends AsyncTask<String, Void, String>
{
    private String getTranslation(String json) throws JSONException {
      //translate("de", "ru", "Hund", "json");
        JSONObject object = new JSONObject(json);
        JSONArray array = object.getJSONArray("tuc");

        if (array.length() == 0)
        {
            return "No results found, please check spelling";
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
                e.printStackTrace();
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
            e.printStackTrace();
        } catch (JSONException e)
        {
            e.printStackTrace();
        }

        return null;
    }
}
