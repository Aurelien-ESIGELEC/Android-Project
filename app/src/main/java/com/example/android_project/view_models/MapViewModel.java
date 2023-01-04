package com.example.android_project.view_models;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.android_project.data.models.fuel_price.GasStation;
import com.example.android_project.data.repositories.MapRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

public class MapViewModel extends ViewModel {

    private MutableLiveData<String> search;

    private MutableLiveData<List<GasStation>> gasStations;

    private MapRepository mapRepository;

    public MapViewModel() {
        this.mapRepository = new MapRepository();

        this.gasStations = new MutableLiveData<>();
    }

    public void updateListStationsByLocation(float lat, float lon, float dist) {
        MediatorLiveData<Boolean> mediatorLiveData = new MediatorLiveData<>();

        List<String> excludedIds = new ArrayList<>();
        if (this.gasStations.getValue() != null) {
            for (GasStation gasStation : this.gasStations.getValue()) {
                excludedIds.add(gasStation.getId());
            }
        }

        Executors.newSingleThreadExecutor().execute(() ->
                mediatorLiveData.addSource(this.mapRepository.getFuelPriceByDistance(lat, lon, dist, excludedIds), gasStations1 -> {
                    if (this.gasStations.getValue() != null) {
                        List<GasStation> gasStationList = new ArrayList<>(gasStations1);
                        gasStationList.addAll(this.gasStations.getValue());
                        this.gasStations.postValue(gasStationList);
                    } else {
                        this.gasStations.postValue(gasStations1);
                    }

                })
        );
    }

//    public void updateListStationsByLocation(float lat, float lon, float dist, List<String> excludedIds) {
//        Executors.newSingleThreadExecutor().execute(() ->
//                this.gasStations = this.mapRepository.getFuelPriceByDistance(lat, lon, dist, excludedIds)
//        );
//    }

    public LiveData<List<GasStation>> getGasStations() {
        return this.gasStations;
    }
}
