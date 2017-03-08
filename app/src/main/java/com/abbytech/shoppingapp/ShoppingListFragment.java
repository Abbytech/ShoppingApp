package com.abbytech.shoppingapp;


import android.app.Fragment;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import com.abbytech.shoppingapp.databinding.ViewListItemBinding;
import com.abbytech.util.adapter.DataBindingRecyclerAdapter;

import java.util.List;

public class ShoppingListFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.layout_recycler,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView recyclerView = (RecyclerView)view.findViewById(R.id.list_recycler);
        recyclerView.setAdapter(new ShoppingListAdapter(ShoppingList.getItems()));
    }

    class ShoppingListAdapter extends DataBindingRecyclerAdapter<ListItem>{

        public ShoppingListAdapter(List<ListItem> objects) {
            super(objects);
        }

        @Override
        protected ViewDataBinding onCreateBinding(LayoutInflater inflater, ViewGroup parent, int viewType) {
            return ViewListItemBinding.inflate(inflater, parent, false);
        }

        @Override
        protected void onViewHolderCreated(DataBindingViewHolder holder) {
            super.onViewHolderCreated(holder);
            CheckBox checkbox = (CheckBox)holder.itemView.findViewById(R.id.checkbox_item);
            checkbox.setOnCheckedChangeListener((buttonView, isChecked) ->
                    getItem(holder.getAdapterPosition()).setChecked(isChecked));
        }

        @Override
        protected int getDataBindingVariableId(int position) {
            return com.abbytech.shoppingapp.BR.item;
        }
    }
}
