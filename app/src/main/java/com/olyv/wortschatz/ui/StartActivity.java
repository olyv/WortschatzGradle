package com.olyv.wortschatz.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import com.olyv.wortschatz.lesson.DatabaseHelper;
import com.olyv.wortschatz.lesson.LessonItemsHelper;
import com.olyv.wortschatz.lesson.items.LessonItemI;
import com.olyv.wortschatz.ui.lesson.LessonActivity;
import com.olyv.wortschatz.ui.editor.AddNewItemActivity;
import com.olyv.wortschatz.ui.manager.LessonItemsManagerActivity;
import com.olyv.wortschatz.ui.settings.NumberPickerPreference;
import com.olyv.wortschatz.ui.settings.SettingsActivity;

import java.util.ArrayList;
import java.util.Collections;

public class StartActivity extends Activity
{
    public static final String LOG_TAG = "StartActivityLog";
    public static final String PREPARED_LESSON = "preparedLessonItems";
    public static final String OPEN_MANAGER = "openItemsManager";

    private DatabaseHelper databaseHelper = null;

    private Button startLessonButton;
    private Button manageWordsButton;
    private Button addNewItemButton;

    private LinearLayout loadingIndicator;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_activity);

        startLessonButton = (Button) findViewById(R.id.startLessonBtn);
        manageWordsButton = (Button) findViewById(R.id.manageLessonItemsBtn);
        addNewItemButton = (Button) findViewById(R.id.addLessonItemBtn);

        loadingIndicator = (LinearLayout) findViewById(R.id.loadingManager);

        startLessonButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                CombineLessonTask task = new CombineLessonTask();
                task.execute();
            }
        });

        manageWordsButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(getApplicationContext(), LessonItemsManagerActivity.class);
                intent.putExtra(OPEN_MANAGER, "open LessonItemsManagerActivity");
                startActivity(intent);
            }
        });

        addNewItemButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(getApplicationContext(), AddNewItemActivity.class);
                startActivity(intent);
            }
        });
        Log.i(LOG_TAG, "initialized layout and UI elements");
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        DatabaseHelper.releaseDatabaseHelper(databaseHelper);
        Log.i(LOG_TAG, "released DatabaseHelper");
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadingIndicator.setVisibility(View.GONE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.start_screen_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.about:
                AlertDialog.Builder builder = new AlertDialog.Builder(StartActivity.this);
                builder.setMessage(R.string.about)
                .setTitle(R.string.app_name)
                .setIcon(R.drawable.ic_launcher)
                .setNeutralButton("Ok", new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int id)
                    {
                        dialog.dismiss();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
                return true;
            case R.id.settings:
                startActivity(new Intent(this, SettingsActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private class CombineLessonTask extends AsyncTask<Void, Integer, Void>
    {
        private ArrayList<LessonItemI> items = new ArrayList<LessonItemI>();

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();

            loadingIndicator.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(Void... params)
        {
            if (databaseHelper == null)
            {
                databaseHelper = new DatabaseHelper(StartActivity.this);
            }

            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

            int numberOfVerbs = prefs.getInt(getString(R.string.preference_verbs_per_lesson_key),
                    NumberPickerPreference.DEFAULT_VALUE);
            int numberOfNouns = prefs.getInt(getString(R.string.preference_nouns_per_lesson_key),
                    NumberPickerPreference.DEFAULT_VALUE);
            int numberOfAdjektives = prefs.getInt(getString(R.string.preference_adjektives_per_lesson_key),
                    NumberPickerPreference.DEFAULT_VALUE);

            items = new LessonItemsHelper().getLessonItems(databaseHelper, numberOfVerbs, numberOfNouns, numberOfAdjektives);
            Collections.shuffle(items);
            return null;
        }

        @Override
        protected void onPostExecute(Void result)
        {
            super.onPostExecute(result);

            Intent intent = new Intent(getApplicationContext(), LessonActivity.class);
            Bundle bundle = new Bundle();
            bundle.putParcelableArrayList(PREPARED_LESSON, items);
            intent.putExtras(bundle);

            startActivity(intent);
        }
    }
}
