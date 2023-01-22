package com.example.android_project.data.data_sources.map;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.android_project.data.api.fuel_price.FuelPriceService;
import com.example.android_project.data.api.fuel_price.pojo.FuelPriceAnalyze;
import com.example.android_project.data.api.fuel_price.pojo.FuelPrices;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FuelPriceDataSource {

    private static final String TAG = "FuelPriceDataSource";
    private FuelPriceService fuelPriceService;
    private MutableLiveData<FuelPrices> fuelPriceMutableLiveData;
    private ExecutorService executorService;

    public FuelPriceDataSource() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://data.economie.gouv.fr/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        this.fuelPriceService = retrofit.create(FuelPriceService.class);
        this.executorService = Executors.newFixedThreadPool(10);
        this.fuelPriceMutableLiveData = new MutableLiveData<>();
    }

    public void updateFuelPriceByDistance(float lat, float lon, float dist) {

        executorService.execute(() -> fuelPriceService
                .getPriceByDistance(lat + "," + lon + "," + dist)
                .enqueue(new Callback<FuelPrices>() {
                    @Override
                    public void onResponse(Call<FuelPrices> call, Response<FuelPrices> response) {
                        if (response.isSuccessful()) {
                            fuelPriceMutableLiveData.postValue(response.body());
                        }
                    }

                    @Override
                    public void onFailure(Call<FuelPrices> call, Throwable t) {
                        fuelPriceMutableLiveData.postValue(null);
                    }
                })
        );
    }

    public void updateFuelPriceByDistance(float lat, float lon, float dist, List<String> excludedIds) {

        executorService.execute(() ->fuelPriceService.getPriceByDistance(lat + "," + lon + "," + dist, excludedIds).enqueue(new Callback<FuelPrices>() {
            @Override
            public void onResponse(@NonNull Call<FuelPrices> call, @NonNull Response<FuelPrices> response) {
                if (response.isSuccessful()) {
                    Log.d(TAG, "onResponse: " + response);
                    Log.d(TAG, "onResponse: " + response.body());
                    fuelPriceMutableLiveData.postValue(response.body());
                }
            }

            @Override
            public void onFailure(@NonNull Call<FuelPrices> call, @NonNull Throwable t) {
                fuelPriceMutableLiveData.postValue(null);
            }
        })
        );


    }

    public LiveData<List<FuelPriceAnalyze>> getAvgPriceInFranceByFuelType(String fuelType) {
        MutableLiveData<List<FuelPriceAnalyze>> mutableLiveData = new MutableLiveData<>();

        executorService.execute(() -> fuelPriceService
                .getAvgPriceInFranceByFuelType(fuelType)
                .enqueue(new Callback<List<FuelPriceAnalyze>>() {
                    @Override
                    public void onResponse(Call<List<FuelPriceAnalyze>> call, Response<List<FuelPriceAnalyze>> response) {
                        Log.d(TAG, "onResponse: "+ response);
                        if (response.isSuccessful()) {
                            Log.d(TAG, "onResponse: " + response.body());
                            mutableLiveData.postValue(response.body());
                        }
                    }

                    @Override
                    public void onFailure(Call<List<FuelPriceAnalyze>> call, Throwable t) {
                        Log.e(TAG, "onFailure: " + t);
                        mutableLiveData.postValue(null);
                    }
                })
        );

        return mutableLiveData;
    }

    public LiveData<List<FuelPriceAnalyze>> getAvgPriceInCountyByFuelType(String fuelType, String county) {
        MutableLiveData<List<FuelPriceAnalyze>> mutableLiveData = new MutableLiveData<>();

        executorService.execute(() -> fuelPriceService
                .getAvgPriceInCountyByFuelType(fuelType, county)
                .enqueue(new Callback<List<FuelPriceAnalyze>>() {
                    @Override
                    public void onResponse(Call<List<FuelPriceAnalyze>> call, Response<List<FuelPriceAnalyze>> response) {
                        Log.d(TAG, "onResponse: "+ response);
                        if (response.isSuccessful()) {
                            Log.d(TAG, "onResponse: " + response.body());
                            mutableLiveData.postValue(response.body());
                        }
                    }

                    @Override
                    public void onFailure(Call<List<FuelPriceAnalyze>> call, Throwable t) {
                        Log.e(TAG, "onFailure: " + t);
                        mutableLiveData.postValue(null);
                    }
                })
        );

        return mutableLiveData;
    }

    public LiveData<List<FuelPriceAnalyze>> getAvgPriceInCityByFuelType(String fuelType, String city) {
        MutableLiveData<List<FuelPriceAnalyze>> mutableLiveData = new MutableLiveData<>();

        executorService.execute(() -> fuelPriceService
                .getAvgPriceInCityByFuelType(fuelType, city)
                .enqueue(new Callback<List<FuelPriceAnalyze>>() {
                    @Override
                    public void onResponse(Call<List<FuelPriceAnalyze>> call, Response<List<FuelPriceAnalyze>> response) {
                        Log.d(TAG, "onResponse: "+ response);
                        if (response.isSuccessful()) {
                            Log.d(TAG, "onResponse: " + response.body());
                            mutableLiveData.postValue(response.body());
                        }
                    }

                    @Override
                    public void onFailure(Call<List<FuelPriceAnalyze>> call, Throwable t) {
                        Log.e(TAG, "onFailure: " + t);
                        mutableLiveData.postValue(null);
                    }
                })
        );

        return mutableLiveData;
    }

    public MutableLiveData<FuelPrices> getFuelPriceMutableLiveData() {
        return fuelPriceMutableLiveData;
    }
}
