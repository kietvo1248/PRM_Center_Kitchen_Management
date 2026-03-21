package com.example.prm_center_kitchen_management.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.prm_center_kitchen_management.R;
import com.example.prm_center_kitchen_management.model.response.PickingTaskItem;
import com.example.prm_center_kitchen_management.model.response.SuggestedBatch;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Hiển thị chi tiết soạn hàng (FEFO) — không còn luồng quét QR.
 */
public class PickingItemAdapter extends RecyclerView.Adapter<PickingItemAdapter.ViewHolder> {

    private final List<PickingTaskItem> itemList;

    public PickingItemAdapter(List<PickingTaskItem> itemList) {
        this.itemList = itemList != null ? itemList : new ArrayList<>();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_picking_detail_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PickingTaskItem item = itemList.get(position);
        holder.tvProductName.setText(item.getProductName());
        holder.tvQtyRequested.setText(String.format(Locale.getDefault(), "Cần soạn: %.1f", item.getQuantityRequested()));

        StringBuilder suggestions = new StringBuilder("Gợi ý FEFO (hết hạn trước):\n");
        List<SuggestedBatch> batches = item.getSuggestedBatches();
        if (batches == null || batches.isEmpty()) {
            suggestions.append("(Chưa có lô gợi ý)");
        } else {
            for (SuggestedBatch sb : batches) {
                if (sb == null) continue;
                String exp = sb.getExpDate() != null ? sb.getExpDate() : "—";
                suggestions.append("• ").append(sb.getBatchCode())
                        .append(" — ").append(String.format(Locale.getDefault(), "%.1f", sb.getQuantityToPick()))
                        .append(" (EXP ").append(exp).append(")\n");
            }
        }
        holder.tvSuggestedBatches.setText(suggestions.toString().trim());
        holder.tvStatus.setText("FEFO");
        holder.tvStatus.setTextColor(Color.parseColor("#1565C0"));
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public List<PickingTaskItem> getItemList() {
        return itemList;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvProductName, tvQtyRequested, tvSuggestedBatches, tvStatus;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvProductName = itemView.findViewById(R.id.tvProductName);
            tvQtyRequested = itemView.findViewById(R.id.tvQtyRequested);
            tvSuggestedBatches = itemView.findViewById(R.id.tvSuggestedBatches);
            tvStatus = itemView.findViewById(R.id.tvStatus);
        }
    }
}
