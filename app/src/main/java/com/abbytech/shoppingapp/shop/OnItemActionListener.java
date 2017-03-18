package com.abbytech.shoppingapp.shop;


import android.support.annotation.IntDef;

import com.abbytech.shoppingapp.model.Item;

public interface OnItemActionListener {
    @IntDef({ACTION_ADD})
    @interface Action {}
    int ACTION_ADD = 0;
    void onItemAction(Item item, @Action int action);
}
