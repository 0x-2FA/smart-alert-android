package com.unipi.projects.smartalert.Services;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;


public class RetrofitService {
    public static Retrofit retrofit;
    private String url;

    public RetrofitService(String url) {
        this.url = url;
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

        retrofit = new Retrofit.Builder()
                .baseUrl(this.url)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build())
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .build();
    }

}
