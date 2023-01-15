package com.example.android_project.api.fuel_price;

import com.example.android_project.api.fuel_price.pojo.FuelPrices;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface FuelPriceService {

    @GET("/api/records/1.0/search/?dataset=prix-carburants-fichier-instantane-test-ods-copie&rows=1000")
    Call<FuelPrices> getPriceByDistance(@Query("geofilter.distance") String distance);

    @GET("/api/records/1.0/search/?dataset=prix-carburants-fichier-instantane-test-ods-copie&rows=1000")
    Call<FuelPrices> getPriceByDistance(@Query("geofilter.distance") String distance, @Query("exclude.id")List<String> excludedId);
}
