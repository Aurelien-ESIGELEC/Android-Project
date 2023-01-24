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

import com.example.android_project.R;
import com.example.android_project.data.models.user.User;
import com.example.android_project.view_models.AuthViewModel;
import com.example.android_project.views.adapters.FriendsListAdapter;
import com.example.android_project.views.components.AddFriendDialogFragment;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.progressindicator.LinearProgressIndicator;

/**
 * A simple {@link Fragment} subclass.
 */
public class FollowerListFragment extends Fragment {

    private AuthViewModel authViewModel;

    public FollowerListFragment() {
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
        return inflater.inflate(R.layout.fragment_follower_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView recyclerView = requireView().findViewById(R.id.friends_rv_follower);

        LinearProgressIndicator progressIndicator = requireView().findViewById(R.id.friends_follower_progress_indicator);
        progressIndicator.setVisibility(View.INVISIBLE);

        User user = authViewModel.getUser().getValue();


        if (user != null) {
            progressIndicator.setVisibility(View.VISIBLE);
            authViewModel.getFollowers().observe(getViewLifecycleOwner(), strings -> {
                recyclerView.setAdapter(new FriendsListAdapter(strings));
                recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
                progressIndicator.setVisibility(View.INVISIBLE);
            });
        }

    }
}