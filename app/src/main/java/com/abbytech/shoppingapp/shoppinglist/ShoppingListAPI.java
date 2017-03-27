package com.abbytech.shoppingapp.shoppinglist;


import com.abbytech.shoppingapp.model.ListItem;
import com.abbytech.shoppingapp.model.ShoppingList;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.adapter.rxjava.Result;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import rx.Observable;

// TODO: 20/03/2017 implement this
public interface ShoppingListAPI {
    @GET("ShoppingList/All")
    Observable<List<ListItem>> getShoppingList();

    @POST("ShoppingList/Save")
    Observable<Result<ResponseBody>> saveShoppingItem(@Body ListItem item);
    @GET("/")
    Observable<List<ShoppingList>> getShoppingLists();
}
