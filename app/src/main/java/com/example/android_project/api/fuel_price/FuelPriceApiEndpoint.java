package com.example.android_project.api.fuel_price;

import com.example.android_project.api.fuel_price.pojo.FuelPrice;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface FuelPriceApiEndpoint {

    @GET("/search/?dataset=prix-carburants-fichier-instantane-test-ods-copie")
    Call<FuelPrice> getPriceByDistance(@Query("geofilter.distance") String distance);

}
