package com.olyv.wortschatz.ui.editor;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.olyv.wortschatz.ui.R;
import com.olyv.wortschatz.ui.editor.fragment.AdjektiveEditorFragment;
import com.olyv.wortschatz.ui.editor.fragment.NounEditorFragment;
import com.olyv.wortschatz.ui.editor.fragment.TranslatorFragment;
import com.olyv.wortschatz.ui.editor.fragment.VerbEditorFragment;
import com.olyv.wortschatz.ui.lesson.fragment.VerbFragment;

import java.util.ArrayList;

public class AddNewItem extends Activity
{
    private static final String VERB_FRAGMENT_TAG = "VerbFragmentTag";
    private static final String NOUN_FRAGMENT_TAG = "NounFragmentTag";
    private static final String ADJEKTIVE_FRAGMENT_TAG = "AdjektiveFragmentTag";
    private FragmentManager fragmentManager;
    private Spinner selectType;
    private String verbType;
    private String nounType;
    private String adjektiveType;
    private FragmentTransaction fragmentTransaction;

    @Override
    protected void onCreate(final Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.add_item_activity);

        selectType = (Spinner) findViewById(R.id.selectTypeSpinner);

        fragmentManager = getFragmentManager();

        //to retain state of translator fragment ypon orientation change
        if (savedInstanceState == null)
        {
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.add(R.id.translator_frame, new TranslatorFragment());
            fragmentTransaction.commit();
        }

        verbType = getString(R.string.verb_item);
        nounType = getString(R.string.noun_item);
        adjektiveType = getString(R.string.adjektive_item);

        ArrayList<String> wordTypeSpinnerArray = new ArrayList<String>();
        wordTypeSpinnerArray.add(verbType);
        wordTypeSpinnerArray.add(nounType);
        wordTypeSpinnerArray.add(adjektiveType);

        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this, R.layout.spinner_item, wordTypeSpinnerArray);
        selectType.setAdapter(spinnerArrayAdapter);

        selectType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                String selectedType = selectType.getSelectedItem().toString();

                //check if fragment exists, if yes -- don't recreate it to be able to retain fragment state
                fragmentTransaction = fragmentManager.beginTransaction();
                if (selectedType.equals(verbType) )
                {
                    if (fragmentManager.findFragmentByTag(VERB_FRAGMENT_TAG) == null)
                    {
                        fragmentTransaction.replace(R.id.item_frame, new VerbEditorFragment(), VERB_FRAGMENT_TAG);
                    }
                }
                else if (selectedType.equals(nounType) )
                {
                    if (fragmentManager.findFragmentByTag(NOUN_FRAGMENT_TAG) == null)
                    {
                        fragmentTransaction.replace(R.id.item_frame, new NounEditorFragment(), NOUN_FRAGMENT_TAG);
                    }
                }
                else if (selectedType.equals(adjektiveType))
                {
                    if (fragmentManager.findFragmentByTag(ADJEKTIVE_FRAGMENT_TAG) == null)
                    {
                        fragmentTransaction.replace(R.id.item_frame, new AdjektiveEditorFragment(), ADJEKTIVE_FRAGMENT_TAG);
                    }
                }
                fragmentTransaction.commit();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {
                // TODO Auto-generated method stub
            }
        });
    }
}
