package com.abbytech.shoppingapp.framework;


import android.os.Bundle;
import android.support.annotation.Nullable;

public interface OnItemActionListener<T> {
    void onItemAction(T item, int action);

    void onItemAction(T item, int action, @Nullable Bundle extra);
}
