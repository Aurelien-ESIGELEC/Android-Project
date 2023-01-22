package com.example.android_project.data.api.fuel_price.pojo;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Parameters {

    @SerializedName("dataset")
    @Expose
    private String dataset;

    @SerializedName("rows")
    @Expose
    private Integer rows;

    @SerializedName("start")
    @Expose
    private Integer start;

    @SerializedName("format")
    @Expose
    private String format;

    @SerializedName("geofilter.distance")
    @Expose
    private List<String> geofilterDistance = null;

    @SerializedName("timezone")
    @Expose
    private String timezone;

    public String getDataset() {
        return dataset;
    }

    public void setDataset(String dataset) {
        this.dataset = dataset;
    }

    public Integer getRows() {
        return rows;
    }

    public void setRows(Integer rows) {
        this.rows = rows;
    }

    public Integer getStart() {
        return start;
    }

    public void setStart(Integer start) {
        this.start = start;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public List<String> getGeofilterDistance() {
        return geofilterDistance;
    }

    public void setGeofilterDistance(List<String> geofilterDistance) {
        this.geofilterDistance = geofilterDistance;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

}
