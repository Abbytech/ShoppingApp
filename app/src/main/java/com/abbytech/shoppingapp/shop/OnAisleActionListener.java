package com.abbytech.shoppingapp.shop;


import android.support.annotation.IntDef;

import com.abbytech.shoppingapp.framework.OnItemActionListener;
import com.abbytech.shoppingapp.shop.aisles.Aisle;

public interface OnAisleActionListener extends OnItemActionListener<Aisle> {
    int ACTION_SELECTED = 0;

    @Override
    void onItemAction(Aisle item, @Action int action);

    @IntDef({ACTION_SELECTED})
    @interface Action {
    }
}
