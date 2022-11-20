package com.example.android_project.view_models;

import android.text.TextUtils;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.android_project.R;

import java.util.Objects;

public class RegisterViewModel extends ViewModel {

    private MutableLiveData<String> email;
    private MutableLiveData<String> username;
    private MutableLiveData<String> password;

    private MutableLiveData<Integer> errorEmail;
    private MutableLiveData<Integer> errorUsername;
    private MutableLiveData<Integer> errorPassword;
    private MutableLiveData<Integer> errorConfirmPassword;

    public RegisterViewModel() {
        email = new MutableLiveData<>();
        username = new MutableLiveData<>();
        password = new MutableLiveData<>();

        errorEmail = new MutableLiveData<>();
        errorPassword = new MutableLiveData<>();
        errorUsername = new MutableLiveData<>();
        errorConfirmPassword = new MutableLiveData<>();
    }

    public void setCurrentEmail(String email) {
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            this.errorEmail.setValue(R.string.register_error_not_email);
        } else {
            this.email.setValue(email);
        }

    }

    private boolean isAStrongPassword(String password) {
        return password.length() >= 8 &&
                password.matches(".*[A-Z].*") &&
                password.matches(".*[a-z].*") &&
                password.matches(".*[0-9].*") &&
                password.matches(".*[^A-Za-z0-9].*");
    }

    private boolean isAValidUsername(String username) {
        return username.matches("[^ _]");
    }

    public void setCurrentPassword(String password) {
        if (!isAStrongPassword(password)) {
            this.errorPassword.setValue(R.string.register_error_password_not_strong);
        }
        this.password.setValue(password);
    }

    public void setCurrentUsername(String username) {
        this.username.setValue(username);
    }

    public LiveData<String> getEmail() {
        return email;
    }

    public LiveData<String> getPassword() {
        return password;
    }

    public LiveData<String> getUsername() {
        return username;
    }

    public LiveData<Integer> getEmailError() {
        return errorEmail;
    }

    public LiveData<Integer> getPasswordError() {
        return errorPassword;
    }

    public LiveData<Integer> getConfirmPasswordError() {
        return errorConfirmPassword;
    }

    public LiveData<Integer> getUsernameError() {
        return errorUsername;
    }

    public void hasSamePassword(String confirmPassword) {
        if (!Objects.equals(password.getValue(), confirmPassword)) {
            this.errorConfirmPassword.setValue(R.string.register_error_password_not_equals);
        }
    }
}
