package com.abbytech.util.adapter;

import android.databinding.ObservableArrayList;
import android.databinding.ObservableList;
import android.support.v7.widget.RecyclerView;

public class ObservableListCallback extends ObservableArrayList.OnListChangedCallback {
    RecyclerView.Adapter adapter;

    public ObservableListCallback(RecyclerView.Adapter adapter) {
        this.adapter = adapter;
    }

    @Override
    public void onChanged(ObservableList observableList) {
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onItemRangeChanged(ObservableList observableList, int i, int i1) {
        adapter.notifyItemRangeChanged(i, i1);
    }

    @Override
    public void onItemRangeInserted(ObservableList observableList, int i, int i1) {
        adapter.notifyItemRangeInserted(i, i1);
    }

    @Override
    public void onItemRangeMoved(ObservableList observableList, int i, int i1, int i2) {
    }

    @Override
    public void onItemRangeRemoved(ObservableList observableList, int i, int i1) {
        adapter.notifyItemRangeRemoved(i, i1);
    }
}
