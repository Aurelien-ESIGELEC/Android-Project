package com.example.android_project.data.repositories;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.android_project.data.models.User;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.Map;
import java.util.Objects;

public class UserRemoteDataSource {

    private static final String TAG = "Authentication";

    private FirebaseFirestore db;

    private MutableLiveData<User> userMutableLiveData;

    private MutableLiveData<String> errorCodeLiveData;

    public UserRemoteDataSource() {
        userMutableLiveData = new MutableLiveData<>();
        db = FirebaseFirestore.getInstance();
        errorCodeLiveData = new MutableLiveData<>();
    }

    public void getUserData(String email) {
        db.collection("users")
                .whereEqualTo("email", email)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Map<String, Object> userMap = task
                                .getResult()
                                .getDocuments()
                                .get(0)
                                .getData();

                        User user = null;
                        if (userMap != null && !userMap.isEmpty()) {
                            user = new User(
                                    (String) userMap.get("username"),
                                    (String) userMap.get("email"),
                                    (String) userMap.get("firstName"),
                                    (String) userMap.get("lastName")
                            );
                        }

                        userMutableLiveData.postValue(user);
                        errorCodeLiveData.postValue(null);
                    } else {
                        errorCodeLiveData.postValue("" + ((FirebaseFirestoreException) task.getException()).getCode());
                    }
                });
    }

    public void removeUser() {
        userMutableLiveData.postValue(null);
        errorCodeLiveData.postValue(null);
    }

    public MutableLiveData<User> getUserMutableLiveData() {
        return userMutableLiveData;
    }
}
