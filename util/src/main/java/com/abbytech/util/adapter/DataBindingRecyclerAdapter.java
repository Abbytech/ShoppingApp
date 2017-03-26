package com.abbytech.util.adapter;

import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public abstract class DataBindingRecyclerAdapter<T> extends
        RecyclerAdapter<T, DataBindingRecyclerAdapter.DataBindingViewHolder> {
    public DataBindingRecyclerAdapter(List<T> objects) {
        super(objects);
    }

    @Override
    protected DataBindingViewHolder doCreateViewHolder(ViewGroup parent, int viewType) {
        ViewDataBinding binding = onCreateBinding(LayoutInflater.from(parent.getContext()),
                parent, viewType);

        return new DataBindingViewHolder(binding.getRoot(), binding);
    }

    protected abstract ViewDataBinding onCreateBinding(LayoutInflater inflater, ViewGroup parent, int viewType);

    @Override
    public void onBindViewHolder(DataBindingViewHolder holder, int position) {
        T item = getItem(position);
        holder.getBinding().setVariable(getDataBindingVariableId(position), item);
    }

    protected abstract int getDataBindingVariableId(int position);

    public static class DataBindingViewHolder extends RecyclerView.ViewHolder {
        private ViewDataBinding binding;

        public DataBindingViewHolder(View itemView, ViewDataBinding binding) {
            super(itemView);
            this.binding = binding;
        }

        public ViewDataBinding getBinding() {
            return binding;
        }
    }
}


