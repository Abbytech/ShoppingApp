package com.abbytech.shoppingapp.shop;


import android.support.annotation.IntDef;

import com.abbytech.shoppingapp.framework.OnItemActionListener;
import com.abbytech.shoppingapp.model.Item;

public interface OnShopItemActionListener extends OnItemActionListener<Item> {
    int ACTION_ADD = 0;

    @Override
    void onItemAction(Item item, @Action int action);

    @IntDef({ACTION_ADD})
    @interface Action {
    }
}
