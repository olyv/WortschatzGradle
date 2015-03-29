package com.olyv.wortschatz.ui.manager;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.*;
import android.widget.*;
import com.olyv.wortschatz.lesson.DatabaseHelper;
import com.olyv.wortschatz.lesson.LessonItemsHelper;
import com.olyv.wortschatz.lesson.items.Adjektive;
import com.olyv.wortschatz.lesson.items.LessonItemI;
import com.olyv.wortschatz.lesson.items.Noun;
import com.olyv.wortschatz.lesson.items.Verb;
import com.olyv.wortschatz.ui.R;
import com.olyv.wortschatz.ui.StartActivity;
import com.olyv.wortschatz.ui.editor.AddNewItem;
import com.olyv.wortschatz.ui.editor.Editor;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class LessonItemsManagerActivity extends ListActivity implements View.OnCreateContextMenuListener
{
    private static final String LOG_TAG = "LessonItemsManagerLog";
    private static final int EDIT_ITEM_REQUEST = 0;
    public static final String EDITED_ITEM = "LessonItemType";

    private ItemsListAdapter adapter;
    private ListView listView;
    private RelativeLayout loadingIndicator;
    private DatabaseHelper databaseHelper = null;

    private ArrayList<LessonItemI> foundLessonItems = new ArrayList<LessonItemI>();     //result od search
    private static String keyword;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        adapter = new ItemsListAdapter(getApplicationContext(), foundLessonItems);

        RelativeLayout loaderView = (RelativeLayout) getLayoutInflater().inflate(R.layout.loader_view, null);
        loadingIndicator = (RelativeLayout) loaderView.findViewById(R.id.loadItems);

        if (null == loaderView)
        {
            return;
        }
        listView = getListView();
        setListAdapter(adapter);

        String caller = getIntent().getStringExtra(StartActivity.OPEN_MANAGER);
        if (caller != null)
        {
            //when LessonItemsManagerActivity called the first time it is filled with all words
            performSearchTask("");
        }

        registerForContextMenu(listView);
        Log.i(LOG_TAG, "onCreate() completed");
    }

    @Override
    public void onResume()
    {
        super.onResume();
        Log.i(LOG_TAG, "onResume() called");
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        DatabaseHelper.releaseDatabaseHelper(databaseHelper);
        Log.i(LOG_TAG, "released DatabaseHelper");
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        Log.i(LOG_TAG, "onPause() called");
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo)
    {
        super.onCreateContextMenu(menu, v, menuInfo);

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.edit_item, menu);

        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        LessonItemI selected = foundLessonItems.get(info.position );
        menu.setHeaderTitle(selected.getWord());
        Log.i(LOG_TAG, "context menu is created");
    }

    @Override
    public boolean onContextItemSelected(final MenuItem item)
    {
        final AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        final LessonItemI targetItem = foundLessonItems.get(info.position);
        switch (item.getItemId())
        {
            case R.id.menu_delete:
                new AlertDialog.Builder(this)
                    .setTitle(R.string.remove_item)
                    .setMessage(R.string.remove_confirmation + targetItem.getWord() + "'?")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener()
                    {
                        public void onClick(DialogInterface dialog, int which)
                        {
                            DeleteWord task = new DeleteWord();
                            task.execute(targetItem);
                            adapter.removeItem(info.position);
                            LessonItemI removedItem = foundLessonItems.remove(info.position);
                            Log.i(LOG_TAG, "removed " + removedItem.getWord());
                        }
                    })
                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener()
                    {
                        public void onClick(DialogInterface dialog, int which)
                        {
                            // do nothing, get back to the list of items
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
                return true;
            case R.id.menu_edit:
//                Intent intent = new Intent(getApplicationContext(), LessonItemEditorActivity.class);
                Intent intent = new Intent(getApplicationContext(), Editor.class);
                Bundle bundle = new Bundle();
                bundle.putParcelable(EDITED_ITEM, targetItem);
                intent.putExtras(bundle);
                startActivityForResult(intent, EDIT_ITEM_REQUEST);
                Log.i(LOG_TAG, "started Item Editor to edit " + targetItem.getWord());
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        Log.i(LOG_TAG, "Entered onActivityResult()");
        if (requestCode == EDIT_ITEM_REQUEST)
        {
            if (resultCode == RESULT_OK)
            {
                performSearchTask(keyword);
                Toast toast = Toast.makeText(getApplicationContext(), R.string.item_is_edited, Toast.LENGTH_SHORT);
                toast.show();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.search_item_action_bar, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();

        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        SearchView.OnQueryTextListener queryTextListener = new SearchView.OnQueryTextListener()
        {
            @Override
            public boolean onQueryTextChange(String newText)
            {
                return true;
            }
            @Override
            public boolean onQueryTextSubmit(String query)
            {
                keyword = query;
                if(keyword.length() != 0)
                {
                    performSearchTask(keyword);

                    if (foundLessonItems.size() == 0)
                    {
                        adapter.clear();
                        Toast.makeText(LessonItemsManagerActivity.this,
                                String.format(getString(R.string.no_results_found), keyword),
                                Toast.LENGTH_LONG).show();
                    }
                }
                return true;
            }
        };
        searchView.setOnQueryTextListener(queryTextListener);

        MenuItem resetSearch = menu.findItem(R.id.reset_search);
        resetSearch.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener()
        {
            @Override
            public boolean onMenuItemClick(MenuItem item)
            {
                performSearchTask("");
                Log.i(LOG_TAG, "list filled with all existent items");
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    private void performSearchTask(String keyword)
    {
        SearchWord task = new SearchWord(); // to refresh list of items
        try
        {
            foundLessonItems = task.execute(keyword).get();
            if (foundLessonItems.size() > 0)
            {
                adapter.clear();
                for (LessonItemI item : foundLessonItems)
                {
                    adapter.add(item);
                }
            }
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
        catch (ExecutionException e)
        {
            e.printStackTrace();
        }
        Log.i(LOG_TAG, "Search by " + keyword + " returned " + foundLessonItems.size() + " elements");
    }

    private class SearchWord extends AsyncTask<String, Integer, ArrayList<LessonItemI>>
    {
        private ArrayList<LessonItemI> foundItems = new ArrayList<LessonItemI>();

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            loadingIndicator.setVisibility(View.VISIBLE);
        }

        @Override
        protected ArrayList<LessonItemI> doInBackground(String... keyword)
        {
            if (databaseHelper == null)
            {
                databaseHelper = new DatabaseHelper(LessonItemsManagerActivity.this);
            }
            String keyWord = keyword[0];    //http://stackoverflow.com/questions/17549042/android-asynctask-passing-a-single-string
            foundItems = new LessonItemsHelper().searchItems(databaseHelper, keyWord);

            return foundItems;
        }

        @Override
        protected void onPostExecute(ArrayList<LessonItemI> result)
        {
            super.onPostExecute(result);
            loadingIndicator.setVisibility(View.GONE);
        }
    }

    private class DeleteWord extends AsyncTask<LessonItemI, Integer, Void>
    {
        private LessonItemsHelper lessonHelper;

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            loadingIndicator.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(LessonItemI... params)
        {
            if (databaseHelper == null)
            {
                databaseHelper = new DatabaseHelper(LessonItemsManagerActivity.this);
            }
            LessonItemI item = params[0];
            lessonHelper = new LessonItemsHelper();
            if (item instanceof Verb)
            {
                lessonHelper.deleteVerb(databaseHelper, (Verb) item);
            }
            if (item instanceof Noun)
            {
                lessonHelper.deleteNoun(databaseHelper, (Noun) item);
            }
            if (item instanceof Adjektive)
            {
                lessonHelper.deleteAdjektive(databaseHelper, (Adjektive) item);
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result)
        {
            super.onPostExecute(result);
            loadingIndicator.setVisibility(View.GONE);
        }
    }
}