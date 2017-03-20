package com.abbytech.shoppingapp.shoppinglist;


import com.abbytech.shoppingapp.model.ListItem;
import com.abbytech.shoppingapp.model.ShoppingList;

import java.util.List;

import retrofit2.adapter.rxjava.Result;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import rx.Observable;

public interface ShoppingListAPI {
    @GET("/{id}")
    Observable<ShoppingList> getShoppingList(@Path("id") int id);
    @POST("/")
    Observable<Result> saveShoppingItem(ListItem item);
    @GET("/")
    Observable<List<ShoppingList>> getShoppingLists();
}
