package com.example.android_project.views;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
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
import com.example.android_project.view_models.RegisterViewModel;
import com.google.android.material.textfield.TextInputLayout;

import java.io.Serializable;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 */
public class RegisterPart2Fragment extends Fragment {

    private RegisterViewModel registerViewModel;

    private RadioGroup rgSharing;
    private CheckBox cbFriendNotification;
    private CheckBox cbFavoriteNotification;
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
            if (isChecked) {
                registerViewModel.setCurrentNotifications((String) compoundButton.getText());
            }
        });

//        Objects.requireNonNull(cbFavoriteNotification.getEditText()).addTextChangedListener(new CustomTextWatcher() {
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                registerViewModel.setCurrentEmail(charSequence.toString());
//            }
//        });
//
//        Objects.requireNonNull(cbFriendNotification.getEditText()).addTextChangedListener(new CustomTextWatcher() {
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                registerViewModel.setCurrentPassword(charSequence.toString());
//            }
//        });

        Objects.requireNonNull(tilFuelType.getEditText()).addTextChangedListener(new CustomTextWatcher() {
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                registerViewModel.hasSamePassword(charSequence.toString());
            }
        });
    }
}