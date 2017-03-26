package com.abbytech.shoppingapp.shop;


import android.databinding.ViewDataBinding;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.abbytech.shoppingapp.BR;
import com.abbytech.shoppingapp.R;
import com.abbytech.shoppingapp.databinding.ViewShopItemAddableBinding;
import com.abbytech.shoppingapp.framework.ItemActionEmitter;
import com.abbytech.shoppingapp.framework.OnItemActionListener;
import com.abbytech.shoppingapp.model.Item;
import com.abbytech.util.adapter.DataBindingRecyclerAdapter;

import java.util.List;

import static com.abbytech.shoppingapp.shop.OnShopItemActionListener.ACTION_ADD;

class ShopItemAdapter extends DataBindingRecyclerAdapter<Item> implements ItemActionEmitter<Item> {
    private OnItemActionListener<Item> listener;

    public ShopItemAdapter(List<Item> objects) {
        super(objects);
    }

    @Override
    public void setOnItemActionListener(OnItemActionListener<Item> listener) {
        this.listener = listener;
    }
    @Override
    protected ViewDataBinding onCreateBinding(LayoutInflater inflater, ViewGroup parent, int viewType) {
        return ViewShopItemAddableBinding.inflate(inflater,parent,false);
    }

    @Override
    protected void onViewHolderCreated(DataBindingViewHolder holder) {
        super.onViewHolderCreated(holder);
        View view = holder.itemView.findViewById(R.id.imageButton_add);
        view.setOnClickListener(v -> {
            Item item = getItem(holder.getAdapterPosition());
            if (listener!=null) listener.onItemAction(item,ACTION_ADD);
        });
    }

    @Override
    protected int getDataBindingVariableId(int position) {
        return BR.shopItem;
    }

}
