package com.abbytech.shoppingapp;


import java.util.Properties;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.schedulers.Schedulers;

public class RetrofitFactory {

    public static Retrofit createRetrofit(Properties properties, OkHttpClient.Builder clientBuilder) {
        String baseUrl = properties.getProperty("BASE_URL");
        String level = properties.getProperty("LOGGER_LEVEL");
        OkHttpClient client = clientBuilder
                .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.valueOf(level)))
                .build();

        return new Retrofit.Builder()
                .client(client)
                .baseUrl(baseUrl)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.createWithScheduler(Schedulers.newThread()))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }
}
