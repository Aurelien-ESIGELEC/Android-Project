package com.example.android_project.api.fuel_price.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FuelPriceAnalyze {

    @SerializedName("x")
    @Expose
    private String x;
    @SerializedName("prix_moy")
    @Expose
    private Double prixMoy;

    public String getX() {
        return x;
    }

    public void setX(String x) {
        this.x = x;
    }

    public Double getPrixMoy() {
        return prixMoy;
    }

    public void setPrixMoy(Double prixMoy) {
        this.prixMoy = prixMoy;
    }

}
