package com.example.hong.saying.Util;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by hong on 2018-04-26.
 */

public class RetrofitCall {
    public static ApiService retrofit(){

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(logging)
                .build();

        Retrofit retrofit = new Retrofit.Builder().baseUrl(ApiService.BASE_URL)
                .client(client).addConverterFactory(GsonConverterFactory.create()).build();


        return retrofit.create(ApiService.class);
    }
}
