package com.abbytech.shoppingapp.account;


import okhttp3.ResponseBody;
import retrofit2.adapter.rxjava.Result;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import rx.Observable;

public interface AccountAPI {
    @GET("User/Login")
    Observable<Result<ResponseBody>> login();

    @POST("User/Register")
    Observable<Result<ResponseBody>> register(@Body RegisterData registerData);
}
