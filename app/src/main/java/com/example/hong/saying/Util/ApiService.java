package com.example.hong.saying.Util;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by hong on 2018-04-26.
 */

public interface ApiService {
    static final String BASE_URL = "http://pixabay.com/";
    static final String APP_KEY = "8739410-14e2b9d475db74a8ac2e77330";

    @GET("api")
    Call<PixabayImage> getRandomImage(@Query("key") String appKey,
                                      @Query("q") String keyword,
                                      @Query("order") String order,
                                      @Query("image_type") String type);


    @GET("api")
    Call<PixabayImage> getMoreImage(@Query("key") String appKey,
                                    @Query("q") String keyword,
                                    @Query("order") String order,
                                    @Query("image_type") String type,
                                    @Query("page") int page);


}
