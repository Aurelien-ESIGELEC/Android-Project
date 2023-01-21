package com.example.android_project.views.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android_project.R;
import com.example.android_project.data.models.fuel_price.Fuel;
import com.example.android_project.data.models.fuel_price.GasStation;

import java.time.Instant;
import java.util.Date;
import java.util.List;

public class GasStationFuelAdapter extends RecyclerView.Adapter<GasStationFuelAdapter.ViewHolder> {

    private List<Fuel> fuelList;

    /**
     * Initialize the dataset of the Adapter
     *
     * @param fuelList {@link List}<{@link Fuel}> containing the data to populate view to be used
     * by RecyclerView
     */
    public GasStationFuelAdapter(List<Fuel> fuelList) {
        this.fuelList = fuelList;
    }

    public GasStationFuelAdapter() {super();}

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder)
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvDate;
        private final TextView tvPrice;

        private final ImageButton ibtnUp;
        private final ImageButton ibtnDown;

        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View

            tvDate = view.findViewById(R.id.station_fuel_tv_date);
            tvPrice = view.findViewById(R.id.station_fuel_tv_price);
            ibtnUp = view.findViewById(R.id.station_fuel_ibtn_up);
            ibtnDown = view.findViewById(R.id.station_fuel_ibtn_down);
        }

        public ImageButton getIbtnUp() {
            return ibtnUp;
        }

        public ImageButton getIbtnDown() {
            return ibtnDown;
        }

        public TextView getTvDate() {
            return tvDate;
        }

        public TextView getTvPrice() {
            return tvPrice;
        }

    }

    // Create new views (invoked by the layout manager)
    @NonNull
    @Override
    public GasStationFuelAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.fuel_price_item, viewGroup, false);

        return new ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(@NonNull GasStationFuelAdapter.ViewHolder viewHolder, final int position) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element

        Context context = viewHolder.itemView.getContext();
        Fuel fuel = fuelList.get(position);

//        viewHolder.getTvDate().setText(context.getString(R.string.fuel_date_eu, Instant.now()));
        viewHolder.getTvPrice().setText(context.getString(R.string.fuel_price_euro, fuel.getPrice()));

        viewHolder.getIbtnUp().setOnClickListener(v -> {
            if (viewHolder.getIbtnUp().getDrawable() == AppCompatResources.getDrawable(context, R.drawable.thumb_up_off)) {
                viewHolder.getIbtnUp().setImageDrawable( AppCompatResources.getDrawable(context, R.drawable.thumb_up));
            } else {
                viewHolder.getIbtnUp().setImageDrawable( AppCompatResources.getDrawable(context, R.drawable.thumb_up_off));
            }
            
        });

        viewHolder.getIbtnDown().setOnClickListener(v -> {
            if (viewHolder.getIbtnDown().getDrawable() == AppCompatResources.getDrawable(context, R.drawable.thumb_down_off)) {
                viewHolder.getIbtnDown().setImageDrawable( AppCompatResources.getDrawable(context, R.drawable.thumb_down));
            } else {
                viewHolder.getIbtnDown().setImageDrawable( AppCompatResources.getDrawable(context, R.drawable.thumb_down_off));
            }

        });

    }

    public void setFuelList(List<Fuel> fuelList) {
        this.fuelList = fuelList;
        this.notifyItemRangeChanged(0, this.fuelList.size());
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return fuelList != null ? fuelList.size() : 0;
    }

}
