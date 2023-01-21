package com.example.android_project.views.pages;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android_project.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class StatsFuelTabFragment extends Fragment {

    private String fuel;

    public StatsFuelTabFragment() {
        // Required empty public constructor
    }

    public StatsFuelTabFragment(String fuel) {
        this.fuel = fuel;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_stats_fuel_tab, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView tvFrance = requireView().findViewById(R.id.stats_tv_price_france);

//        tvFrance.setText(getString(R.string.stats_avg_france, ));
    }
}