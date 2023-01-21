package com.example.android_project.views.pages;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android_project.R;
import com.example.android_project.views.adapters.FriendsViewPagerAdapter;
import com.example.android_project.views.adapters.StatsViewPagerAdapter;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.shape.MaterialShapeDrawable;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

/**
 * A simple {@link Fragment} subclass.
 */
public class StatsFragment extends DialogFragment {

    public StatsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_DeviceDefault_NoActionBar);
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

        TabLayout tabLayout = requireView().findViewById(R.id.stats_tab_layout);
        ViewPager2 viewPager2 = requireView().findViewById(R.id.stats_view_pager);

        StatsViewPagerAdapter statsViewPagerAdapter = new StatsViewPagerAdapter(requireActivity().getSupportFragmentManager(),getLifecycle());

        String[] fuelList = getResources().getStringArray(R.array.app_fuel_type);

        statsViewPagerAdapter.setFuelTypeList(fuelList);
        viewPager2.setAdapter(statsViewPagerAdapter);

        new TabLayoutMediator(tabLayout,viewPager2, (tab, position) -> tab.setText(fuelList[position])).attach();
    }

    private void onBackArrowClick(View view) {
        if (this.isVisible()){
            this.dismiss();
        }
    }
}