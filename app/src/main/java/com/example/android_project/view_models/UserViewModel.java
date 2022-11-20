package com.example.android_project.view_models;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.android_project.data.repositories.UserRepository;

public class UserViewModel extends ViewModel {

    private UserRepository userRepository;

    public UserViewModel() {
        userRepository = new UserRepository();
    }

    public LiveData<Boolean> isLogged() {
        return userRepository.isLogged();
    }

//    public String getUserUsername() {
//        return
//    }
}
