package com.olyv.wortschatz.ui.manager;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.olyv.wortschatz.lesson.items.LessonItemI;
import com.olyv.wortschatz.ui.R;

import java.util.ArrayList;
import java.util.List;

public class ItemsListAdapter extends BaseAdapter
{
    private List<LessonItemI> listOfItems = new ArrayList<LessonItemI>();
    private final Context context;
    private LayoutInflater inflater;

    private static final String LOG_TAG = "ItemsListAdapterLog";

    static class ViewHolder
    {
        TextView foundItemTextView;
        TextView foundItemTranslationTextView;
    }

    public ItemsListAdapter(Context context, ArrayList<LessonItemI> items)
    {
        this.listOfItems = items;
        this.context = context;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    // Add aItem to the adapter, notify observers that the data set has changed
    public void add(LessonItemI item)
    {
        listOfItems.add(item);
        notifyDataSetChanged();
    }

    // Clears the list adapter of all items.
    public void clear()
    {
        listOfItems.clear();
        notifyDataSetChanged();
        Log.i(LOG_TAG, "list adapter is cleared from all items");
    }

    //remove single item
    public void removeItem(int pos)
    {
        listOfItems.remove(pos);
        notifyDataSetChanged();
        Log.i(LOG_TAG, "item with position " + pos + " removed from list adapter");
    }

    public void update(ArrayList<LessonItemI> newListOfItems)
    {
        listOfItems = newListOfItems;
        notifyDataSetChanged();
    }

    // Returns the number of Items
    @Override
    public int getCount()
    {
        return listOfItems.size();
    }

    // Retrieve the number of Items
    @Override
    public LessonItemI getItem(int pos)
    {
        return listOfItems.get(pos);
    }

    @Override
    public long getItemId(int pos)
    {
        return listOfItems.get(pos).getId();
    }

    // Reason to use ViewHolder: http://developer.android.com/training/improving-layouts/smooth-scrolling.html

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        ViewHolder holder;
        if (convertView == null)
        {
            convertView = inflater.inflate(R.layout.found_item, parent, false);
            holder = new ViewHolder();
            holder.foundItemTextView = (TextView) convertView.findViewById(R.id.foundItem);
            holder.foundItemTranslationTextView = (TextView) convertView.findViewById(R.id.foundItemTranslation);
            convertView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder) convertView.getTag();
        }

        LessonItemI item = getItem(position);

        holder.foundItemTextView.setText(item.getWord());
        holder.foundItemTranslationTextView.setText(item.getTranslation());

        Log.i(LOG_TAG, "getItemId() returns convertView");

        return convertView;
    }
}
