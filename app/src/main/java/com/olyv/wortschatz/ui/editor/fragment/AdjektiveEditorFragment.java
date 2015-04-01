package com.olyv.wortschatz.ui.editor.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.olyv.wortschatz.lesson.items.Adjektive;
import com.olyv.wortschatz.lesson.items.LessonItemI;
import com.olyv.wortschatz.ui.R;
import com.olyv.wortschatz.ui.manager.LessonItemsManagerActivity;

public class AdjektiveEditorFragment extends BaseFragment
{
    private static final String LOG_TAG = "AdjektiveEditorFragment";
    private static final String ENTERED_ADJEKTIVE = "EnteredNoun";
    private static final String ENTERED_TRANSLATION = "EnteredTranslation";
    private EditText adjektive;
    private EditText translation;
    private Button save;
    private LessonItemI adjektiveToEdit;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        try
        {
            adjektiveToEdit = getArguments().getParcelable(LessonItemsManagerActivity.EDITED_ITEM);
        }
        catch (NullPointerException e)
        {
            Log.i(LOG_TAG, "fragment is in Add New Item Mode");
        }

        View view = inflater.inflate(R.layout.adjektive_editor_fragment, container, false);

        adjektive = (EditText) view.findViewById(R.id.adjektive);
        translation = (EditText) view.findViewById(R.id.adjektiveTranslation);
        save = (Button) view.findViewById(R.id.saveBtn);

        if (adjektiveToEdit != null)
        {
            adjektive.setText(adjektiveToEdit.getWord());
            translation.setText(adjektiveToEdit.getTranslation());
        }

        save.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Adjektive newAdjektive = (adjektiveToEdit == null)?
                        getEnteredAdjektive(new Adjektive()) : getEnteredAdjektive((Adjektive) adjektiveToEdit);

                if (newAdjektive != null)
                {
                    if (adjektiveToEdit != null)
                    {
                        UpdateItemTask task = new UpdateItemTask();
                        task.execute(newAdjektive);
                        getActivity().setResult(getActivity().RESULT_OK);
                        Log.i(LOG_TAG, "updating Adjektive " + newAdjektive.getWord());
                    }
                    else
                    {
                        InsertItemTask task = new InsertItemTask();
                        task.execute(newAdjektive);
                        Log.i(LOG_TAG, "inserting new Adjektive " + newAdjektive.getWord());
                    }
                    getActivity().finish();
                }
            }
        });
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);

        if (savedInstanceState != null)
        {
            adjektive.setText(savedInstanceState.getString(ENTERED_ADJEKTIVE));
            translation.setText(savedInstanceState.getString(ENTERED_TRANSLATION));
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);

        outState.putString(ENTERED_ADJEKTIVE, adjektive.getText().toString());
        outState.putString(ENTERED_TRANSLATION, translation.getText().toString());
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
