package com.example.android_project.data.data_sources.map;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.android_project.data.api.nominatim.NominatimService;
import com.example.android_project.data.api.nominatim.pojo.NominatimReverse;
import com.example.android_project.data.api.nominatim.pojo.NominatimSearch;

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
    private final NominatimService nominatimService;
    private final MutableLiveData<List<NominatimSearch>> nominatimAddressLiveData;
    private final ExecutorService executorService;

    public NominatimDataSource() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://nominatim.openstreetmap.org")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        this.nominatimService = retrofit.create(NominatimService.class);
        this.executorService = Executors.newFixedThreadPool(1);
        this.nominatimAddressLiveData = new MutableLiveData<>();
    }

    /**
     *
     * @param search
     */
    public void getAddressBySearch(String search) {

        executorService.execute(() ->
                nominatimService
                        .getAddressBySearch(search)
                        .enqueue(
                                new Callback<List<NominatimSearch>>() {
                                    @Override
                                    public void onResponse(
                                            @NonNull Call<List<NominatimSearch>> call,
                                            @NonNull Response<List<NominatimSearch>> response
                                    ) {
                                        Log.d(TAG, "onResponse: " + response);
                                        if (response.isSuccessful()) {
                                            nominatimAddressLiveData.setValue(response.body());
                                        }
                                    }

                                    @Override
                                    public void onFailure(
                                            @NonNull Call<List<NominatimSearch>> call,
                                            @NonNull Throwable t
                                    ) {
                                        nominatimAddressLiveData.setValue(null);
                                    }
                                }
                        )
        );
    }

    /**
     *
     * @param lat
     * @param lon
     * @return
     */
    public LiveData<NominatimReverse> getCountyByReverse(String lat, String lon) {
        MutableLiveData<NominatimReverse> nominatimReverseMutableLiveData = new MutableLiveData<>();
        executorService.execute(() ->
                nominatimService
                        .getCountyByReverse(lat, lon)
                        .enqueue(new Callback<NominatimReverse>() {
                                     @Override
                                     public void onResponse(@NonNull Call<NominatimReverse> call, @NonNull Response<NominatimReverse> response) {
                                         Log.d(TAG, "onResponse: " + response);
                                         if (response.isSuccessful()) {
                                             nominatimReverseMutableLiveData.setValue(response.body());
                                         }
                                     }

                                     @Override
                                     public void onFailure(@NonNull Call<NominatimReverse> call, @NonNull Throwable t) {
                                         nominatimReverseMutableLiveData.setValue(null);
                                     }
                                 }
                        )
        );

        return nominatimReverseMutableLiveData;
    }

    /**
     *
     * @param lat
     * @param lon
     * @return
     */
    public LiveData<NominatimReverse> getCityByReverse(String lat, String lon) {
        MutableLiveData<NominatimReverse> nominatimReverseMutableLiveData = new MutableLiveData<>();
        executorService.execute(() ->
                nominatimService
                        .getCityByReverse(lat, lon)
                        .enqueue(new Callback<NominatimReverse>() {
                                     @Override
                                     public void onResponse(@NonNull Call<NominatimReverse> call, @NonNull Response<NominatimReverse> response) {
                                         Log.d(TAG, "onResponse: " + response);
                                         if (response.isSuccessful()) {
                                             nominatimReverseMutableLiveData.setValue(response.body());
                                         }
                                     }

                                     @Override
                                     public void onFailure(@NonNull Call<NominatimReverse> call, @NonNull Throwable t) {
                                         nominatimReverseMutableLiveData.setValue(null);
                                     }
                                 }
                        )
        );

        return nominatimReverseMutableLiveData;
    }


    public MutableLiveData<List<NominatimSearch>> getNominatimAddressLiveData() {
        return nominatimAddressLiveData;
    }
}
