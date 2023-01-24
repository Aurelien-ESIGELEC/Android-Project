package com.example.android_project.views.pages;

import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android_project.R;
import com.example.android_project.data.models.fuel_price.Fuel;
import com.example.android_project.data.models.fuel_price.GasStation;
import com.example.android_project.data.models.user.User;
import com.example.android_project.utils.Utils;
import com.example.android_project.view_models.AuthViewModel;
import com.example.android_project.view_models.MapViewModel;
import com.example.android_project.views.adapters.GasStationFuelAdapter;
import com.example.android_project.views.components.AddFuelDialogFragment;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.progressindicator.CircularProgressIndicator;

import java.util.Collections;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class StationPriceListFragment extends Fragment {

    private static final String TAG = "StationPriceListFragment";

    private String fuel;

    private MapViewModel mapViewModel;
    private AuthViewModel authViewModel;

    private MaterialButton btnAdd;

    public StationPriceListFragment() {
        this("");
    }

    public StationPriceListFragment(String fuel) {
        this.fuel = fuel;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mapViewModel = new ViewModelProvider(requireActivity()).get(MapViewModel.class);
        authViewModel = new ViewModelProvider(requireActivity()).get(AuthViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_station_price_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView recyclerView = requireView().findViewById(R.id.station_rv_fuel_list);

        GasStation gasStation = mapViewModel.getSelectedStation().getValue();

        Log.d(TAG, "onViewCreated: created");

        if (gasStation != null && gasStation.getFuelList() != null && !gasStation.getFuelList().isEmpty()) {

            List<Fuel> fuelList = gasStation.getFuelList().get(fuel);
            User user = authViewModel.getUser().getValue();

            if (fuelList != null) {

                recyclerView.setAdapter(new GasStationFuelAdapter(sortFuelList(fuelList), new GasStationFuelAdapter.OnReviewListener() {
                    @Override
                    public void onUpToggled(GasStationFuelAdapter.ViewHolder view, Fuel fuel, boolean isToggled) {
                        Log.d(TAG, "onUpToggled: " + fuel);
                        if (isToggled) {
                            updateReview(view,fuel,user, "up");
                        } else {
                            updateReview(view,fuel,user, "");
                        }

                    }

                    @Override
                    public void onDownToggled(GasStationFuelAdapter.ViewHolder view, Fuel fuel, boolean isToggled) {
                        Log.d(TAG, "onDownToggled: "+ fuel);
                        if (isToggled) {
                            updateReview(view,fuel,user, "down");
                        } else {
                            updateReview(view,fuel,user, "");
                        }
                    }

                }));
                recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

                mapViewModel.getPricesOfStationsByFuel(
                        user != null ? user.getUsername() : "",
                        gasStation,
                        fuel
                ).observe(getViewLifecycleOwner(), newFuelList -> {

                    ((GasStationFuelAdapter) recyclerView.getAdapter()).setFuelList(sortFuelList(gasStation.getFuelList().get(fuel)));
                });
            }

            btnAdd = requireView().findViewById(R.id.station_btn_add_price);

            Location userLocation = mapViewModel.getUserLocation().getValue();
            if (userLocation != null) {
                setIfButtonEnabled(gasStation, userLocation);
            }


            mapViewModel.getUserLocation().observe(getViewLifecycleOwner(), location -> {
                if (userLocation != null) {
                    setIfButtonEnabled(gasStation, location);
                }
            });

            if (user == null) {
                btnAdd.setEnabled(false);
            }

            btnAdd.setOnClickListener(view1 -> {
                AddFuelDialogFragment newFragment = new AddFuelDialogFragment(gasStation, fuel);
                newFragment.show(requireActivity().getSupportFragmentManager(), "add_fuel");
            });

        }
    }

    private List<Fuel> sortFuelList(List<Fuel> fuelList) {
        Collections.sort(fuelList, (lhs, rhs) -> Double.compare(rhs.getReliabilityIndex(), lhs.getReliabilityIndex()));
        return fuelList;
    }

    /**
     *
     * @param view
     * @param fuel
     * @param user
     * @param review
     */
    private void updateReview(GasStationFuelAdapter.ViewHolder view, Fuel fuel, User user, String review) {
        if (user != null) {
            CircularProgressIndicator progressIndicator = view.getProgressIndicator();
            progressIndicator.setVisibility(View.VISIBLE);

            ImageButton ibtnUp = view.getIbtnUp();
            ImageButton ibtnDown = view.getIbtnDown();

            ibtnUp.setEnabled(false);
            ibtnDown.setEnabled(false);

            Log.d(TAG, "updateReview: " + review);

            mapViewModel
                    .updateReviewOnPrice(fuel, user.getUsername(), review)
                    .observe(getViewLifecycleOwner(), fuel1 -> {
                        progressIndicator.setVisibility(View.GONE);
                        view.getTvIv().setText(getString(R.string.fuel_price_iv, fuel1.getReliabilityIndex()));
                        ibtnUp.setEnabled(true);
                        ibtnDown.setEnabled(true);
                    });
        }
    }

    private void setIfButtonEnabled(GasStation gasStation, Location userLocation) {
        float distance = Utils.getDistanceBetweenTwoPoint(
                userLocation.getLatitude(),
                userLocation.getLongitude(),
                gasStation.getLat(),
                gasStation.getLon()
        );

        if (distance > 200) {
            btnAdd.setEnabled(false);
        }
    }
}