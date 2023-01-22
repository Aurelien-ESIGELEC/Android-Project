package com.example.android_project.data.api.fuel_price.pojo;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Fields {

    @SerializedName("ville")
    @Expose
    private String ville;

    @SerializedName("pop")
    @Expose
    private String pop;

    @SerializedName("reg_name")
    @Expose
    private String regName;

    @SerializedName("com_arm_code")
    @Expose
    private String comArmCode;

    @SerializedName("dep_name")
    @Expose
    private String depName;

    @SerializedName("prix_nom")
    @Expose
    private String prixNom;

    @SerializedName("com_code")
    @Expose
    private String comCode;

    @SerializedName("epci_name")
    @Expose
    private String epciName;

    @SerializedName("dep_code")
    @Expose
    private String depCode;

    @SerializedName("services_service")
    @Expose
    private String servicesService;

    @SerializedName("prix_id")
    @Expose
    private String prixId;

    @SerializedName("horaires_automate_24_24")
    @Expose
    private String horairesAutomate2424;

    @SerializedName("horaires")
    @Expose
    private String horaires;

    @SerializedName("com_arm_name")
    @Expose
    private String comArmName;

    @SerializedName("prix_maj")
    @Expose
    private String prixMaj;

    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("reg_code")
    @Expose
    private String regCode;

    @SerializedName("adresse")
    @Expose
    private String adresse;

    @SerializedName("geom")
    @Expose
    private List<Double> geom = null;

    @SerializedName("epci_code")
    @Expose
    private String epciCode;

    @SerializedName("cp")
    @Expose
    private String cp;

    @SerializedName("prix_valeur")
    @Expose
    private Double prixValeur;

    @SerializedName("com_name")
    @Expose
    private String comName;

    @SerializedName("dist")
    @Expose
    private String dist;

    public String getVille() {
        return ville;
    }

    public void setVille(String ville) {
        this.ville = ville;
    }

    public String getPop() {
        return pop;
    }

    public void setPop(String pop) {
        this.pop = pop;
    }

    public String getRegName() {
        return regName;
    }

    public void setRegName(String regName) {
        this.regName = regName;
    }

    public String getComArmCode() {
        return comArmCode;
    }

    public void setComArmCode(String comArmCode) {
        this.comArmCode = comArmCode;
    }

    public String getDepName() {
        return depName;
    }

    public void setDepName(String depName) {
        this.depName = depName;
    }

    public String getPrixNom() {
        return prixNom;
    }

    public void setPrixNom(String prixNom) {
        this.prixNom = prixNom;
    }

    public String getComCode() {
        return comCode;
    }

    public void setComCode(String comCode) {
        this.comCode = comCode;
    }

    public String getEpciName() {
        return epciName;
    }

    public void setEpciName(String epciName) {
        this.epciName = epciName;
    }

    public String getDepCode() {
        return depCode;
    }

    public void setDepCode(String depCode) {
        this.depCode = depCode;
    }

    public String getServicesService() {
        return servicesService;
    }

    public void setServicesService(String servicesService) {
        this.servicesService = servicesService;
    }

    public String getPrixId() {
        return prixId;
    }

    public void setPrixId(String prixId) {
        this.prixId = prixId;
    }

    public String getHorairesAutomate2424() {
        return horairesAutomate2424;
    }

    public void setHorairesAutomate2424(String horairesAutomate2424) {
        this.horairesAutomate2424 = horairesAutomate2424;
    }

    public String getHoraires() {
        return horaires;
    }

    public void setHoraires(String horaires) {
        this.horaires = horaires;
    }

    public String getComArmName() {
        return comArmName;
    }

    public void setComArmName(String comArmName) {
        this.comArmName = comArmName;
    }

    public String getPrixMaj() {
        return prixMaj;
    }

    public void setPrixMaj(String prixMaj) {
        this.prixMaj = prixMaj;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRegCode() {
        return regCode;
    }

    public void setRegCode(String regCode) {
        this.regCode = regCode;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public List<Double> getGeom() {
        return geom;
    }

    public void setGeom(List<Double> geom) {
        this.geom = geom;
    }

    public String getEpciCode() {
        return epciCode;
    }

    public void setEpciCode(String epciCode) {
        this.epciCode = epciCode;
    }

    public String getCp() {
        return cp;
    }

    public void setCp(String cp) {
        this.cp = cp;
    }

    public Double getPrixValeur() {
        return prixValeur;
    }

    public void setPrixValeur(Double prixValeur) {
        this.prixValeur = prixValeur;
    }

    public String getComName() {
        return comName;
    }

    public void setComName(String comName) {
        this.comName = comName;
    }

    public String getDist() {
        return dist;
    }

    public void setDist(String dist) {
        this.dist = dist;
    }

}
