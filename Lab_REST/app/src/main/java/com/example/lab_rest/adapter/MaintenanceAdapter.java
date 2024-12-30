package com.example.lab_rest.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.lab_rest.R;
import com.example.lab_rest.model.Book;
import com.example.lab_rest.model.Maintenance;

import java.util.List;

public class MaintenanceAdapter extends RecyclerView.Adapter<MaintenanceAdapter.ViewHolder> {

    /**
     * Create ViewHolder class to bind list item view
     */
    class ViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener {
        public TextView tvType;
        public TextView tvMaintenanceDate;
        public TextView tvCost;
        public TextView tvUpdatedMain;


        public ViewHolder(View itemView) {
            super(itemView);
            tvMaintenanceDate = itemView.findViewById(R.id.tvMaintenanceDate);
            tvCost = itemView.findViewById(R.id.tvCost);
            tvType = itemView.findViewById(R.id.tvType);
            tvUpdatedMain = itemView.findViewById(R.id.tvUpdatedMain);

            itemView.setOnLongClickListener(this); //register long click action to this viewholder instance
        }

        @Override
        public boolean onLongClick(View v) {
            currentPos = getAdapterPosition(); //key point, record the position here
            return false;
        }
    } // close ViewHolder class

    //////////////////////////////////////////////////////////////////////
    // adapter class definitions

    private List<Maintenance> maintenanceListData;   // list of book objects
    private Context mContext;       // activity context
    private int currentPos;         // currently selected item (long press)

    public MaintenanceAdapter(Context context, List<Maintenance> listData) {
        maintenanceListData = listData;
        mContext = context;
    }

    private Context getmContext() {
        return mContext;
    }

    @Override
    public MaintenanceAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        // Inflate layout using the single item layout
        View view = inflater.inflate(R.layout.maintenance_list_item, parent, false);
        // Return a new holder instance
        MaintenanceAdapter.ViewHolder viewHolder = new MaintenanceAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MaintenanceAdapter.ViewHolder holder, int position) {
        // bind data to the view holder instance
        Maintenance m = maintenanceListData.get(position);
        holder.tvType.setText(m.getType());
        double cost = m.getCost();
        holder.tvCost.setText(String.format("%.2f", cost));
        holder.tvMaintenanceDate.setText(m.getMaintenanceDate());
        holder.tvUpdatedMain.setText(m.getUpdateMaintenanceDate());




    }

    @Override
    public int getItemCount() {
        return maintenanceListData.size();
    }


    /**
     * return book object for currently selected book (index already set by long press in viewholder)
     * @return
     */
    public Maintenance getSelectedItem() {
        // return the book record if the current selected position/index is valid
        if(currentPos>=0 && maintenanceListData !=null && currentPos<maintenanceListData.size()) {
            return maintenanceListData.get(currentPos);
        }
        return null;
    }
}