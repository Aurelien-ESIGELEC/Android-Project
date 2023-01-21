package com.example.android_project.views.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.android_project.views.FollowerListFragment;
import com.example.android_project.views.FriendsListFragment;
import com.example.android_project.views.pages.StatsFuelTabFragment;
import com.google.android.material.tabs.TabLayout;

import java.util.Objects;

public class StatsViewPagerAdapter extends FragmentStateAdapter {

    private String[] fuelTypeList;

    public StatsViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    public StatsViewPagerAdapter(@NonNull Fragment fragment) {
        super(fragment);
    }

    public StatsViewPagerAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    public void setFuelTypeList(String[] fuelTypeList) {
        this.fuelTypeList = fuelTypeList;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return new StatsFuelTabFragment(Objects.requireNonNull(fuelTypeList[position]));
    }

    @Override
    public int getItemCount() {
        return fuelTypeList != null ? fuelTypeList.length : 0;
    }
}
