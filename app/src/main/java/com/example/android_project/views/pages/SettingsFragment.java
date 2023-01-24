package com.example.android_project.views.pages;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.android_project.R;
import com.example.android_project.data.models.user.User;
import com.example.android_project.view_models.AuthViewModel;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.google.android.material.shape.MaterialShapeDrawable;

/**
 * A simple {@link DialogFragment} subclass.
 */
public class SettingsFragment extends DialogFragment {

    private static final String TAG = "SettingsFragment";
    private AuthViewModel authViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogFullScreenStyle);

        authViewModel = new ViewModelProvider(requireActivity()).get(AuthViewModel.class);
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

        authViewModel.resetFuelTypesList();
        authViewModel.resetNotification();

        User user = authViewModel.getUser().getValue();

        Log.d(TAG, "onViewCreated: "+ user);

        AppBarLayout appBarLayout = requireView().findViewById(R.id.settings_app_bar_layout);
        MaterialToolbar topAppBar = requireView().findViewById(R.id.settings_top_app_bar);

        appBarLayout.setStatusBarForeground(
                MaterialShapeDrawable.createWithElevationOverlay(requireContext()));

        topAppBar.setNavigationOnClickListener(this::onBackArrowClick);

        RadioGroup rgSharing = requireView().findViewById(R.id.settings_rg_sharing);
        CheckBox cbFavoriteNotification = requireView().findViewById(R.id.settings_swc_fav_notification);
        CheckBox cbFriendNotification = requireView().findViewById(R.id.settings_swc_friend_notification);
        ChipGroup cgFuelType = requireView().findViewById(R.id.settings_cg_fuel_type);

        String [] fuelTypeArray = getResources().getStringArray(R.array.app_fuel_type);
        
        RadioButton rbPublic = requireView().findViewById(R.id.settings_rb_public);
        RadioButton rbFriends = requireView().findViewById(R.id.settings_rb_friends);
        RadioButton rbPrivate = requireView().findViewById(R.id.settings_rb_private);

        LinearProgressIndicator progressIndicator = requireView().findViewById(R.id.settings_progress_indicator);
        progressIndicator.setVisibility(View.INVISIBLE);

        MaterialButton button = requireView().findViewById(R.id.settings_btn_update);

        button.setOnClickListener(view1 -> {
            progressIndicator.setVisibility(View.VISIBLE);
            this.authViewModel.updateUserPreferences().observe(getViewLifecycleOwner(), aBoolean -> {
                progressIndicator.setVisibility(View.INVISIBLE);
            });
        });
        
        if (user != null && user.getSharing() != null) {
            switch (user.getSharing()) {
                case "all":
                    rbPublic.setChecked(true);
                    break;
                case "friends":
                    rbFriends.setChecked(true);
                    break;
                case "myself":
                    rbPrivate.setChecked(true);
            }
        }

        for (String fuelType: fuelTypeArray) {

            Chip chip = new Chip(requireContext());
            chip.setText(fuelType);
            chip.setCheckable(true);
            chip.setCheckedIconResource(R.drawable.check);
            chip.setCheckedIconVisible(true);
            chip.setOnCheckedChangeListener((compoundButton, isChecked) -> {
                if (isChecked) {
                    authViewModel.addFuelType(fuelType);
                } else {
                    authViewModel.removeFuelType(fuelType);
                }
            });
            if (user != null && user.getFavoriteFuels() != null && user.getFavoriteFuels().contains(fuelType)) {
                chip.setChecked(true);
            }
            cgFuelType.addView(chip);
        }

        LinearProgressIndicator lpiProgressRegister = requireView().findViewById(R.id.settings_progress_indicator);
        lpiProgressRegister.setVisibility(View.INVISIBLE);

        authViewModel.setCurrentSharing((String) ((RadioButton) rgSharing.findViewById(rgSharing.getCheckedRadioButtonId())).getTag());

        rgSharing.setOnCheckedChangeListener( (radioGroup, radioButtonId) -> {

            // This will get the radiobutton that has changed in its check state
            RadioButton checkedRadioButton = radioGroup.findViewById(radioButtonId);

            // This puts the value (true/false) into the variable
            boolean isChecked = checkedRadioButton.isChecked();

            // If the radiobutton that has changed in check state is now checked...
            if (isChecked) {
                authViewModel.setCurrentSharing((String) checkedRadioButton.getTag());
            }
        });

        cbFavoriteNotification.setOnCheckedChangeListener((compoundButton, isChecked ) -> {
            changeNotificationValues(isChecked, "favorites");
        });

        cbFriendNotification.setOnCheckedChangeListener((compoundButton, isChecked ) -> {
            changeNotificationValues(isChecked, "friends");
        });

        if (user != null && user.getNotifications() != null) {
            for (String notification: user.getNotifications()) {
                if (notification.equals("favorites")) {
                    cbFavoriteNotification.setChecked(true);
                }
                if (notification.equals("friends")) {
                    cbFriendNotification.setChecked(true);
                }
            }
        }
    }

    private void changeNotificationValues(Boolean isChecked, String text) {
        if (isChecked) {
            authViewModel.addNotification(text);
        } else {
            authViewModel.removeNotification(text);
        }
    }
}