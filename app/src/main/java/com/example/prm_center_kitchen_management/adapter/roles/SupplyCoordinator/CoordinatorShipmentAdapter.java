package com.example.prm_center_kitchen_management.adapter.roles.SupplyCoordinator;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.prm_center_kitchen_management.R;
import com.example.prm_center_kitchen_management.model.response.CoordinatorShipmentResponse;
import java.util.List;
public class CoordinatorShipmentAdapter extends RecyclerView.Adapter<CoordinatorShipmentAdapter.ViewHolder> {
    private List<CoordinatorShipmentResponse.ShipmentItem> list;
    private OnItemClickListener listener;

    public interface OnItemClickListener { void onClick(CoordinatorShipmentResponse.ShipmentItem item); }

    public CoordinatorShipmentAdapter(List<CoordinatorShipmentResponse.ShipmentItem> list, OnItemClickListener listener) {
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_coordinator_shipment, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CoordinatorShipmentResponse.ShipmentItem item = list.get(position);

        holder.tvId.setText("VẬN ĐƠN: #" + item.getId().substring(0, 8).toUpperCase());
        holder.tvStore.setText(item.getStoreName());
        holder.tvDate.setText("Ngày tạo: " + item.getCreatedAt().substring(0, 10));

        String status = item.getStatus().toLowerCase();
        holder.tvStatus.setText(status.toUpperCase());

        GradientDrawable gd = new GradientDrawable();
        gd.setCornerRadius(16);
        if (status.equals("preparing")) {
            holder.tvStatus.setTextColor(Color.parseColor("#E65100"));
            gd.setColor(Color.parseColor("#FFF3E0"));
        } else if (status.equals("in_transit")) {
            holder.tvStatus.setTextColor(Color.parseColor("#0D47A1"));
            gd.setColor(Color.parseColor("#E3F2FD"));
        } else {
            holder.tvStatus.setTextColor(Color.parseColor("#1B5E20"));
            gd.setColor(Color.parseColor("#E8F5E9"));
        }
        holder.tvStatus.setBackground(gd);

        holder.itemView.setOnClickListener(v -> listener.onClick(item));
    }

    @Override public int getItemCount() { return list != null ? list.size() : 0; }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvId, tvStore, tvStatus, tvDate;
        public ViewHolder(View itemView) {
            super(itemView);
            tvId = itemView.findViewById(R.id.tvCoordinatorShipmentId);
            tvStore = itemView.findViewById(R.id.tvCoordinatorStoreName);
            tvStatus = itemView.findViewById(R.id.tvCoordinatorShipmentStatus);
            tvDate = itemView.findViewById(R.id.tvCoordinatorCreatedAt);
        }
    }
}