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

    public MutableLiveData<Fuel> addReview(Fuel fuel, String user, String newReview) {
        MutableLiveData<Fuel> fuelMutableLiveData = new MutableLiveData<>();


        final DocumentReference documentReference = db.collection("price_users")
                .document(fuel.getId());

        db.runTransaction(transaction -> {
            DocumentSnapshot snapshot = transaction.get(documentReference);

            HashMap<String, String> reviews = snapshot.get("reviews", HashMap.class);

            Double reliabilityIndex = snapshot.getDouble("reliability_index");

            if (reviews == null) {
                reviews = new HashMap<>();
            }


            if (reviews.containsKey(user)) {
                String oldReview = reviews.get(user);
                if (!oldReview.equals(newReview)) {
                    if (oldReview.equals("up")) {
                        reliabilityIndex -= 0.02;
                    }

                    if (oldReview.equals("down")) {
                        reliabilityIndex += 0.02;
                    }
                }
            } else {
                if (newReview.equals("up")) {
                    reliabilityIndex += 0.01;
                }

                if (newReview.equals("down")) {
                    reliabilityIndex -= 0.01;
                }
                reviews.put(user, newReview);
            }

            fuel.setReliabilityIndex(reliabilityIndex);
            fuel.setCanUpdate(reliabilityIndex < 10 && reliabilityIndex > 0 &&
                    !Objects.requireNonNull(user).isEmpty() &&
                    !reviews.containsKey(user) &&
                    !user.equals(snapshot.getString("user")));

            transaction.update(documentReference, "reliability_index", reliabilityIndex);
            transaction.update(documentReference, "reviews", reviews);

            // Success
            return fuel;

        }).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                fuelMutableLiveData.setValue(fuel);
            } else {
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

                            Fuel fuel = getFuelFromMap(map, user);
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

    private Fuel getFuelFromMap(Map<String, Object> map, String user)  {
        double price = 0;
        String userAdded = "";
        double reliabilityIndex = 0d;
        Timestamp timestamp = null;
        String fuelType = "";

        if (map.containsKey("price")) {
            price = Double.parseDouble(map.get("price").toString());
        }

        if (map.containsKey("fuel")) {
            fuelType = (String) map.get("fuel");
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

        return new Fuel()
                .setPrice(price)
                .setName(fuelType)
                .setCanUpdate(canUpdate)
                .setReliabilityIndex(reliabilityIndex)
                .setUpdateDateFromTimestamp(timestamp);
    }

}
