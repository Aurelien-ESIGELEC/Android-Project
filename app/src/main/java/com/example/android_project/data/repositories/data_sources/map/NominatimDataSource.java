package com.example.android_project.data.repositories.data_sources.map;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.example.android_project.api.fuel_price.pojo.FuelPrices;
import com.example.android_project.api.nominatim.NominatimService;
import com.example.android_project.api.nominatim.pojo.NominatimAddress;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NominatimDataSource {

    private static final String TAG = "NominatimDataSource";
    private NominatimService nominatimService;
    private MutableLiveData<List<NominatimAddress>> nominatimAddressLiveData;
    private ExecutorService executorService;

    public NominatimDataSource() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://nominatim.openstreetmap.org")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        this.nominatimService = retrofit.create(NominatimService.class);
        this.executorService = Executors.newFixedThreadPool(1);
        this.nominatimAddressLiveData = new MutableLiveData<>();
    }

    public void getAddressBySearch(String search) {

        executorService.execute(() ->
                nominatimService
                        .getAddressBySearch(search)
                        .enqueue(
                                new Callback<List<NominatimAddress>>() {
                                    @Override
                                    public void onResponse(
                                            @NonNull Call<List<NominatimAddress>> call,
                                            @NonNull Response<List<NominatimAddress>> response
                                    ) {
                                        Log.d(TAG, "onResponse: " + response);
                                        if (response.isSuccessful()) {
                                            nominatimAddressLiveData.setValue(response.body());
                                        }
                                    }

                                    @Override
                                    public void onFailure(
                                            @NonNull Call<List<NominatimAddress>> call,
                                            @NonNull Throwable t
                                    ) {
                                        nominatimAddressLiveData.setValue(null);
                                    }
                                }
                        )
        );
    }

    public MutableLiveData<List<NominatimAddress>> getNominatimAddressLiveData() {
        return nominatimAddressLiveData;
    }
}
