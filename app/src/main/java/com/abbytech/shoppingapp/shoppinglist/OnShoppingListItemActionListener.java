package com.abbytech.shoppingapp.shoppinglist;


import android.support.annotation.IntDef;
import android.support.annotation.Nullable;

import com.abbytech.shoppingapp.framework.OnItemActionListener;
import com.abbytech.shoppingapp.model.Item;
import com.abbytech.shoppingapp.model.ListItem;

public interface OnShoppingListItemActionListener extends OnItemActionListener<ListItem> {
    int ACTION_CHECK = 0;
    int ACTION_DELETE = 1;
    int ACTION_MODIFY = 2;

    @Override
    void onItemAction(ListItem item, @Action int action);


    void onItemAction(Item item, @Action int action, @Nullable Long extras);

    @IntDef({ACTION_CHECK, ACTION_DELETE, ACTION_MODIFY})
    @interface Action {
    }
}
