package com.olyv.wortschatz.ui.editor.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;

import com.olyv.wortschatz.lesson.items.LessonItemI;
import com.olyv.wortschatz.lesson.items.Verb;
import com.olyv.wortschatz.ui.R;
import com.olyv.wortschatz.ui.manager.LessonItemsManagerActivity;

public class VerbEditorFragment extends BaseFragment
{
    private static final String LOG_TAG = "VerbEditorFragment";
    private static final String ENTERED_VERB = "EnterdVerb";
    private static final String ENTERED_TRANSLATION = "EnteredTranslation";
    private static final String ENTERED_PARTIZIP = "EnterdPartizip";
    private static final String ENTERED_AUXVERB = "SelectedAuxverb";
    private EditText verb;
    private EditText partizip;
    private RadioGroup auxverb;
    private EditText translation;
    private Button save;
    private LessonItemI verbToEdit;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        try
        {
            verbToEdit = getArguments().getParcelable(LessonItemsManagerActivity.EDITED_ITEM);
        }
        catch (NullPointerException e)
        {
            Log.i(LOG_TAG, "fragment is in Add New Item Mode");
        }

        View view = inflater.inflate(R.layout.verb_editor_fragment, container, false);

        verb = (EditText) view.findViewById(R.id.verb);
        partizip = (EditText) view.findViewById(R.id.verbPartizip);
        auxverb = (RadioGroup) view.findViewById(R.id.auxverbSelect);
        translation = (EditText) view.findViewById(R.id.verbTranslation);
        save = (Button) view.findViewById(R.id.saveBtn);

        if (verbToEdit != null)
        {
            verb.setText(( verbToEdit).getWord());
            partizip.setText(((Verb) verbToEdit).getPartizip());
            translation.setText(( verbToEdit).getTranslation());
            if (((Verb) verbToEdit).getAuxverb().equals(Verb.AUXVERB_IST))
            {
                auxverb.check(R.id.auxverbIst);
            }
            else
            {
                auxverb.check(R.id.auxverbHat);
            }
        }

        save.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Verb newVerb = (verbToEdit == null)? getEnteredVerb(new Verb()) : getEnteredVerb((Verb) verbToEdit);

                if (newVerb != null)
                {
                    if (verbToEdit != null)
                    {
                        UpdateItemTask task = new UpdateItemTask();
                        task.execute(newVerb);
                        getActivity().setResult(getActivity().RESULT_OK);
                        Log.i(LOG_TAG, "updating verb " + newVerb.getWord());
                    }
                    else
                    {
                        InsertItemTask task = new InsertItemTask();
                        task.execute(newVerb);
                        Log.i(LOG_TAG, "inserting new verb " + newVerb.getWord());
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
            verb.setText(savedInstanceState.getString(ENTERED_VERB));
            translation.setText(savedInstanceState.getString(ENTERED_TRANSLATION));
            partizip.setText(savedInstanceState.getString(ENTERED_PARTIZIP));
            auxverb.check(savedInstanceState.getInt(ENTERED_AUXVERB));
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);

        outState.putString(ENTERED_VERB, verb.getText().toString());
        outState.putString(ENTERED_TRANSLATION, translation.getText().toString());
        outState.putString(ENTERED_PARTIZIP, partizip.getText().toString());
        outState.putInt(ENTERED_AUXVERB, auxverb.getCheckedRadioButtonId());
    }

    private Verb getEnteredVerb(Verb verbItem)
    {
        if (isEmptyField(verb))
        {
            verb.setError(getString(R.string.enter_verb_hint));
            return null;
        }
        else
        {
            verbItem.setWord(verb.getText().toString().trim());
        }
        if (isEmptyField(translation))
        {
            translation.setError(getString(R.string.enter_translation_hint));
            return null;
        }
        else
        {
            verbItem.setTranslation(translation.getText().toString().trim());
        }
        if (isEmptyField(partizip))
        {
            partizip.setError(getString(R.string.verb_partizip_hint));
            return null;
        }
        else
        {
            verbItem.setPartizip(partizip.getText().toString().trim());
        }
        verbItem.setAuxverb(getSelectedAuxVerb());

        return verbItem;
    }

    private String getSelectedAuxVerb()
    {
        String selectedAuxVerb = null;
        switch (auxverb.getCheckedRadioButtonId())
        {
            case R.id.auxverbHat:
                selectedAuxVerb = Verb.AUXVERB_HAT;
                break;
            case R.id.auxverbIst:
                selectedAuxVerb = Verb.AUXVERB_IST;
                break;
        }
        return selectedAuxVerb;
    }
}
