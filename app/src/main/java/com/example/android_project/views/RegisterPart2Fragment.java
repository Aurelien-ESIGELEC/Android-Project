package com.example.android_project.views;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.android_project.R;
import com.example.android_project.data.models.User;
import com.example.android_project.utils.CustomTextWatcher;
import com.example.android_project.utils.Utils;
import com.example.android_project.view_models.RegisterViewModel;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;

import java.io.Serializable;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 */
public class RegisterPart2Fragment extends Fragment {

    private RegisterViewModel registerViewModel;

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

        registerViewModel = new ViewModelProvider(requireActivity()).get(RegisterViewModel.class);

        Log.v("RegisterPart2Fragment", registerViewModel.getEmail().getValue());
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rgSharing = requireView().findViewById(R.id.register2_rg_sharing);
        cbFavoriteNotification = requireView().findViewById(R.id.register2_cb_favorite_station_notification);
        cbFriendNotification = requireView().findViewById(R.id.register2_cb_friends_notification);
        tilFuelType = requireView().findViewById(R.id.register2_til_fuel_type);

        Button btnRegister = requireView().findViewById(R.id.register2_btn_register);
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
                registerViewModel.setCurrentSharing((String) checkedRadioButton.getText());
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
                registerViewModel.hasSamePassword(charSequence.toString());
            }
        });
    }

    private void changeNotificationValues(Boolean isChecked, String text) {
        if (isChecked) {
            registerViewModel.addNotification(text);
        } else {
            registerViewModel.removeNotification(text);
        }
    }

    public void onRegisterClick(View view) {
        if (!Utils.isNetworkUnavailable(requireContext())) {
            Snackbar.make(
                    requireView(),
                    R.string.register_error_no_network,
                    Snackbar.LENGTH_LONG
            ).setAction(
                    R.string.app_retry_action,
                    v -> this.onRegisterClick(view)
            ).show();
        } else {
//            final Observer<Boolean> errorIsRegistered = canBeRegistered -> {
//                if (canBeRegistered) {
//                    NavDirections action =
//                            RegisterFragmentDirections.actionRegisterFragmentToRegisterPart2Fragment();
//
//                    Navigation.findNavController(view).navigate(action);
//                }
//            };
//
//            registerViewModel.hasRegistedUser().observe(this, errorIsRegistered);
        }
    }
}