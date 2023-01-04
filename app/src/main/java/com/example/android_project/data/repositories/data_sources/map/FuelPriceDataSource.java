package com.example.android_project.data.repositories.data_sources.map;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.android_project.api.fuel_price.FuelPriceService;
import com.example.android_project.api.fuel_price.pojo.FuelPrices;

import java.util.List;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FuelPriceDataSource {

    private FuelPriceService fuelPriceService;
    private MutableLiveData<FuelPrices> fuelPriceMutableLiveData;

    public FuelPriceDataSource() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://data.economie.gouv.fr/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        this.fuelPriceService = retrofit.create(FuelPriceService.class);
        this.fuelPriceMutableLiveData = new MutableLiveData<>();
    }

    public void updateFuelPriceByDistance(float lat, float lon, float dist) {

        fuelPriceService.getPriceByDistance(lat + "," + lon + "," + dist).enqueue(new Callback<FuelPrices>() {
            @Override
            public void onResponse(Call<FuelPrices> call, Response<FuelPrices> response) {
                if (response.isSuccessful()) {
                    fuelPriceMutableLiveData.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<FuelPrices> call, Throwable t) {
                fuelPriceMutableLiveData.setValue(null);
            }
        });


    }

    public void updateFuelPriceByDistance(float lat, float lon, float dist, List<String> excludedIds) {

        fuelPriceService.getPriceByDistance(lat + "," + lon + "," + dist, excludedIds).enqueue(new Callback<FuelPrices>() {
            @Override
            public void onResponse(Call<FuelPrices> call, Response<FuelPrices> response) {
                if (response.isSuccessful()) {
                    fuelPriceMutableLiveData.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<FuelPrices> call, Throwable t) {
                fuelPriceMutableLiveData.setValue(null);
            }
        });


    }

    public MutableLiveData<FuelPrices> getFuelPriceMutableLiveData() {
        return fuelPriceMutableLiveData;
    }
}
