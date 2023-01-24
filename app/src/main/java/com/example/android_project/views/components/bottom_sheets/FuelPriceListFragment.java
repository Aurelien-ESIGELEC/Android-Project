package com.example.android_project.views.components.bottom_sheets;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android_project.R;
import com.example.android_project.data.models.fuel_price.Fuel;
import com.example.android_project.data.models.fuel_price.GasStation;
import com.example.android_project.view_models.MapViewModel;
import com.example.android_project.views.adapters.GasStationFuelAdapter;
import com.google.android.material.tabs.TabLayout;

import java.util.List;
import java.util.Objects;

public class FuelPriceListFragment extends Fragment {

    public static final String TAG = "GasStationFragment";

    private MapViewModel mapViewModel;


    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {
        return inflater.inflate(R.layout.fragment_gas_station_info_bottom_sheet, container, false);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mapViewModel = new ViewModelProvider(requireActivity()).get(MapViewModel.class);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        GasStation gasStation = mapViewModel.getSelectedStation().getValue();

        TextView tvAddress = requireView().findViewById(R.id.station_tv_address);
        TextView tvCity = requireView().findViewById(R.id.station_tv_city);
        TextView tvKm = requireView().findViewById(R.id.station_km);

        if (gasStation != null) {

            tvAddress.setText(gasStation.getAddress());
            tvCity.setText(getString(R.string.station_city, gasStation.getPostalCode(), gasStation.getCity()));

            mapViewModel.getDistanceBetweenLocationAndGasStation(gasStation).observe(getViewLifecycleOwner(), aDouble -> {
                tvKm.setText(getString(R.string.map_kilometer, aDouble.floatValue()));
            });


            TabLayout tabLayout = requireView().findViewById(R.id.station_tabs_fuels);
            RecyclerView recyclerView = requireView().findViewById(R.id.station_rv_fuel_list);

            for (String fuel: gasStation.getFuelList().keySet()) {
                TabLayout.Tab tab = tabLayout.newTab();
                tab.setText(fuel);
                tabLayout.addTab(tab);
            }

            tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {

                    List<Fuel> fuelList = gasStation.getFuelList().get(Objects.requireNonNull(tab.getText()).toString());
                    ((GasStationFuelAdapter) Objects.requireNonNull(recyclerView.getAdapter())).setFuelList(fuelList);

                    recyclerView.getAdapter().notifyDataSetChanged();
                    recyclerView.invalidate();
                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {}

                @Override
                public void onTabReselected(TabLayout.Tab tab) {}

            });

            tabLayout.selectTab(tabLayout.getTabAt(0));

        }

    }

}
