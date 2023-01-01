package com.example.android_project.views;

import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavDirections;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.android_project.R;
import com.example.android_project.utils.CustomTextWatcher;
import com.example.android_project.utils.Utils;
import com.example.android_project.view_models.AuthViewModel;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

public class LoginFragment extends Fragment {

    private TextInputLayout etEmail;
    private TextInputLayout etPassword;
    private LinearProgressIndicator lpiProgressRegister;

    private AuthViewModel authViewModel;

    public LoginFragment() {
        super(R.layout.fragment_login);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        authViewModel = new ViewModelProvider(requireActivity()).get(AuthViewModel.class);

        authViewModel.resetError();

        final Observer<Integer> errorPasswordObserver = idError -> {
            if (idError != null) {
                Log.e("RegisterFragment", getString(idError));
                etPassword.setError(getString(idError));
            } else {
                etPassword.setError(null);
            }
        };

        final Observer<Integer> errorEmailObserver = idError -> {
            if (idError != null) {
                Log.e("RegisterFragment", getString(idError));
                etEmail.setError(getString(idError));
            } else {
                etEmail.setError(null);
            }
        };

        authViewModel.getPasswordError().observe(this, errorPasswordObserver);
        authViewModel.getEmailError().observe(this, errorEmailObserver);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Button btnRegister = requireView().findViewById(R.id.login_btn_register);
        btnRegister.setOnClickListener(this::onRegisterClick);

        Button btnLogin = requireView().findViewById(R.id.login_btn_login);
        btnLogin.setOnClickListener(this::onLoginClick);

        lpiProgressRegister = requireView().findViewById(R.id.login_progress_indicator);
        lpiProgressRegister.setVisibility(View.INVISIBLE);

        etEmail = requireView().findViewById(R.id.login_et_email);
        etPassword = requireView().findViewById(R.id.login_et_password);

        Objects.requireNonNull(etEmail.getEditText()).addTextChangedListener(new CustomTextWatcher() {
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                authViewModel.setCurrentEmail(charSequence.toString());
            }
        });

        Objects.requireNonNull(etPassword.getEditText()).addTextChangedListener(new CustomTextWatcher() {
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                authViewModel.resetPasswordError();
            }
        });

        if (authViewModel.getEmail() != null) {
            Objects.requireNonNull(etEmail.getEditText()).setText(authViewModel.getEmail().getValue());
        }
    }

    public void onLoginClick(View view) {
        if (Utils.isNetworkUnavailable(requireContext())) {
            Utils.createSnackbarNoNetwork(requireView(), v -> this.onLoginClick(view)).show();
        } else {
            lpiProgressRegister.setVisibility(View.VISIBLE);
            final Observer<Boolean> errorIsLogged = canBeRegistered -> {
                if (canBeRegistered) {
                    NavHostFragment.findNavController(this).navigate(R.id.mapOsmFragment);
                } else {
                    lpiProgressRegister.setVisibility(View.INVISIBLE);
                }
            };

            authViewModel.loginUser().observe(this, errorIsLogged);
        }
    }

    public void onRegisterClick(View view) {
        NavDirections action = LoginFragmentDirections.actionLoginFragmentToRegisterFragment();

        Navigation.findNavController(view).navigate(action);
    }
}