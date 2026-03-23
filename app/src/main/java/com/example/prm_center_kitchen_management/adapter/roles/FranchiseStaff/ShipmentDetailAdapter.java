package com.example.prm_center_kitchen_management.adapter.roles.FranchiseStaff;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.prm_center_kitchen_management.R;
import com.example.prm_center_kitchen_management.model.response.ShipmentDetailResponse;
import com.squareup.picasso.Picasso;
import java.util.List;

public class ShipmentDetailAdapter extends RecyclerView.Adapter<ShipmentDetailAdapter.ViewHolder> {
    private final List<ShipmentDetailResponse.Item> items;

    public ShipmentDetailAdapter(List<ShipmentDetailResponse.Item> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_shipment_detail, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ShipmentDetailResponse.Item item = items.get(position);

        holder.tvProductName.setText(item.getProductName() != null ? item.getProductName() : "Chưa có tên SP");
        holder.tvProductSku.setText("SKU: " + (item.getSku() != null ? item.getSku() : "N/A"));
        holder.tvBatchAndQty.setText("Lô: " + (item.getBatchCode() != null ? item.getBatchCode() : "N/A") + "  |  SL: " + item.getQuantity());
        holder.tvExpiry.setText("HSD: " + (item.getExpiryDate() != null ? item.getExpiryDate() : "N/A"));

        if (item.getImageUrl() != null && !item.getImageUrl().isEmpty()) {
            Picasso.get().load(item.getImageUrl()).placeholder(R.mipmap.ic_launcher).error(R.mipmap.ic_launcher).into(holder.ivImage);
        } else {
            holder.ivImage.setImageResource(R.mipmap.ic_launcher);
        }
    }

    @Override public int getItemCount() { return items != null ? items.size() : 0; }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivImage;
        TextView tvProductName, tvProductSku, tvBatchAndQty, tvExpiry;

        public ViewHolder(View itemView) {
            super(itemView);
            ivImage = itemView.findViewById(R.id.ivProductImage);
            tvProductName = itemView.findViewById(R.id.tvProductName);
            tvProductSku = itemView.findViewById(R.id.tvProductSku);
            tvBatchAndQty = itemView.findViewById(R.id.tvBatchAndQty);
            tvExpiry = itemView.findViewById(R.id.tvExpiry);
        }
    }
}