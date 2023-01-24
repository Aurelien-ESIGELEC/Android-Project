package com.example.android_project.views.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android_project.R;
import com.example.android_project.data.models.fuel_price.Fuel;
import com.google.android.material.progressindicator.CircularProgressIndicator;

import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.List;

public class GasStationFuelAdapter extends RecyclerView.Adapter<GasStationFuelAdapter.ViewHolder> {

    private static final String TAG = "GasStationFuelAdapter";
    private List<Fuel> fuelList;

    private boolean isUpToggled;
    private boolean isDownToggled;

    private OnReviewListener onReviewListener;

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

    public GasStationFuelAdapter(List<Fuel> fuelList, OnReviewListener onReviewListener) {
        this();
        this.fuelList = fuelList;
        this.onReviewListener = onReviewListener;
    }

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder)
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvDate;
        private final TextView tvPrice;
        private final TextView tvIv;

        private final ImageButton ibtnUp;
        private final ImageButton ibtnDown;

        private final CircularProgressIndicator progressIndicator;

        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View

            tvDate = view.findViewById(R.id.station_fuel_tv_update_date);
            tvPrice = view.findViewById(R.id.station_fuel_tv_price);
            tvIv = view.findViewById(R.id.station_fuel_tv_ri);
            ibtnUp = view.findViewById(R.id.station_fuel_ibtn_up);
            ibtnDown = view.findViewById(R.id.station_fuel_ibtn_down);
            progressIndicator = view.findViewById(R.id.station_fuel_progress_indicator);
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
        public TextView getTvIv() {
            return tvIv;
        }

        public CircularProgressIndicator getProgressIndicator() {
            return progressIndicator;
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
        viewHolder.getTvIv().setText(context.getString(R.string.fuel_price_iv, fuel.getReliabilityIndex()));

        Log.d(TAG, "onBindViewHolder: " + fuel.getReliabilityIndex());

        viewHolder.getProgressIndicator().setVisibility(View.GONE);

        if (!fuel.canBeUpdated()) {
            viewHolder.getIbtnUp().setEnabled(false);
            viewHolder.getIbtnDown().setEnabled(false);
        }

        if (fuel.getMyReview().equals("up")) {
            isUpToggled = true;
            updateUpButtonIcon(viewHolder.getIbtnUp());
        }

        if (fuel.getMyReview().equals("down")) {
            isDownToggled = true;
            updateDownButtonIcon(viewHolder.getIbtnDown());
        }

        viewHolder.getIbtnUp().setOnClickListener(v -> {
            if (isDownToggled) {
                isDownToggled = false;
                updateDownButtonIcon(viewHolder.getIbtnDown());
            }

            isUpToggled = !isUpToggled;
            updateUpButtonIcon(viewHolder.getIbtnUp());

            if (onReviewListener != null) {
                onReviewListener.onUpToggled(viewHolder, fuel, isUpToggled);
            }
        });

        viewHolder.getIbtnDown().setOnClickListener(v -> {

            if (isUpToggled) {
                isUpToggled = false;
                updateUpButtonIcon(viewHolder.getIbtnUp());
            }

            isDownToggled = !isDownToggled;
            updateDownButtonIcon(viewHolder.getIbtnDown());

            if (onReviewListener != null) {
                onReviewListener.onDownToggled(viewHolder, fuel, isDownToggled);
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

    private void updateUpButtonIcon(ImageButton imageButton) {
        if (isUpToggled) {
            imageButton.setImageResource(R.drawable.thumb_up);
        } else {
            imageButton.setImageResource(R.drawable.thumb_up_off);
        }
    }

    private void updateDownButtonIcon(ImageButton imageButton) {
        if (isDownToggled) {
            imageButton.setImageResource(R.drawable.thumb_down);
        } else {
            imageButton.setImageResource(R.drawable.thumb_down_off);
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return fuelList != null ? fuelList.size() : 0;
    }

    public interface OnReviewListener{
        void onUpToggled(ViewHolder view, Fuel fuel, boolean isToggled);
        void onDownToggled(ViewHolder view, Fuel fuel, boolean isToggled);
    }

}
