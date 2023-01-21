package com.example.android_project.views.pages;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.example.android_project.R;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.shape.MaterialShapeDrawable;

/**
 * A simple {@link DialogFragment} subclass.
 */
public class SettingsFragment extends DialogFragment {

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
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        AppBarLayout appBarLayout = requireView().findViewById(R.id.settings_app_bar_layout);
        MaterialToolbar topAppBar = requireView().findViewById(R.id.settings_top_app_bar);

        appBarLayout.setStatusBarForeground(
                MaterialShapeDrawable.createWithElevationOverlay(requireContext()));

        topAppBar.setNavigationOnClickListener(this::onBackArrowClick);
    }
}