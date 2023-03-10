package com.app.activeparks.data;


import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;


public class NetworkModule {
    private  String BASE_URL = "https://web.sportforall.gov.ua";
    private  String URL_SEARCH = "https://api.hutsalod.com";
    private  String URL_LOCATION = "https://map.technodreams.biz";

    public ApiService getInterface() {
        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(new OkHttpClient.Builder()
                        .connectTimeout(60, TimeUnit.SECONDS)
                        .readTimeout(60, TimeUnit.SECONDS)
                        .writeTimeout(60, TimeUnit.SECONDS)
                        .build())
                .build().create(ApiService.class);
    }

    public ApiService getInterfaceSearch() {
        return new Retrofit.Builder()
                .baseUrl(URL_SEARCH)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(new OkHttpClient.Builder().build())
                .build().create(ApiService.class);
    }

    public ApiService getInterfacLocation() {
        return new Retrofit.Builder()
                .baseUrl(URL_LOCATION)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(new OkHttpClient.Builder().build())
                .build().create(ApiService.class);
    }
}
