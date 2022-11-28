package com.example.android_project.data.repositories;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.android_project.data.models.User;
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
        return this.authenticationDataSource.isLoggedLiveData();
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
