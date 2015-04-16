package com.olyv.wortschatz.test.integration.addword;

import android.widget.EditText;
import android.widget.ListView;

import com.olyv.wortschatz.lesson.DatabaseHelper;
import com.olyv.wortschatz.lesson.LessonItemsHelper;
import com.olyv.wortschatz.lesson.items.LessonItemI;
import com.olyv.wortschatz.lesson.items.Noun;
import com.olyv.wortschatz.ui.R;
import com.olyv.wortschatz.ui.StartActivity;
import com.olyv.wortschatz.ui.editor.AddNewItem;
import com.olyv.wortschatz.ui.manager.LessonItemsManagerActivity;

import junit.framework.Assert;

import java.util.ArrayList;

public class AddNewNounTest extends BaseAddWordTest
{
    public void testAddNewNoun() throws Exception
    {
        solo.clickOnButton(solo.getString(R.string.add_lesson_item));

        solo.assertCurrentActivity("Add new item activity is failed to open", AddNewItem.class);

        solo.pressSpinnerItem(0, itemTypes.get(NOUN));

        EditText enterNoun = solo.getEditText(solo.getString(R.string.enter_noun_hint));
        solo.typeText(enterNoun, newNoun.getWord());

        EditText enterTranslation = solo.getEditText(solo.getString(R.string.enter_translation_hint));
        solo.typeText(enterTranslation, newNoun.getTranslation());

        solo.clickOnRadioButton(0);

        EditText enterPlural = solo.getEditText(solo.getString(R.string.enter_plural_hint));
        solo.typeText(enterPlural, newNoun.getPlural());

        solo.clickOnButton(solo.getString(R.string.save_button_text));

        //editor is closed and StartActivity is displayed
        solo.assertCurrentActivity("StartActivity is opened after word is saved", StartActivity.class);

        //find just added word in manager
        solo.clickOnButton(solo.getString(R.string.manage_lesson_items));

        solo.assertCurrentActivity("List of items is not displayed", LessonItemsManagerActivity.class);

        solo.clickOnActionBarItem(R.id.action_search);

        solo.typeText(0, newNoun.getWord());

        ListView filteredList = solo.getView(ListView.class, 0);

        // expected 2 = 1 word + 1 banner at the footer
        Assert.assertEquals("Found more then one word", 2, filteredList.getAdapter().getCount());
    }

    @Override
    public void tearDown() throws Exception
    {
        solo.finishOpenedActivities();

        databaseHelper = new DatabaseHelper(this.getInstrumentation().getTargetContext());
        lessonHelper = new LessonItemsHelper();
        ArrayList<LessonItemI> foundItems = lessonHelper.searchItems(databaseHelper, newNoun.getWord());

        lessonHelper.deleteNoun(databaseHelper, (Noun) foundItems.get(0));

        DatabaseHelper.releaseDatabaseHelper(databaseHelper);
        super.tearDown();
    }
}