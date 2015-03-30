package com.olyv.wortschatz.ui.editor;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;

import com.olyv.wortschatz.lesson.items.Adjektive;
import com.olyv.wortschatz.lesson.items.LessonItemI;
import com.olyv.wortschatz.lesson.items.Noun;
import com.olyv.wortschatz.lesson.items.Verb;
import com.olyv.wortschatz.ui.R;
import com.olyv.wortschatz.ui.editor.fragment.AdjektiveEditorFragment;
import com.olyv.wortschatz.ui.editor.fragment.BaseFragment;
import com.olyv.wortschatz.ui.editor.fragment.NounEditorFragment;
import com.olyv.wortschatz.ui.editor.fragment.VerbEditorFragment;
import com.olyv.wortschatz.ui.manager.LessonItemsManagerActivity;

public class Editor extends Activity
{
    private FragmentTransaction fragmentTransaction;
    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.editor_activity);

        Bundle extras = getIntent().getExtras();
        final LessonItemI item = extras.getParcelable(LessonItemsManagerActivity.EDITED_ITEM);

        if (item != null)
        {
            fragmentManager = getFragmentManager();
            Bundle bundle = new Bundle();
            bundle.putParcelable(LessonItemsManagerActivity.EDITED_ITEM, item);

            fragmentTransaction = fragmentManager.beginTransaction();

            BaseFragment fragment = null;
            if (item instanceof Verb)
            {
                fragment = new VerbEditorFragment();
            }
            if (item instanceof Noun)
            {
                fragment = new NounEditorFragment();
            }
            if (item instanceof Adjektive)
            {
                fragment = new AdjektiveEditorFragment();
            }
            fragment.setArguments(bundle);
            fragmentTransaction.add(R.id.editor_frame, fragment);
            fragmentTransaction.commit();
        }
    }
}