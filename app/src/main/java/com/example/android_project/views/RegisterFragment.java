package com.example.android_project.views;

import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.android_project.R;
import com.example.android_project.utils.CustomTextWatcher;
import com.example.android_project.view_models.RegisterViewModel;

/**
 * A simple {@link Fragment} subclass.
 */
public class RegisterFragment extends Fragment {

    private EditText etUsername;
    private EditText etEmail;
    private EditText etPassword;
    private EditText etConfirmPassword;

    private RegisterViewModel registerViewModel;

    public RegisterFragment() {
        super(R.layout.fragment_register);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        registerViewModel = new ViewModelProvider(this).get(RegisterViewModel.class);

        final Observer<Integer> errorEmailObserver = idError -> {
            Log.e("RegisterFragment", getString(idError));
            etEmail.setError(getString(idError));
        };

        final Observer<Integer> errorUsernameObserver = idError -> {
            Log.e("RegisterFragment", getString(idError));
            etUsername.setError(getString(idError));
        };

        final Observer<Integer> errorPasswordObserver = idError -> {
            Log.e("RegisterFragment", getString(idError));
            etPassword.setError(getString(idError));
        };

        final Observer<Integer> errorConfirmPasswordObserver = idError -> {
            Log.e("RegisterFragment", getString(idError));
            etConfirmPassword.setError(getString(idError));
        };

        registerViewModel.getEmailError().observe(this, errorEmailObserver);
        registerViewModel.getUsernameError().observe(this, errorUsernameObserver);
        registerViewModel.getPasswordError().observe(this, errorPasswordObserver);
        registerViewModel.getConfirmPasswordError().observe(this, errorConfirmPasswordObserver);

        // This callback will only be called when MyFragment is at least Started.
        OnBackPressedCallback callback = new OnBackPressedCallback(false) {
            @Override
            public void handleOnBackPressed() {

            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);
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

        Button btnNext = requireView().findViewById(R.id.register_btn_next);
        btnNext.setOnClickListener(this::onNextClick);

        etEmail.addTextChangedListener(new CustomTextWatcher() {
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                registerViewModel.setCurrentEmail(charSequence.toString());
            }
        });

        etPassword.addTextChangedListener(new CustomTextWatcher() {
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                registerViewModel.setCurrentPassword(charSequence.toString());
            }
        });

        etConfirmPassword.addTextChangedListener(new CustomTextWatcher() {
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

    private boolean isFormEmpty() {
        return TextUtils.isEmpty(etEmail.getText()) &&
                TextUtils.isEmpty(etUsername.getText()) &&
                TextUtils.isEmpty(etPassword.getText()) &&
                TextUtils.isEmpty(etConfirmPassword.getText());
    }

    public void onLoginClick(View view) {
        NavDirections action =
                RegisterFragmentDirections.actionRegisterFragmentToLoginFragment();

        Navigation.findNavController(view).navigate(action);
    }

    public void onNextClick(View view) {
        if (!isFormEmpty() && isFormCorrectlyFilled()) {
            NavDirections action =
                    RegisterFragmentDirections.actionRegisterFragmentToRegisterPart2Fragment();

            Navigation.findNavController(view).navigate(action);
        }
    }
}