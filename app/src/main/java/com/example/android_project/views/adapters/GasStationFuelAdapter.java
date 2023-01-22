package com.example.android_project.views.adapters;

import android.content.Context;
import android.util.Log;
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

import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.List;

public class GasStationFuelAdapter extends RecyclerView.Adapter<GasStationFuelAdapter.ViewHolder> {

    private static final String TAG = "GasStationFuelAdapter";
    private List<Fuel> fuelList;

    private boolean isUpToggled;
    private boolean isDownToggled;

    public GasStationFuelAdapter() {
        super();
        this.isUpToggled = false;
        this.isDownToggled = false;
    }

    /**
     * Initialize the dataset of the Adapter
     *
     * @param fuelList {@link List}<{@link Fuel}> containing the data to populate view to be used
     * by RecyclerView
     */
    public GasStationFuelAdapter(List<Fuel> fuelList) {
        this();
        this.fuelList = fuelList;
    }

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

        Calendar calendar = Calendar.getInstance();
        calendar.clear();

        LocalDateTime dateTime = fuel.getUpdateDate();
        calendar.set(dateTime.getYear(), dateTime.getMonthValue() - 1, dateTime.getDayOfMonth());

        viewHolder.getTvDate().setText(context.getString(R.string.fuel_date_eu, calendar));
        viewHolder.getTvPrice().setText(context.getString(R.string.fuel_price_euro, fuel.getPrice()));

        if (!fuel.canBeUpdated()) {
            viewHolder.getIbtnUp().setEnabled(false);
            viewHolder.getIbtnDown().setEnabled(false);
        }

        viewHolder.getIbtnUp().setOnClickListener(v -> {
            isUpToggled = !isUpToggled;
            if (isUpToggled) {
                viewHolder.getIbtnUp().setImageResource( R.drawable.thumb_up);
            } else {
                viewHolder.getIbtnUp().setImageResource(R.drawable.thumb_up_off);
            }
        });

        viewHolder.getIbtnDown().setOnClickListener(v -> {
            isDownToggled = !isDownToggled;
            if (isDownToggled) {
                viewHolder.getIbtnDown().setImageResource(R.drawable.thumb_down);
            } else {
                viewHolder.getIbtnDown().setImageResource(R.drawable.thumb_down_off);
            }
        });

    }

    public void setFuelList(List<Fuel> fuelList) {
        this.fuelList = fuelList;
        this.notifyItemRangeChanged(0, fuelList.size());
    }

    public void addFuelList(List<Fuel> fuelList) {
        int size = this.fuelList.size();
        this.fuelList.addAll(fuelList);
        this.notifyItemRangeChanged(size, fuelList.size());
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return fuelList != null ? fuelList.size() : 0;
    }

}
