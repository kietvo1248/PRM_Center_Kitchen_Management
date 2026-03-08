package com.example.prm_center_kitchen_management.adapter.roles.FranchiseStaff;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prm_center_kitchen_management.R;
import com.example.prm_center_kitchen_management.model.response.ShipmentResponse;

import java.util.ArrayList;
import java.util.List;

public class ShipmentAdapter extends RecyclerView.Adapter<ShipmentAdapter.ViewHolder> {
    private final List<ShipmentResponse.ShipmentItem> list;

    public ShipmentAdapter(List<ShipmentResponse.ShipmentItem> list) {
        this.list = list != null ? list : new ArrayList<>();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_shipment, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ShipmentResponse.ShipmentItem item = list.get(position);
        
        String orderId = "N/A";
        if (item.getOrderId() != null) {
            orderId = item.getOrderId().length() > 8 
                    ? item.getOrderId().substring(0, 8) + "..." 
                    : item.getOrderId();
        }
        
        holder.tvOrderId.setText("Mã Đơn: " + orderId);
        holder.tvDate.setText("Ngày tạo: " + item.getCreatedAt());

        // Đổi màu Status dựa vào trạng thái
        String status = item.getStatus();
        if ("in_transit".equalsIgnoreCase(status)) {
            holder.tvStatus.setText("ĐANG GIAO");
            holder.tvStatus.setTextColor(0xFFF57C00); // Màu cam
        } else if ("preparing".equalsIgnoreCase(status)) {
            holder.tvStatus.setText("ĐANG CHUẨN BỊ");
            holder.tvStatus.setTextColor(0xFF1976D2); // Màu xanh dương
        } else {
            holder.tvStatus.setText(status != null ? status.toUpperCase() : "N/A");
            holder.tvStatus.setTextColor(0xFF757575); // Màu xám mặc định
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvOrderId, tvStatus, tvDate;

        public ViewHolder(View itemView) {
            super(itemView);
            tvOrderId = itemView.findViewById(R.id.tvOrderId);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            tvDate = itemView.findViewById(R.id.tvDate);
        }
    }
}