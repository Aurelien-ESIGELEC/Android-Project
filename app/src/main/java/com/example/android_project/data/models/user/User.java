package com.example.android_project.data.models.user;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.google.firebase.firestore.PropertyName;

import java.util.List;

public class User implements Parcelable {

    private String username;
    private String email;
    private List<String> notifications;
    private String sharing;
    private List<String> friends;
    @PropertyName("favorite_fuels")
    private List<String> favoriteFuels;

    public User(String username, String email, List<String> notifications, String sharing, List<String> favoriteFuels) {
        this.username = username;
        this.email = email;
        this.notifications = notifications;
        this.sharing = sharing;
        this.favoriteFuels = favoriteFuels;
    }

    public User(String username, String email, List<String> notifications, String sharing,List<String> favoriteFuels, List<String> friends) {
        this(username,email,notifications,sharing, favoriteFuels);
        this.friends = friends;
    }

    public User() {
        super();
    }

    protected User(Parcel in) {
        username = in.readString();
        email = in.readString();
        notifications = in.createStringArrayList();
        sharing = in.readString();
        friends = in.createStringArrayList();
        favoriteFuels = in.createStringArrayList();
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

    @PropertyName("favorite_fuels")
    public List<String> getFavoriteFuels() {
        return favoriteFuels;
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

    public List<String> getFriends() {
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

    public User setFriends(List<String> friends) {
        this.friends = friends;
        return this;
    }

    @PropertyName("favorite_fuels")
    public User setFavoriteFuels(List<String> favoriteFuels) {
        this.favoriteFuels = favoriteFuels;
        return this;
    }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", notifications=" + notifications +
                ", sharing='" + sharing + '\'' +
                ", friends=" + friends +
                ", favoriteFuels=" + favoriteFuels +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeString(username);
        parcel.writeString(email);
        parcel.writeStringList(notifications);
        parcel.writeString(sharing);
        parcel.writeStringList(friends);
        parcel.writeStringList(favoriteFuels);
    }
}
