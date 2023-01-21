package com.example.android_project.data.repositories;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;

import com.example.android_project.api.fuel_price.pojo.Fields;
import com.example.android_project.api.fuel_price.pojo.FuelPrices;
import com.example.android_project.api.fuel_price.pojo.Record;
import com.example.android_project.api.nominatim.pojo.Address;
import com.example.android_project.api.nominatim.pojo.Namedetails;
import com.example.android_project.api.nominatim.pojo.NominatimSearch;
import com.example.android_project.data.models.address.SearchAddress;
import com.example.android_project.data.models.fuel_price.Fuel;
import com.example.android_project.data.models.fuel_price.GasStation;
import com.example.android_project.data.repositories.data_sources.map.FuelPriceDataSource;
import com.example.android_project.data.repositories.data_sources.map.MatrixDataSource;
import com.example.android_project.data.repositories.data_sources.map.NominatimDataSource;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapRepository {

    public static final String TAG = "MapRepository";

    private FuelPriceDataSource fuelPriceDataSource;
    private NominatimDataSource nominatimDataSource;
    private MatrixDataSource    matrixDataSource;

    public MapRepository() {
        this.nominatimDataSource = new NominatimDataSource();
        this.fuelPriceDataSource = new FuelPriceDataSource();
        this.matrixDataSource    = new MatrixDataSource();
    }

    public LiveData<List<SearchAddress>> getAddressBySearch(String search) {
        this.nominatimDataSource.getAddressBySearch(search);
        LiveData<List<NominatimSearch>> nominatimAddressLiveDataLiveData = nominatimDataSource.getNominatimAddressLiveData();
        return Transformations.map(
                nominatimAddressLiveDataLiveData,
                this::transformNominatimAddressesToAddresses);
    }

    public LiveData<List<GasStation>> getFuelPriceByDistance(float lat, float lon, float dist) {
        this.fuelPriceDataSource.updateFuelPriceByDistance(lat, lon, dist);
        LiveData<FuelPrices> fuelPricesLiveData = fuelPriceDataSource.getFuelPriceMutableLiveData();
        return Transformations.map(
                fuelPricesLiveData,
                this::transformFuelPricesToGasStations);
    }

    public LiveData<List<GasStation>> getFuelPriceByDistance(float lat, float lon, float dist, List<String> excludedIds) {
        this.fuelPriceDataSource.updateFuelPriceByDistance(lat, lon, dist, excludedIds);
        return Transformations.map(
                fuelPriceDataSource.getFuelPriceMutableLiveData(),
                this::transformFuelPricesToGasStations);
    }

    public LiveData<Double> getDistanceBetweenPoints(double sourceLat, double sourceLon, double destLat, double destLon) {
        this.matrixDataSource.updateDistanceBetweenPoints(Arrays.asList(sourceLon, sourceLat), Arrays.asList(destLon, destLat));
        Log.d(TAG, "getDistanceBetweenPoints: Here");
        return Transformations.map(
                matrixDataSource.getMatrixInfoMutableLiveData(), input -> input.getDistances().get(0).get(0));
    }

    private List<GasStation> transformFuelPricesToGasStations(FuelPrices fuelPrices) {
        Log.d(TAG, "transformFuelPricesToGasStations");
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
                            fields.getCp(),
                            fields.getVille(),
                            fields.getGeom().get(0),
                            fields.getGeom().get(1)
                    ));
                    mapGasStation.put(fields.getId(), gasStations.size() - 1);
                }

                gasStations.get(mapGasStation.get(fields.getId())).addFuel(fuel.getName(),fuel);
            }

        }

        return gasStations;
    }

    private List<SearchAddress> transformNominatimAddressesToAddresses(List<NominatimSearch> nominatimSearchList) {
        List<SearchAddress> searchAddressList = new ArrayList<>();
        for (NominatimSearch nominatimSearch : nominatimSearchList) {
            SearchAddress searchAddress = new SearchAddress()
                    .setLat(Double.parseDouble(nominatimSearch.getLat()))
                    .setLon(Double.parseDouble(nominatimSearch.getLon()))
                    .setImportance(nominatimSearch.getImportance());

            Address address = nominatimSearch.getAddress();
            Namedetails namedetails = nominatimSearch.getNames();

            Log.d(TAG, "transformNominatimAddressesToAddresses: " + namedetails);

            if (namedetails != null) {
                if (namedetails.getShortName() != null && !namedetails.getShortName().isEmpty()) {
                    searchAddress.setName(namedetails.getShortName());
                } else if (namedetails.getNameFr() != null && !namedetails.getNameFr().isEmpty()) {
                    searchAddress.setName(namedetails.getNameFr());
                } else if (namedetails.getName() != null && !namedetails.getName().isEmpty()) {
                    searchAddress.setName(namedetails.getName());
                }
            }

            if (address != null) {

                if (address.getCity() != null && !address.getCity().isEmpty()) {
                    searchAddress.setCity(address.getCity());
                }

                if (address.getVillage() != null && !address.getVillage().isEmpty()) {
                    searchAddress.setCity(address.getVillage());
                }

                if (address.getTown() != null && !address.getTown().isEmpty()) {
                    searchAddress.setCity(address.getTown());
                }

                if (address.getRoad() != null && !address.getRoad().isEmpty()) {
                    searchAddress.setRoad(address.getRoad());
                }

                if (address.getHouseNumber() != null && !address.getHouseNumber().isEmpty()) {
                    searchAddress.setHouseNumber(address.getHouseNumber());
                }

                if (address.getPostcode() != null && !address.getPostcode().isEmpty()) {
                    searchAddress.setPostCode(
                            address.getPostcode()
                    );
                }
            }

            searchAddressList.add(searchAddress);
        }

        return searchAddressList;
    }
}
