package com.example.android_project.data.models.fuel_price;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class GasStation {

    private String id;
    private String address;
    private String city;
    private String postalCode;
    private double lon;
    private double lat;

    private HashMap<String, List<Fuel>> fuelList;

    public GasStation(String id, String address, String postalCode, String city, double lat, double lon) {
        this.id = id;
        this.address = address;
        this.postalCode = postalCode;
        this.city = city;
        this.lon = lon;
        this.lat = lat;
        this.fuelList = new HashMap<>();
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public void addFuel(String name, Fuel fuel) {
        if (!fuelList.containsKey(name)) {
            fuelList.put(name, new ArrayList<>());
        }
        Objects.requireNonNull(fuelList.get(name)).add(fuel);
    }

    public HashMap<String, List<Fuel>> getFuelList() {
        return new HashMap<>(fuelList);
    }

    @Override
    public String toString() {
        return "GasStation{" +
                "id='" + id + '\'' +
                ", address='" + address + '\'' +
                ", city='" + city + '\'' +
                ", postalCode='" + postalCode + '\'' +
                ", lon=" + lon +
                ", lat=" + lat +
                ", fuelList=" + fuelList +
                '}';
    }
}
