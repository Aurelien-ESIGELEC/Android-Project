package com.example.android_project.data.api.nominatim.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Address {

    @SerializedName("city")
    @Expose
    private String city;
    @SerializedName("municipality")
    @Expose
    private String municipality;
    @SerializedName("county")
    @Expose
    private String county;
    @SerializedName("ISO3166-2-lvl6")
    @Expose
    private String iSO31662Lvl6;
    @SerializedName("state")
    @Expose
    private String state;
    @SerializedName("ISO3166-2-lvl4")
    @Expose
    private String iSO31662Lvl4;
    @SerializedName("region")
    @Expose
    private String region;
    @SerializedName("country")
    @Expose
    private String country;
    @SerializedName("country_code")
    @Expose
    private String countryCode;
    @SerializedName("village")
    @Expose
    private String village;
    @SerializedName("postcode")
    @Expose
    private String postcode;
    @SerializedName("amenity")
    @Expose
    private String amenity;
    @SerializedName("house_number")
    @Expose
    private String houseNumber;
    @SerializedName("road")
    @Expose
    private String road;
    @SerializedName("suburb")
    @Expose
    private String suburb;
    @SerializedName("hamlet")
    @Expose
    private String hamlet;
    @SerializedName("isolated_dwelling")
    @Expose
    private String isolatedDwelling;
    @SerializedName("town")
    @Expose
    private String town;
    @SerializedName("residential")
    @Expose
    private String residential;
    @SerializedName("man_made")
    @Expose
    private String manMade;
    @SerializedName("neighbourhood")
    @Expose
    private String neighbourhood;

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getMunicipality() {
        return municipality;
    }

    public void setMunicipality(String municipality) {
        this.municipality = municipality;
    }

    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    public String getISO31662Lvl6() {
        return iSO31662Lvl6;
    }

    public void setISO31662Lvl6(String iSO31662Lvl6) {
        this.iSO31662Lvl6 = iSO31662Lvl6;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getISO31662Lvl4() {
        return iSO31662Lvl4;
    }

    public void setISO31662Lvl4(String iSO31662Lvl4) {
        this.iSO31662Lvl4 = iSO31662Lvl4;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getVillage() {
        return village;
    }

    public void setVillage(String village) {
        this.village = village;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public String getAmenity() {
        return amenity;
    }

    public void setAmenity(String amenity) {
        this.amenity = amenity;
    }

    public String getHouseNumber() {
        return houseNumber;
    }

    public void setHouseNumber(String houseNumber) {
        this.houseNumber = houseNumber;
    }

    public String getRoad() {
        return road;
    }

    public void setRoad(String road) {
        this.road = road;
    }

    public String getSuburb() {
        return suburb;
    }

    public void setSuburb(String suburb) {
        this.suburb = suburb;
    }

    public String getHamlet() {
        return hamlet;
    }

    public void setHamlet(String hamlet) {
        this.hamlet = hamlet;
    }

    public String getIsolatedDwelling() {
        return isolatedDwelling;
    }

    public void setIsolatedDwelling(String isolatedDwelling) {
        this.isolatedDwelling = isolatedDwelling;
    }

    public String getTown() {
        return town;
    }

    public void setTown(String town) {
        this.town = town;
    }

    public String getResidential() {
        return residential;
    }

    public void setResidential(String residential) {
        this.residential = residential;
    }

    public String getManMade() {
        return manMade;
    }

    public void setManMade(String manMade) {
        this.manMade = manMade;
    }

    public String getNeighbourhood() {
        return neighbourhood;
    }

    public void setNeighbourhood(String neighbourhood) {
        this.neighbourhood = neighbourhood;
    }

}