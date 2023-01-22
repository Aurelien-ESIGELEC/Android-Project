package com.example.android_project.data.api.nominatim.pojo;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Geojson {

    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("coordinates")
    @Expose
    private List<List<List<Double>>> coordinates = null;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<List<List<Double>>> getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(List<List<List<Double>>> coordinates) {
        this.coordinates = coordinates;
    }

    public String getCoordinatesToPolygon() {
        StringBuilder stringBuilder = new StringBuilder();

        if (coordinates!=null && !coordinates.isEmpty()) {
            List<List<Double>> polygon = coordinates.get(0);
            stringBuilder.append("( ");
            for (List<Double> point : polygon) {
                stringBuilder.append("( ");
                stringBuilder.append(point.get(0));
                stringBuilder.append(", ");
                stringBuilder.append(point.get(1));
                stringBuilder.append(" )");
            }
            stringBuilder.append(" )");
        }

        return stringBuilder.toString();
    }
}
