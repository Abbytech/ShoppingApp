package com.abbytech.shoppingapp.shoppinglist;


import com.abbytech.shoppingapp.model.ListItem;
import com.abbytech.shoppingapp.model.ShoppingList;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.adapter.rxjava.Result;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import rx.Observable;

// TODO: 20/03/2017 implement this
public interface ShoppingListAPI {
    @GET("/{id}")
    Observable<ShoppingList> getShoppingList(@Path("id") int id);
    @POST("/")
    Observable<Result<ResponseBody>> saveShoppingItem(ListItem item);
    @GET("/")
    Observable<List<ShoppingList>> getShoppingLists();
}
