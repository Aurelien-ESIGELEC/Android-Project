package com.example.android_project.views.components;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.example.android_project.R;
import com.example.android_project.data.models.fuel_price.GasStation;
import com.example.android_project.data.models.user.User;
import com.example.android_project.view_models.AuthViewModel;
import com.example.android_project.view_models.MapViewModel;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddFuelDialogFragment extends DialogFragment {

    private MapViewModel mapViewModel;
    private AuthViewModel authViewModel;

    private GasStation gasStation;
    private String fuelType;

    private TextInputLayout tvPrice;

    public AddFuelDialogFragment(GasStation gasStation, String fuelType) {
        this.gasStation = gasStation;
        this.fuelType = fuelType;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mapViewModel = new ViewModelProvider(requireActivity()).get(MapViewModel.class);
        authViewModel = new ViewModelProvider(requireActivity()).get(AuthViewModel.class);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(requireContext());
        // Get the layout inflater
        LayoutInflater inflater = requireActivity().getLayoutInflater();

        View dialogView = inflater.inflate(R.layout.fragment_add_fuel_dialog, null);

        tvPrice = dialogView.findViewById(R.id.station_tf_add_price);

        builder.setView(dialogView)
                .setTitle(getString(R.string.fuel_add_text))
                .setPositiveButton(R.string.app_add, (dialog, id) -> {
                    User user = authViewModel.getUser().getValue();
                    if (user != null) {
                        mapViewModel.addPrice(
                                user.getUsername(),
                                fuelType,
                                Double.parseDouble(String.valueOf(tvPrice.getEditText().getText())),
                                gasStation.getId()
                        ).observe(this, fuel -> mapViewModel.setSelectedStation(gasStation));
                        this.dismiss();
                    }
                })
                .setNegativeButton(R.string.app_cancel, (dialog, id) ->
                        Objects.requireNonNull(AddFuelDialogFragment.this.getDialog()).cancel()
                );

        return builder.create();
    }

}