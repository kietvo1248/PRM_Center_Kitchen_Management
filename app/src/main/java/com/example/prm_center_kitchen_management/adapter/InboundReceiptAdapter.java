package com.example.prm_center_kitchen_management.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.prm_center_kitchen_management.R;
import com.example.prm_center_kitchen_management.model.response.InboundReceipt;
import java.util.List;

public class InboundReceiptAdapter extends RecyclerView.Adapter<InboundReceiptAdapter.ViewHolder> {

    private List<InboundReceipt> receiptList;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(InboundReceipt receipt);
    }

    public InboundReceiptAdapter(List<InboundReceipt> receiptList, OnItemClickListener listener) {
        this.receiptList = receiptList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_inbound_receipt, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        InboundReceipt receipt = receiptList.get(position);
        holder.tvReceiptCode.setText(receipt.getReceiptCode());
        holder.tvSupplier.setText("Supplier ID: #" + receipt.getSupplierId());
        holder.tvStatus.setText(receipt.getStatus());
        
        int itemCount = receipt.getItems() != null ? receipt.getItems().size() : 0;
        holder.tvItemCount.setText(String.format("Dòng hàng: %d", itemCount));

        if (holder.tvDate != null) {
            String created = receipt.getCreatedAt();
            holder.tvDate.setText(created != null ? "Tạo: " + created : "");
        }

        String st = receipt.getStatus() != null ? receipt.getStatus() : "";
        if ("completed".equalsIgnoreCase(st)) {
            holder.tvStatus.setBackgroundResource(R.drawable.bg_status_completed);
        } else if ("draft".equalsIgnoreCase(st)) {
            holder.tvStatus.setBackgroundResource(R.drawable.bg_status_draft);
        } else if ("cancelled".equalsIgnoreCase(st)) {
            holder.tvStatus.setBackgroundResource(R.drawable.bg_status_draft);
        } else {
            holder.tvStatus.setBackgroundResource(R.drawable.bg_status_pending);
        }

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(receipt);
            }
        });
    }

    @Override
    public int getItemCount() {
        return receiptList != null ? receiptList.size() : 0;
    }

    public void updateData(List<InboundReceipt> newList) {
        this.receiptList = newList;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvReceiptCode, tvStatus, tvSupplier, tvItemCount, tvDate;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvReceiptCode = itemView.findViewById(R.id.tvReceiptCode);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            tvSupplier = itemView.findViewById(R.id.tvSupplier);
            tvItemCount = itemView.findViewById(R.id.tvItemCount);
            tvDate = itemView.findViewById(R.id.tvDate);
        }
    }
}
