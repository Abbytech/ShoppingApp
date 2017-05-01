package com.abbytech.shoppingapp;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import com.abbytech.shoppingapp.framework.OnItemActionListener;
import com.abbytech.shoppingapp.model.ListItem;
import com.abbytech.shoppingapp.model.ShoppingList;
import com.abbytech.shoppingapp.repo.IShoppingListRepo;
import com.abbytech.shoppingapp.shoppinglist.OnShoppingListItemActionListener;

import java.util.List;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;

public class PriceCalculator implements OnItemActionListener {
    OnTotalChangedListener listener;
    private IShoppingListRepo repo;
    private String TAG = PriceCalculator.class.getSimpleName();

    public PriceCalculator(IShoppingListRepo repo) {
        this.repo = repo;
        recalculate();
    }

    private void recalculate() {
        repo.getShoppingList(1)
                .map(ShoppingList::getItems)
                .flatMap(this::calculateTotal)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::notifyListener);
    }

    public void setListener(OnTotalChangedListener listener) {
        this.listener = listener;
    }

    private void notifyListener(float total) {
        if (total < 0)
            throw new IllegalStateException(String.format("total is less than 0, actual=%1$f", total));
        if (listener != null) listener.onTotalChanged(total);
    }

    private Observable<Float> calculateTotal(List<ListItem> listItems) {
        return Observable.from(listItems)
                .filter(ListItem::isChecked)
                .map(this::calculateListItemPrice)
                .reduce(0F, (num1, num2) -> num1 + num2)
                .doOnError(throwable -> Log.d(TAG, "calculateTotal: ", throwable))
                .onErrorReturn(throwable -> 0F);
    }

    private float calculateListItemPrice(ListItem listItem) {
        String dirtyPriceString = listItem.getItem().getPrice();
        String priceString = dirtyPriceString.substring(0, dirtyPriceString.indexOf('J') - 1).trim();
        float itemPrice = Float.parseFloat(priceString);
        return itemPrice * (float) listItem.getQuantity();
    }

    @Override
    public void onItemAction(Object item, @OnShoppingListItemActionListener.Action int action) {
        recalculate();
    }

    @Override
    public void onItemAction(Object item, int action, @Nullable Bundle extra) {

    }

    public interface OnTotalChangedListener {
        void onTotalChanged(float total);
    }
}
