package com.olyv.wortschatz.ui.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.util.Log;
import android.widget.RemoteViews;

import com.olyv.wortschatz.lesson.DatabaseHelper;
import com.olyv.wortschatz.lesson.LessonItemsHelper;
import com.olyv.wortschatz.lesson.items.LessonItemI;
import com.olyv.wortschatz.ui.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class WortschatzWidgetProvider extends AppWidgetProvider
{
    private static final String LOG_TAG = "WortschatzWidgetLog";

    private ArrayList<LessonItemI> items = new ArrayList<LessonItemI>();

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds)
    {
        super.onUpdate(context, appWidgetManager, appWidgetIds);

        Log.d(LOG_TAG, "onUpdate " + Arrays.toString(appWidgetIds));

        for(int i : appWidgetIds)
        {
            updateWidget(context, appWidgetManager, i);
        }
    }

    private void updateWidget(Context context, AppWidgetManager appWidgetManager, int widgetID)
    {
        DatabaseHelper databaseHelper = new DatabaseHelper(context);

        items = new LessonItemsHelper().getLessonItems(databaseHelper, 1, 1, 1);
        LessonItemI item = items.get(new Random().nextInt(3));

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget);
        views.setTextViewText(R.id.random_word, item.getWord());
        appWidgetManager.updateAppWidget(widgetID, views);
    }
}
