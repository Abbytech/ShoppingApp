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
import android.widget.Toast;

import com.abbytech.shoppingapp.databinding.ViewListItemBinding;
import com.abbytech.shoppingapp.model.ListItem;
import com.abbytech.shoppingapp.model.ShoppingList;
import com.abbytech.util.adapter.DataBindingRecyclerAdapter;

import java.util.List;

import rx.Observable;

public class ShoppingListFragment extends Fragment {
    private ShoppingListAdapter adapter;
    private ShoppingList shoppingList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_shopping_list, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.list_recycler);
        adapter = new ShoppingListAdapter(null);
        loadShoppingList(1);
        recyclerView.setAdapter(adapter);
    }
    public void loadShoppingList(int id) {
        Observable<ShoppingList> shoppingListObservable = ShoppingApp.getInstance().getShoppingListRepo()
                .getShoppingList(id);
        shoppingListObservable.doOnError(throwable ->
                Toast.makeText(getActivity(), "Error while getting list", Toast.LENGTH_SHORT).show());
        shoppingListObservable.subscribe(this::setShoppingList);
    }

    public void setShoppingList(ShoppingList list) {
        this.shoppingList = list;
        if (adapter != null) adapter.setItemList(this.shoppingList.getItems());
    }

    class ShoppingListAdapter extends DataBindingRecyclerAdapter<ListItem> {

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
            CheckBox checkbox = (CheckBox) holder.itemView.findViewById(R.id.checkbox_item);
            checkbox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                int position = holder.getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    ListItem item = getItem(position);
                    if (item.isChecked() != isChecked) {
                        item.setChecked(isChecked);
                        set(position, item);
                    }
                }
            });
        }

        @Override
        protected int getDataBindingVariableId(int position) {
            return com.abbytech.shoppingapp.BR.item;
        }
    }
}
