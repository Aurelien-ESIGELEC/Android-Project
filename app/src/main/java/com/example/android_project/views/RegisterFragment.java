package com.example.android_project.views;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.android_project.R;
import com.example.android_project.utils.CustomTextWatcher;
import com.example.android_project.utils.Utils;
import com.example.android_project.view_models.RegisterViewModel;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 */
public class RegisterFragment extends Fragment {

    private TextInputLayout etUsername;
    private TextInputLayout etEmail;
    private TextInputLayout etPassword;
    private TextInputLayout etConfirmPassword;

    private RegisterViewModel registerViewModel;

    public RegisterFragment() {
        super(R.layout.fragment_register);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        registerViewModel = new ViewModelProvider(requireActivity()).get(RegisterViewModel.class);

        final Observer<Integer> errorEmailObserver = idError -> {
            if (idError != null) {
                Log.e("RegisterFragment", getString(idError));
                etEmail.setError(getString(idError));
            } else {
                etEmail.setError(null);
            }
        };

        final Observer<Integer> errorUsernameObserver = idError -> {
            if (idError != null) {
                Log.e("RegisterFragment", getString(idError));
                etUsername.setError(getString(idError));
            } else {
                etUsername.setError(null);
            }
        };

        final Observer<Integer> errorPasswordObserver = idError -> {
            if (idError != null) {
                Log.e("RegisterFragment", getString(idError));
                etPassword.setError(getString(idError));
            } else {
                etPassword.setError(null);
            }
        };

        final Observer<Integer> errorConfirmPasswordObserver = idError -> {
            if (idError != null) {
                Log.e("RegisterFragment", getString(idError));
                etConfirmPassword.setError(getString(idError));
            } else {
                etConfirmPassword.setError(null);
            }
        };

        registerViewModel.getEmailError().observe(this, errorEmailObserver);
        registerViewModel.getUsernameError().observe(this, errorUsernameObserver);
        registerViewModel.getPasswordError().observe(this, errorPasswordObserver);
        registerViewModel.getConfirmPasswordError().observe(this, errorConfirmPasswordObserver);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        etEmail = requireView().findViewById(R.id.register_et_email);
        etUsername = requireView().findViewById(R.id.register_et_username);
        etPassword = requireView().findViewById(R.id.register_et_password);
        etConfirmPassword = requireView().findViewById(R.id.register_et_confirm_password);

        Button btnRegister = requireView().findViewById(R.id.register_btn_login);
        btnRegister.setOnClickListener(this::onLoginClick);

        Button btnNext = requireView().findViewById(R.id.register2_btn_register);
        btnNext.setOnClickListener(this::onNextClick);

        if (registerViewModel.getEmail() != null ) {
            Objects.requireNonNull(etEmail.getEditText()).setText(registerViewModel.getEmail().getValue());
        }

        if (registerViewModel.getUsername() != null ) {
            Objects.requireNonNull(etUsername.getEditText()).setText(registerViewModel.getUsername().getValue());
        }

        if (registerViewModel.getPassword() != null ) {
            registerViewModel.setCurrentPassword("");
        }

        Objects.requireNonNull(etUsername.getEditText()).addTextChangedListener(new CustomTextWatcher() {
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                registerViewModel.setCurrentUsername(charSequence.toString());
            }
        });

        Objects.requireNonNull(etEmail.getEditText()).addTextChangedListener(new CustomTextWatcher() {
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                registerViewModel.setCurrentEmail(charSequence.toString());
            }
        });

        Objects.requireNonNull(etPassword.getEditText()).addTextChangedListener(new CustomTextWatcher() {
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                registerViewModel.setCurrentPassword(charSequence.toString());
            }
        });

        Objects.requireNonNull(etConfirmPassword.getEditText()).addTextChangedListener(new CustomTextWatcher() {
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                registerViewModel.hasSamePassword(charSequence.toString());
            }
        });
    }

    private boolean isFormCorrectlyFilled() {
        return TextUtils.isEmpty(etEmail.getError()) &&
                TextUtils.isEmpty(etUsername.getError()) &&
                TextUtils.isEmpty(etPassword.getError()) &&
                TextUtils.isEmpty(etConfirmPassword.getError());
    }

    public void onLoginClick(View view) {
        NavDirections action =
                RegisterFragmentDirections.actionRegisterFragmentToLoginFragment();

        Navigation.findNavController(view).navigate(action);
    }

    private boolean isFieldEmpty(TextInputLayout et) {
        if (TextUtils.isEmpty(Objects.requireNonNull(et.getEditText()).getText())) {
            et.setError(getString(R.string.form_error_required));
            return true;
        }
        et.setError(null);
        return false;
    }

    public void onNextClick(View view) {
        boolean isFormNotFilled = isFieldEmpty(etEmail);
        if (isFieldEmpty(etUsername)) {
            isFormNotFilled = true;
        }
        if (isFieldEmpty(etPassword)) {
            isFormNotFilled = true;
        }
        if (isFieldEmpty(etConfirmPassword)) {
            isFormNotFilled = true;
        }

        if (!Utils.isNetworkUnavailable(requireContext())) {
            Snackbar.make(
                    requireView(),
                    R.string.register_error_no_network,
                    Snackbar.LENGTH_LONG
            ).setAction(
                    R.string.app_retry_action,
                    v -> this.onNextClick(view)
            ).show();
        } else {
            if ( !isFormNotFilled && isFormCorrectlyFilled()) {
                final Observer<Boolean> errorCanBeRegistered = canBeRegistered -> {
                    if (canBeRegistered) {
                        NavDirections action =
                                RegisterFragmentDirections.actionRegisterFragmentToRegisterPart2Fragment();

                        Navigation.findNavController(view).navigate(action);
                    }
                };

                String username = String.valueOf(Objects.requireNonNull(etUsername.getEditText()).getText());
                String email = String.valueOf(Objects.requireNonNull(etEmail.getEditText()).getText());

                registerViewModel.canRegisterUser(username,email).observe(this, errorCanBeRegistered);
            }
        }


    }
}