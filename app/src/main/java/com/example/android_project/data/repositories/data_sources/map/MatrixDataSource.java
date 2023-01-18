package com.example.android_project.data.repositories.data_sources.map;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.android_project.api.fuel_price.pojo.FuelPrices;
import com.example.android_project.api.matrix.MatrixService;
import com.example.android_project.api.matrix.pojo.request.MatrixRequestBody;
import com.example.android_project.api.matrix.pojo.response.MatrixInfo;

import org.osmdroid.util.GeoPoint;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MatrixDataSource {

    private static final String TAG = "MatrixDataSource";
    private final MatrixService matrixService;
    private MutableLiveData<MatrixInfo> matrixInfoMutableLiveData;
    private final ExecutorService executorService;

    public MatrixDataSource() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://openrouteservice.org")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        this.matrixService = retrofit.create(MatrixService.class);
        this.executorService = Executors.newFixedThreadPool(3);
        this.matrixInfoMutableLiveData = new MutableLiveData<>();
    }

    public void updateDistanceBetweenPoints(List<Double> sources, List<Double> destination) {

        MatrixRequestBody matrixRequestBody = new MatrixRequestBody()
                .setDestinations(Collections.singletonList(1))
                .setSources(Collections.singletonList(0))
                .setLocations(Arrays.asList(sources, destination))
                .setMetrics(Arrays.asList("distance", "duration"))
                .setUnits("km");

        executorService.execute(() -> matrixService
                .getDistanceBetweenPoint(matrixRequestBody)
                .enqueue(new Callback<MatrixInfo>() {
                    @Override
                    public void onResponse(Call<MatrixInfo> call, Response<MatrixInfo> response) {
                        if (response.isSuccessful()) {
                            Log.d(TAG, "onResponse: "+response);
                            matrixInfoMutableLiveData.postValue(response.body());
                        }
                    }

                    @Override
                    public void onFailure(Call<MatrixInfo> call, Throwable t) {
                        matrixInfoMutableLiveData.postValue(null);
                    }
                })
        );

    }

    public MutableLiveData<MatrixInfo> getMatrixInfoMutableLiveData() {
        return matrixInfoMutableLiveData;
    }
}
