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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.example.android_project.R;
import com.example.android_project.data.models.fuel_price.Fuel;
import com.example.android_project.data.models.fuel_price.GasStation;
import com.example.android_project.view_models.MapViewModel;
import com.example.android_project.views.adapters.FriendsViewPagerAdapter;
import com.example.android_project.views.adapters.GasStationFuelAdapter;
import com.example.android_project.views.adapters.StationPriceViewPagerAdapter;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class GasStationFragment extends Fragment {

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
            ViewPager2 viewPager2 = requireView().findViewById(R.id.station_view_pager);

            List<String> fuelList = new ArrayList<>(gasStation.getFuelList().keySet());

            viewPager2.setAdapter(new StationPriceViewPagerAdapter(requireActivity().getSupportFragmentManager(),getLifecycle(), fuelList));

            new TabLayoutMediator(tabLayout,viewPager2, (tab, position) -> {
                tab.setText(fuelList.get(position));
            }).attach();

        }

    }

}
