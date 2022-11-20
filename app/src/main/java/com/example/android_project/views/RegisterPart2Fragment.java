package com.example.android_project.views;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.android_project.R;
import com.example.android_project.data.models.User;

import java.io.Serializable;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RegisterPart2Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RegisterPart2Fragment extends Fragment {

    private static final String ARG_EMAIL = "email";

    private String mEmail;

    public RegisterPart2Fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment RegisterPart2Fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RegisterPart2Fragment newInstance(String param1) {
        RegisterPart2Fragment fragment = new RegisterPart2Fragment();
        Bundle args = new Bundle();
        args.putString(ARG_EMAIL, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mEmail =  getArguments().getString(ARG_EMAIL);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_register_part2, container, false);
    }
}