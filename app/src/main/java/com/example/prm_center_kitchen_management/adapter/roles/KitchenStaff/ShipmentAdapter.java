package com.example.prm_center_kitchen_management.adapter.roles.KitchenStaff;

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

    public ShipmentAdapter(List<Shipment> list, OnItemClickListener listener) {
        this.list = list != null ? list : new ArrayList<>();
        this.listener = listener;
    }

    public void updateData(List<Shipment> newList) {
        if (this.list != null) {
            this.list.clear();
            if (newList != null) this.list.addAll(newList);
            notifyDataSetChanged();
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_shipment, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Shipment item = list.get(position);

        String sId = item.getId() != null ? item.getId() : "";
        holder.tvShipmentCode.setText("Mã Chuyến: " + (sId.length() > 8 ? sId.substring(0,8) + "..." : sId));

        // FIX LỖI: Gộp Tên Cửa Hàng và Ngày vào chung 1 dòng Text vì đã xóa tvToStore trong XML
        String date = item.getCreatedAt() != null ? item.getCreatedAt() : "N/A";
        holder.tvShippedDate.setText(" Ngày: " + date);

        String status = item.getStatus() != null ? item.getStatus() : "";

        if ("in_transit".equalsIgnoreCase(status)) {
            holder.tvStatus.setText("ĐANG GIAO");
            holder.tvStatus.setTextColor(Color.parseColor("#F57C00"));
        } else if ("completed".equalsIgnoreCase(status)) {
            holder.tvStatus.setText("ĐÃ GIAO");
            holder.tvStatus.setTextColor(Color.parseColor("#388E3C"));
        } else if ("preparing".equalsIgnoreCase(status)) {
            holder.tvStatus.setText("CHUẨN BỊ");
            holder.tvStatus.setTextColor(Color.parseColor("#1976D2"));
        } else {
            holder.tvStatus.setText(status.toUpperCase());
            holder.tvStatus.setTextColor(Color.parseColor("#757575"));
        }

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) listener.onItemClick(item);
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvShipmentCode, tvStatus, tvShippedDate;

        public ViewHolder(View itemView) {
            super(itemView);
            tvShipmentCode = itemView.findViewById(R.id.tvShipmentCode);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            tvShippedDate = itemView.findViewById(R.id.tvShippedDate);
            // Đã xóa dòng tvToStore = itemView.findViewById(...) để tránh lỗi
        }
    }
}