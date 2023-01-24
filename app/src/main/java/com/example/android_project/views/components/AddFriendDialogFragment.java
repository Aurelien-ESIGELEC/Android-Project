package com.example.android_project.views.components;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.android_project.R;
import com.example.android_project.data.models.fuel_price.GasStation;
import com.example.android_project.data.models.user.User;
import com.example.android_project.utils.CustomTextWatcher;
import com.example.android_project.view_models.AuthViewModel;
import com.example.android_project.view_models.MapViewModel;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddFriendDialogFragment extends DialogFragment {

    private AuthViewModel authViewModel;
    private TextInputLayout tvUsernameEmail;

    public AddFriendDialogFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        authViewModel = new ViewModelProvider(requireActivity()).get(AuthViewModel.class);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(requireContext());
        // Get the layout inflater
        LayoutInflater inflater = requireActivity().getLayoutInflater();

        View dialogView = inflater.inflate(R.layout.fragment_add_friend_dialog, null);

        tvUsernameEmail = dialogView.findViewById(R.id.friends_tf_username_or_email);

        tvUsernameEmail.getEditText().addTextChangedListener(new CustomTextWatcher() {
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                super.onTextChanged(charSequence, i, i1, i2);

                AlertDialog dialog = (AlertDialog) getDialog();
                if (!charSequence.toString().isEmpty()) {
                    dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(true);
                } else {
                    dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
                    tvUsernameEmail.setError(getString(R.string.friends_no_user));
                }
            }
            //
        });

        builder.setView(dialogView).setTitle(getString(R.string.friends_add_friend)).setPositiveButton(R.string.app_add, (dialog, id) -> {
            User user = authViewModel.getUser().getValue();
            if (user != null && tvUsernameEmail.getEditText() != null && !tvUsernameEmail.getEditText().getText().toString().isEmpty()) {
                authViewModel.addFriendToUser(String.valueOf(tvUsernameEmail.getEditText().getText())).observe(this, aBoolean -> {});
                this.dismiss();
            }
        }).setNegativeButton(R.string.app_cancel, (dialog, id) -> Objects.requireNonNull(AddFriendDialogFragment.this.getDialog()).cancel());

        return builder.create();
    }

    @Override
    public void onResume() {
        super.onResume();
        AlertDialog dialog = (AlertDialog) getDialog();
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
    }
}