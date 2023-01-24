package com.example.android_project.data.data_sources.user;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class AuthenticationDataSource {

    private static final String TAG = "AuthenticationDataSource";

    private FirebaseAuth mAuth;
    private MutableLiveData<Boolean> isLoggedLiveData;

    private MutableLiveData<String> errorCodeLiveData;

    public AuthenticationDataSource() {
        mAuth = FirebaseAuth.getInstance();
        isLoggedLiveData = new MutableLiveData<>(null);
        errorCodeLiveData = new MutableLiveData<>();
    }

    public MutableLiveData<Boolean> register(String email, String password) {
        MutableLiveData<Boolean> isRegistered = new MutableLiveData<>();
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
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
        mAuth.signInAnonymously().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
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

    public MutableLiveData<Boolean> login(String email, String password) {
        MutableLiveData<Boolean> isLogged = new MutableLiveData<>();
        if (email != null && password != null && !email.isEmpty() && !password.isEmpty()) {

            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Log.d(TAG, "login: " + task);
                    isLoggedLiveData.setValue(true);
                    errorCodeLiveData.setValue(null);
                    isLogged.setValue(true);
                } else {
                    isLogged.setValue(false);
                    isLoggedLiveData.setValue(false);
                    errorCodeLiveData.setValue(((FirebaseAuthException) Objects.requireNonNull(task.getException())).getErrorCode());
                }
            });
        } else {
            isLogged.setValue(false);
        }
        return isLogged;
    }

    public void logOut() {
        mAuth.signOut();
        isLoggedLiveData.setValue(false);
        errorCodeLiveData.setValue(null);
    }

    public String getEmailFromFirebaseUser() {
        return Objects.requireNonNull(mAuth.getCurrentUser()).getEmail();
    }

    public Boolean hasExistingUser() {
        return mAuth.getCurrentUser() != null;
    }

    public Boolean isAnonymous() {
        return Objects.requireNonNull(mAuth.getCurrentUser()).isAnonymous();
    }

    public MutableLiveData<Boolean> isLoggedLiveData() {
        return isLoggedLiveData;
    }

}
