package com.example.lab_rest.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.lab_rest.R;
import com.example.lab_rest.model.Booking;
import com.example.lab_rest.model.Car;
import com.bumptech.glide.Glide;

import java.util.List;

public class CarAdapter extends RecyclerView.Adapter<CarAdapter.ViewHolder> {

    /**
     * ViewHolder class to bind list item view
     */
    class ViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener {
        public TextView tvCarID;
        public TextView tvModel;
        public TextView tvBrand;
        public TextView tvPlateNumber;
        public TextView tvAvailability;
        public TextView tvCreatedAt;
        public ImageView imgCarCover;

        public ViewHolder(View itemView) {
            super(itemView);
            tvModel = itemView.findViewById(R.id.tvModel);
            tvBrand = itemView.findViewById(R.id.tvBrand);
            tvPlateNumber = itemView.findViewById(R.id.tvPlateNumber);
            tvAvailability = itemView.findViewById(R.id.tvAvailability);
            imgCarCover = itemView.findViewById(R.id.imgCarCover);
            tvCreatedAt = itemView.findViewById(R.id.tvCreatedAt);

            itemView.setOnLongClickListener(this); // Register long click action to this ViewHolder instance
        }

        @Override
        public boolean onLongClick(View v) {
            currentPos = getAdapterPosition(); // Record the position here
            return false;
        }
    }

    private List<Car> carListData; // List of car objects
    private Context mContext; // Activity context
    private int currentPos; // Currently selected item (long press)

    public CarAdapter(Context context, List<Car> listData) {
        carListData = listData;
        mContext = context;
    }

    private Context getmContext() {
        return mContext;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        // Inflate layout using the single item layout
        View view = inflater.inflate(R.layout.car_list_item, parent, false);
        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // Bind data to the ViewHolder instance
        Car car = carListData.get(position);
        holder.tvModel.setText("Model :" +car.getModel());
        holder.tvBrand.setText("Brand: " +car.getBrand());
        holder.tvPlateNumber.setText("Plate Number : "+car.getPlateNumber());
        holder.tvAvailability.setText("Availabality : " + car.getAvailability());
        holder.tvCreatedAt.setText("Created At : " + car.getCreatedAt());

        // Use Glide to load the image into the ImageView
        Glide.with(mContext)
                .load("http://178.128.220.20/2023500191/api/" + car.getImage())
                .placeholder(R.drawable.bmw) // Placeholder image if the URL is empty
                .error(R.drawable.bmw) // Error image if there is a problem loading the image
                .into(holder.imgCarCover);
    }


    @Override
    public int getItemCount() {
        return carListData.size();
    }

    /**
     * Return car object for currently selected item (index already set by long press in ViewHolder)
     */
    public Car getSelectedItem() {
        // Return the car record if the current selected position/index is valid
        if (currentPos >= 0 && carListData != null && currentPos < carListData.size()) {
            return carListData.get(currentPos);
        }
        return null;
    }
}
