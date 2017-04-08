package com.abbytech.shoppingapp.notification;


import com.abbytech.shoppingapp.model.Beacon;
import com.abbytech.shoppingapp.model.ListItem;

import java.util.List;

import retrofit2.http.Body;
import retrofit2.http.POST;
import rx.Observable;

public interface LocationAPI {
    @POST("Beacon/GetuncheckedItem")
    Observable<List<ListItem>> onExitLocation(@Body Beacon beaconID);
}
