package com.example.android_project.views.components.bottom_sheets;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android_project.R;
import com.example.android_project.data.models.fuel_price.GasStation;
import com.example.android_project.data.models.user.User;
import com.example.android_project.view_models.AuthViewModel;
import com.example.android_project.view_models.MapViewModel;
import com.example.android_project.views.adapters.GasStationListAdapter;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.List;

public class ListStationBottomSheetFragment extends BottomSheetDialogFragment {

    public static final String TAG = "ListStationBottomSheetFragment";

    private RecyclerView stationListRecyclerView;

    private GasStationListAdapter gasStationListAdapter;

    private MapViewModel mapViewModel;

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {
        return inflater.inflate(R.layout.fragment_list_station_bottom_sheet, container, false);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mapViewModel = new ViewModelProvider(requireActivity()).get(MapViewModel.class);

        gasStationListAdapter = new GasStationListAdapter(mapViewModel.getGasStations().getValue(), position -> {
            List<GasStation> gasStationList = mapViewModel.getGasStations().getValue();
            if ( gasStationList!= null && !gasStationList.isEmpty() && gasStationList.size() > position) {
                GasStation gasStation = gasStationList.get(position);
                if (gasStation != null) {
                    this.dismiss();
                    mapViewModel.setSelectedStation(gasStation);
                    mapViewModel.setZoomOnPoint(gasStation.getLat(), gasStation.getLon());
                }

            }

        });
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        stationListRecyclerView = requireView().findViewById(R.id.list_station_recyclerview);
        stationListRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        stationListRecyclerView.setAdapter(gasStationListAdapter);

        mapViewModel.getGasStations().observe(getViewLifecycleOwner(), gasStations -> {

            int nbItem = 0;
            for (GasStation station: gasStations) {
                if (!gasStations.contains(station)) {
                    gasStationListAdapter.addGasStation(station);
                    nbItem++;
                }
            }

            gasStationListAdapter.notifyItemRangeInserted(gasStationListAdapter.getItemCount(), nbItem);
        });

        ImageButton btnClose = requireView().findViewById(R.id.list_station_btn_close);
        btnClose.setOnClickListener(view1 -> this.dismiss());

    }

}
