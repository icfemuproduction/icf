package com.example.application.iricf;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {

    private final static String BASE_URL = "http://icfchennai.000webhostapp.com/";
    private static Retrofit retrofit =  null;

    static Retrofit getClient(){
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create());

        retrofit = builder.build();
        return retrofit;
    }


}
