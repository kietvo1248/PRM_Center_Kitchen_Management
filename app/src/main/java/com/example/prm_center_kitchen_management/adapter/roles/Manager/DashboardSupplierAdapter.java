package com.example.prm_center_kitchen_management.adapter.roles.Manager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.prm_center_kitchen_management.R;
import com.example.prm_center_kitchen_management.model.response.DashboardSupplierResponse;
import java.util.ArrayList;
import java.util.List;

public class DashboardSupplierAdapter extends RecyclerView.Adapter<DashboardSupplierAdapter.ViewHolder>{

    private final List<DashboardSupplierResponse.SupplierItem> list;

    public DashboardSupplierAdapter(List<DashboardSupplierResponse.SupplierItem> list) {
        this.list = list != null ? list : new ArrayList<>();
    }

    @NonNull @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_dashboard_supplier, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DashboardSupplierResponse.SupplierItem item = list.get(position);
        holder.tvName.setText(item.getName());
        holder.tvContact.setText("Liên hệ: " + (item.getContactName() != null ? item.getContactName() : "N/A"));
        holder.tvPhone.setText("SĐT: " + (item.getPhone() != null ? item.getPhone() : "N/A"));
    }

    @Override public int getItemCount() { return list.size(); }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvContact, tvPhone;
        public ViewHolder(View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvSupplierName);
            tvContact = itemView.findViewById(R.id.tvContact);
            tvPhone = itemView.findViewById(R.id.tvPhone);
        }
    }

}
