package com.example.android_project.views;


import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import com.example.android_project.R;
import com.example.android_project.data.models.user.User;
import com.example.android_project.view_models.AuthViewModel;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.Objects;

public class MenuBottomSheetFragment extends BottomSheetDialogFragment {

    public static final String TAG = "ModalBottomSheet";

    private AuthViewModel authViewModel;

    private boolean isLargeLayout;

    private TextView tvUsername;

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {
        return inflater.inflate(R.layout.fragment_menu_bottom_sheet, container, false);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        authViewModel = new ViewModelProvider(requireActivity()).get(AuthViewModel.class);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        isLargeLayout = getResources().getBoolean(R.bool.large_layout);

        tvUsername = requireView().findViewById(R.id.menu_tv_username);
        ImageButton btnClose = requireView().findViewById(R.id.menu_btn_close);

        Button btnLogout = requireView().findViewById(R.id.menu_btn_logout);
        Button btnSettings = requireView().findViewById(R.id.menu_btn_settings);
        Button btnStat = requireView().findViewById(R.id.menu_btn_stat);
        Button btnMyFriends = requireView().findViewById(R.id.menu_btn_friends);

        btnMyFriends.setEnabled(authViewModel.getUser().getValue() != null);
        btnSettings.setEnabled(authViewModel.getUser().getValue() != null);

        authViewModel.getUser().observe(getViewLifecycleOwner(), user -> btnMyFriends.setEnabled(user != null));
        authViewModel.getUser().observe(getViewLifecycleOwner(), user -> btnSettings.setEnabled(user != null));

        setUsernameTitle(authViewModel.getUser().getValue());

        btnStat.setOnClickListener(this::onStatClick);
        btnSettings.setOnClickListener(this::onSettingsClick);
        btnMyFriends.setOnClickListener(this::onMyFriendsClick);

        btnClose.setOnClickListener(view1 -> this.dismiss());

        btnLogout.setOnClickListener(view1 -> {
            this.dismiss();
            authViewModel.logout();
        });

        setUsernameTitle(authViewModel.getUser().getValue());

        authViewModel.getUser().observe(getViewLifecycleOwner(), this::setUsernameTitle);

    }

    public void onSettingsClick(View v) {
        SettingsFragment settingsFragment = new SettingsFragment();

        launchDialog(settingsFragment);
    }

    public void onStatClick(View v) {
        SettingsFragment settingsFragment = new SettingsFragment();

        launchDialog(settingsFragment);
    }

    public void onMyFriendsClick(View v) {
        MyFriendsFragment myFriendsFragment = new MyFriendsFragment();

        launchDialog(myFriendsFragment);
    }

    private void launchDialog(DialogFragment dialogFragment) {
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();

        this.dismiss();

        dialogFragment.show(fragmentManager, "dialog");
    }

    private void setUsernameTitle(User user) {
        Log.d(TAG, "setUsernameTitle: "+ user);
        tvUsername.setText(
                getString(
                        R.string.menu_username,
                        (user != null && !user.getUsername().isEmpty())
                                ? user.getUsername()
                                : getString(R.string.menu_anonymous_user)
                )
        );
        this.requireView().invalidate();
    }

}
