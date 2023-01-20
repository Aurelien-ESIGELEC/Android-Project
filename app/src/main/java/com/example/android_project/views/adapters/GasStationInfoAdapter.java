package com.example.android_project.views.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.example.android_project.R;
import com.example.android_project.data.models.fuel_price.Fuel;
import com.example.android_project.data.models.fuel_price.GasStation;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class GasStationInfoAdapter extends RecyclerView.Adapter<GasStationInfoAdapter.ViewHolder> {

    private final GasStation gasStation;
    private Double distance;

    private String fuelType;

    /**
     * Initialize the dataset of the Adapter
     *
     * @param gasStation {@link GasStation} containing the data to populate view to be used
     * by RecyclerView
     */
    public GasStationInfoAdapter(GasStation gasStation, Double distance) {
        this.gasStation = gasStation;
        this.distance = distance;
        this.fuelType = "";
    }

    public GasStationInfoAdapter(GasStation gasStation) {
        this.gasStation = gasStation;
    }

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder)
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvAddress;
        private final TextView tvCity;
        private final TextView tvDistance;

        private final RecyclerView rvFuelList;

        private ViewPager2 viewPager;

        private TabLayout tabs;

        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View

            tvAddress = view.findViewById(R.id.station_fuel_tv_date);
            tvCity = view.findViewById(R.id.station_fuel_tv_price);
            tvDistance = view.findViewById(R.id.station_km);

            rvFuelList = view.findViewById(R.id.station_rv_fuel_list);

            viewPager = view.findViewById(R.id.station_view_pager);

            tabs = view.findViewById(R.id.station_tabs_fuels);
        }

        public TabLayout getTabs() {
            return tabs;
        }

        public ViewPager2 getViewPager() {
            return viewPager;
        }

        public TextView getTvAddress() {
            return tvAddress;
        }

        public TextView getTvCity() {
            return tvCity;
        }

        public TextView getTvDistance() {
            return tvDistance;
        }

        public RecyclerView getRvFuelList() {
            return rvFuelList;
        }
    }

    // Create new views (invoked by the layout manager)
    @NonNull
    @Override
    public GasStationInfoAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.gas_station_info, viewGroup, false);



        return new ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(@NonNull GasStationInfoAdapter.ViewHolder viewHolder, final int position) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element

        Context context = viewHolder.itemView.getContext();
        viewHolder.getTvAddress().setText(gasStation.getAddress());
        viewHolder.getTvCity().setText(context.getString(R.string.station_city, gasStation.getPostalCode(), gasStation.getCity()));
        if (distance != null) {
            viewHolder.getTvDistance().setText(context.getString(R.string.map_kilometer, distance));
        }

        TabLayout tabLayout = viewHolder.getTabs();
        ViewPager2 viewPager2 = viewHolder.getViewPager();
        RecyclerView recyclerView = viewHolder.getRvFuelList();

        recyclerView.setAdapter(new GasStationFuelAdapter());

        viewPager2.setAdapter(recyclerView.getAdapter());

        for (String fuel: gasStation.getFuelList().keySet()) {
            TabLayout.Tab tab = tabLayout.newTab();
            tab.setText(fuel);
            tabLayout.addTab(tab);
        }

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                List<Fuel> fuelList = gasStation.getFuelList().get(Objects.requireNonNull(tab.getText()).toString());
                ((GasStationFuelAdapter) Objects.requireNonNull(viewPager2.getAdapter())).setFuelList(fuelList);

                viewPager2.getAdapter().notifyDataSetChanged();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}

        });

//        for (String fuelType: gasStation.getFuelList().keySet()) {
//            Chip chip = new Chip(context);
//            chip.setChecked(true);
//            chip.setCheckable(true);
//            chip.setText(fuelType);
//            chip.setOnCheckedChangeListener((compoundButton, isChecked) -> {
//                if (isChecked) {
//                    this.fuelType = fuelType;
//                }
//            });
//            cgFuelType.addView(chip);
//        }

//        rv.setAdapter(new GasStationFuelAdapter(gasStation.getFuelList().get(fuelType)));
//        rv.setLayoutManager(new LinearLayoutManager(context));
    }



    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return 1;
    }

}
