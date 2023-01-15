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

    private static final String TAG = "UserRepository";
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

    public void checkIfLoggedInCache() {
        Log.d(TAG, "checkIfLoggedInCache: "+ authenticationDataSource.hasExistingUser());
        if (authenticationDataSource.hasExistingUser()) {
            this.authenticationDataSource.isLoggedLiveData().setValue(true);
        }
    }

    public LiveData<Boolean> retrieveUser() {
        MediatorLiveData<Boolean> isLogged = new MediatorLiveData<>();

        if (authenticationDataSource.hasExistingUser() && !authenticationDataSource.isAnonymous()) {

            isLogged.addSource(
                    this.userRemoteDataSource.getUser(
                            this.authenticationDataSource.getEmailFromFirebaseUser()
                    ),
                    user -> {
                        if (user != null) {
                            isLogged.setValue(true);
                        } else {
                            isLogged.setValue(false);
                        }
                    }
            );
        } else  {
            isLogged.setValue(false);
        }

        return isLogged;
    }

    public Boolean hasExistingUserInCache() {
        return this.authenticationDataSource.hasExistingUser();
    }

    public LiveData<Boolean> login(String email, String password) {
        MediatorLiveData<Boolean> isFullyLogged = new MediatorLiveData<>();
        isFullyLogged.setValue(null);

        MutableLiveData<Boolean> isLogged = this.authenticationDataSource.login(email, password);
        isFullyLogged.addSource(
                isLogged,
                isRegisteredInAuth -> {
                    Log.d(TAG, "login: "+ isFullyLogged);
                    if (isRegisteredInAuth) {
                        isFullyLogged.addSource(
                                this.userRemoteDataSource.getUser(email),
                                user -> {
                                    if (user != null) {
                                        Log.v("UserRepository", "User getted : " + user );
                                        isFullyLogged.setValue(true);
                                    } else {
                                        isFullyLogged.setValue(false);
                                    }
                                }
                        );
                    } else {
                        isFullyLogged.setValue(false);
                    }
                }
        );



        return isFullyLogged;
    }

//    public MutableLiveData<Boolean> addFriendToUser(String username) {
//        MediatorLiveData<Boolean> isAdded = new MediatorLiveData<>();
//
//        if (username != null && !username.isEmpty()) {
//
//        }
//    }

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
        return this.authenticationDataSource.isLoggedLiveData();
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
