package com.example.android_project.view_models;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.example.android_project.api.fuel_price.pojo.Fields;
import com.example.android_project.api.fuel_price.pojo.FuelPrices;
import com.example.android_project.api.fuel_price.pojo.Record;
import com.example.android_project.data.models.fuel_price.Fuel;
import com.example.android_project.data.models.fuel_price.GasStation;
import com.example.android_project.data.repositories.MapRepository;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapViewModel extends ViewModel {

    private MutableLiveData<String> search;

    private MutableLiveData<List<GasStation>> gasStations;

    private MapRepository mapRepository;

    public MapViewModel() {
        this.mapRepository = new MapRepository();

        this.gasStations = new MutableLiveData<>();
    }

    public LiveData<Boolean> updateListStationsByLocation(float lat, float lon, float dist) {
        LiveData<List<GasStation>> newGasStation = this.mapRepository.getFuelPriceByDistance(lat, lon, dist);

        MediatorLiveData<Boolean> isUpdated = new MediatorLiveData<>();

        isUpdated.addSource(newGasStation,gasStations1 -> {
            Log.v("MapViewModel", String.valueOf(gasStations1 != null && gasStations1.size() > 0));
            if (gasStations1 != null && gasStations1.size() > 0) {
                Log.v("MapViewModel", gasStations1.toString());
                gasStations.setValue(gasStations1);
                isUpdated.setValue(true);
            } else {
                isUpdated.setValue(false);
            }
        });

        return isUpdated;
    }

    public LiveData<List<GasStation>> getGasStations() {
        return this.gasStations;
    }
}
