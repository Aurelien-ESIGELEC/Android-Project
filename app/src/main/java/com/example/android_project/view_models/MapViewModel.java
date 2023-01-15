package com.example.android_project.view_models;

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
import java.util.List;
import java.util.concurrent.Executors;

public class MapViewModel extends ViewModel {

    private static final String TAG = "MapViewModel";
    private MutableLiveData<String> search;

    private MutableLiveData<List<GasStation>> gasStations;

    private MutableLiveData<List<SearchAddress>> listResultSearch;
    private MutableLiveData<GasStation> selectedStation;

    private MapRepository mapRepository;

    public MapViewModel() {
        this.mapRepository = new MapRepository();

        this.gasStations = new MutableLiveData<>();
        this.search = new MutableLiveData<>();
        this.listResultSearch = new MutableLiveData<>();
        this.selectedStation = new MutableLiveData<>();
    }

    public void setSearchValue(String search) {
        this.search.setValue(search);
    }

    public MutableLiveData<String> getSearch() {
        return search;
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

    public MutableLiveData<GasStation> getSelectedStation() {
        return selectedStation;
    }

    //    public void updateListStationsByLocation(float lat, float lon, float dist, List<String> excludedIds) {
//        Executors.newSingleThreadExecutor().execute(() ->
//                this.gasStations = this.mapRepository.getFuelPriceByDistance(lat, lon, dist, excludedIds)
//        );
//    }


    public MutableLiveData<List<SearchAddress>> getListResultSearch() {
        return listResultSearch;
    }

    public LiveData<List<GasStation>> getGasStations() {
        return this.gasStations;
    }
}
