package com.abbytech.shoppingapp.shoppinglist;


import android.util.Log;

import com.abbytech.shoppingapp.model.ListItem;
import com.abbytech.shoppingapp.model.ShoppingList;
import com.abbytech.shoppingapp.repo.IShoppingListRepo;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.adapter.rxjava.Result;
import rx.Observable;
import rx.Subscriber;

public class RemoteShoppingListRepo implements IShoppingListRepo {
    ShoppingListAPI api;
    private String TAG = "shoppinglistrepo";

    public RemoteShoppingListRepo(ShoppingListAPI api) {
        this.api = api;
    }

    @Override
    public Observable<ShoppingList> getShoppingList(int id) {
        return api.getShoppingList().map(listItems -> {
            ShoppingList shoppingList = new ShoppingList(1L, "Default");
            shoppingList.setItems(listItems);
            return shoppingList;
        });
    }

    @Override
    public void saveShoppingItem(ListItem item) {
        api.saveShoppingItem(item).subscribe(new Subscriber<Result<ResponseBody>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "onError: ", e);
            }

            @Override
            public void onNext(Result<ResponseBody> responseBodyResult) {
                Log.d(TAG, "onNext: ");
            }
        });
    }

    @Override
    public Observable<List<ShoppingList>> getShoppingLists() {
        return api.getShoppingLists();
    }

    @Override
    public void deleteShoppingItem(ListItem item) {
        api.deleteShoppingItem(item.getId()).subscribe(new Subscriber<Result<ResponseBody>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "onError: delete failed", e);
            }

            @Override
            public void onNext(Result<ResponseBody> responseBodyResult) {
                Log.d(TAG, "onNext: delete succeeded " + responseBodyResult.toString());
            }
        });
    }
}
