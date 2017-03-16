package com.abbytech.util.adapter;

import android.databinding.ObservableList;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

abstract public class RecyclerAdapter<T, VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {
    private final List<VH> viewHolders = new ArrayList<>();
    private List<T> objects;
    private OnItemClickListener<T> onItemClickListener;

    public RecyclerAdapter(List<T> objects) {
        setItemList(objects);
    }

    public void setOnItemClickListener(final OnItemClickListener<T> onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
        for (final VH viewHolder : viewHolders) setOnClickListener(onItemClickListener, viewHolder);
    }

    private void setOnClickListener(final OnItemClickListener<T> onItemClickListener,
                                    final VH viewHolder) {
        View.OnClickListener clickListener = null;
        if (onItemClickListener != null) {
            clickListener = new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    T item = getItem(viewHolder.getAdapterPosition());
                    onItemClickListener.onItemClicked(item);
                }
            };
        }
        viewHolder.itemView.setOnClickListener(clickListener);
    }

    public void setItemList(List<T> objects) {
        this.objects = objects;
        if (objects instanceof ObservableList)
            ((ObservableList<T>) objects)
                    .addOnListChangedCallback(new ObservableListCallback(this));
        notifyDataSetChanged();
    }

    public void clear() {
        objects.clear();
    }

    public void addAll(Collection<T> items) {
        objects.addAll(items);
    }

    public void add(T item){
        objects.add(item);
    }

    public void remove(T item){
        objects.remove(item);
    }

    public void set(int index,T item){
        objects.set(index,item);
    }
    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        VH viewHolder = doCreateViewHolder(parent, viewType);
        viewHolders.add(viewHolder);
        onViewHolderCreated(viewHolder);
        return viewHolder;
    }

    protected abstract VH doCreateViewHolder(ViewGroup parent, int viewType);

    protected void onViewHolderCreated(VH holder) {
        setOnClickListener(onItemClickListener, holder);
    }

    public T getItem(int position) {
        return objects.get(position);
    }

    @Override
    public int getItemCount() {
        return objects != null ? objects.size() : 0;
    }

    public interface OnItemClickListener<T> {
        void onItemClicked(T item);
    }
}
