package com.example.application.iricf;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
/*
    http://d50a197f.ngrok.io/*/
    private final static String BASE_URL = "https://icfchennai.000webhostapp.com/";
    private static Retrofit retrofit =  null;

    static Retrofit getClient(){

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BASIC);

        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(100, TimeUnit.SECONDS)
                .addInterceptor(logging)
                .readTimeout(100,TimeUnit.SECONDS).build();

        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create());

        retrofit = builder.build();
        return retrofit;
    }


}
