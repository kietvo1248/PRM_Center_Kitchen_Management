package com.example.prm_center_kitchen_management.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.prm_center_kitchen_management.R;
import com.example.prm_center_kitchen_management.model.response.Shipment;
import java.util.List;

public class ShipmentAdapter extends RecyclerView.Adapter<ShipmentAdapter.ViewHolder> {

    private List<Shipment> shipmentList;

    public ShipmentAdapter(List<Shipment> shipmentList) {
        this.shipmentList = shipmentList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_shipment, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Shipment shipment = shipmentList.get(position);
        holder.tvShipmentCode.setText(shipment.getShipmentCode());
        holder.tvToStore.setText("To: " + shipment.getToStoreName());
        holder.tvStatus.setText(shipment.getStatus());
        holder.tvShippedDate.setText("Shipped: " + shipment.getShippedDate());

        // Status Coloring Logic
        switch (shipment.getStatus().toLowerCase()) {
            case "claimed":
                holder.tvStatus.setBackgroundResource(R.drawable.bg_status_draft); // Use red if available
                holder.tvStatus.setTextColor(Color.WHITE);
                // Note: In real app, create bg_status_claimed.xml with red color
                break;
            case "received":
                holder.tvStatus.setBackgroundResource(R.drawable.bg_status_completed);
                break;
            case "shipped":
                holder.tvStatus.setBackgroundResource(R.drawable.bg_status_pending);
                break;
            default:
                holder.tvStatus.setBackgroundResource(R.drawable.bg_status_draft);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return shipmentList != null ? shipmentList.size() : 0;
    }

    public void updateData(List<Shipment> newList) {
        this.shipmentList = newList;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvShipmentCode, tvStatus, tvToStore, tvShippedDate;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvShipmentCode = itemView.findViewById(R.id.tvShipmentCode);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            tvToStore = itemView.findViewById(R.id.tvToStore);
            tvShippedDate = itemView.findViewById(R.id.tvShippedDate);
        }
    }
}
