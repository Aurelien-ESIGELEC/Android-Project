package com.example.android_project.data.data_sources.user;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.android_project.data.models.user.User;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class UserRemoteDataSource {

    private static final String TAG = "Authentication";

    private FirebaseFirestore db;

    private MutableLiveData<User> userMutableLiveData;

    private MutableLiveData<String> errorCodeLiveData;

    private MutableLiveData<String> userIdLiveData;

    public UserRemoteDataSource() {
        userMutableLiveData = new MutableLiveData<>();
        db = FirebaseFirestore.getInstance();
        errorCodeLiveData = new MutableLiveData<>();
        userIdLiveData = new MutableLiveData<>();
    }

    public void removeUser() {
        userMutableLiveData.postValue(null);
        errorCodeLiveData.postValue(null);
    }

    public MutableLiveData<Boolean> isUsernameAlreadyInUse(String username) {
        MutableLiveData<Boolean> isUsernameAlreadyInUse = new MutableLiveData<>();
        db.collection("users").whereEqualTo("username", username).get().addOnCompleteListener(task -> {

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
        db.collection("users").whereEqualTo("email", email).get().addOnCompleteListener(task -> {
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
        db.collection("users").add(user).addOnCompleteListener(task -> {
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
        db.collection("users").add(user).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Log.v("Firestore", user.toString());
                this.userMutableLiveData.setValue(user);
                this.userIdLiveData.setValue(task.getResult().getId());
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

    public MutableLiveData<Boolean> updateUserPreferences(List<String> notifications, List<String> favoriteFuels, String sharing) {
        MutableLiveData<Boolean> booleanMutableLiveData = new MutableLiveData<>();

        final DocumentReference documentReference = db.collection("users").document(Objects.requireNonNull(userIdLiveData.getValue()));

        db.runTransaction(transaction -> {
            DocumentSnapshot snapshot = transaction.get(documentReference);

            User user = snapshot.toObject(User.class);

            if (user != null) {
                user.setFavoriteFuels(favoriteFuels);
                user.setNotifications(notifications);
                user.setSharing(sharing);

                transaction.update(documentReference, "favorite_fuels", favoriteFuels);
                transaction.update(documentReference, "notifications", notifications);
                transaction.update(documentReference, "sharing", sharing);
            }

            // Success
            return user;

        }).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                userMutableLiveData.setValue(task.getResult());
                booleanMutableLiveData.setValue(true);
            } else {
                booleanMutableLiveData.setValue(false);
            }
        });

        return booleanMutableLiveData;
    }

    public MutableLiveData<Boolean> getUser(String email) {
        MutableLiveData<Boolean> isUserRetrieved = new MutableLiveData<>();
        db.collection("users").whereEqualTo("email", email).get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult().size() == 1) {
                User user = task.getResult().toObjects(User.class).get(0);
                userIdLiveData.setValue(task.getResult().getDocuments().get(0).getId());
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

    public MutableLiveData<List<User>> getFollowers() {
        MutableLiveData<List<User>> listMutableLiveData = new MutableLiveData<>();

        User user = userMutableLiveData.getValue();

        if (user != null) {
            db.collection("users").whereArrayContains("friends", user.getUsername()).get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    List<User> users = task.getResult().toObjects(User.class);
                    listMutableLiveData.setValue(users);
                    errorCodeLiveData.postValue(null);
                } else {
                    listMutableLiveData.setValue(null);
                    errorCodeLiveData.postValue("" + ((FirebaseFirestoreException) Objects.requireNonNull(task.getException())).getCode());
                    Log.e("Firestore err", "" + ((FirebaseFirestoreException) task.getException()).getCode());
                }
            });
        } else {
            listMutableLiveData.setValue(null);
        }

        return listMutableLiveData;
    }

    public MutableLiveData<Boolean> addFriendToUser(String username) {
        MutableLiveData<Boolean> isAdded = new MutableLiveData<>();

        if (username != null && !username.isEmpty()) {

            db.collection("users").whereEqualTo("username", username).get().addOnCompleteListener(task -> {
                if (task.isSuccessful() && task.getResult().size() == 1) {
                    errorCodeLiveData.postValue(null);

                    User user = this.userMutableLiveData.getValue();

                    if (user == null) {
                        isAdded.setValue(false);
                        return;
                    }

                    if (user.getFriends() == null) {
                        user.setFriends(new ArrayList<>());
                    }

                    user.getFriends().add(username);


                    db.collection("users").document(Objects.requireNonNull(userIdLiveData.getValue())).update("friends", user.getFriends()).addOnCompleteListener(task1 -> {
                        if (task.isSuccessful()) {
                            this.userMutableLiveData.setValue(user);
                            isAdded.setValue(true);
                        } else {
                            isAdded.setValue(false);
                            errorCodeLiveData.postValue("" + ((FirebaseFirestoreException) Objects.requireNonNull(task.getException())).getCode());
                            Log.e("Firestore err", "" + ((FirebaseFirestoreException) task.getException()).getCode());
                        }
                    });

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
