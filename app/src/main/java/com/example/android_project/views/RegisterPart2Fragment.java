package com.example.android_project.views;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;

import com.example.android_project.R;
import com.example.android_project.utils.CustomTextWatcher;
import com.example.android_project.utils.Utils;
import com.example.android_project.view_models.AuthViewModel;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.materialswitch.MaterialSwitch;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 */
public class RegisterPart2Fragment extends Fragment {

    private static final String TAG = "RegisterPart2Fragment";
    private AuthViewModel authViewModel;

    private CheckBox                cbFriendNotification;
    private CheckBox                cbFavoriteNotification;
    private RadioGroup              rgSharing;
//    private TextInputLayout         tilFuelType;

    private ChipGroup               cgFuelType;
    private LinearProgressIndicator lpiProgressRegister;


    public RegisterPart2Fragment() {
        super(R.layout.fragment_register_part2);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        authViewModel = new ViewModelProvider(requireActivity()).get(AuthViewModel.class);

        Log.v("RegisterPart2Fragment", authViewModel.getEmail().getValue());
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rgSharing = requireView().findViewById(R.id.register2_rg_sharing);
        cbFavoriteNotification = requireView().findViewById(R.id.register2_swc_fav_notification);
        cbFriendNotification = requireView().findViewById(R.id.register2_swc_friend_notification);
        cgFuelType = requireView().findViewById(R.id.register2_cg_fuel_type);

        String [] fuelTypeArray = getResources().getStringArray(R.array.register2_fuel_type);

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
            cgFuelType.addView(chip);
        }

        lpiProgressRegister = requireView().findViewById(R.id.register2_progress_indicator);
        lpiProgressRegister.setVisibility(View.INVISIBLE);

        Button btnRegister = requireView().findViewById(R.id.register_btn_register);
        btnRegister.setOnClickListener(this::onRegisterClick);

        rgSharing.setOnCheckedChangeListener( (radioGroup, radioButtonId) -> {
            // This will get the radiobutton that has changed in its check state
            RadioButton checkedRadioButton = radioGroup.findViewById(radioButtonId);
            // This puts the value (true/false) into the variable
            boolean isChecked = checkedRadioButton.isChecked();
            // If the radiobutton that has changed in check state is now checked...
            if (isChecked)
            {
                // Changes the textview's text to "Checked: example radiobutton text"
                authViewModel.setCurrentSharing((String) checkedRadioButton.getText());
            }
        });

        cbFavoriteNotification.setOnCheckedChangeListener((compoundButton, isChecked ) -> {
            changeNotificationValues(isChecked, "favorites");
        });

        cbFriendNotification.setOnCheckedChangeListener((compoundButton, isChecked ) -> {
            changeNotificationValues(isChecked, "friends");
        });

    }

    private void changeNotificationValues(Boolean isChecked, String text) {
        if (isChecked) {
            authViewModel.addNotification(text);
        } else {
            authViewModel.removeNotification(text);
        }
    }

    public void onRegisterClick(View view) {
        if (Utils.isNetworkUnavailable(requireContext())) {
            Utils.createSnackbarNoNetwork(
                    requireView(),
                    v -> this.onRegisterClick(view)
            ).show();
        } else {
            lpiProgressRegister.setVisibility(View.VISIBLE);
            final Observer<Boolean> errorIsRegistered = canBeRegistered -> {
                if (canBeRegistered) {
                    NavHostFragment.findNavController(this).navigate(R.id.mapOsmFragment);
                }
            };

            authViewModel.registerUser().observe(this, errorIsRegistered);
        }
    }
}