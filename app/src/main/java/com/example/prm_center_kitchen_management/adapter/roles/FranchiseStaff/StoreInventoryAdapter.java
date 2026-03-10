package com.example.prm_center_kitchen_management.adapter.roles.FranchiseStaff;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prm_center_kitchen_management.R;
import com.example.prm_center_kitchen_management.model.response.InventoryResponse;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class StoreInventoryAdapter extends RecyclerView.Adapter<StoreInventoryAdapter.ViewHolder>{
    private final List<InventoryResponse.InventoryItem> list;

    public StoreInventoryAdapter(List<InventoryResponse.InventoryItem> list) {
        this.list = list != null ? list : new ArrayList<>();
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_inventory, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        InventoryResponse.InventoryItem item = list.get(position);
        holder.tvProductName.setText(item.getProductName() + " (SKU: " + item.getSku() + ")");
        holder.tvBatchCode.setText("Lô: " + item.getBatchCode());
        holder.tvQuantity.setText("Tồn kho: " + item.getQuantity() + " " + item.getUnit());

        String expiry = item.getExpiryDate() != null && item.getExpiryDate().length() >= 10
                ? item.getExpiryDate().substring(0, 10) : "N/A";
        holder.tvExpiry.setText("HSD: " + expiry);

        if (item.getImageUrl() != null && !item.getImageUrl().isEmpty()) {
            Picasso.get().load(item.getImageUrl())
                    .placeholder(R.mipmap.ic_launcher)
                    .error(R.mipmap.ic_launcher)
                    .into(holder.ivProduct);
        } else {
            holder.ivProduct.setImageResource(R.mipmap.ic_launcher);
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivProduct;
        TextView tvProductName, tvBatchCode, tvQuantity, tvExpiry;

        public ViewHolder(View itemView) {
            super(itemView);
            ivProduct = itemView.findViewById(R.id.ivProduct);
            tvProductName = itemView.findViewById(R.id.tvProductName);
            tvBatchCode = itemView.findViewById(R.id.tvBatchCode);
            tvQuantity = itemView.findViewById(R.id.tvQuantity);
            tvExpiry = itemView.findViewById(R.id.tvExpiry);
        }
    }
}
