package com.example.android_project.views.pages;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
import com.example.android_project.view_models.AuthViewModel;
import com.example.android_project.view_models.MapViewModel;
import com.example.android_project.views.adapters.GasStationFuelAdapter;
import com.example.android_project.views.components.AddFuelDialogFragment;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.common.collect.Lists;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 */
public class StationPriceListFragment extends Fragment {

    private String fuel;

    private MapViewModel mapViewModel;
    private AuthViewModel authViewModel;

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

        if (gasStation != null && gasStation.getFuelList() != null && !gasStation.getFuelList().isEmpty()) {

            List<Fuel> fuelList = gasStation.getFuelList().get(fuel);
            User user = authViewModel.getUser().getValue();

            if (fuelList != null) {

                Fuel apiFuelPrice = fuelList.get(0);

                recyclerView.setAdapter(new GasStationFuelAdapter(fuelList));
                recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

                mapViewModel.getPricesOfStationsByFuel(
                        user != null ? user.getUsername() : "" ,
                        gasStation.getId(),
                        fuel
                ).observe(getViewLifecycleOwner(), newFuelList -> {
                    if (!newFuelList.isEmpty()) {
                        fuelList.clear();

                        fuelList.addAll(newFuelList);
                        fuelList.add(apiFuelPrice);

                        // Sort the list by reliability index
                        Collections.sort(fuelList,(lhs, rhs) -> Double.compare(rhs.getReliabilityIndex(),lhs.getReliabilityIndex()));

                        // Reverse it to have the larger reliability index at the top of the list
                        List<Fuel> reverseSorted = Lists.reverse(fuelList);

                        ((GasStationFuelAdapter) Objects.requireNonNull(recyclerView.getAdapter())).setFuelList(reverseSorted);
                    }
                });
            }

            MaterialButton btnAdd = requireView().findViewById(R.id.station_btn_add_price);

            if (user == null) {
                btnAdd.setEnabled(false);
            }

            btnAdd.setOnClickListener(view1 -> {

                AddFuelDialogFragment newFragment = new AddFuelDialogFragment(gasStation, fuel);
                newFragment.show(requireActivity().getSupportFragmentManager(), "add_fuel");

            });

        }

    }
}