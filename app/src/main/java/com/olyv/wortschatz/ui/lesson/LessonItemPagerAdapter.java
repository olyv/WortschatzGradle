package com.olyv.wortschatz.ui.lesson;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import com.olyv.wortschatz.lesson.items.Adjektive;
import com.olyv.wortschatz.lesson.items.LessonItemI;
import com.olyv.wortschatz.lesson.items.Noun;
import com.olyv.wortschatz.lesson.items.Verb;
import com.olyv.wortschatz.ui.lesson.fragment.AdjektiveFragment;
import com.olyv.wortschatz.ui.lesson.fragment.NounFragment;
import com.olyv.wortschatz.ui.lesson.fragment.VerbFragment;

import java.util.ArrayList;

public class LessonItemPagerAdapter extends FragmentPagerAdapter
{
    private ArrayList<LessonItemI> arrayOfLessonItems;

    public LessonItemPagerAdapter(FragmentManager fm, ArrayList<LessonItemI> flag)
    {
        super(fm);
        this.arrayOfLessonItems = flag;
    }

    @Override
    public Fragment getItem(int i)
    {
        Fragment fragment = null;
        Bundle bundle = new Bundle();
        LessonItemI item = arrayOfLessonItems.get(i);

        if (Verb.class.isInstance(item))
        {
            bundle.putParcelable(LessonActivity.VERB_TAG, item);
            fragment = new VerbFragment();
        }
        else if (Noun.class.isInstance(item))
        {
            bundle.putParcelable(LessonActivity.NOUN_TAG, item);
            fragment = new NounFragment();
        }
        else if (Adjektive.class.isInstance(item))
        {
            bundle.putParcelable(LessonActivity.ADJEKTIVE_TAG, item);
            fragment = new AdjektiveFragment();
        }
        //indicates that item is new and hasn't been answered yet
        bundle.putBoolean(AdjektiveFragment.IS_ANSWERED, false);
        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public int getCount()
    {
        return arrayOfLessonItems.size();
    }

    @Override
    public Parcelable saveState()
    {
        return super.saveState();
    }
}