package com.example.android_project.data.models.user;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.Arrays;
import java.util.List;

public class User implements Parcelable {

    private String id;
    private String username;
    private String email;
    private List<String> notifications;
    private String sharing;
    private List<User> friends;
    private List<String> fuelTypes;

    public User(String username, String email, List<String> notifications, String sharing, List<String> fuelTypes) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.notifications = notifications;
        this.sharing = sharing;
        this.fuelTypes = fuelTypes;
    }

    public User(String username, String email, List<String> notifications, String sharing,List<String> fuelTypes, List<User> friends) {
        this(username,email,notifications,sharing, fuelTypes);
        this.friends = friends;
    }

    public User() {
        super();
    }

    protected User(Parcel in) {
        id = in.readString();
        username = in.readString();
        email = in.readString();
        notifications = in.createStringArrayList();
        sharing = in.readString();
        friends = in.createTypedArrayList(User.CREATOR);
        fuelTypes = in.createStringArrayList();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    public String getId() {
        return id;
    }

    public User setId(String id) {
        this.id = id;
        return this;
    }

    public List<String> getFuelTypes() {
        return fuelTypes;
    }

    public List<String> getNotifications() {
        return notifications;
    }

    public String getSharing() {
        return sharing;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public List<User> getFriends() {
        return friends;
    }

    public User setUsername(String username) {
        this.username = username;
        return this;
    }

    public User setEmail(String email) {
        this.email = email;
        return this;
    }

    public User setNotifications(List<String> notifications) {
        this.notifications = notifications;
        return this;
    }

    public User setSharing(String sharing) {
        this.sharing = sharing;
        return this;
    }

    public User setFriends(List<User> friends) {
        this.friends = friends;
        return this;
    }

    public User setFuelTypes(List<String> fuelTypes) {
        this.fuelTypes = fuelTypes;
        return this;
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", notifications=" + notifications +
                ", sharing='" + sharing + '\'' +
                ", friends=" + friends +
                ", fuelTypes=" + fuelTypes +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(username);
        parcel.writeString(email);
        parcel.writeStringList(notifications);
        parcel.writeString(sharing);
        parcel.writeTypedList(friends);
        parcel.writeStringList(fuelTypes);
    }
}
