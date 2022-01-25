package com.example.androidproject.ui.ui.history.map;

import io.reactivex.Single;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiInterface {
    @GET("maps/api/directions/json")
    Single<Result> gitDirection(@Query("mode") String mode,
                                @Query("transit_routing_preferance") String pref
            , @Query("origin") String origin
            , @Query("destinaiton") String destinaiton
            , @Query("key") String key);
}