package com.example.prm_center_kitchen_management.adapter.roles.FranchiseStaff;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prm_center_kitchen_management.R;
import com.example.prm_center_kitchen_management.model.response.OrderResponse;

import java.util.ArrayList;
import java.util.List;

public class StoreOrderAdapter extends RecyclerView.Adapter<StoreOrderAdapter.ViewHolder>{
    private final List<OrderResponse.OrderItem> list;
    private final OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(OrderResponse.OrderItem item);
    }

    public StoreOrderAdapter(List<OrderResponse.OrderItem> list, OnItemClickListener listener) {
        this.list = list != null ? list : new ArrayList<>();
        this.listener = listener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvId, tvDate, tvStatus;

        public ViewHolder(View itemView) {
            super(itemView);
            tvId = itemView.findViewById(R.id.tvOrderId);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvStatus = itemView.findViewById(R.id.tvStatus);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_store_order, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        OrderResponse.OrderItem item = list.get(position);

        // Cắt ngắn UUID cho gọn giao diện
        String displayId = (item.getId() != null && item.getId().length() >= 8)
                ? item.getId().substring(0, 8) : item.getId();

        holder.tvId.setText("Mã Đơn: " + displayId);
        holder.tvDate.setText("Ngày tạo: " + item.getCreatedAt());

        // Xử lý hiển thị trạng thái và màu sắc
        String status = item.getStatus();
        holder.tvStatus.setText(status != null ? status.toUpperCase() : "N/A");

        if ("pending".equalsIgnoreCase(status)) {
            holder.tvStatus.setTextColor(0xFFF57C00); // Màu cam
        } else if ("approved".equalsIgnoreCase(status) || "completed".equalsIgnoreCase(status)) {
            holder.tvStatus.setTextColor(0xFF388E3C); // Màu xanh lá
        } else if ("cancelled".equalsIgnoreCase(status) || "rejected".equalsIgnoreCase(status)) {
            holder.tvStatus.setTextColor(0xFFD32F2F); // Màu đỏ
        } else {
            holder.tvStatus.setTextColor(0xFF1976D2); // Màu xanh dương mặc định
        }

        // Bắt sự kiện click vào item
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) listener.onItemClick(item);
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

}
