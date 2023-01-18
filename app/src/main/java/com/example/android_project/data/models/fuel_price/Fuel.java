package com.example.android_project.data.models.fuel_price;

import java.time.LocalDateTime;

public class Fuel {

    private double price;
    private String name;
    private LocalDateTime updateDate;

    private double reliabilityIndex;

    public Fuel(double price, String name, LocalDateTime updateDate) {
        this(price, name, updateDate, 100);
    }

    public Fuel(double price, String name, LocalDateTime updateDate, double reliabilityIndex) {
        this.price = price;
        this.name = name;
        this.updateDate = updateDate;
        this.reliabilityIndex = reliabilityIndex;
    }

    public void setReliabilityIndex(double reliabilityIndex) {
        this.reliabilityIndex = reliabilityIndex;
    }

    public double getReliabilityIndex() {
        return reliabilityIndex;
    }

    public LocalDateTime getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(LocalDateTime updateDate) {
        this.updateDate = updateDate;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Fuel{" +
                "price=" + price +
                ", name='" + name + '\'' +
                ", updateDate=" + updateDate +
                ", reliabilityIndex=" + reliabilityIndex +
                '}';
    }
}
