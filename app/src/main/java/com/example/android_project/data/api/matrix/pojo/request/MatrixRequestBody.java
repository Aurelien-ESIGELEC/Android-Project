
package com.example.android_project.data.api.matrix.pojo.request;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MatrixRequestBody {

    @SerializedName("locations")
    @Expose
    private List<List<Double>> locations = null;

    @SerializedName("destinations")
    @Expose
    private List<Integer> destinations = null;

    @SerializedName("metrics")
    @Expose
    private List<String> metrics = null;

    @SerializedName("sources")
    @Expose
    private List<Integer> sources = null;

    @SerializedName("units")
    @Expose
    private String units;

    public List<List<Double>> getLocations() {
        return locations;
    }

    public List<Integer> getDestinations() {
        return destinations;
    }


    public List<String> getMetrics() {
        return metrics;
    }

    public List<Integer> getSources() {
        return sources;
    }

    public String getUnits() {
        return units;
    }

    public MatrixRequestBody setLocations(List<List<Double>> locations) {
        this.locations = locations;
        return this;
    }

    public MatrixRequestBody setDestinations(List<Integer> destinations) {
        this.destinations = destinations;
        return this;
    }

    public MatrixRequestBody setMetrics(List<String> metrics) {
        this.metrics = metrics;
        return this;
    }

    public MatrixRequestBody setSources(List<Integer> sources) {
        this.sources = sources;
        return this;
    }

    public MatrixRequestBody setUnits(String units) {
        this.units = units;
        return this;
    }

    @Override
    public String toString() {
        return "MatrixRequestBody{" +
                "locations=" + locations +
                ", destinations=" + destinations +
                ", metrics=" + metrics +
                ", sources=" + sources +
                ", units='" + units + '\'' +
                '}';
    }
}
