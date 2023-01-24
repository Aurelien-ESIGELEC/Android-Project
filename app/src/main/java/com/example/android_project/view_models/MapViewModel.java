package com.example.android_project.view_models;

import android.location.Location;
import android.util.Log;

import androidx.core.util.Pair;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.android_project.data.models.address.SearchAddress;
import com.example.android_project.data.models.fuel_price.Fuel;
import com.example.android_project.data.models.fuel_price.GasStation;
import com.example.android_project.data.repositories.MapRepository;
import com.example.android_project.utils.Utils;

import org.osmdroid.util.GeoPoint;

import java.util.ArrayList;
import java.util.List;

public class MapViewModel extends ViewModel {

    private static final String TAG = "MapViewModel";
    private final MutableLiveData<String> search;

    private final MutableLiveData<List<GasStation>> gasStations;
    private final MutableLiveData<GeoPoint> zoomOnPoint;

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
        this.zoomOnPoint = new MutableLiveData<>();
    }

    public void setZoomOnPoint(GeoPoint geoPoint) {
        this.zoomOnPoint.setValue(geoPoint);
    }

    public void setZoomOnPoint(double lat, double lon) {
        this.zoomOnPoint.setValue(new GeoPoint(lat, lon));
    }

    public MutableLiveData<GeoPoint> getZoomOnPoint() {
        return zoomOnPoint;
    }

    public void setSearchValue(String search) {
        this.search.setValue(search);
    }

    public void setUserLocation(Location newLocation) {
        this.userLocation.setValue(newLocation);
    }

    public LiveData<String> getSearch() {
        return search;
    }

    /**
     *
     * @param user
     * @param gasStation
     * @param fuelType
     * @return
     */
    public LiveData<List<Fuel>> getPricesOfStationsByFuel(String user, GasStation gasStation, String fuelType) {
        MediatorLiveData<List<Fuel>> listMediatorLiveData = new MediatorLiveData<>();

        listMediatorLiveData.addSource(mapRepository.getPricesOfStationsByFuel(user ,gasStation.getId(), fuelType), fuelList -> {
            if (fuelList != null) {

                for (Fuel fuel: fuelList) {
                    if (fuel.canBeUpdated()) {
                        if (this.userLocation.getValue() != null) {
                            float distance = Utils.getDistanceBetweenTwoPoint(
                                    userLocation.getValue().getLatitude(),
                                    userLocation.getValue().getLongitude(),
                                    gasStation.getLat(),
                                    gasStation.getLon()
                            );
                            fuel.setCanUpdate(distance <= 200);
                        }
                    }

                    if (gasStation.getFuelList().containsKey(fuelType) && gasStation.getFuelList().get(fuelType) != null) {
                        if (gasStation.getFuelList().get(fuelType).contains(fuel)) {
                            gasStation.getFuelList().get(fuelType).remove(fuel);
                        }

                        gasStation.getFuelList().get(fuelType).add(fuel);
                    }
                }

                listMediatorLiveData.setValue(fuelList);
            }
        });

        return listMediatorLiveData;
    }

    /**
     *
     * @param gasStation
     * @return
     */
    public LiveData<Double> getDistanceBetweenLocationAndGasStation(GasStation gasStation) {
        Location sourceLocation = userLocation.getValue();

        if (sourceLocation != null) {
            return this.mapRepository.getDistanceBetweenPoints(
                    sourceLocation.getLatitude(),
                    sourceLocation.getLongitude(),
                    gasStation.getLat(),
                    gasStation.getLon()
            );
        } else {
            return new MutableLiveData<>(0d);
        }
    }

    /**
     *
     * @param lat
     * @param lon
     * @param dist
     * @return
     */
    public LiveData<List<GasStation>> updateListStationsByLocation(float lat, float lon, float dist) {
        MediatorLiveData<List<GasStation>> mediatorLiveData = new MediatorLiveData<>();

        List<String> excludedIds = new ArrayList<>();
        if (this.gasStations.getValue() != null) {
            for (GasStation gasStation : this.gasStations.getValue()) {
                excludedIds.add(gasStation.getId());
            }
        }

        mediatorLiveData.addSource(this.mapRepository.getFuelPriceByDistance(lat, lon, dist, excludedIds), gasStations1 -> {
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

    /**
     *
     * @param fuel
     * @param user
     * @param newReview
     * @return
     */
    public LiveData<Fuel> updateReviewOnPrice(Fuel fuel, String user, String newReview) {
        return this.mapRepository.updateReviewOnPrice(fuel,user, newReview);
    }

    /**
     *
     * @param user
     * @param fuelType
     * @param price
     * @param gasStation
     * @return
     */
    public LiveData<Fuel> addPrice(String user, String fuelType, double price, GasStation gasStation) {
        MediatorLiveData<Fuel> fuelMediatorLiveData = new MediatorLiveData<>();

        fuelMediatorLiveData.addSource(mapRepository.addPrice(user, fuelType, price, gasStation.getId()), fuel -> {


            if (gasStation.getFuelList().containsKey(fuelType) && gasStation.getFuelList().get(fuelType) != null) {
                gasStation.getFuelList().get(fuelType).add(fuel);
            }

            fuelMediatorLiveData.setValue(fuel);
        });

        return fuelMediatorLiveData;
    }

    public LiveData<Double> getAvgPriceInFrance(String fuelType) {
        return mapRepository.getAvgPriceInFrance(fuelType);
    }

    public LiveData<String> getCountyByReverse() {
        Location sourceLocation = userLocation.getValue();

        if (sourceLocation != null) {
            return mapRepository.getCountyByReverse(
                    Double.toString(sourceLocation.getLatitude()),
                    Double.toString(sourceLocation.getLongitude())
            );
        } else {
            return new MutableLiveData<>("");
        }

    }

    public LiveData<String> getCityByReverse() {
        Location sourceLocation = userLocation.getValue();

        if (sourceLocation != null) {
            return mapRepository.getCityByReverse(
                    Double.toString(sourceLocation.getLatitude()),
                    Double.toString(sourceLocation.getLongitude())
            );
        } else {
            return new MutableLiveData<>("");
        }
    }

    public LiveData<Double> getAvgPriceInCounty(String fuelType, String county) {
        return this.mapRepository.getAvgPriceInCounty(fuelType, county);
    }

    public LiveData<Double> getAvgPriceInCity(String fuelType, String city) {
        return this.mapRepository.getAvgPriceInCity(fuelType, city);
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
