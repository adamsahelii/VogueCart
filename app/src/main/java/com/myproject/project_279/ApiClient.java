package com.myproject.project_279;

import com.google.gson.GsonBuilder;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    // IMPORTANT: point to your PHP backend + end with a slash
    private static final String BASE_URL = "http://10.0.2.2/snapshop-api/";

    private static final GsonBuilder gsonBuilder = new GsonBuilder()
            .setLenient();

    private static final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gsonBuilder.create()))
            .build();

    public static final ApiService retrofitService = retrofit.create(ApiService.class);
}
