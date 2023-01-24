package com.example.android_project.views.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.android_project.R;
import com.example.android_project.data.models.address.SearchAddress;

import java.util.List;

public class SearchAddressListAdapter extends RecyclerView.Adapter<SearchAddressListAdapter.ViewHolder> {

    private List<SearchAddress> searchAddresses;
    private OnItemListener onItemListener;

    /**
     * Initialize the dataset of the Adapter
     *
     * @param searchAddresses List<SearchAddress> containing the data to populate views to be used
     * by RecyclerView
     */
    public SearchAddressListAdapter(List<SearchAddress> searchAddresses, OnItemListener onItemListener) {
        this(searchAddresses);
        this.onItemListener = onItemListener;
    }

    public SearchAddressListAdapter(List<SearchAddress> searchAddresses) {
        this.searchAddresses = searchAddresses;
    }

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder)
     */
    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final TextView tvAddress;
        private final TextView tvCity;
        private OnItemListener onItemListener;

        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View

            tvAddress = view.findViewById(R.id.list_tv_friend);
            tvCity = view.findViewById(R.id.list_tv_city);
        }

        public ViewHolder(View view, OnItemListener onItemListener) {
            this(view);
            view.setOnClickListener(this);
            this.onItemListener = onItemListener;

        }

        public TextView getTvAddress() {
            return tvAddress;
        }

        public TextView getTvCity() {
            return tvCity;
        }

        @Override
        public void onClick(View view) {
            if (this.onItemListener != null){
                this.onItemListener.onItemClick(getAbsoluteAdapterPosition());
            }
        }
    }

    // Create new views (invoked by the layout manager)
    @Override
    public SearchAddressListAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.search_address_item, viewGroup, false);

        return new ViewHolder(view, this.onItemListener);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(SearchAddressListAdapter.ViewHolder viewHolder, final int position) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element

        SearchAddress searchAddress = searchAddresses.get(position);

        if (!searchAddress.getFullName().isEmpty()) {
            viewHolder.getTvAddress().setText(searchAddress.getFullName());
            viewHolder.getTvCity().setText(searchAddress.getFullCity());
        } else {
            viewHolder.getTvAddress().setText(searchAddress.getFullCity());
            viewHolder.getTvCity().setText("");
        }

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return searchAddresses.size();
    }

    public interface OnItemListener{
        void onItemClick(int position);
    }
}
