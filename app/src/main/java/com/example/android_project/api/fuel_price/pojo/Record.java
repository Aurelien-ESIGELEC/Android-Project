package com.example.android_project.api.fuel_price.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Record {

    @SerializedName("datasetid")
    @Expose
    private String datasetid;

    @SerializedName("recordid")
    @Expose
    private String recordid;

    @SerializedName("fields")
    @Expose
    private Fields fields;

    @SerializedName("geometry")
    @Expose
    private Geometry geometry;

    public String getDatasetid() {
        return datasetid;
    }

    public void setDatasetid(String datasetid) {
        this.datasetid = datasetid;
    }

    public String getRecordid() {
        return recordid;
    }

    public void setRecordid(String recordid) {
        this.recordid = recordid;
    }

    public Fields getFields() {
        return fields;
    }

    public void setFields(Fields fields) {
        this.fields = fields;
    }

    public Geometry getGeometry() {
        return geometry;
    }

    public void setGeometry(Geometry geometry) {
        this.geometry = geometry;
    }

}
