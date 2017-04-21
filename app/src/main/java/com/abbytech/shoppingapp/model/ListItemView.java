package com.abbytech.shoppingapp.model;


import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.ObservableBoolean;

import com.abbytech.shoppingapp.BR;
import com.abbytech.shoppingapp.util.ActionModeDelegate;

public class ListItemView extends BaseObservable implements ActionModeDelegate.Selectable {
    private ObservableBoolean selected = new ObservableBoolean(false);
    private ListItem listItem;

    public ListItemView(ListItem listItem) {
        this.listItem = listItem;
    }

    @Bindable
    public boolean isSelected() {
        return selected.get();
    }

    public void setSelected(boolean selected) {
        this.selected.set(selected);
        notifyPropertyChanged(BR.selected);
    }

    public ListItem getListItem() {
        return listItem;
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof ListItemView && this.listItem.equals(((ListItemView) other).listItem);
    }
}
