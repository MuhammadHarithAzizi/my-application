package com.example.lab_rest.adapter;


        import android.content.Context;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.TextView;

        import androidx.recyclerview.widget.RecyclerView;

        import com.example.lab_rest.R;
        import com.example.lab_rest.model.Booking;

        import java.util.List;

public class BookingAdapter extends RecyclerView.Adapter<BookingAdapter.ViewHolder> {

    /**
     * ViewHolder class to bind list item view
     */
    class ViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener {
        public TextView tvUserId;
        public TextView tvCarId;
        public TextView tvBookingDate;
        public TextView tvStatus;
        public TextView tvRemarks;
        public TextView tvAdminMsg;
        public TextView tvCreatedAt;

        public ViewHolder(View itemView) {
            super(itemView);
            tvUserId = itemView.findViewById(R.id.tvUserId);
            tvCarId = itemView.findViewById(R.id.tvCarId);
            tvBookingDate = itemView.findViewById(R.id.tvBookingDate);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            tvRemarks = itemView.findViewById(R.id.tvRemarks);
            tvAdminMsg = itemView.findViewById(R.id.tvAdminMsg);
            tvCreatedAt = itemView.findViewById(R.id.tvCreatedAt);

            itemView.setOnLongClickListener(this); // Register long click action to this ViewHolder instance instance
        }

        @Override
        public boolean onLongClick(View v) {
            currentPos = getAdapterPosition(); // Record the position here
            return false;
        }
    }

    private  List<Booking> bookingListData; // List of booking objects
    private Context mContext; // Activity context
    private int currentPos; // Currently selected item (long press)

    public BookingAdapter(Context context, List<Booking> listData) {
        bookingListData = listData;
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
        View view = inflater.inflate(R.layout.booking_list_item, parent, false);
        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // Bind data to the ViewHolder instance
        Booking booking = bookingListData.get(position);
        holder.tvUserId.setText("User ID: " + booking.getUserId());
        holder.tvCarId.setText("Car ID: " + booking.getCarId());
        holder.tvBookingDate.setText("Booking Date: " + booking.getBookingDate());
        holder.tvStatus.setText("Status: " + booking.getStatus());
        holder.tvRemarks.setText("Remarks: " + booking.getRemarks());
        holder.tvAdminMsg.setText("Admin Message: " + booking.getAdminMsg());
        holder.tvCreatedAt.setText("Created At: " + booking.getCreatedAt());
    }

    @Override
    public int getItemCount() {
        return bookingListData.size();
    }

    /**
     * Return booking object for currently selected item (index already set by long press in ViewHolder)
     */
    public Booking getSelectedItem() {
        // Return the booking record if the current selected position/index is valid
        if (currentPos >= 0 && bookingListData != null && currentPos < bookingListData.size()) {
            return bookingListData.get(currentPos);
        }
        return null;
    }
}