package com.example.prm_center_kitchen_management.adapter.roles.FranchiseStaff;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.prm_center_kitchen_management.R;
import com.example.prm_center_kitchen_management.model.response.Shipment;
import java.util.ArrayList;
import java.util.List;

public class ShipmentAdapter extends RecyclerView.Adapter<ShipmentAdapter.ViewHolder> {
    private List<Shipment> list;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Shipment shipment);
    }

    // CONSTRUCTOR ĐÃ SỬA LẠI ĐÚNG CHUẨN
    public ShipmentAdapter(List<Shipment> list, OnItemClickListener listener) {
        this.list = list != null ? list : new ArrayList<>();
        this.listener = listener;
    }

    // HÀM NÀY SẼ FIX LỖI Ở DÒNG 103
    public void updateData(List<Shipment> newList) {
        if (this.list != null) {
            this.list.clear();
            if (newList != null) this.list.addAll(newList);
            notifyDataSetChanged();
        }
    }

    @NonNull @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_shipment, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Shipment item = list.get(position);

        // 1. LẤY ID CHUẨN TỪ API
        String sId = item.getId() != null ? item.getId() : "";
        holder.tvShipmentCode.setText("Mã Chuyến: " + (sId.length() > 8 ? sId.substring(0,8) + "..." : sId));

        // 2. LẤY NGÀY TẠO CHUẨN TỪ API
        String date = item.getCreatedAt() != null ? item.getCreatedAt() : "N/A";
        holder.tvShippedDate.setText("Ngày: " + date);

        String status = item.getStatus() != null ? item.getStatus() : "";

        if ("in_transit".equalsIgnoreCase(status)) {
            holder.tvStatus.setText("ĐANG GIAO");
            holder.tvStatus.setTextColor(android.graphics.Color.parseColor("#F57C00")); // Cam
        } else if ("completed".equalsIgnoreCase(status)) {
            holder.tvStatus.setText("ĐÃ NHẬN");
            holder.tvStatus.setTextColor(android.graphics.Color.parseColor("#388E3C")); // Xanh lá
        } else if ("preparing".equalsIgnoreCase(status)) {
            holder.tvStatus.setText("CHUẨN BỊ");
            holder.tvStatus.setTextColor(android.graphics.Color.parseColor("#1976D2")); // Xanh dương
        } else {
            holder.tvStatus.setText(status.toUpperCase());
            holder.tvStatus.setTextColor(android.graphics.Color.parseColor("#757575")); // Xám
        }

        // Sự kiện click truyền ID chuẩn vào Fragment
        holder.itemView.setOnClickListener(v -> listener.onItemClick(item));
    }

    @Override public int getItemCount() { return list.size(); }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvShipmentCode, tvStatus, tvShippedDate;

        public ViewHolder(View itemView) {
            super(itemView);
            tvShipmentCode = itemView.findViewById(R.id.tvShipmentCode);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            tvShippedDate = itemView.findViewById(R.id.tvShippedDate);
        }
    }
}