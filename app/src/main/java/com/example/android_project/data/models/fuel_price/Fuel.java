package com.example.android_project.data.models.fuel_price;

import com.google.firebase.Timestamp;

import java.time.LocalDateTime;
import java.time.ZoneId;

public class Fuel {
    private double price;
    private String name;
    private LocalDateTime updateDate;
    private double reliabilityIndex;
    private boolean canUpdate;

    public Fuel() {
        super();
        this.price = 0;
        this.canUpdate = false;
        this.name = "";
        this.reliabilityIndex = 0;
        this.updateDate = null;
    }

    public Fuel(double price, String name, LocalDateTime updateDate) {
        this(price, name, updateDate, 100, false);
    }

    public Fuel(double price, String name, LocalDateTime updateDate, double reliabilityIndex, boolean canUpdate) {
        this.price = price;
        this.name = name;
        this.updateDate = updateDate;
        this.reliabilityIndex = reliabilityIndex;
        this.canUpdate = false;
    }

    public Fuel(Fuel fuel) {
        this(fuel.price, fuel.name, fuel.updateDate, fuel.reliabilityIndex, fuel.canUpdate);
    }

    public Fuel setPrice(double price) {
        this.price = price;
        return this;
    }

    public Fuel setName(String name) {
        this.name = name;
        return this;
    }

    public Fuel setUpdateDate(LocalDateTime updateDate) {
        this.updateDate = updateDate;
        return this;
    }

    public Fuel setUpdateDateFromTimestamp(Timestamp updateDate) {
        this.updateDate = updateDate.toDate()
                .toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();;
        return this;
    }

    public Fuel setReliabilityIndex(double reliabilityIndex) {
        this.reliabilityIndex = reliabilityIndex;
        return this;
    }

    public Fuel setCanUpdate(boolean canUpdate) {
        this.canUpdate = canUpdate;
        return this;
    }

    public boolean canBeUpdated() {
        return canUpdate;
    }

    public double getReliabilityIndex() {
        return reliabilityIndex;
    }

    public LocalDateTime getUpdateDate() {
        return updateDate;
    }


    public double getPrice() {
        return price;
    }


    public String getName() {
        return name;
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
