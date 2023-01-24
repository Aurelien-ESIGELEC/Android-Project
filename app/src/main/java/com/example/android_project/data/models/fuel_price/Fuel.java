package com.example.android_project.data.models.fuel_price;

import androidx.annotation.Nullable;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentId;
import com.google.firebase.firestore.Exclude;
import com.google.firebase.firestore.PropertyName;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Objects;

public class Fuel {
    private String id;
    private double price;
    private String name;
    private LocalDateTime updateDate;
    private double reliabilityIndex;
    private boolean canUpdate;
    private String myReview;

    public Fuel() {
        super();
        this.price = 0;
        this.canUpdate = false;
        this.name = "";
        this.reliabilityIndex = 0;
        this.updateDate = null;
        this.myReview = "";
        this.id = "";
    }

    public Fuel(double price, String name, LocalDateTime updateDate) {
        this("", price, name, updateDate, 100, false, "");
    }

    public Fuel(String id, double price, String name, LocalDateTime updateDate, double reliabilityIndex, boolean canUpdate, String myReview) {
        this.id = id;
        this.price = price;
        this.name = name;
        this.updateDate = updateDate;
        this.reliabilityIndex = reliabilityIndex;
        this.canUpdate = canUpdate;
    }

    public Fuel(Fuel fuel) {
        this(fuel.id, fuel.price, fuel.name, fuel.updateDate, fuel.reliabilityIndex, fuel.canUpdate, fuel.myReview);
    }

    public String getId() {
        return id;
    }

    public Fuel setId(String id) {
        this.id = id;
        return this;
    }

    public String getMyReview() {
        return myReview;
    }

    public Fuel setMyReview(String myReview) {
        this.myReview = myReview;
        return this;
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
    public boolean equals(@Nullable Object obj) {
        return obj instanceof Fuel && Objects.equals(this.getId(), ((Fuel) obj).getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, price, name, updateDate, reliabilityIndex, canUpdate, myReview);
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
