package com.olyv.wortschatz.ui.settings;

import android.content.Context;
import android.content.res.TypedArray;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TimePicker;

import java.util.Calendar;

public class TimePickerDialog extends DialogPreference
{
    public static final String DEFAULT_VALUE = "08:00";

    private int lastHour = 0;
    private int lastMinute = 0;
    private TimePicker picker = null;

    public static long timeToIntervalInMillis(String timeInHourAndMinutes)
    {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, getHour(timeInHourAndMinutes));
        calendar.set(Calendar.MINUTE, getMinute(timeInHourAndMinutes));

        if(Calendar.getInstance().after(calendar))
        {
            calendar.add(Calendar.DATE, 1);
        }

        return calendar.getTimeInMillis();
    }

    private static int getHour(String time)
    {
        String[] pieces = time.split(":");

        return(Integer.parseInt(pieces[0]));
    }

    private static int getMinute(String time)
    {
        String[] pieces=time.split(":");

        return(Integer.parseInt(pieces[1]));
    }

    public TimePickerDialog(Context ctxt, AttributeSet attrs)
    {
        super(ctxt, attrs);

        setPositiveButtonText("Set");
        setNegativeButtonText("Cancel");
    }

    @Override
    protected View onCreateDialogView()
    {
        picker = new TimePicker(getContext());

        return(picker);
    }

    @Override
    protected void onBindDialogView(View v)
    {
        super.onBindDialogView(v);
        picker.setIs24HourView(true);
        picker.setCurrentHour(lastHour);
        picker.setCurrentMinute(lastMinute);
    }

    @Override
    protected void onDialogClosed(boolean positiveResult)
    {
        super.onDialogClosed(positiveResult);

        if (positiveResult) {
            lastHour = picker.getCurrentHour();
            lastMinute = picker.getCurrentMinute();

            String time = String.valueOf(lastHour) + ":" + String.valueOf(lastMinute);

            if (callChangeListener(time)) {
                persistString(time);
            }
        }
    }

    @Override
    protected Object onGetDefaultValue(TypedArray a, int index)
    {
        return(a.getString(index));
    }

    @Override
    protected void onSetInitialValue(boolean restoreValue, Object defaultValue)
    {
        String time = null;

        if (restoreValue)
        {
            if (defaultValue == null)
            {
                time = getPersistedString("00:00");
            }
            else
            {
                time = getPersistedString(defaultValue.toString());
            }
        }
        else
        {
            time = defaultValue.toString();
        }

        lastHour = getHour(time);
        lastMinute = getMinute(time);
    }
}