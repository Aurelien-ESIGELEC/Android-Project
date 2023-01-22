package com.example.android_project.views.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android_project.R;
import com.example.android_project.data.models.fuel_price.GasStation;

import java.util.ArrayList;
import java.util.List;

public class GasStationListAdapter extends RecyclerView.Adapter<GasStationListAdapter.ViewHolder> {

    private List<GasStation> gasStations;
    private OnItemListener onItemListener;

    /**
     * Initialize the dataset of the Adapter
     *
     * @param gasStations List<{@link GasStation}> containing the data to populate views to be used
     * by RecyclerView
     */
    public GasStationListAdapter(List<GasStation> gasStations, OnItemListener onItemListener) {
        this(gasStations);
        this.onItemListener = onItemListener;
    }

    public GasStationListAdapter(List<GasStation> gasStations) {
        this.gasStations = gasStations;
    }

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder)
     */
    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private OnItemListener onItemListener;
        private TextView tvAddress;
        private TextView tvCity;

        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View
            tvAddress = view.findViewById(R.id.station_fuel_tv_date);
            tvCity = view.findViewById(R.id.station_fuel_tv_price);
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
    @NonNull
    @Override
    public GasStationListAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.gas_station_item, viewGroup, false);

        return new ViewHolder(view, this.onItemListener);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(GasStationListAdapter.ViewHolder viewHolder, final int position) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element

        Context context = viewHolder.itemView.getContext();

        GasStation gasStation = gasStations.get(position);

        viewHolder.getTvAddress().setText(gasStation.getAddress());
        viewHolder.getTvCity().setText(
                context.getString(
                        R.string.station_city,
                        gasStation.getPostalCode(),
                        gasStation.getCity()
                )
        );

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return gasStations.size();
    }

    public void addGasStation(GasStation station) {
        gasStations.add(station);
    }

    public List<GasStation> getGasStations() {
        return new ArrayList<>(gasStations);
    }

    public interface OnItemListener{
        void onItemClick(int position);
    }
}
