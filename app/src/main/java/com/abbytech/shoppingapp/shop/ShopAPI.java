package com.abbytech.shoppingapp.shop;


import com.abbytech.shoppingapp.model.Item;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

public interface ShopAPI {
    @GET("Items/")
    Observable<List<Item>> getItems();
    @GET("Items/{id}")
    Observable<Item> getItem(@Path("id") int id);
}
