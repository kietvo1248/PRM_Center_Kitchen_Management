package com.example.prm_center_kitchen_management.adapter.roles.KitchenStaff;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.prm_center_kitchen_management.R;
import com.example.prm_center_kitchen_management.model.response.KitchenInventoryItem;
import java.util.List;

public class KitchenInventoryAdapter extends RecyclerView.Adapter<KitchenInventoryAdapter.ViewHolder> {

    private List<KitchenInventoryItem> inventoryItems;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onCheckBatches(KitchenInventoryItem item);
    }

    public KitchenInventoryAdapter(List<KitchenInventoryItem> inventoryItems, OnItemClickListener listener) {
        this.inventoryItems = inventoryItems;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_kitchen_inventory, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        KitchenInventoryItem item = inventoryItems.get(position);
        holder.tvProductName.setText(item.getProductName());
        holder.tvSku.setText("SKU: " + item.getSku());
        holder.tvTotalQty.setText(String.format("%.1f %s", item.getTotalQuantity(), item.getUnit()));

        holder.btnViewBatches.setOnClickListener(v -> {
            if (listener != null) {
                listener.onCheckBatches(item);
            }
        });
    }

    @Override
    public int getItemCount() {
        return inventoryItems != null ? inventoryItems.size() : 0;
    }

    public void updateData(List<KitchenInventoryItem> newList) {
        this.inventoryItems = newList;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvProductName, tvSku, tvTotalQty;
        Button btnViewBatches;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvProductName = itemView.findViewById(R.id.tvProductName);
            tvSku = itemView.findViewById(R.id.tvSku);
            tvTotalQty = itemView.findViewById(R.id.tvTotalQty);
            btnViewBatches = itemView.findViewById(R.id.btnViewBatches);
        }
    }
}
