package com.example.android_project.views;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android_project.R;
import com.example.android_project.data.models.User;
import com.example.android_project.view_models.UserViewModel;

/**
 * A simple {@link Fragment} subclass.
 */
public class MapOsmFragment extends Fragment {

    private UserViewModel userViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_map_osm, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        userViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);
        NavController navController = Navigation.findNavController(view);
        userViewModel.isLogged().observe(getViewLifecycleOwner(), (Observer<Boolean>) isLogged -> {
            if (isLogged) {
                Log.d("MapOsmFragment","User logged");
            } else {
                navController.navigate(R.id.authNavigation);
            }
        });
    }

}