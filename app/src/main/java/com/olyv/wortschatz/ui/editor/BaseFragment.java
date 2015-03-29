package com.olyv.wortschatz.ui.editor;

import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.EditText;

import com.olyv.wortschatz.lesson.DatabaseHelper;
import com.olyv.wortschatz.lesson.LessonItemsHelper;
import com.olyv.wortschatz.lesson.items.Adjektive;
import com.olyv.wortschatz.lesson.items.LessonItemI;
import com.olyv.wortschatz.lesson.items.Noun;
import com.olyv.wortschatz.lesson.items.Verb;

public class BaseFragment extends Fragment
{
    protected DatabaseHelper databaseHelper = null;

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
    }

    protected boolean isEmptyField(EditText field)
    {
        return field.getText().toString().trim().matches("");
    }

    protected class InsertItemTask extends AsyncTask<LessonItemI, Integer, Void>
    {
        private LessonItemsHelper lessonHelper;

        @Override
        protected Void doInBackground(LessonItemI... params)
        {
            if (databaseHelper == null)
            {
                databaseHelper = new DatabaseHelper(getActivity().getApplicationContext());
            }

            LessonItemI item = params[0];    //http://stackoverflow.com/questions/17549042/android-asynctask-passing-a-single-string
            lessonHelper = new LessonItemsHelper();
            if (item instanceof Verb)
            {
                lessonHelper.insertNewVerb(databaseHelper, (Verb) item);
            }
            if (item instanceof Noun)
            {
                lessonHelper.insertNewNoun(databaseHelper, (Noun) item);
            }
            if (item instanceof Adjektive)
            {
                lessonHelper.insertNewAdjektive(databaseHelper, (Adjektive) item);
            }
            return null;
        }
    }
}
