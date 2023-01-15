package com.example.android_project.data.models.address;

import androidx.annotation.NonNull;

public class SearchAddress {

    private double lon;
    private double lat;
    private double importance;
    private String city;
    private String postCode;
    private String road;
    private String houseNumber;
    private String name;

    public SearchAddress(float lon, float lat, float importance) {
        this();
        this.lon = lon;
        this.lat = lat;
        this.importance = importance;
    }

    public SearchAddress() {
        super();
    }

    public String getName() {
        return name;
    }

    public SearchAddress setName(String name) {
        this.name = name;
        return this;
    }

    public String getCity() {
        return city;
    }

    public SearchAddress setCity(String city) {
        this.city = city;
        return this;
    }

    public String getPostCode() {
        return postCode;
    }

    public SearchAddress setPostCode(String postCode) {
        this.postCode = postCode;
        return this;
    }

    public String getRoad() {
        return road;
    }

    public SearchAddress setRoad(String road) {
        this.road = road;
        return this;
    }

    public String getHouseNumber() {
        return houseNumber;
    }

    public SearchAddress setHouseNumber(String houseNumber) {
        this.houseNumber = houseNumber;
        return this;
    }

    public double getLon() {
        return lon;
    }

    public SearchAddress setLon(double lon) {
        this.lon = lon;
        return this;
    }

    public double getLat() {
        return lat;
    }

    public SearchAddress setLat(double lat) {
        this.lat = lat;
        return this;
    }

    public double getImportance() {
        return importance;
    }

    public SearchAddress setImportance(double importance) {
        this.importance = importance;
        return this;
    }

    public String getFullName() {
        StringBuilder address = new StringBuilder();

        if (houseNumber != null && !houseNumber.isEmpty()) {
            address.append(houseNumber).append(" ");
        }

        if (road != null && !road.isEmpty()) {
            address.append(road);
        }

        if (name != null && !name.isEmpty() && !name.equals(road)) {
            address.insert(0, name + ((address.length() > 0) ? ", " : ""));
        }

        return address.toString();
    }

    public String getFullCity() {
        String returnedCity = "";

        if (postCode != null && !postCode.isEmpty()) {
            returnedCity += postCode + " ";
        }

        if (city != null && !city.isEmpty()) {
            returnedCity += city;
        }

        return returnedCity;
    }

    @NonNull
    @Override
    public String toString() {
        return "SearchAddress{" +
                "lon=" + lon +
                ", lat=" + lat +
                ", importance=" + importance +
                ", city='" + city + '\'' +
                ", postCode=" + postCode +
                ", road='" + road + '\'' +
                ", houseNumber=" + houseNumber +
                ", name='" + name + '\'' +
                '}';
    }
}
