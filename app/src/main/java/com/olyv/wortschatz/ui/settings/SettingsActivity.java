package com.olyv.wortschatz.ui.settings;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.util.Log;

import com.olyv.wortschatz.ui.R;
import com.olyv.wortschatz.ui.StartActivity;

public class SettingsActivity extends PreferenceActivity
{
    private CheckBoxPreference enableNotification;

    private static AlarmManager alarmManager;
    private Intent notificationReceiverIntent;
    private static PendingIntent notificationReceiverPendingIntent;
    private TimePickerDialog timePicker;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);

        enableNotification = (CheckBoxPreference) findPreference(getString(R.string.preference_notify_me_key));
        timePicker = (TimePickerDialog) findPreference(getString(R.string.preference_notification_time_key));

        timePicker.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener()
        {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue)
            {
                if (enableNotification.isChecked())
                {
                    setAlarm();
                }
                return false;
            }
        });


        enableNotification.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener()
        {
            @Override
            public boolean onPreferenceClick(Preference preference)
            {
                enableNotification.setChecked(enableNotification.isChecked());
                if (enableNotification.isChecked())
                {
                    setAlarm();
                    Log.e("time from picker   ", TimePickerDialog.timeToIntervalInMillis(PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString(getString(R.string.preference_notification_time_key), "")) + "");
                }
                else
                {
                    if (alarmManager != null)
                    {
                        alarmManager.cancel(notificationReceiverPendingIntent);
                    }
                }
                return false;
            }
        });
    }

    private void setAlarm()
    {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        String notifyTimePreference = prefs.getString(getString(R.string.preference_notification_time_key), "");

        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        notificationReceiverIntent = new Intent(getApplicationContext(), StartActivity.AlarmReceiver.class);
        notificationReceiverPendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, notificationReceiverIntent, 0);
        alarmManager.setInexactRepeating(
                AlarmManager.RTC_WAKEUP,
                TimePickerDialog.timeToIntervalInMillis(notifyTimePreference),
                AlarmManager.INTERVAL_DAY,
                notificationReceiverPendingIntent);
    }
}
