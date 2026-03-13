package com.example.prm_center_kitchen_management.adapter.roles.Manager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.prm_center_kitchen_management.R;
import com.example.prm_center_kitchen_management.model.response.StoreResponse;
import java.util.ArrayList;
import java.util.List;
public class StoreAdapter extends RecyclerView.Adapter<StoreAdapter.ViewHolder>{
    private final List<StoreResponse.StoreItem> list;
    private final OnItemClickListener listener;

    public interface OnItemClickListener { void onItemClick(String storeId); }

    public StoreAdapter(List<StoreResponse.StoreItem> list, OnItemClickListener listener) {
        this.list = list != null ? list : new ArrayList<>();
        this.listener = listener;
    }

    @NonNull @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_store, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        StoreResponse.StoreItem item = list.get(position);
        holder.tvName.setText(item.getName());
        holder.tvAddress.setText("ĐC: " + item.getAddress());
        holder.tvPhone.setText("SĐT: " + (item.getPhone() != null ? item.getPhone() : "N/A"));

        String manager = item.getManagerName() != null ? item.getManagerName() : "Vô Chủ";
        holder.tvManager.setText("Quản lý: " + manager);

        if (item.isActive()) {
            holder.tvStatus.setText("ĐANG HOẠT ĐỘNG");
            holder.tvStatus.setTextColor(0xFF388E3C); // Xanh
        } else {
            holder.tvStatus.setText("NGƯNG HOẠT ĐỘNG");
            holder.tvStatus.setTextColor(0xFFD32F2F); // Đỏ
        }

        holder.itemView.setOnClickListener(v -> {
            if(listener != null) listener.onItemClick(item.getId());
        });
    }

    @Override public int getItemCount() { return list.size(); }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvManager, tvAddress, tvPhone, tvStatus;
        public ViewHolder(View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvStoreName);
            tvManager = itemView.findViewById(R.id.tvManager);
            tvAddress = itemView.findViewById(R.id.tvAddress);
            tvPhone = itemView.findViewById(R.id.tvPhone);
            tvStatus = itemView.findViewById(R.id.tvStatus);
        }
    }
}
