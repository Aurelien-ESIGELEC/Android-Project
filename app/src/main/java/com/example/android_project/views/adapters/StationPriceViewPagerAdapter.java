package com.example.android_project.views.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.android_project.data.models.fuel_price.Fuel;
import com.example.android_project.data.models.fuel_price.GasStation;
import com.example.android_project.views.pages.FollowerListFragment;
import com.example.android_project.views.pages.FriendsListFragment;
import com.example.android_project.views.pages.StationPriceListFragment;

import java.util.List;

public class StationPriceViewPagerAdapter extends FragmentStateAdapter {

    private List<String> fuelTypes;



    public StationPriceViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    public StationPriceViewPagerAdapter(@NonNull Fragment fragment) {
        super(fragment);
    }

    public StationPriceViewPagerAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    public StationPriceViewPagerAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle, List<String> fuelTypes) {
        this(fragmentManager, lifecycle);
        this.fuelTypes = fuelTypes;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (fuelTypes != null && !fuelTypes.isEmpty()) {
            return new StationPriceListFragment(fuelTypes.get(position));
        }

        return new StationPriceListFragment();
    }

    @Override
    public int getItemCount() {
        return fuelTypes != null ? fuelTypes.size() : 0;
    }
}
