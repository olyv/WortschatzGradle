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
    private List<LessonItemI> mItems = new ArrayList<LessonItemI>();
    private final Context mContext;
    private LayoutInflater inflater;

    private static final String LOG_TAG = "ItemsListAdapterLog";

    static class ViewHolder
    {
        TextView foundItemTextView;
        TextView foundItemTranslationTextView;
    }

    public ItemsListAdapter(Context context, ArrayList<LessonItemI> items)
    {
        this.mItems = items;
        this.mContext = context;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    // Add aItem to the adapter, notify observers that the data set has changed
    public void add(LessonItemI item)
    {
        mItems.add(item);
        notifyDataSetChanged();
    }

    // Clears the list adapter of all items.
    public void clear()
    {
        mItems.clear();
        notifyDataSetChanged();
        Log.i(LOG_TAG, "list adapter is cleared from all items");
    }

    //remove single item
    public void removeItem(int pos)
    {
        mItems.remove(pos);
        notifyDataSetChanged();
        Log.i(LOG_TAG, "item with position " + pos + " removed from list adapter");
    }

    // Returns the number of ToDoItems
    @Override
    public int getCount()
    {
        return mItems.size();
    }

    // Retrieve the number of ToDoItems
    @Override
    public LessonItemI getItem(int pos)
    {
        return mItems.get(pos);
    }

    @Override
    public long getItemId(int pos)
    {
        return mItems.get(pos).getId();
    }

    // Create a View for the ToDoItem at specified position
    // Remember to check whether convertView holds an already allocated View
    // before created a new View.
    // Consider using the ViewHolder pattern to make scrolling more efficient
    // See: http://developer.android.com/training/improving-layouts/smooth-scrolling.html

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
