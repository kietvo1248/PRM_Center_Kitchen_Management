package com.example.prm_center_kitchen_management.adapter.roles.KitchenStaff;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.prm_center_kitchen_management.R;
import com.example.prm_center_kitchen_management.model.response.Receipt;
import java.util.List;

public class ReceiptAdapter extends RecyclerView.Adapter<ReceiptAdapter.ViewHolder> {

    private List<Receipt> receiptList;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Receipt receipt);
    }

    public ReceiptAdapter(List<Receipt> receiptList, OnItemClickListener listener) {
        this.receiptList = receiptList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_receipt, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Receipt receipt = receiptList.get(position);
        holder.tvReceiptCode.setText(receipt.getReceiptCode());
        holder.tvSupplierId.setText("Supplier ID: #" + receipt.getSupplierId());
        holder.tvStatus.setText(receipt.getStatus());
        
        int itemCount = receipt.getItems() != null ? receipt.getItems().size() : 0;
        holder.tvItemCount.setText("Items: " + itemCount + " products");

        // Simple status styling
        if ("completed".equalsIgnoreCase(receipt.getStatus())) {
            holder.tvStatus.setBackgroundResource(R.drawable.bg_status_completed);
        } else if ("draft".equalsIgnoreCase(receipt.getStatus())) {
            holder.tvStatus.setBackgroundResource(R.drawable.bg_status_draft);
        } else {
            holder.tvStatus.setBackgroundResource(R.drawable.bg_status_pending);
        }

        holder.btnDetail.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(receipt);
            }
        });
    }

    @Override
    public int getItemCount() {
        return receiptList != null ? receiptList.size() : 0;
    }

    public void updateData(List<Receipt> newList) {
        this.receiptList = newList;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvReceiptCode, tvStatus, tvSupplierId, tvItemCount;
        Button btnDetail;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvReceiptCode = itemView.findViewById(R.id.tvReceiptCode);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            tvSupplierId = itemView.findViewById(R.id.tvSupplierId);
            tvItemCount = itemView.findViewById(R.id.tvItemCount);
            btnDetail = itemView.findViewById(R.id.btnDetail);
        }
    }
}
