package com.example.android_project.data.repositories;

import androidx.lifecycle.MutableLiveData;

import com.example.android_project.data.models.User;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;

public class UserRepository {

    private final AuthenticationDataSource authenticationDataSource;
    private final UserRemoteDataSource userRemoteDataSource;

    public UserRepository() {
        this.authenticationDataSource = new AuthenticationDataSource();
        this.userRemoteDataSource = new UserRemoteDataSource();
    }

    public Task<AuthResult> login(String email, String password) {
        return this.authenticationDataSource.login(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        this.userRemoteDataSource.getUserData(email);
                    }
                });
    }

    public Task<AuthResult> anonymousLogin() {
        return this.authenticationDataSource.anonymousLogin();
    }

    public Task<AuthResult> register(String email, String password) {
        return this.authenticationDataSource.register(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        this.userRemoteDataSource.getUserData(email);
                    }
                });
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

    private MutableLiveData<User> getLoggedUserData(String email) {
        MutableLiveData<Boolean> isLogged = this.authenticationDataSource.isLoggedLiveData();

        if (Boolean.TRUE.equals(isLogged.getValue())) {
            this.userRemoteDataSource.getUserData(email);
            return this.userRemoteDataSource.getUserMutableLiveData();
        }

        return new MutableLiveData<>();
    }
}
