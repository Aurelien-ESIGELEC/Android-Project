package com.example.android_project.data.api.matrix.pojo.response;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MatrixInfo {

    @SerializedName("durations")
    @Expose
    private List<List<Double>> durations = null;
    @SerializedName("distances")
    @Expose
    private List<List<Double>> distances = null;
    @SerializedName("destinations")
    @Expose
    private List<Destination> destinations = null;
    @SerializedName("sources")
    @Expose
    private List<Source> sources = null;
    @SerializedName("metadata")
    @Expose
    private Metadata metadata;

    public List<List<Double>> getDurations() {
        return durations;
    }

    public void setDurations(List<List<Double>> durations) {
        this.durations = durations;
    }

    public List<List<Double>> getDistances() {
        return distances;
    }

    public void setDistances(List<List<Double>> distances) {
        this.distances = distances;
    }

    public List<Destination> getDestinations() {
        return destinations;
    }

    public void setDestinations(List<Destination> destinations) {
        this.destinations = destinations;
    }

    public List<Source> getSources() {
        return sources;
    }

    public void setSources(List<Source> sources) {
        this.sources = sources;
    }

    public Metadata getMetadata() {
        return metadata;
    }

    public void setMetadata(Metadata metadata) {
        this.metadata = metadata;
    }

}
