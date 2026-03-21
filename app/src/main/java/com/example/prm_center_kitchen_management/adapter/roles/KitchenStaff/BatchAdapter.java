package com.example.prm_center_kitchen_management.adapter.roles.KitchenStaff;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.prm_center_kitchen_management.R;
import com.example.prm_center_kitchen_management.model.response.Batch;
import com.example.prm_center_kitchen_management.model.response.ExpiryStatus;
import java.util.List;

public class BatchAdapter extends RecyclerView.Adapter<BatchAdapter.ViewHolder> {

    private List<Batch> batchList;
    private OnBatchClickListener listener;

    public interface OnBatchClickListener {
        void onAdjustClick(Batch batch);
    }

    public BatchAdapter(List<Batch> batchList, OnBatchClickListener listener) {
        this.batchList = batchList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_batch_detail, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Batch batch = batchList.get(position);
        holder.tvBatchCode.setText(batch.getBatchCode());
        holder.tvExpDate.setText("Exp: " + batch.getExpDate());
        holder.tvBatchQty.setText(String.format("%.1f", batch.getQuantity()));

        // Expiry Status Indicator Logic
        ExpiryStatus status = batch.getExpiryStatus();
        switch (status) {
            case EXPIRED:
                holder.viewExpiryIndicator.setBackgroundColor(Color.parseColor("#D32F2F")); // Red
                break;
            case WARNING:
                holder.viewExpiryIndicator.setBackgroundColor(Color.parseColor("#FBC02D")); // Yellow
                break;
            case SAFE:
                holder.viewExpiryIndicator.setBackgroundColor(Color.parseColor("#388E3C")); // Green
                break;
        }

        holder.tvAdjustmentLink.setOnClickListener(v -> {
            if (listener != null) {
                listener.onAdjustClick(batch);
            }
        });
    }

    @Override
    public int getItemCount() {
        return batchList != null ? batchList.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvBatchCode, tvExpDate, tvBatchQty, tvAdjustmentLink;
        View viewExpiryIndicator;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvBatchCode = itemView.findViewById(R.id.tvBatchCode);
            tvExpDate = itemView.findViewById(R.id.tvExpDate);
            tvBatchQty = itemView.findViewById(R.id.tvBatchQty);
            tvAdjustmentLink = itemView.findViewById(R.id.tvAdjustmentLink);
            viewExpiryIndicator = itemView.findViewById(R.id.viewExpiryIndicator);
        }
    }
}
