package com.example.prm_center_kitchen_management.adapter.roles.KitchenStaff;

import android.view.LayoutInflater;
import android.view.View;
import java.util.List;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.prm_center_kitchen_management.R;
import com.example.prm_center_kitchen_management.model.response.InboundDetailResponse;
public class InboundDetailItemAdapter extends RecyclerView.Adapter<InboundDetailItemAdapter.ViewHolder>{
    private List<InboundDetailResponse.Item> items;

    public InboundDetailItemAdapter(List<InboundDetailResponse.Item> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_inbound_detail, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        InboundDetailResponse.Item item = items.get(position);
        if (item.getBatch() != null && item.getBatch().getProduct() != null) {
            holder.tvProductName.setText(item.getBatch().getProduct().getName());
            holder.tvSku.setText("SKU: " + item.getBatch().getProduct().getSku());
            holder.tvBatchCode.setText("Lô: " + item.getBatch().getBatchCode());
            holder.tvQuantity.setText("SL: " + item.getQuantity() + " " + item.getBatch().getProduct().getUnit());
            holder.tvExpiryDate.setText("HSD: " + item.getBatch().getExpiryDate());
        }
    }

    @Override
    public int getItemCount() { return items != null ? items.size() : 0; }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvProductName, tvSku, tvBatchCode, tvQuantity, tvExpiryDate;
        public ViewHolder(View itemView) {
            super(itemView);
            tvProductName = itemView.findViewById(R.id.tvProductName);
            tvSku = itemView.findViewById(R.id.tvSku);
            tvBatchCode = itemView.findViewById(R.id.tvBatchCode);
            tvQuantity = itemView.findViewById(R.id.tvQuantity);
            tvExpiryDate = itemView.findViewById(R.id.tvExpiryDate);
        }
    }

}
