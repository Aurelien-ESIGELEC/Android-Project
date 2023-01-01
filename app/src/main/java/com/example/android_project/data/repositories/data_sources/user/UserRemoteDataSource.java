package com.example.android_project.data.repositories.data_sources.user;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.android_project.data.models.user.User;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

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

    /*
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
                        Log.e("Firestore err", "" + ((FirebaseFirestoreException) task.getException()).getCode());
                        errorCodeLiveData.postValue("" + ((FirebaseFirestoreException) task.getException()).getCode());
                    }
                });
    }*/

    public void removeUser() {
        userMutableLiveData.postValue(null);
        errorCodeLiveData.postValue(null);
    }

    public MutableLiveData<Boolean> isUsernameAlreadyInUse(String username) {
        MutableLiveData<Boolean> isUsernameAlreadyInUse = new MutableLiveData<>();
        db.collection("users")
                .whereEqualTo("username", username)
                .get()
                .addOnCompleteListener(task -> {

                    if (task.isSuccessful()) {
                        Log.d("isUsernameAlreadyUsed", "" + username + " : " + (task.getResult().size() != 0));
                        if (task.getResult().size() != 0) {
                            isUsernameAlreadyInUse.postValue(true);
                        } else {
                            isUsernameAlreadyInUse.postValue(false);
                        }

                        errorCodeLiveData.postValue(null);
                    } else {
                        Log.e("Firestore err", "" + ((FirebaseFirestoreException) Objects.requireNonNull(task.getException())).getCode());
                        errorCodeLiveData.postValue("" + ((FirebaseFirestoreException) task.getException()).getCode());
                    }
                });
        return isUsernameAlreadyInUse;
    }

    public MutableLiveData<Boolean> isEmailAlreadyInUse(String email) {
        MutableLiveData<Boolean> isEmailAlreadyInUse = new MutableLiveData<>();
        db.collection("users")
                .whereEqualTo("email", email)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {

                        Log.d("isEmailAlreadyUsed", email + " : " + (task.getResult().size() != 0));
                        if (task.getResult().size() != 0) {
                            isEmailAlreadyInUse.postValue(true);
                        } else {
                            isEmailAlreadyInUse.postValue(false);
                        }
                        errorCodeLiveData.postValue(null);
                    } else {
                        errorCodeLiveData.postValue("" + ((FirebaseFirestoreException) task.getException()).getCode());
                        Log.e("Firestore err", "" + ((FirebaseFirestoreException) task.getException()).getCode());
                    }
                });
        return isEmailAlreadyInUse;
    }

    public MutableLiveData<Boolean> createUser(User user) {
        MutableLiveData<Boolean> isUserCreated = new MutableLiveData<>();
        db.collection("users")
                .add(user)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.v("Firestore", user.toString());
                        this.userMutableLiveData.setValue(user);
                        isUserCreated.setValue(true);
                        errorCodeLiveData.postValue(null);
                    } else {
                        isUserCreated.setValue(false);
                        errorCodeLiveData.postValue("" + ((FirebaseFirestoreException) Objects.requireNonNull(task.getException())).getCode());
                        Log.e("Firestore err", "" + ((FirebaseFirestoreException) task.getException()).getCode());
                    }
                });
        return isUserCreated;
    }

    public MutableLiveData<Boolean> getUser(String email) {
        MutableLiveData<Boolean> isUserCreated = new MutableLiveData<>();
        db.collection("users")
                .whereEqualTo("email", email)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult().size() == 1) {
                        User user = task.getResult().toObjects(User.class).get(0);
                        Log.v("Firestore", "User : " + user.toString());
                        this.userMutableLiveData.setValue(user);
                        isUserCreated.setValue(true);
                        errorCodeLiveData.postValue(null);
                    } else {
                        isUserCreated.setValue(false);
                        errorCodeLiveData.postValue("" + ((FirebaseFirestoreException) Objects.requireNonNull(task.getException())).getCode());
                        Log.e("Firestore err", "" + ((FirebaseFirestoreException) task.getException()).getCode());
                    }
                });
        return isUserCreated;
    }

    public MutableLiveData<User> getUserMutableLiveData() {
        return userMutableLiveData;
    }


}
