package com.olyv.wortschatz.ui.editor;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.olyv.wortschatz.ui.R;
import com.olyv.wortschatz.ui.editor.fragment.AdjektiveEditorFragment;
import com.olyv.wortschatz.ui.editor.fragment.NounEditorFragment;
import com.olyv.wortschatz.ui.editor.fragment.TranslatorFragment;
import com.olyv.wortschatz.ui.editor.fragment.VerbEditorFragment;

import java.util.ArrayList;

public class AddNewItem extends Activity
{
    private FragmentManager fragmentManager;
    private Spinner selectType;
    private String verbType;
    private String nounType;
    private String adjektiveType;
    private FragmentTransaction fragmentTransaction;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.add_item_activity);

        fragmentManager = getFragmentManager();

        selectType = (Spinner) findViewById(R.id.selectTypeSpinner);

        verbType = getString(R.string.verb_item);
        nounType = getString(R.string.noun_item);
        adjektiveType = getString(R.string.adjektive_item);

        fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.add(R.id.translator_frame, new TranslatorFragment());

        fragmentTransaction.commit();

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

                if (selectedType.equals(verbType))
                {
                    fragmentTransaction = fragmentManager.beginTransaction();

                    fragmentTransaction.replace(R.id.item_frame, new VerbEditorFragment());

                    fragmentTransaction.commit();
                }
                else if (selectedType.equals(nounType))
                {
                    fragmentTransaction = fragmentManager.beginTransaction();

                    fragmentTransaction.replace(R.id.item_frame, new NounEditorFragment());

                    fragmentTransaction.commit();
                }
                else if (selectedType.equals(adjektiveType))
                {
                    fragmentTransaction = fragmentManager.beginTransaction();

                    fragmentTransaction.replace(R.id.item_frame, new AdjektiveEditorFragment());

                    fragmentTransaction.commit();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {
                // TODO Auto-generated method stub
            }
        });
    }
}
