package com.olyv.wortschatz.ui.settings;

import android.os.Bundle;
import android.preference.PreferenceActivity;

import com.olyv.wortschatz.ui.R;

public class SettingsActivity extends PreferenceActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
    }
}
