package com.olyv.wortschatz.test.integration.manager;

import android.test.ActivityInstrumentationTestCase2;

import com.olyv.wortschatz.lesson.DatabaseHelper;
import com.olyv.wortschatz.lesson.LessonItemsHelper;
import com.olyv.wortschatz.lesson.items.Adjektive;
import com.olyv.wortschatz.lesson.items.Noun;
import com.olyv.wortschatz.lesson.items.Verb;
import com.olyv.wortschatz.ui.StartActivity;
import com.robotium.solo.Solo;

import java.util.HashMap;
import java.util.Map;

public class BaseItemsManagerTest extends ActivityInstrumentationTestCase2<StartActivity>
{
//    protected static final String VERB = "Verb";
//    protected static final String NOUN = "Noun";
//    protected static final String ADJEKTIVE = "Adjektive";
//
//    protected Solo solo;
//    protected Map<String, Integer> itemTypes = new HashMap<String, Integer>(3);
//
//    //words to be added during test
//    protected Noun newNoun;
//    protected Verb newVerb;
//    protected Adjektive newAdjektive;
//    protected DatabaseHelper databaseHelper;
//    protected LessonItemsHelper lessonHelper;

    protected Solo solo;

    public BaseItemsManagerTest()
    {
        super(StartActivity.class);
    }

    protected void setUp() throws Exception
    {
        solo = new Solo(getInstrumentation(), getActivity());


    }
}
