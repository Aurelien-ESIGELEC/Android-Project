package com.example.android_project.data.repositories;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.example.android_project.api.fuel_price.pojo.Fields;
import com.example.android_project.api.fuel_price.pojo.FuelPrices;
import com.example.android_project.api.fuel_price.pojo.Record;
import com.example.android_project.data.models.fuel_price.Fuel;
import com.example.android_project.data.models.fuel_price.GasStation;
import com.example.android_project.data.repositories.data_sources.map.FuelPriceDataSource;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapRepository {

    private FuelPriceDataSource fuelPriceDataSource;

    public MapRepository() {
        this.fuelPriceDataSource = new FuelPriceDataSource();
    }

    public LiveData<List<GasStation>> getFuelPriceByDistance(float lat, float lon, float dist) {
        Log.v("MapRepository", "getFuelPriceByDistance");
        this.fuelPriceDataSource.updateFuelPriceByDistance(lat, lon, dist);
        LiveData<FuelPrices> fuelPricesLiveData = fuelPriceDataSource.getFuelPriceMutableLiveData();
        return Transformations.map(
                fuelPricesLiveData,
                this::transformFuelPricesToGasStations);
    }

    private List<GasStation> transformFuelPricesToGasStations(FuelPrices fuelPrices) {
        List<GasStation> gasStations = new ArrayList<>();
        Map<String, Integer> mapGasStation = new HashMap<>();
        for (Record record : fuelPrices.getRecords()) {
            Fields fields = record.getFields();

            if (
                    fields.getPrixValeur() != null &&
                    fields.getPrixNom() != null &&
                    fields.getPrixMaj() != null
            ) {
                Fuel fuel = new Fuel(
                        fields.getPrixValeur(),
                        fields.getPrixNom(),
                        LocalDateTime.parse(fields.getPrixMaj(), DateTimeFormatter.ISO_DATE_TIME)
                );

                if (!mapGasStation.containsKey(fields.getId())) {
                    gasStations.add(new GasStation(
                            fields.getId(),
                            fields.getAdresse(),
                            fields.getVille(),
                            fields.getGeom().get(0),
                            fields.getGeom().get(1),
                            new ArrayList<>()
                    ));
                    mapGasStation.put(fields.getId(), gasStations.size() - 1);
                }

                gasStations.get(mapGasStation.get(fields.getId())).addFuel(fuel);
            }

        }

        Log.v("MapRepository",gasStations.toString());
        return gasStations;
    }
}
