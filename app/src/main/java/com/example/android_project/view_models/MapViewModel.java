package com.example.android_project.view_models;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.android_project.data.models.fuel_price.GasStation;
import com.example.android_project.data.repositories.MapRepository;

import java.util.List;
import java.util.concurrent.Executors;

public class MapViewModel extends ViewModel {

    private MutableLiveData<String> search;

    private LiveData<List<GasStation>> gasStations;

    private MapRepository mapRepository;

    public MapViewModel() {
        this.mapRepository = new MapRepository();

        this.gasStations = new MutableLiveData<>();
    }

    public void updateListStationsByLocation(float lat, float lon, float dist) {
        Executors.newSingleThreadExecutor().execute(() ->
                this.gasStations = this.mapRepository.getFuelPriceByDistance(lat, lon, dist)
        );
    }

    public LiveData<List<GasStation>> getGasStations() {
        return this.gasStations;
    }
}
