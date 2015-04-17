package com.olyv.wortschatz.test.integration.manager;

import android.test.ActivityInstrumentationTestCase2;
import android.widget.ListView;

import com.olyv.wortschatz.lesson.DatabaseHelper;
import com.olyv.wortschatz.lesson.LessonItemsHelper;
import com.olyv.wortschatz.lesson.items.Verb;
import com.olyv.wortschatz.ui.R;
import com.olyv.wortschatz.ui.StartActivity;
import com.olyv.wortschatz.ui.manager.LessonItemsManagerActivity;
import com.robotium.solo.Solo;

import junit.framework.Assert;

public class ItemsManagerActivityTest extends ActivityInstrumentationTestCase2<StartActivity>
{
    private Solo solo;
    private Verb newVerb;

    public ItemsManagerActivityTest()
    {
        super(StartActivity.class);
    }

    public void setUp() throws Exception
    {
        solo = new Solo(getInstrumentation(), getActivity());

        //insert new verb
        DatabaseHelper databaseHelper = new DatabaseHelper(this.getInstrumentation().getTargetContext());
        LessonItemsHelper lessonHelper = new LessonItemsHelper();

        newVerb = new Verb()
                .setWord("AddVerbTest")
                .setPartizip("addPartizipTest")
                .setAuxverb("hat")
                .setTranslation("addVerbTranslationTest");

        lessonHelper.insertNewVerb(databaseHelper, newVerb);
    }

    public void testDeleteItem() throws Exception
    {
        solo.clickOnButton(solo.getString(R.string.manage_lesson_items));

        solo.assertCurrentActivity("Add new item activity is failed to open", LessonItemsManagerActivity.class);

        solo.clickOnActionBarItem(R.id.action_search);
        solo.typeText(0, newVerb.getWord());

        ListView filteredList = solo.getView(ListView.class, 0);

        // expected 2 = 1 word + 1 banner at the footer
        Assert.assertEquals("Found more then one word", 2, filteredList.getAdapter().getCount());

        solo.clickLongInList(0);

        solo.clickOnText(solo.getString(R.string.remove_item));

        solo.clickOnButton(solo.getString(android.R.string.yes));

        solo.clickOnActionBarItem(R.id.action_search);
        solo.clearEditText(0);
        solo.typeText(0, newVerb.getWord());

        filteredList = solo.getView(ListView.class, 0);

        // expected 1 = 0 word + 1 banner at the footer
        Assert.assertEquals("Found more then one word", 1, filteredList.getAdapter().getCount());
    }

    @Override
    public void tearDown() throws Exception
    {
        super.tearDown();
    }
}