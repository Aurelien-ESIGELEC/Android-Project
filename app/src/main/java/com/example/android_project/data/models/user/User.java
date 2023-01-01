package com.example.android_project.data.models.user;

import androidx.annotation.NonNull;

import java.util.Arrays;
import java.util.List;

public class User {
    private String username;
    private String email;
    private List<String> notifications;
    private String sharing;
    private List<User> friends;

    public User(String username, String email, List<String> notifications, String sharing) {
        this.username = username;
        this.email = email;
        this.notifications = notifications;
        this.sharing = sharing;
    }

    public User(String username, String email, List<String> notifications, String sharing, List<User> friends) {
        this.username = username;
        this.email = email;
        this.notifications = notifications;
        this.sharing = sharing;
        this.friends = friends;
    }

    public List<String> getNotifications() {
        return notifications;
    }

    public void setNotifications(List<String> notifications) {
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

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", notifications=" + notifications +
                ", sharing='" + sharing + '\'' +
                ", friends=" + friends +
                '}';
    }
}
