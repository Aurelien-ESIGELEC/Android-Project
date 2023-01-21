package com.example.android_project.api.fuel_price;

import com.example.android_project.api.fuel_price.pojo.FuelPriceAnalyze;
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

    @GET("/api/records/1.0/analyze/?dataset=prix-carburants-fichier-instantane-test-ods-copie&rows=-1&x=prix_nom&y.prix_moy.func=AVG&y.prix_moy.expr=prix_valeur")
    Call<FuelPriceAnalyze> getAvgPriceInFranceByFuelType(@Query("refine.prix_nom") String fuelType);

    @GET("/api/records/1.0/analyze/?dataset=prix-carburants-fichier-instantane-test-ods-copie&rows=-1&x=prix_nom&y.prix_moy.func=AVG&y.prix_moy.expr=prix_valeur")
    Call<FuelPriceAnalyze> getAvgPriceInPolygonByFuelType(@Query("geofilter.polygon") String polygon, @Query("refine.prix_nom") String fuelType);

    @GET("/api/records/1.0/analyze/?dataset=prix-carburants-fichier-instantane-test-ods-copie&rows=-1&x=prix_nom&y.prix_moy.func=AVG&y.prix_moy.expr=prix_valeur")
    Call<FuelPriceAnalyze> getAvgPriceInCountyByFuelType(@Query("refine.dep_name") String county, @Query("refine.prix_nom") String fuelType);
}
