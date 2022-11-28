package com.example.android_project.data.models;

import androidx.annotation.NonNull;

import java.util.Arrays;
import java.util.List;

public class User {
    private String username;
    private String email;
    private String[] notifications;
    private String sharing;
    private List<User> friends;

    public User(String username, String email, String[] notifications, String sharing) {
        this.username = username;
        this.email = email;
        this.notifications = notifications;
        this.sharing = sharing;
    }

    public User(String username, String email, String[] notifications, String sharing, List<User> friends) {
        this.username = username;
        this.email = email;
        this.notifications = notifications;
        this.sharing = sharing;
        this.friends = friends;
    }

    public String[] getNotifications() {
        return notifications;
    }

    public void setNotifications(String[] notifications) {
        this.notifications = notifications;
    }

    public String getSharing() {
        return sharing;
    }

    public void setSharing(String sharing) {
        this.sharing = sharing;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<User> getFriends() {
        return friends;
    }

    public void setFriends(List<User> friends) {
        this.friends = friends;
    }

    @NonNull
    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", notifications=" + Arrays.toString(notifications) +
                ", sharing='" + sharing + '\'' +
                ", friends=" + friends +
                '}';
    }
}
