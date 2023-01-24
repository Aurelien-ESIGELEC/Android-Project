package com.example.android_project.views.pages;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android_project.R;
import com.example.android_project.data.models.user.User;
import com.example.android_project.view_models.AuthViewModel;
import com.example.android_project.views.adapters.FriendsListAdapter;
import com.example.android_project.views.components.AddFriendDialogFragment;
import com.example.android_project.views.components.AddFuelDialogFragment;
import com.google.android.material.button.MaterialButton;

/**
 * A simple {@link Fragment} subclass.
 */
public class FriendsListFragment extends Fragment {

    private AuthViewModel authViewModel;

    public FriendsListFragment() {
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
        return inflater.inflate(R.layout.fragment_friends_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        MaterialButton btnAddFriend = requireView().findViewById(R.id.friends_btn_add_friend);

        RecyclerView recyclerView = requireView().findViewById(R.id.friends_rv_friends);

        User user = authViewModel.getUser().getValue();

        if (user != null) {
            recyclerView.setAdapter(new FriendsListAdapter(user.getFriends()));
            recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

            authViewModel.getUser().observe(getViewLifecycleOwner(), user1 -> {
                ((FriendsListAdapter)recyclerView.getAdapter()).setFriends(user1.getFriends());
            });

            btnAddFriend.setOnClickListener(view1 -> {
                AddFriendDialogFragment newFragment = new AddFriendDialogFragment();
                newFragment.show(requireActivity().getSupportFragmentManager(), "add_friend");
            });
        }
    }
}