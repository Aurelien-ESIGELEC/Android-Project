package com.example.android_project.views;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.example.android_project.R;
import com.example.android_project.views.adapters.FriendsViewPagerAdapter;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

/**
 * A simple {@link DialogFragment} subclass.
 */
public class MyFriendsFragment extends DialogFragment {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_DeviceDefault_NoActionBar);
    }

    private void onBackArrowClick(View view) {
        if (this.isVisible()){
            this.dismiss();
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_my_friends, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        MaterialToolbar topAppBar = requireView().findViewById(R.id.topAppBar);

        topAppBar.setNavigationOnClickListener(this::onBackArrowClick);

        TabLayout tabLayout = requireView().findViewById(R.id.friends_tab_layout);
        ViewPager2 viewPager2 = requireView().findViewById(R.id.friends_view_pager);

        viewPager2.setAdapter(new FriendsViewPagerAdapter(requireActivity().getSupportFragmentManager(),getLifecycle()));

        new TabLayoutMediator(tabLayout,viewPager2, (tab, position) -> {
            switch (position) {
                case 1:
                    tab.setText(getString(R.string.friends_follower_title));
                    break;
                case 0:
                default:
                    tab.setText(getString(R.string.friends_my_friends_title));
            }
        }).attach();

    }
}