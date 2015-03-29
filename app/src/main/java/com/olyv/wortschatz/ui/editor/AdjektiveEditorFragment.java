package com.olyv.wortschatz.ui.editor;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;

import com.olyv.wortschatz.lesson.items.Adjektive;
import com.olyv.wortschatz.ui.R;

public class AdjektiveEditorFragment extends BaseFragment
{
    private static final String LOG_TAG = "AdjektiveEditorFragment";
    private EditText adjektive;
    private EditText translation;
    private Button save;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.adjektive_editor_fragment, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);

        adjektive = (EditText) getActivity().findViewById(R.id.adjektive);
        translation = (EditText) getActivity().findViewById(R.id.adjektiveTranslation);
        save = (Button) getActivity().findViewById(R.id.saveBtn);

        save.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Adjektive newAdjektive = getEnteredAdjektive(new Adjektive());

                if (newAdjektive != null)
                {
                    Log.i(LOG_TAG, "inserting new Adjektive " + adjektive.getText());
                    InsertItemTask task = new InsertItemTask();
                    task.execute(newAdjektive);
                    getActivity().finish();
                }
            }
        });
    }

    private Adjektive getEnteredAdjektive(Adjektive adjektiveItem)
    {
        if (isEmptyField(adjektive))
        {
            adjektive.setError(getString(R.string.enter_adjektive_hint));
            return null;
        }
        else
        {
            adjektiveItem.setWord(adjektive.getText().toString().trim());
        }
        if (isEmptyField(translation))
        {
            translation.setError(getString(R.string.enter_translation_hint));
            return null;
        }
        else
        {
            adjektiveItem.setTranslation(translation.getText().toString().trim());
        }
        return adjektiveItem;
    }
}
