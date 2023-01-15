package com.example.android_project.views.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.android_project.views.FollowerListFragment;
import com.example.android_project.views.FriendsListFragment;

public class GasStationPriceListAdapter extends FragmentStateAdapter {

    private static final int NUM_TABS = 2;

    public GasStationPriceListAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    public GasStationPriceListAdapter(@NonNull Fragment fragment) {
        super(fragment);
    }

    public GasStationPriceListAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 1:
                return new FollowerListFragment();
            case 0:
            default:
                return new FriendsListFragment();
        }
    }

    @Override
    public int getItemCount() {
        return NUM_TABS;
    }
}
