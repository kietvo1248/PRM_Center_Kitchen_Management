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
import com.example.prm_center_kitchen_management.model.response.CoordinatorOrderResponse;

import java.util.List;

/**
 * Adapter hiển thị danh sách đơn hàng cho Supply Coordinator
 * Kế thừa RecyclerView.Adapter để sửa lỗi 'setAdapter' cannot be applied
 */
public class CoordinatorOrderAdapter extends RecyclerView.Adapter<CoordinatorOrderAdapter.ViewHolder> {

    private final List<CoordinatorOrderResponse.OrderItem> list;
    private final OnItemClickListener listener;

    public interface OnItemClickListener {
        void onOrderClick(CoordinatorOrderResponse.OrderItem item);
    }

    public CoordinatorOrderAdapter(List<CoordinatorOrderResponse.OrderItem> list, OnItemClickListener listener) {
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_coordinator_order, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CoordinatorOrderResponse.OrderItem item = list.get(position);

        // 1. Cắt ID (substring 8 ký tự đầu)
        String shortId = item.getId().substring(0, 8).toUpperCase();
        holder.tvId.setText("ĐƠN HÀNG: #" + shortId);

        // 2. Tên cửa hàng (Lấy từ object store)
        if (item.getStore() != null) {
            holder.tvStore.setText(item.getStore().getName());
        }

        // 3. Định dạng ngày (YYYY-MM-DD)
        if (item.getCreatedAt() != null && item.getCreatedAt().length() >= 10) {
            holder.tvDate.setText("Ngày tạo: " + item.getCreatedAt().substring(0, 10));
        }

        // 4. Logic màu sắc Status
        String status = item.getStatus().toLowerCase();
        holder.tvStatus.setText(status.toUpperCase());
        applyStatusStyle(holder.tvStatus, status);

        holder.itemView.setOnClickListener(v -> listener.onOrderClick(item));
    }

    private void applyStatusStyle(TextView tv, String status) {
        int color, bgColor;
        switch (status) {
            case "approved":
                color = Color.parseColor("#1B5E20"); // Xanh lá đậm
                bgColor = Color.parseColor("#E8F5E9"); // Xanh lá nhạt
                break;
            case "rejected":
                color = Color.parseColor("#B71C1C"); // Đỏ đậm
                bgColor = Color.parseColor("#FFEBEE"); // Đỏ nhạt
                break;
            default: // pending hoặc các trạng thái khác
                color = Color.parseColor("#E65100"); // Cam đậm
                bgColor = Color.parseColor("#FFF3E0"); // Cam nhạt
                break;
        }
        tv.setTextColor(color);
        GradientDrawable shape = new GradientDrawable();
        shape.setCornerRadius(20f);
        shape.setColor(bgColor);
        tv.setBackground(shape);
    }

    @Override
    public int getItemCount() {
        return list != null ? list.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvId, tvStatus, tvStore, tvDate;

        public ViewHolder(View itemView) {
            super(itemView);
            // Copy y nguyên 4 dòng này, đảm bảo không bị sai lệch chữ nào
            tvId = itemView.findViewById(R.id.tvCoordinatorOrderId);
            tvStatus = itemView.findViewById(R.id.tvCoordinatorOrderStatus);
            tvStore = itemView.findViewById(R.id.tvCoordinatorOrderStore); // Đã sửa cho khớp
            tvDate = itemView.findViewById(R.id.tvCoordinatorOrderDate);
        }
    }
}