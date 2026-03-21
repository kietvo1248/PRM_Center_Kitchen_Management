package com.example.prm_center_kitchen_management.adapter.roles.KitchenStaff;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.prm_center_kitchen_management.R;
import com.example.prm_center_kitchen_management.model.response.WasteReport;
import java.util.List;

public class WasteReportAdapter extends RecyclerView.Adapter<WasteReportAdapter.ViewHolder> {

    private List<WasteReport> reportList;

    public WasteReportAdapter(List<WasteReport> reportList) {
        this.reportList = reportList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_waste_report, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        WasteReport report = reportList.get(position);
        holder.tvProductName.setText(report.getProductName());
        holder.tvBatchInfo.setText("Batch: " + report.getBatchCode() + " | Reason: " + report.getReason());
        holder.tvQty.setText("-" + report.getQuantity() + " kg");
    }

    @Override
    public int getItemCount() {
        return reportList != null ? reportList.size() : 0;
    }

    public void updateData(List<WasteReport> newList) {
        if (this.reportList != null) {
            this.reportList.clear();
            if (newList != null) {
                this.reportList.addAll(newList);
            }
        } else {
            this.reportList = newList;
        }
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvProductName, tvBatchInfo, tvQty;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvProductName = itemView.findViewById(R.id.tvProductName);
            tvBatchInfo = itemView.findViewById(R.id.tvBatchInfo);
            tvQty = itemView.findViewById(R.id.tvQty);
        }
    }
}
