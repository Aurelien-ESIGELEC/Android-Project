package com.example.android_project.view_models;

import android.text.TextUtils;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.example.android_project.R;
import com.example.android_project.data.models.User;
import com.example.android_project.data.repositories.UserRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class RegisterViewModel extends ViewModel {

    private MutableLiveData<String> email;
    private MutableLiveData<String> username;
    private MutableLiveData<String> password;
    private MutableLiveData<String> fuel;
    private MutableLiveData<String> sharing;
    private MutableLiveData<String[]> notifications;

    private MutableLiveData<Integer> errorEmail;
    private MutableLiveData<Integer> errorUsername;
    private MutableLiveData<Integer> errorPassword;
    private MutableLiveData<Integer> errorConfirmPassword;

    private UserRepository userRepository;

    public RegisterViewModel() {
        email = new MutableLiveData<>();
        username = new MutableLiveData<>();
        password = new MutableLiveData<>();
        fuel = new MutableLiveData<>();
        sharing = new MutableLiveData<>();
        notifications = new MutableLiveData<>();

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
        if (!isAStrongPassword(password) && !password.isEmpty()) {
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

    public void setCurrentFuel(String fuel) {
        this.username.setValue(fuel);
    }

    public void setCurrentSharing(String sharing) {
        this.sharing.setValue(sharing);
    }

    public void addNotification(String notification) {

        if (this.notifications != null) {
            if (
                    this.notifications.getValue() != null &&
                            this.notifications.getValue().length > 0
            ) {
                boolean isAlreadyAdded = false;
                for (int i = 0; i < this.notifications.getValue().length; i++) {
                    if (this.notifications.getValue()[i].equals(notification)) {
                        isAlreadyAdded = true;
                        break;
                    }
                }

                if (!isAlreadyAdded) {
                    String[] newArray = Arrays.copyOf(this.notifications.getValue(), this.notifications.getValue().length + 1);
                    newArray[newArray.length - 1] = notification;
                    this.notifications.setValue(newArray);
                }
            } else {
                this.notifications.setValue(new String[]{notification});
            }
        }

        if (this.notifications != null && this.notifications.getValue() != null && this.notifications.getValue().length > 0) {
            Log.v("RegisterViewModel", Arrays.toString(this.notifications.getValue()));
        }

    }

    public void removeNotification(String notification) {
        if (
                this.notifications != null &&
                this.notifications.getValue() != null
        ) {
            if (this.notifications.getValue().length > 1) {

                String[] array = this.notifications.getValue();
                int index = -1;
                for (int i = 0; i < array.length; i++) {
                    if (array[i].equals(notification)) {
                        index = i;
                        break;
                    }
                }
                if (index != -1) {
                    System.arraycopy(array, index + 1, array, index, array.length - index - 1);
                    this.notifications.setValue(array);
                }
            }

            if (this.notifications.getValue().length == 1) {
                this.notifications.setValue(null);
            }
        }

        if (this.notifications != null && this.notifications.getValue() != null && this.notifications.getValue().length > 0) {
            Log.v("RegisterViewModel", Arrays.toString(this.notifications.getValue()));
        }

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

    public LiveData<User> getUser() {
        return this.userRepository.getUser();
    }

    public LiveData<Boolean> registerUser() {

        User user = new User(
                username.getValue(),
                email.getValue(),
                notifications.getValue(),
                sharing.getValue()
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

    public LiveData<Boolean> loginAnonymously() {
        return this.userRepository.anonymousLogin();
    }
}
