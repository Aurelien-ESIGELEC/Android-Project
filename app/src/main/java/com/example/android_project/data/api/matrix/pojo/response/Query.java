package com.example.android_project.data.api.matrix.pojo.response;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Query {

    @SerializedName("locations")
    @Expose
    private List<List<Double>> locations = null;
    @SerializedName("profile")
    @Expose
    private String profile;
    @SerializedName("responseType")
    @Expose
    private String responseType;
    @SerializedName("sources")
    @Expose
    private List<String> sources = null;
    @SerializedName("destinations")
    @Expose
    private List<String> destinations = null;
    @SerializedName("metrics")
    @Expose
    private List<String> metrics = null;
    @SerializedName("units")
    @Expose
    private String units;

    public List<List<Double>> getLocations() {
        return locations;
    }

    public void setLocations(List<List<Double>> locations) {
        this.locations = locations;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public String getResponseType() {
        return responseType;
    }

    public void setResponseType(String responseType) {
        this.responseType = responseType;
    }

    public List<String> getSources() {
        return sources;
    }

    public void setSources(List<String> sources) {
        this.sources = sources;
    }

    public List<String> getDestinations() {
        return destinations;
    }

    public void setDestinations(List<String> destinations) {
        this.destinations = destinations;
    }

    public List<String> getMetrics() {
        return metrics;
    }

    public void setMetrics(List<String> metrics) {
        this.metrics = metrics;
    }

    public String getUnits() {
        return units;
    }

    public void setUnits(String units) {
        this.units = units;
    }

}
