package com.example.lab_rest.adapter;

import android.widget.ImageView;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.lab_rest.R;
import com.example.lab_rest.model.Car;
import com.example.lab_rest.model.Toilets;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ToiletAdapter extends RecyclerView.Adapter<ToiletAdapter.ViewHolder> {

    /**
     * Create ViewHolder class to bind list item view
     */
    class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView ivToilet;
        public TextView tvToiletName;
        public TextView tvToiletId;
        public TextView tvToiletLocation;
        public TextView tvToiletDescription;

        public ViewHolder(View itemView) {
            super(itemView);
            ivToilet = itemView.findViewById(R.id.ivToilet);
            tvToiletName = itemView.findViewById(R.id.tvToiletName);
            tvToiletId = itemView.findViewById(R.id.tvToiletId);
            tvToiletLocation = itemView.findViewById(R.id.tvToiletLocation);
            tvToiletDescription = itemView.findViewById(R.id.tvToiletDescription);
        }
    }


    private List<Toilets> toiletListData; // List of car objects
    private Context mContext; // Activity context
    private int currentPos; // Currently selected item (long press)

    public ToiletAdapter(Context context, List<Toilets> listData) {
        toiletListData = listData;
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
        View view = inflater.inflate(R.layout.toilet_list_item, parent, false);
        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Bind data to the ViewHolder instance
        Toilets toilet = toiletListData.get(position);
        holder.tvToiletName.setText("Toilet Name:" + toilet.getName);
        holder.tvBrand.setText("Brand: " +car.getBrand());
        holder.tvPlateNumber.setText("Plate Number : "+car.getPlateNumber());
        holder.tvAvailability.setText("Availabality : " + car.getAvailability());
        holder.tvCreatedAt.setText("Created At : " + car.getCreatedAt());

        // Use Glide to load the image into the ImageView
        Glide.with(mContext)
                .load("http://178.128.220.20/2023500191/api/" + car.getImage())
                .placeholder(R.drawable.noimage) // Placeholder image if the URL is empty
                .error(R.drawable.noimage) // Error image if there is a problem loading the image
                .into(holder.ivToilet);
    }

    @Override
    public int getItemCount() {
        return toiletListData.size();
    }

        //////////////////////////////////////////////////////////////////////
        // adapter class definitions

//        private List<Book> bookListData;   // list of book objects
//        private Context mContext;       // activity context
//
//        public BookAdapter(Context context, List<Book> listData) {
//            bookListData = listData;
//            mContext = context;
//        }
//
//        private Context getmContext() {
//            return mContext;
//        }
//
//        @Override
//        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//            Context context = parent.getContext();
//            LayoutInflater inflater = LayoutInflater.from(context);
//            // Inflate layout using the single item layout
//            View view = inflater.inflate(R.layout.book_list_item, parent, false);
//            // Return a new holder instance
//            ViewHolder viewHolder = new ViewHolder(view);
//            return viewHolder;
//        }
//
//        @Override
//        public void onBindViewHolder(ViewHolder holder, int position) {
//            // bind data to the view holder instance
//            Book m = bookListData.get(position);
//            holder.tvTitle.setText(m.getName());
//            holder.tvAuthor.setText(m.getAuthor());
//            holder.tvDescription.setText(m.getDescription());
//        }
//
//        @Override
//        public int getItemCount() {
//            return bookListData.size();
//        }

    }
}
