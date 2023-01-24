package com.example.android_project.views.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.android_project.R;
import com.example.android_project.data.models.address.SearchAddress;

import java.util.List;

public class FriendsListAdapter extends RecyclerView.Adapter<FriendsListAdapter.ViewHolder> {

    private List<String> friends;


    public FriendsListAdapter(List<String> friends) {
        this.friends = friends;
    }

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder)
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvFriend;
        public ViewHolder(View view) {
            super(view);
            tvFriend = view.findViewById(R.id.list_tv_friend);
        }

        public TextView getTvFriend() {
            return tvFriend;
        }
    }

    // Create new views (invoked by the layout manager)
    @Override
    public FriendsListAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.friends_item, viewGroup, false);

        return new ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(FriendsListAdapter.ViewHolder viewHolder, final int position) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        String friend = friends.get(position);

        viewHolder.getTvFriend().setText(friend);

    }

    public void setFriends(List<String> friends) {
        this.friends = friends;
        this.notifyDataSetChanged();
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return friends != null ? friends.size() : 0;
    }
}
