package com.example.android_project.data.models.fuel_price;

import java.util.List;

public class GasStation {

    private String id;

    private String address;
    private String city;
    private String postalCode;

    private double lon;
    private double lat;

    private List<Fuel> fuelList;

    public GasStation(String id, String address, String postalCode, String city, double lat, double lon, List<Fuel> fuelList) {
        this.id = id;
        this.address = address;
        this.postalCode = postalCode;
        this.city = city;
        this.lon = lon;
        this.lat = lat;
        this.fuelList = fuelList;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public List<Fuel> getFuelList() {
        return fuelList;
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

    public void setFuelList(List<Fuel> fuelList) {
        this.fuelList = fuelList;
    }

    public void addFuel(Fuel fuel) {
        this.fuelList.add(fuel);
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
