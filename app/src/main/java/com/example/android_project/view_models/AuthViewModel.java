package com.example.android_project.view_models;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.android_project.R;
import com.example.android_project.data.models.user.User;
import com.example.android_project.data.repositories.UserRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class AuthViewModel extends ViewModel {

    private static final String TAG = "AuthViewModel";

    private MutableLiveData<String> email;
    private MutableLiveData<String> username;
    private MutableLiveData<String> password;
    private MutableLiveData<String> sharing;
    private MutableLiveData<List<String>> fuelTypes;
    private MutableLiveData<List<String>> notifications;

    private MutableLiveData<Integer> errorEmail;
    private MutableLiveData<Integer> errorUsername;
    private MutableLiveData<Integer> errorPassword;
    private MutableLiveData<Integer> errorConfirmPassword;

    private UserRepository userRepository;

    public AuthViewModel() {
        email = new MutableLiveData<>();
        username = new MutableLiveData<>();
        password = new MutableLiveData<>();
        sharing = new MutableLiveData<>();
        fuelTypes = new MutableLiveData<>(new ArrayList<>());
        notifications = new MutableLiveData<>(new ArrayList<>());

        errorEmail = new MutableLiveData<>();
        errorPassword = new MutableLiveData<>();
        errorUsername = new MutableLiveData<>();
        errorConfirmPassword = new MutableLiveData<>();

        userRepository = new UserRepository();

        Log.d(TAG, "AuthViewModel: " + userRepository.hasExistingUserInCache());
        if (userRepository.hasExistingUserInCache()) {
            this.userRepository.checkIfLoggedInCache();
            this.userRepository.retrieveUser();
        }
    }

    public void resetError() {
        errorPassword.setValue(null);
        errorEmail.setValue(null);
        errorUsername.setValue(null);
        errorConfirmPassword.setValue(null);
    }

    public void resetPasswordError() {
        errorPassword.setValue(null);
    }

    public void resetPassword() {
        password.setValue(null);
    }

    public void setCurrentEmail(String email) {
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            this.errorEmail.setValue(R.string.register_error_not_email);
        } else {
            this.errorEmail.setValue(null);
        }
        this.email.setValue(email);
    }

    public void setCurrentRegisterPassword(String password) {
        if (!isAStrongPassword(password) && !password.isEmpty()) {
            this.errorPassword.setValue(R.string.register_error_password_not_strong);
        } else {
            this.errorPassword.setValue(null);
        }
        this.password.setValue(password);
    }

    public void setCurrentPassword(String password) {
        this.password.setValue(password);
    }

    public void setCurrentRegisterUsername(String username) {
        if (!isAValidUsername(username)) {
            this.errorUsername.setValue(R.string.register_error_username_not_valid);
        } else {
            this.errorUsername.setValue(null);
        }
        this.username.setValue(username);
    }

    public void setCurrentFuel(String fuel) {
        this.username.setValue(fuel);
    }

    public void setCurrentSharing(String sharing) {
        this.sharing.setValue(sharing);
    }

    public void addNotification(String notification) {
        List<String> currentNotification = this.notifications.getValue();
        currentNotification.add(notification);
        this.notifications.setValue(currentNotification);
        Log.d(TAG, "addNotification: " + this.notifications.getValue());
    }

    public void removeNotification(String notification) {
        List<String> currentNotification = this.notifications.getValue();
        currentNotification.removeAll(Collections.singleton(notification));
        this.notifications.setValue(currentNotification);
        Log.d(TAG, "removeNotification: " + this.notifications.getValue());
    }

    public void addFuelType(String fuelType) {
        List<String> currentFuelType = this.fuelTypes.getValue();
        currentFuelType.add(fuelType);
        this.fuelTypes.setValue(currentFuelType);
        Log.d(TAG, "addFuelType: " + this.fuelTypes.getValue());
    }

    public void removeFuelType(String fuelType) {
        List<String> currentFuelType = this.fuelTypes.getValue();
        currentFuelType.removeAll(Collections.singleton(fuelType));
        this.fuelTypes.setValue(currentFuelType);
        Log.d(TAG, "removeFuelType: " + this.fuelTypes.getValue());
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

    public LiveData<Boolean> isLogged() {
        return this.userRepository.isLogged();
    }

    public LiveData<Boolean> loginUser() {
        MediatorLiveData<Boolean> isLogged = new MediatorLiveData<>();

        Log.d(TAG, "loginUser: " + email.getValue() + ", " + password.getValue());

        MutableLiveData<Boolean> isEmailAlreadyInUse = this.userRepository.isEmailAlreadyInUse(email.getValue());

        isLogged.addSource(isEmailAlreadyInUse, isAlreadyUsed -> {
            if (!isAlreadyUsed) {
                this.errorEmail.setValue(R.string.login_error_email_no_account);
                isLogged.setValue(false);
            } else {
                this.errorEmail.setValue(null);
                isLogged.addSource(
                        this.userRepository.login(email.getValue(), password.getValue()), logged -> {
                            if (logged != null) {
                                if (logged) {
                                    isLogged.setValue(true);
                                    this.errorPassword.setValue(null);
                                } else {
                                    isLogged.setValue(false);
                                    this.errorPassword.setValue(R.string.login_error_wrong_password);
                                }
                            }
                        });
            }
        });

        return isLogged;
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
            } else {
                this.errorUsername.setValue(null);
            }
        });

        canBeRegistered.addSource(isEmailAlreadyInUse, isAlreadyUsed -> {
            canBeRegistered.setValue(
                    Boolean.FALSE.equals(isUsernameAlreadyInUse.getValue()) &&
                            Boolean.FALSE.equals(isEmailAlreadyInUse.getValue())
            );
            if (isAlreadyUsed) {
                this.errorEmail.setValue(R.string.register_error_email_already_in_use);
            } else {
                this.errorEmail.setValue(null);
            }

        });

        return canBeRegistered;
    }

    public LiveData<User> getUser() {
        return this.userRepository.getUser();
    }

    public LiveData<Boolean> registerUser() {

        User user = new User(
                username.getValue(),
                email.getValue(),
                notifications.getValue(),
                sharing.getValue(),
                fuelTypes.getValue()
        );

        return this.userRepository.register(user, password.getValue());
    }

    public void hasSamePassword(String confirmPassword) {
        if (!Objects.equals(password.getValue(), confirmPassword)) {
            this.errorConfirmPassword.setValue(R.string.register_error_password_not_equals);
        } else {
            this.errorConfirmPassword.setValue(null);
        }
    }

    public void logout() {
        this.userRepository.logOut();
    }

    public LiveData<Boolean> loginAnonymously() {
        return this.userRepository.anonymousLogin();
    }

    public void resetLoggedState() {
        this.userRepository.isLogged().setValue(null);
    }
}
