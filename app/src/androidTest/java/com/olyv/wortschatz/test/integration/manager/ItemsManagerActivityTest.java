package com.olyv.wortschatz.test.integration.manager;

import android.test.ActivityInstrumentationTestCase2;
import android.widget.EditText;
import android.widget.ListView;

import com.olyv.wortschatz.lesson.DatabaseHelper;
import com.olyv.wortschatz.lesson.LessonItemsHelper;
import com.olyv.wortschatz.lesson.items.LessonItemI;
import com.olyv.wortschatz.lesson.items.Noun;
import com.olyv.wortschatz.test.integration.addword.BaseAddWordTest;
import com.olyv.wortschatz.ui.R;
import com.olyv.wortschatz.ui.StartActivity;
import com.olyv.wortschatz.ui.editor.AddNewItem;
import com.olyv.wortschatz.ui.manager.LessonItemsManagerActivity;
import com.robotium.solo.Solo;

import junit.framework.Assert;

import java.util.ArrayList;

public class ItemsManagerActivityTest extends BaseAddWordTest
{
    public void deleteItemTest() throws Exception
    {

    }

    @Override
    public void tearDown() throws Exception
    {
        super.tearDown();
    }
}