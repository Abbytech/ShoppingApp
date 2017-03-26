package com.abbytech.shoppingapp.framework;


public interface ItemActionEmitter<T> {
    void setOnItemActionListener(OnItemActionListener<T> listener);
}
