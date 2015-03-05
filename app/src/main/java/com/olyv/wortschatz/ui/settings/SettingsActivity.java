package com.olyv.wortschatz.ui.settings;

import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;

import com.olyv.wortschatz.ui.R;

public class SettingsActivity extends PreferenceActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);

//        final TimePickerDialog dp = (TimePickerDialog) findPreference(getString(R.string.preference_notification_time_key));
////        dp.setText("2014-08-02");
////        dp.setSummary("2014-08-02");
//        dp.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener()
//        {
//            @Override
//            public boolean onPreferenceChange(Preference preference,Object newValue)
//            {
//                //your code to change values.
////                dp.setSummary((String) newValue); d
//
//                return true;
//            }
//        });


    }
}
