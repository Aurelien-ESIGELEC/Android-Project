package com.example.android_project.views;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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

import com.example.android_project.R;
import com.example.android_project.utils.CustomTextWatcher;
import com.example.android_project.utils.Utils;
import com.example.android_project.view_models.AuthViewModel;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 */
public class RegisterPart2Fragment extends Fragment {

    private AuthViewModel authViewModel;

    private CheckBox        cbFriendNotification;
    private CheckBox        cbFavoriteNotification;
    private RadioGroup      rgSharing;
    private TextInputLayout tilFuelType;


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
        cbFavoriteNotification = requireView().findViewById(R.id.register2_cb_favorite_station_notification);
        cbFriendNotification = requireView().findViewById(R.id.register2_cb_friends_notification);
        tilFuelType = requireView().findViewById(R.id.register2_til_fuel_type);

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
            changeNotificationValues(isChecked, "favorite stations");
        });

        cbFriendNotification.setOnCheckedChangeListener((compoundButton, isChecked ) -> {
            changeNotificationValues(isChecked, "friends");
        });

        Objects.requireNonNull(tilFuelType.getEditText()).addTextChangedListener(new CustomTextWatcher() {
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                authViewModel.hasSamePassword(charSequence.toString());
            }
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
            final Observer<Boolean> errorIsRegistered = canBeRegistered -> {
                if (canBeRegistered) {
                    NavHostFragment.findNavController(this).navigate(R.id.loginFragment);
                }
            };

            authViewModel.registerUser().observe(this, errorIsRegistered);
        }
    }
}