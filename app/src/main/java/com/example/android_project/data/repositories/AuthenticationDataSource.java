package com.example.android_project.data.repositories;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class AuthenticationDataSource {

    private FirebaseAuth mAuth;
    private MutableLiveData<FirebaseUser> currentUserLiveData;
    private MutableLiveData<Boolean> isLoggedLiveData;

    private MutableLiveData<String> errorCodeLiveData;

    public AuthenticationDataSource() {
        mAuth = FirebaseAuth.getInstance();
        currentUserLiveData = new MutableLiveData<>();
        isLoggedLiveData = new MutableLiveData<>(false);
        errorCodeLiveData = new MutableLiveData<>();

        if (mAuth.getCurrentUser() != null) {
            currentUserLiveData.setValue(mAuth.getCurrentUser());
            isLoggedLiveData.setValue(true);
        }
    }

    public MutableLiveData<Boolean> register(String email, String password) {
        MutableLiveData<Boolean> isRegistered = new MutableLiveData<>();
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        currentUserLiveData.setValue(mAuth.getCurrentUser());
                        isLoggedLiveData.setValue(true);
                        isRegistered.setValue(true);
                        errorCodeLiveData.setValue(null);
                    } else {
                        isRegistered.setValue(false);
                        isLoggedLiveData.setValue(false);
                        errorCodeLiveData.setValue(((FirebaseAuthException) Objects.requireNonNull(task.getException())).getErrorCode());
                    }
                });
        return isRegistered;
    }

    public MutableLiveData<Boolean> anonymousLogin() {
        MutableLiveData<Boolean> isLogged = new MutableLiveData<>();
        mAuth.signInAnonymously()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        currentUserLiveData.setValue(mAuth.getCurrentUser());
                        isLoggedLiveData.setValue(true);
                        errorCodeLiveData.setValue(null);
                        isLogged.setValue(true);
                    } else {
                        isLogged.setValue(false);
                        isLoggedLiveData.setValue(false);
                        errorCodeLiveData.setValue(((FirebaseAuthException) Objects.requireNonNull(task.getException())).getErrorCode());
                    }
                });
        return isLogged;
    }

    public Task<AuthResult> login(String email, String password) {
        return mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        currentUserLiveData.setValue(mAuth.getCurrentUser());
                        isLoggedLiveData.setValue(true);
                        errorCodeLiveData.setValue(null);
                    } else {
                        errorCodeLiveData.setValue(((FirebaseAuthException) Objects.requireNonNull(task.getException())).getErrorCode());
                    }
                });
    }

    public void logOut() {
        mAuth.signOut();
        isLoggedLiveData.setValue(false);
        currentUserLiveData.setValue(null);
        errorCodeLiveData.setValue(null);
    }

    public MutableLiveData<Boolean> isLoggedLiveData() {
        return isLoggedLiveData;
    }

    public MutableLiveData<FirebaseUser> getCurrentUserLiveData() {
        return currentUserLiveData;
    }
}
