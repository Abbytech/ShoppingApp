package com.abbytech.shoppingapp.shop;


import android.os.Bundle;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;

import com.abbytech.shoppingapp.framework.OnItemActionListener;
import com.abbytech.shoppingapp.model.Item;

public interface OnShopItemActionListener extends OnItemActionListener<Item> {
    int ACTION_ADD = 0;

    @Override
    void onItemAction(Item item, @Action int action, @Nullable Bundle extras);

    @Override
    void onItemAction(Item item, int action);

    @IntDef({ACTION_ADD})
    @interface Action {
    }

}
