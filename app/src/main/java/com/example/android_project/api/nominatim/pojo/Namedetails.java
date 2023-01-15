package com.example.android_project.api.nominatim.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Namedetails {

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("name:fr")
    @Expose
    private String nameFr;
    @SerializedName("short_name")
    @Expose
    private String shortName;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNameFr() {
        return nameFr;
    }

    public void setNameFr(String nameFr) {
        this.nameFr = nameFr;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }
}
