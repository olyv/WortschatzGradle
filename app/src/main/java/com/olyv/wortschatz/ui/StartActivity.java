package com.olyv.wortschatz.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.olyv.wortschatz.lesson.DatabaseHelper;
import com.olyv.wortschatz.lesson.LessonItemsHelper;
import com.olyv.wortschatz.lesson.items.LessonItemI;
import com.olyv.wortschatz.ui.editor.AddNewItem;
import com.olyv.wortschatz.ui.lesson.LessonActivity;
import com.olyv.wortschatz.ui.manager.LessonItemsManagerActivity;
import com.olyv.wortschatz.ui.settings.NumberPickerPreference;
import com.olyv.wortschatz.ui.settings.SettingsActivity;

import java.util.ArrayList;
import java.util.Collections;

public class StartActivity extends Activity
{
    public static final String PREPARED_LESSON = "preparedLessonItems";
    public static final String OPEN_MANAGER = "openItemsManager";

    private static final int NOTIFICATION_ID = 1;
    private static final String LOG_TAG = "StartActivityLog";

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
//                Intent intent = new Intent(getApplicationContext(), AddNewItemActivity.class);
                Intent intent = new Intent(getApplicationContext(), AddNewItem.class);
                startActivity(intent);
            }
        });
        Log.i(LOG_TAG, "initialized layout and UI elements");

        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }

    public static class AlarmReceiver extends BroadcastReceiver
    {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            // The Intent to be used when the user clicks on the Notification View
            Intent notificationIntent = new Intent(context, StartActivity.class);

            // The PendingIntent that wraps the underlying Intent
            PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notificationIntent, Intent.FLAG_ACTIVITY_NEW_TASK);

            long[] mVibratePattern = { 0, 200, 200, 300 };

            String uriPath = "android.resource://" + getClass().getPackage() + "/" + R.raw.notification;

            Uri soundURI = Uri.parse(uriPath);

            // Build the Notification
            Notification.Builder notificationBuilder = new Notification.Builder(
                    context).setTicker(context.getString(R.string.wortschatz_reminder))
                    .setSmallIcon(R.drawable.ic_stat_action_class)
                    .setAutoCancel(true).setContentTitle(context.getString(R.string.app_name))
                    .setContentText(context.getString(R.string.notification_to_complete_lesson)).setContentIntent(contentIntent)
                    .setSound(soundURI).setVibrate(mVibratePattern);

            // Get the NotificationManager
            NotificationManager notificationManager = (NotificationManager) context
                    .getSystemService(Context.NOTIFICATION_SERVICE);

            // Pass the Notification to the NotificationManager:
            notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build());

            Log.e(LOG_TAG, "========================= notification received");
        }
    }

    @Override
    protected void onStop()
    {
        super.onStop();
        DatabaseHelper.releaseDatabaseHelper(databaseHelper);
        Log.i(LOG_TAG, "released DatabaseHelper");
    }

    @Override
    protected void onResume()
    {
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