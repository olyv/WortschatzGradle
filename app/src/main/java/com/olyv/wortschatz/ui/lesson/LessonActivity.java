package com.olyv.wortschatz.ui.lesson;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

import com.olyv.wortschatz.lesson.items.LessonItemI;
import com.olyv.wortschatz.ui.R;
import com.olyv.wortschatz.ui.StartActivity;

import java.util.ArrayList;

public class LessonActivity extends FragmentActivity
{
    public static final String VERB_TAG = "Verb";
    public static final String NOUN_TAG = "Noun";
    public static final String ADJEKTIVE_TAG = "Adjektive";
    private static final String LOG_TAG = "LessonActivityLog";

    public static int answeredItemsCounter = 0;
    public static int correctAnswersCounter = 0;

    private ArrayList<LessonItemI> arrayOfLessonItems;
    private LessonItemPagerAdapter itemsPagerAdapter;
    private ViewPager viewPager;
    private LinearLayout progressDots;

    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lesson_pager_main);

        Bundle bundle = getIntent().getExtras();
        arrayOfLessonItems = bundle.getParcelableArrayList(StartActivity.PREPARED_LESSON);

        // Create the adapter that will return a fragment for each of the three primary sections of the app.
        itemsPagerAdapter = new LessonItemPagerAdapter(getSupportFragmentManager(), arrayOfLessonItems);

        // Set up the action bar.
        final ActionBar actionBar = getActionBar();

        // Specify that we will be displaying NO tabs in the action bar.
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);

        //MY PAGER INDICATOR
        progressDots = (LinearLayout) findViewById(R.id.llDots);

        // Set up the ViewPager, attaching the adapter and setting up a listener for when the user swipes between sections.
        viewPager = (ViewPager) findViewById(R.id.pager);
        viewPager.setAdapter(itemsPagerAdapter);
        viewPager.setOffscreenPageLimit(arrayOfLessonItems.size());
        viewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener()
        {
            @Override
            public void onPageSelected(int position)
            {
                Log.i("", "Page Selected is ===> " + position);
                for (int i = 0; i < itemsPagerAdapter.getCount(); i++)
                {
                    if (i != position)
                    {
                        ((ImageView) progressDots.findViewWithTag(i)).setSelected(false);
                    }
                }
                ((ImageView) progressDots.findViewWithTag(position)).setSelected(true);
            }
        });

        // For each of the sections in the app, add a tab to the action bar.
        for (int i = 0; i < itemsPagerAdapter.getCount(); i++)
        {
            ImageButton imgDot = new ImageButton(this);
            imgDot.setTag(i);
            imgDot.setImageResource(R.drawable.dot_selector);
            imgDot.setBackgroundResource(0);
            imgDot.setPadding(5, 5, 5, 5);
            LayoutParams params = new LayoutParams(20, 20);
            imgDot.setLayoutParams(params);
            if (i == 0)
                imgDot.setSelected(true);

            progressDots.addView(imgDot);
        }
    }

    public void showResultsDialog()
    {
        if (answeredItemsCounter == arrayOfLessonItems.size())
        {
            String dialogMessage = String.format("Your result is %d out of %d\nfinish lesson?",
                    correctAnswersCounter, answeredItemsCounter);

            new AlertDialog.Builder(this)
                    .setMessage(dialogMessage)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener()
                    {
                        public void onClick(DialogInterface dialog, int which)
                        {
                            Log.i(LOG_TAG, "finished");
                            finish();
                        }
                    })
                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener()
                    {
                        public void onClick(DialogInterface dialog, int which)
                        {
                            Log.i(LOG_TAG, "canceled");
                            // User cancelled the dialog
                        }
                    })
                    .show();
        }
    }
}
