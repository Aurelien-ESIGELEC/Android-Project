package com.example.android_project.views.pages;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android_project.R;
import com.example.android_project.data.models.user.User;
import com.example.android_project.view_models.AuthViewModel;
import com.example.android_project.view_models.MapViewModel;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.google.android.material.shape.MaterialShapeDrawable;

import java.util.Arrays;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class StatsFragment extends DialogFragment {

    private MapViewModel mapViewModel;
    private AuthViewModel authViewModel;

    private List<String> fuelTypeArray;

    private boolean hasCounty;

    private String county;

    private LinearProgressIndicator progressIndicator;
    private String city;
    private boolean hasCity;

    private static final int NB_UPDATE = 3;

    public StatsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_DeviceDefault_NoActionBar);

        mapViewModel = new ViewModelProvider(requireActivity()).get(MapViewModel.class);
        authViewModel = new ViewModelProvider(requireActivity()).get(AuthViewModel.class);

        hasCounty = false;
        hasCity = false;

        city = "";
        county = "";
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_stats, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        AppBarLayout appBarLayout = requireView().findViewById(R.id.stats_app_bar_layout);
        MaterialToolbar topAppBar = requireView().findViewById(R.id.stats_top_app_bar);

        appBarLayout.setStatusBarForeground(
                MaterialShapeDrawable.createWithElevationOverlay(requireContext()));

        topAppBar.setNavigationOnClickListener(this::onBackArrowClick);

        TextView tvFrance = requireView().findViewById(R.id.stats_tv_price_france);
        TextView tvCounty = requireView().findViewById(R.id.stats_tv_price_county);
        TextView tvCity = requireView().findViewById(R.id.stats_tv_price_city);

        TextView tvCountyTitle = requireView().findViewById(R.id.stats_tv_price_county_title);
        TextView tvCityTitle = requireView().findViewById(R.id.stats_tv_price_city_title);

        mapViewModel.getCountyByReverse().observe(getViewLifecycleOwner(), county -> {
            this.hasCounty = true;
            this.county = county;
            tvCountyTitle.setText(getString(R.string.stats_avg_county, county));
        });

        mapViewModel.getCityByReverse().observe(getViewLifecycleOwner(), city -> {
            this.hasCity = true;
            this.city = city;
            tvCityTitle.setText(getString(R.string.stats_avg_city, city));
        });

        progressIndicator = requireView().findViewById(R.id.stats_progress_indicator);
        progressIndicator.setVisibility(View.INVISIBLE);
        progressIndicator.setMax(99);

        User user = authViewModel.getUser().getValue();

        ChipGroup cgFuelType = requireView().findViewById(R.id.stats_chip_group);

        if (user != null) {
            fuelTypeArray = user.getFuelTypes();
        } else {
            fuelTypeArray = Arrays.asList(getResources().getStringArray(R.array.app_fuel_type));
        }

        cgFuelType.setSingleSelection(true);
        cgFuelType.setSelectionRequired(true);

        for (String fuelType : fuelTypeArray) {
            Chip chip = new Chip(requireContext());
            chip.setText(fuelType);
            chip.setCheckable(true);
            chip.setOnCheckedChangeListener((chip1, isChecked) -> {

                if (isChecked) {

                    progressIndicator.setVisibility(View.VISIBLE);
                    progressIndicator.setProgress(0);

                    mapViewModel.getAvgPriceInFrance((String) chip1.getText()).observe(getViewLifecycleOwner(), aDouble -> {
                        updateProgressIndicator(NB_UPDATE);
                        tvFrance.setText(getString(R.string.fuel_price_euro, aDouble.floatValue()));
                    });

                    if (hasCounty) {
                        mapViewModel.getAvgPriceInCounty((String) chip1.getText(), county).observe(getViewLifecycleOwner(), aDouble -> {
                            updateProgressIndicator(NB_UPDATE);
                            tvCounty.setText(getString(R.string.fuel_price_euro, aDouble.floatValue()));
                        });
                    } else {
                        updateProgressIndicator(NB_UPDATE);
                    }

                    if (hasCity) {
                        mapViewModel.getAvgPriceInCity((String) chip1.getText(), city).observe(getViewLifecycleOwner(), aDouble -> {
                            updateProgressIndicator(NB_UPDATE);
                            tvCity.setText(getString(R.string.fuel_price_euro, aDouble.floatValue()));
                        });
                    } else {
                        updateProgressIndicator(NB_UPDATE);
                    }
                }


            });
            cgFuelType.addView(chip);
        }

        if (fuelTypeArray.isEmpty() || cgFuelType.getCheckedChipId() == View.NO_ID) {
            tvFrance.setText(getString(R.string.stats_no_chosen));
            tvCounty.setText(getString(R.string.stats_no_chosen));
            tvCity.setText(getString(R.string.stats_no_chosen));
        } else {
            progressIndicator.setVisibility(View.VISIBLE);
            progressIndicator.setProgress(0);

            mapViewModel.getAvgPriceInFrance(fuelTypeArray.get(cgFuelType.getCheckedChipId())).observe(getViewLifecycleOwner(), aDouble -> {
                tvFrance.setText(getString(R.string.fuel_price_euro, aDouble.floatValue()));
            });


        }

    }

    private void updateProgressIndicator(int nbOfTotalUpdate) {
        Handler handler = new Handler();
        handler.postDelayed(() -> {
            progressIndicator.setProgress(
                    progressIndicator.getProgress() + progressIndicator.getMax() / nbOfTotalUpdate
            );

            handler.postDelayed(() -> {
                if (progressIndicator.getProgress() == progressIndicator.getMax()) {
                    progressIndicator.setVisibility(View.INVISIBLE);
                }
            }, 250);

        }, 250);
    }

    private void initFuelPrice(TextView textView) {
        textView.setText(getString(R.string.fuel_price_euro, 0f));
    }

    private void onBackArrowClick(View view) {
        if (this.isVisible()) {
            this.dismiss();
        }
    }
}