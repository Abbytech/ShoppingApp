package com.abbytech.shoppingapp.shop;


import com.abbytech.shoppingapp.model.Item;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

public interface ShopAPI {
    @GET("Items/All/")
    Observable<List<Item>> getItems();

    @GET("Items/All")
    Observable<List<Item>> getItems(@Query("section") String section);
    @GET("Items/Get/{id}")
    Observable<Item> getItem(@Path("id") int id);
}
