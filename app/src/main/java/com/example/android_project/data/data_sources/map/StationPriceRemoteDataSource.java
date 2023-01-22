package com.example.android_project.data.data_sources.map;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.android_project.data.models.fuel_price.Fuel;
import com.example.android_project.data.models.user.User;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.Transaction;

import java.text.DateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TimeZone;

public class StationPriceRemoteDataSource {

    private static final String TAG = "StationPriceRemoteDataSource";

    private final FirebaseFirestore db;

    public StationPriceRemoteDataSource() {
        db = FirebaseFirestore.getInstance();
    }

    public MutableLiveData<Fuel> addPrice(String user, String fuelType, double price, String stationId) {
        MutableLiveData<Fuel> fuelMutableLiveData = new MutableLiveData<>();

        Map<String, Object> docData = new HashMap<>();
        docData.put("fuel", fuelType);
        docData.put("price", price);
        docData.put("reliability_index", 5.0d);
        docData.put("station_id", stationId);
        docData.put("user", user);
        docData.put("update_date", Timestamp.now());

        db.collection("price_users")
                .add(docData)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {

                        Fuel fuel = new Fuel()
                                .setPrice(price)
                                .setName(fuelType)
                                .setCanUpdate(false)
                                .setReliabilityIndex((double) docData.get("reliability_index"))
                                .setUpdateDateFromTimestamp((Timestamp) docData.get("update_date"));
                        fuelMutableLiveData.setValue(fuel);

                    } else {
                        Log.e("Firestore err", "" + ((FirebaseFirestoreException) Objects.requireNonNull(task.getException())).getCode());
                        fuelMutableLiveData.setValue(null);
                    }
                });

        return fuelMutableLiveData;
    }

    public MutableLiveData<List<Fuel>> getPricesOfStationsByFuel(String user, String stationId, String fuelType) {
        MutableLiveData<List<Fuel>> fuelMutableLiveData = new MutableLiveData<>();

        db.collection("price_users")
                .whereEqualTo("fuel", fuelType)
                .whereEqualTo("station_id", stationId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {

                        List<Fuel> fuelPrice = new ArrayList<>();
                        for (QueryDocumentSnapshot document : task.getResult()) {

                            Map<String, Object> map = document.getData();

                            double price = 0;
                            String userAdded = "";
                            double reliabilityIndex = 0d;
                            Timestamp timestamp = null;

                            if (map.containsKey("price")) {
                                price = Double.parseDouble(map.get("price").toString());
                            }

                            if (map.containsKey("user")) {
                                userAdded = (String) map.get("user");
                            }

                            if (map.containsKey("reliability_index")) {
                                reliabilityIndex = Double.parseDouble(map.get("reliability_index").toString());
                            }

                            boolean canUpdate = reliabilityIndex < 10 && reliabilityIndex > 0 &&
                                    !Objects.requireNonNull(user).isEmpty() &&
                                    !user.equals(userAdded);


                            if (map.containsKey("update_date")) {
                                timestamp = (Timestamp) map.get("update_date");
                            }


                            Fuel fuel = new Fuel()
                                    .setPrice(price)
                                    .setName(fuelType)
                                    .setCanUpdate(canUpdate)
                                    .setReliabilityIndex(reliabilityIndex)
                                    .setUpdateDateFromTimestamp(timestamp);
                            fuelPrice.add(fuel);
                            Log.d(TAG, "getPricesOfStationsByFuel: " + fuel);
                        }
                        fuelMutableLiveData.setValue(fuelPrice);
                    } else {
                        Log.e("Firestore err", "" + ((FirebaseFirestoreException) Objects.requireNonNull(task.getException())).getCode());
                        fuelMutableLiveData.setValue(null);
                    }
                });

        return fuelMutableLiveData;
    }

//    public MutableLiveData<Boolean> isUsernameAlreadyInUse(String username) {
//        MutableLiveData<Boolean> isUsernameAlreadyInUse = new MutableLiveData<>();
//        db.collection("users")
//                .whereEqualTo("username", username)
//                .get()
//                .addOnCompleteListener(task -> {
//
//                    if (task.isSuccessful()) {
//                        Log.d("isUsernameAlreadyUsed", "" + username + " : " + (task.getResult().size() != 0));
//                        if (task.getResult().size() != 0) {
//                            isUsernameAlreadyInUse.postValue(true);
//                        } else {
//                            isUsernameAlreadyInUse.postValue(false);
//                        }
//
//                        errorCodeLiveData.postValue(null);
//                    } else {
//                        Log.e("Firestore err", "" + ((FirebaseFirestoreException) Objects.requireNonNull(task.getException())).getCode());
//                        errorCodeLiveData.postValue("" + ((FirebaseFirestoreException) task.getException()).getCode());
//                    }
//                });
//        return isUsernameAlreadyInUse;
//    }
//
//    public MutableLiveData<Boolean> isEmailAlreadyInUse(String email) {
//        MutableLiveData<Boolean> isEmailAlreadyInUse = new MutableLiveData<>();
//        db.collection("users")
//                .whereEqualTo("email", email)
//                .get()
//                .addOnCompleteListener(task -> {
//                    if (task.isSuccessful()) {
//
//                        Log.d("isEmailAlreadyUsed", email + " : " + (task.getResult().size() != 0));
//                        if (task.getResult().size() != 0) {
//                            isEmailAlreadyInUse.postValue(true);
//                        } else {
//                            isEmailAlreadyInUse.postValue(false);
//                        }
//                        errorCodeLiveData.postValue(null);
//                    } else {
//                        errorCodeLiveData.postValue("" + ((FirebaseFirestoreException) task.getException()).getCode());
//                        Log.e("Firestore err", "" + ((FirebaseFirestoreException) task.getException()).getCode());
//                    }
//                });
//        return isEmailAlreadyInUse;
//    }

//    public MutableLiveData<Boolean> createUser(User user) {
//        MutableLiveData<Boolean> isUserCreated = new MutableLiveData<>();
//        db.collection("users")
//                .add(user)
//                .addOnCompleteListener(task -> {
//                    if (task.isSuccessful()) {
//                        Log.v("Firestore", user.toString());
//                        this.userMutableLiveData.setValue(user);
//                        isUserCreated.setValue(true);
//                        errorCodeLiveData.postValue(null);
//                    } else {
//                        isUserCreated.setValue(false);
//                        errorCodeLiveData.postValue("" + ((FirebaseFirestoreException) Objects.requireNonNull(task.getException())).getCode());
//                        Log.e("Firestore err", "" + ((FirebaseFirestoreException) task.getException()).getCode());
//                    }
//                });
//        return isUserCreated;
//    }

//    public MutableLiveData<Boolean> getUser(String email) {
//        MutableLiveData<Boolean> isUserRetrieved = new MutableLiveData<>();
//        db.collection("users")
//                .whereEqualTo("email", email)
//                .get()
//                .addOnCompleteListener(task -> {
//                    if (task.isSuccessful() && task.getResult().size() == 1) {
//                        User user = task.getResult().toObjects(User.class).get(0);
//                        user.setId(task.getResult().getDocuments().get(0).getId());
//                        Log.d(TAG, "getUser: " + user);
//                        userMutableLiveData.setValue(user);
//                        isUserRetrieved.setValue(true);
//                        errorCodeLiveData.postValue(null);
//                    } else {
//                        isUserRetrieved.setValue(false);
//                        errorCodeLiveData.postValue("" + ((FirebaseFirestoreException) Objects.requireNonNull(task.getException())).getCode());
//                        Log.e("Firestore err", "" + ((FirebaseFirestoreException) task.getException()).getCode());
//                    }
//                });
//        return isUserRetrieved;
//    }

//    public MutableLiveData<Boolean> addFriendToUser(String username) {
//        MutableLiveData<Boolean> isAdded = new MutableLiveData<>();
//
//        if (username != null && !username.isEmpty()) {
//
//            db.collection("users")
//                    .whereEqualTo("username", username)
//                    .get()
//                    .addOnCompleteListener(task -> {
//                        if (task.isSuccessful() && task.getResult().size() == 1) {
//                            errorCodeLiveData.postValue(null);
//
//                            final DocumentReference sfDocRef = db.collection("users")
//                                    .document(Objects.requireNonNull(userMutableLiveData.getValue()).getId());
//
//                            db.runTransaction((Transaction.Function<Void>) transaction -> {
//                                        DocumentSnapshot snapshot = transaction.get(sfDocRef);
//
//                                        User[] users = snapshot.get("friends", User[].class);
//                                        Log.d(TAG, "addFriendToUser: " + users);
////                                        transaction.update(sfDocRef, "friends", newPopulation);
//
//                                        // Success
//                                        return null;
//                                    })
//                                    .addOnSuccessListener(aVoid -> Log.d(TAG, "Transaction success!"))
//                                    .addOnFailureListener(e -> Log.w(TAG, "Transaction failure.", e));
//                        } else {
//                            isAdded.setValue(false);
//                            errorCodeLiveData.postValue("" + ((FirebaseFirestoreException) Objects.requireNonNull(task.getException())).getCode());
//                            Log.e("Firestore err", "" + ((FirebaseFirestoreException) task.getException()).getCode());
//                        }
//                    });
//        }
//        return isAdded;
//    }

}
