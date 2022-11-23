package com.example.android_project.view_models;

import android.text.TextUtils;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.example.android_project.R;
import com.example.android_project.data.repositories.UserRepository;

import java.util.ArrayList;
import java.util.Objects;

public class RegisterViewModel extends ViewModel {

    private MutableLiveData<String> email;
    private MutableLiveData<String> username;
    private MutableLiveData<String> password;

    private MutableLiveData<Integer> errorEmail;
    private MutableLiveData<Integer> errorUsername;
    private MutableLiveData<Integer> errorPassword;
    private MutableLiveData<Integer> errorConfirmPassword;

    private UserRepository userRepository;

    public RegisterViewModel() {
        email = new MutableLiveData<>();
        username = new MutableLiveData<>();
        password = new MutableLiveData<>();

        errorEmail = new MutableLiveData<>();
        errorPassword = new MutableLiveData<>();
        errorUsername = new MutableLiveData<>();
        errorConfirmPassword = new MutableLiveData<>();
        userRepository = new UserRepository();
    }

    public void setCurrentEmail(String email) {
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            this.errorEmail.setValue(R.string.register_error_not_email);
        } else {
            this.errorEmail.setValue(null);
        }
        this.email.setValue(email);
    }

    public void setCurrentPassword(String password) {
        if (!isAStrongPassword(password)) {
            this.errorPassword.setValue(R.string.register_error_password_not_strong);
        } else {
            this.errorPassword.setValue(null);
        }
        this.password.setValue(password);
    }

    public void setCurrentUsername(String username) {
        if (!isAValidUsername(username)) {
            this.errorUsername.setValue(R.string.register_error_username_not_valid);
        } else {
            this.errorUsername.setValue(null);
        }
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

    private boolean isAStrongPassword(String password) {
        return password.length() >= 8 /*&&
                password.matches(".*[A-Z].*") &&
                password.matches(".*[a-z].*") &&
                password.matches(".*[0-9].*") &&
                password.matches(".*[^A-Za-z0-9].*")*/;
    }

    private boolean isAValidUsername(String username) {
        return username.matches(".*[A-Za-z0-9].*");
    }

    public LiveData<Boolean> isUsernameAlreadyInUse(String username) {
        return this.userRepository.isUsernameAlreadyInUse(username);
    }

    public LiveData<Boolean> isEmailAlreadyInUse(String email) {
        return this.userRepository.isEmailAlreadyInUse(email);
    }

    public LiveData<Boolean> canRegisterUser(String username, String email) {
        MediatorLiveData<Boolean> canBeRegistered = new MediatorLiveData<>();

        LiveData<Boolean> isUsernameAlreadyInUse = isUsernameAlreadyInUse(username);
        LiveData<Boolean> isEmailAlreadyInUse = isEmailAlreadyInUse(email);

        canBeRegistered.addSource(isUsernameAlreadyInUse, isAlreadyUsed -> {
            canBeRegistered.setValue(
                    Boolean.FALSE.equals(isUsernameAlreadyInUse.getValue()) &&
                            Boolean.FALSE.equals(isEmailAlreadyInUse.getValue())
            );
            if (isAlreadyUsed) {
                this.errorUsername.setValue(R.string.register_error_username_already_in_use);
            }
        });

        canBeRegistered.addSource(isEmailAlreadyInUse, isAlreadyUsed -> {
            canBeRegistered.setValue(
                    Boolean.FALSE.equals(isUsernameAlreadyInUse.getValue()) &&
                            Boolean.FALSE.equals(isEmailAlreadyInUse.getValue())
            );
            if (isAlreadyUsed) {
                this.errorEmail.setValue(R.string.register_error_email_already_in_use);
            }

        });

        return canBeRegistered;
    }

    public void hasSamePassword(String confirmPassword) {
        if (!Objects.equals(password.getValue(), confirmPassword)) {
            this.errorConfirmPassword.setValue(R.string.register_error_password_not_equals);
        } else {
            this.errorConfirmPassword.setValue(null);
        }
    }
}
