
package com.example.android_project.api.nominatim.pojo;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class NominatimAddress {

    @SerializedName("place_id")
    @Expose
    private Integer placeId;
    @SerializedName("licence")
    @Expose
    private String licence;
    @SerializedName("osm_type")
    @Expose
    private String osmType;
    @SerializedName("osm_id")
    @Expose
    private Long osmId;
    @SerializedName("boundingbox")
    @Expose
    private List<String> boundingbox = null;
    @SerializedName("lat")
    @Expose
    private String lat;
    @SerializedName("lon")
    @Expose
    private String lon;
    @SerializedName("display_name")
    @Expose
    private String displayName;
    @SerializedName("namedetails")
    @Expose
    private Namedetails namedetails;
    @SerializedName("place_rank")
    @Expose
    private Integer placeRank;
    @SerializedName("category")
    @Expose
    private String category;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("importance")
    @Expose
    private Double importance;
    @SerializedName("icon")
    @Expose
    private String icon;
    @SerializedName("address")
    @Expose
    private Address address;

    public Namedetails getNames() {
        return namedetails;
    }

    public void setNames(Namedetails namedetails) {
        this.namedetails = namedetails;
    }

    public Integer getPlaceId() {
        return placeId;
    }

    public void setPlaceId(Integer placeId) {
        this.placeId = placeId;
    }

    public String getLicence() {
        return licence;
    }

    public void setLicence(String licence) {
        this.licence = licence;
    }

    public String getOsmType() {
        return osmType;
    }

    public void setOsmType(String osmType) {
        this.osmType = osmType;
    }

    public Long getOsmId() {
        return osmId;
    }

    public void setOsmId(Long osmId) {
        this.osmId = osmId;
    }

    public List<String> getBoundingbox() {
        return boundingbox;
    }

    public void setBoundingbox(List<String> boundingbox) {
        this.boundingbox = boundingbox;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public Integer getPlaceRank() {
        return placeRank;
    }

    public void setPlaceRank(Integer placeRank) {
        this.placeRank = placeRank;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Double getImportance() {
        return importance;
    }

    public void setImportance(Double importance) {
        this.importance = importance;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

}

