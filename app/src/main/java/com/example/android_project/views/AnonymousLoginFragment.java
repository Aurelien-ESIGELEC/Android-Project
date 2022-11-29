package com.example.android_project.views;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.android_project.R;
import com.example.android_project.utils.Utils;
import com.example.android_project.view_models.AuthViewModel;

/**
 * A simple {@link Fragment} subclass.
 */
public class AnonymousLoginFragment extends Fragment {

    private AuthViewModel authViewModel;

    public AnonymousLoginFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        authViewModel = new ViewModelProvider(requireActivity()).get(AuthViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_anonymous_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button btnAnonymous = requireView().findViewById(R.id.register_btn_anonymous);
        btnAnonymous.setOnClickListener(this::onAnonymousLoginClick);
    }

    public void onAnonymousLoginClick(View view) {
        if (Utils.isNetworkUnavailable(requireContext())) {
            Utils.createSnackbarNoNetwork(
                    requireView(),
                    v -> this.onAnonymousLoginClick(view)
            ).show();
        } else {
            final Observer<Boolean> observerLoginAnonymous = isLogged -> {
                if (isLogged) {
//                    Log.v("RegisterFragment", )
                    Navigation.findNavController(view).navigate(R.id.mapOsmFragment);
                }
            };

            authViewModel.loginAnonymously().observe(this, observerLoginAnonymous);
        }

    }
}