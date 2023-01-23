package com.example.android_project.data.data_sources.user;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.android_project.data.models.user.User;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Transaction;

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

    public MutableLiveData<Boolean> addReview(User user) {
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
        MutableLiveData<Boolean> isUserRetrieved = new MutableLiveData<>();
        db.collection("users")
                .whereEqualTo("email", email)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult().size() == 1) {
                        User user = task.getResult().toObjects(User.class).get(0);
                        user.setId(task.getResult().getDocuments().get(0).getId());
                        Log.d(TAG, "getUser: " + user);
                        userMutableLiveData.setValue(user);
                        isUserRetrieved.setValue(true);
                        errorCodeLiveData.postValue(null);
                    } else {
                        isUserRetrieved.setValue(false);
                        errorCodeLiveData.postValue("" + ((FirebaseFirestoreException) Objects.requireNonNull(task.getException())).getCode());
                        Log.e("Firestore err", "" + ((FirebaseFirestoreException) task.getException()).getCode());
                    }
                });
        return isUserRetrieved;
    }

    public MutableLiveData<Boolean> addFriendToUser(String username) {
        MutableLiveData<Boolean> isAdded = new MutableLiveData<>();

        if (username != null && !username.isEmpty()) {

            db.collection("users")
                    .whereEqualTo("username", username)
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful() && task.getResult().size() == 1) {
                            errorCodeLiveData.postValue(null);

                            final DocumentReference sfDocRef = db.collection("users")
                                    .document(Objects.requireNonNull(userMutableLiveData.getValue()).getId());

                            db.runTransaction((Transaction.Function<Void>) transaction -> {
                                        DocumentSnapshot snapshot = transaction.get(sfDocRef);

                                        User[] users = snapshot.get("friends", User[].class);
                                        Log.d(TAG, "addFriendToUser: " + users);
//                                        transaction.update(sfDocRef, "friends", newPopulation);

                                        // Success
                                        return null;
                                    })
                                    .addOnSuccessListener(aVoid -> Log.d(TAG, "Transaction success!"))
                                    .addOnFailureListener(e -> Log.w(TAG, "Transaction failure.", e));
                        } else {
                            isAdded.setValue(false);
                            errorCodeLiveData.postValue("" + ((FirebaseFirestoreException) Objects.requireNonNull(task.getException())).getCode());
                            Log.e("Firestore err", "" + ((FirebaseFirestoreException) task.getException()).getCode());
                        }
                    });
        }
        return isAdded;
    }

    public MutableLiveData<User> getUserMutableLiveData() {
        return userMutableLiveData;
    }


}
