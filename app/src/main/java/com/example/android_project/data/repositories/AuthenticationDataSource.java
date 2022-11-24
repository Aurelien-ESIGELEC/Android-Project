package com.example.android_project.data.repositories;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;

public class AuthenticationDataSource {

    private static final String TAG = "Authentication";
    private FirebaseAuth mAuth;
    private MutableLiveData<FirebaseUser> currentUserLiveData;
    private MutableLiveData<Boolean> isLoggedLiveData;

    private MutableLiveData<String> errorCodeLiveData;

    public AuthenticationDataSource() {
        mAuth = FirebaseAuth.getInstance();
        currentUserLiveData = new MutableLiveData<>();
        isLoggedLiveData = new MutableLiveData<>(false);

        if (mAuth.getCurrentUser() != null) {
            currentUserLiveData.postValue(mAuth.getCurrentUser());
            isLoggedLiveData.postValue(true);
        }
    }

    public Task<AuthResult> register(String email, String password) {
        return mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        currentUserLiveData.postValue(mAuth.getCurrentUser());
                        isLoggedLiveData.postValue(true);
                        errorCodeLiveData.postValue(null);
                    } else {
                        errorCodeLiveData.postValue(((FirebaseAuthException) task.getException()).getErrorCode());
                    }
                });
    }

    public Task<AuthResult> anonymousLogin() {
        return mAuth.signInAnonymously()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        currentUserLiveData.postValue(mAuth.getCurrentUser());
                        isLoggedLiveData.postValue(true);
                        errorCodeLiveData.postValue(null);
                    } else {
                        errorCodeLiveData.postValue(((FirebaseAuthException) task.getException()).getErrorCode());
                    }
                });
    }

    public Task<AuthResult> login(String email, String password) {
        return mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        currentUserLiveData.postValue(mAuth.getCurrentUser());
                        isLoggedLiveData.postValue(true);
                        errorCodeLiveData.postValue(null);
                    } else {
                        errorCodeLiveData.postValue(((FirebaseAuthException) task.getException()).getErrorCode());
                    }
                });
    }

    public void logOut() {
        mAuth.signOut();
        isLoggedLiveData.postValue(false);
        currentUserLiveData.postValue(null);
        errorCodeLiveData.postValue(null);
    }

    public MutableLiveData<Boolean> isLoggedLiveData() {
        return isLoggedLiveData;
    }

    public MutableLiveData<FirebaseUser> getCurrentUserLiveData() {
        return currentUserLiveData;
    }
}
