package com.abbytech.util.adapter;

import android.support.v7.util.SortedList;
import android.support.v7.widget.RecyclerView;

public abstract class SortedListCallback<T extends Comparable> extends SortedList.Callback<T> {
    RecyclerView.Adapter adapter;

    public SortedListCallback(RecyclerView.Adapter adapter) {
        this.adapter = adapter;
    }

    @Override
    public abstract boolean areContentsTheSame(T oldItem, T newItem);

    @Override
    public int compare(T o1, T o2) {
        return o1.compareTo(o2);
    }

    @Override
    public void onInserted(int position, int count) {
        adapter.notifyItemRangeInserted(position, count);
    }

    @Override
    public void onRemoved(int position, int count) {
        adapter.notifyItemRangeRemoved(position, count);
    }

    @Override
    public void onMoved(int fromPosition, int toPosition) {
        adapter.notifyItemMoved(fromPosition, toPosition);
    }

    @Override
    public void onChanged(int position, int count) {
        adapter.notifyItemRangeChanged(position, count);
    }

    @Override
    public boolean areItemsTheSame(T item1, T item2) {
        return item1.equals(item2);
    }

}
