package com.projectpractice.lqlm.utils;

import android.content.Context;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitManager {
    private static RetrofitManager ourInstance;
    private final Retrofit retrofit;

    public static RetrofitManager getInstance(Context context) {
        ourInstance = new RetrofitManager(context);
        return ourInstance;
    }

    public static RetrofitManager getInstance() {
        ourInstance = new RetrofitManager();
        return ourInstance;
    }


    private RetrofitManager(Context context) {
        OkHttpClient client = new OkHttpClient.Builder()
//                .addNetworkInterceptor(new LoggingInterceptor())
//                .addInterceptor(new AddCookiesInterceptor(context))
//                .addInterceptor(new SaveCookiesInterceptor(context))
                .build();

        retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
    }

    private RetrofitManager() {
        OkHttpClient client = new OkHttpClient.Builder()
                .build();

        retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
    }

    public Retrofit getRetrofit() {
        return retrofit;
    }
}
