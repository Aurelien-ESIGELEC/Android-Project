package com.example.android_project.view_models;

import android.location.Location;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.android_project.api.nominatim.pojo.NominatimAddress;
import com.example.android_project.data.models.address.SearchAddress;
import com.example.android_project.data.models.fuel_price.GasStation;
import com.example.android_project.data.repositories.MapRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Executors;

public class MapViewModel extends ViewModel {

    private static final String TAG = "MapViewModel";
    private final MutableLiveData<String> search;

    private final MutableLiveData<List<GasStation>> gasStations;

    private final MutableLiveData<List<SearchAddress>> listResultSearch;
    private final MutableLiveData<GasStation> selectedStation;
    private final MutableLiveData<Location> userLocation;
    private final MapRepository mapRepository;

    public MapViewModel() {
        this.mapRepository = new MapRepository();

        this.gasStations = new MutableLiveData<>();
        this.search = new MutableLiveData<>();
        this.listResultSearch = new MutableLiveData<>();
        this.selectedStation = new MutableLiveData<>();
        this.userLocation = new MutableLiveData<>();
    }

    public void setSearchValue(String search) {
        this.search.setValue(search);
    }

    public void setUserLocation(Location newLocation) {
        this.userLocation.setValue(newLocation);
    }

    public MutableLiveData<String> getSearch() {
        return search;
    }

    public LiveData<Double> getDistanceBetweenLocationAndGasStation(GasStation gasStation) {
        Location sourceLocation = userLocation.getValue();
        if (sourceLocation != null) {
            return this.mapRepository.getDistanceBetweenPoints(
                    sourceLocation.getLatitude(),
                    sourceLocation.getLongitude(),
                    gasStation.getLat(),
                    gasStation.getLon()
            );
        }
        return null;
    }

    public LiveData<List<GasStation>> updateListStationsByLocation(float lat, float lon, float dist) {
        MediatorLiveData<List<GasStation>> mediatorLiveData = new MediatorLiveData<>();

        List<String> excludedIds = new ArrayList<>();
        if (this.gasStations.getValue() != null) {
            for (GasStation gasStation : this.gasStations.getValue()) {
                excludedIds.add(gasStation.getId());
            }
        }

        mediatorLiveData.addSource(this.mapRepository.getFuelPriceByDistance(lat, lon, dist, excludedIds), gasStations1 -> {
            Log.d(TAG, "updateListStationsByLocation");
            if (this.gasStations.getValue() != null) {
                List<GasStation> gasStationList = new ArrayList<>(gasStations1);
                gasStationList.addAll(this.gasStations.getValue());
                this.gasStations.postValue(gasStationList);
                mediatorLiveData.postValue(gasStationList);
            } else {
                this.gasStations.postValue(gasStations1);
                mediatorLiveData.postValue(gasStations1);
            }

        });

        return mediatorLiveData;
    }


    public LiveData<List<SearchAddress>> getAddressBySearch(String search) {
        MediatorLiveData<List<SearchAddress>> listMediatorLiveData = new MediatorLiveData<>();

        listMediatorLiveData.addSource(
                this.mapRepository.getAddressBySearch(search),
                searchAddresses -> {
                    this.listResultSearch.setValue(searchAddresses);
                    listMediatorLiveData.setValue(searchAddresses);
                }
        );

        return listMediatorLiveData;
    }

    public void setSelectedStation(GasStation gasStation) {
        this.selectedStation.setValue(gasStation);
    }

    public MutableLiveData<Location> getUserLocation() {
        return userLocation;
    }

    public MutableLiveData<GasStation> getSelectedStation() {
        return selectedStation;
    }

    public MutableLiveData<List<SearchAddress>> getListResultSearch() {
        return listResultSearch;
    }

    public LiveData<List<GasStation>> getGasStations() {
        return this.gasStations;
    }
}
