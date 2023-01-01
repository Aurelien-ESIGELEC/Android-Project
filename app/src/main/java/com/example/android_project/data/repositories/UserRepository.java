package com.example.android_project.data.repositories;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.android_project.data.models.user.User;
import com.example.android_project.data.repositories.data_sources.user.AuthenticationDataSource;
import com.example.android_project.data.repositories.data_sources.user.UserRemoteDataSource;
import com.google.firebase.auth.FirebaseUser;

public class UserRepository {

    private final AuthenticationDataSource authenticationDataSource;
    private final UserRemoteDataSource userRemoteDataSource;

    public UserRepository() {
        this.authenticationDataSource = new AuthenticationDataSource();
        this.userRemoteDataSource = new UserRemoteDataSource();
    }

    public LiveData<Boolean> register(User user, String password) {
        MediatorLiveData<Boolean> isRegistered = new MediatorLiveData<>();

        isRegistered.addSource(
                this.authenticationDataSource.register(user.getEmail(), password),
                isRegisteredInAuth -> {
                    if (isRegisteredInAuth) {
                        this.userRemoteDataSource.createUser(user);
                    } else {
                        isRegistered.setValue(false);
                    }
                }
        );

        isRegistered.addSource(
                this.userRemoteDataSource.getUserMutableLiveData(),
                newUser -> {
                    if (newUser != null) {
                        Log.v("UserRepository", "User created");
                        isRegistered.setValue(true);
                    } else {
                        isRegistered.setValue(false);
                    }
                }
        );

        return isRegistered;
    }

    public LiveData<Boolean> login(String email, String password) {
        MediatorLiveData<Boolean> isRegistered = new MediatorLiveData<>();

        isRegistered.addSource(
                this.authenticationDataSource.login(email, password),
                isRegisteredInAuth -> {
                    if (isRegisteredInAuth) {
                        this.userRemoteDataSource.getUser(email);
                    } else {
                        isRegistered.setValue(false);
                    }
                }
        );

        isRegistered.addSource(
                this.userRemoteDataSource.getUserMutableLiveData(),
                user -> {
                    if (user != null) {
                        Log.v("UserRepository", "User created");
                        isRegistered.setValue(true);
                    } else {
                        isRegistered.setValue(false);
                    }
                }
        );

        return isRegistered;
    }

    public MutableLiveData<Boolean> anonymousLogin() {
        return this.authenticationDataSource.anonymousLogin();
    }

    public MutableLiveData<User> getUser() {
        return this.userRemoteDataSource.getUserMutableLiveData();
    }

    public void logOut() {
        this.userRemoteDataSource.removeUser();
        this.authenticationDataSource.logOut();
    }

    public MutableLiveData<Boolean> isLogged() {
        MediatorLiveData<Boolean> isLogged = new MediatorLiveData<>();

        MutableLiveData<Boolean> hasUser = new MutableLiveData<>();
        MutableLiveData<Boolean> isLoggedInAuth = new MutableLiveData<>();

        isLogged.addSource(this.authenticationDataSource.isLoggedLiveData(), isLoggedInAuth::setValue);

        isLogged.addSource(this.userRemoteDataSource.getUserMutableLiveData(), user -> {
            hasUser.setValue(user != null);

            isLogged.setValue(
                    Boolean.TRUE.equals(isLoggedInAuth.getValue()) &&
                    Boolean.TRUE.equals(hasUser.getValue())
            );
        });

        return isLogged;
    }

    public MutableLiveData<FirebaseUser> getLoggedAuthUser() {
        return this.authenticationDataSource.getCurrentUserLiveData();
    }

    public MutableLiveData<Boolean> isUsernameAlreadyInUse(String username) {
        return this.userRemoteDataSource.isUsernameAlreadyInUse(username);
    }

    public MutableLiveData<Boolean> isEmailAlreadyInUse(String email) {
        return this.userRemoteDataSource.isEmailAlreadyInUse(email);
    }

//    private MutableLiveData<User> getLoggedUserData(String email) {
//        MutableLiveData<Boolean> isLogged = this.authenticationDataSource.isLoggedLiveData();
//
//        if (Boolean.TRUE.equals(isLogged.getValue())) {
//            this.userRemoteDataSource.getUserData(email);
//            return this.userRemoteDataSource.getUserMutableLiveData();
//        }
//
//        return new MutableLiveData<>();
//    }
}
