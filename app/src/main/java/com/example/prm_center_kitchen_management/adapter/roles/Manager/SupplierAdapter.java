package com.example.prm_center_kitchen_management.adapter.roles.Manager;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.prm_center_kitchen_management.R;
import com.example.prm_center_kitchen_management.model.response.Supplier;
import java.util.ArrayList;
import java.util.List;
public class SupplierAdapter extends RecyclerView.Adapter<SupplierAdapter.ViewHolder> {
    private List<Supplier> list = new ArrayList<>();
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Supplier supplier);
    }

    public void setSuppliers(List<Supplier> suppliers, OnItemClickListener listener) {
        this.list = suppliers;
        this.listener = listener;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_supplier, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Supplier supplier = list.get(position);
        holder.tvName.setText(supplier.getName());
        holder.tvContact.setText(supplier.getContactName() + " - " + supplier.getPhone());

        if (supplier.isActive()) {
            holder.tvStatus.setText("Đang hoạt động");
            holder.tvStatus.setTextColor(Color.parseColor("#388E3C")); // Xanh lá
        } else {
            holder.tvStatus.setText("Ngừng hoạt động");
            holder.tvStatus.setTextColor(Color.parseColor("#D32F2F")); // Đỏ
        }

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) listener.onItemClick(supplier);
        });
    }

    @Override
    public int getItemCount() { return list != null ? list.size() : 0; }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvContact, tvStatus;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvSupplierName);
            tvContact = itemView.findViewById(R.id.tvSupplierContact);
            tvStatus = itemView.findViewById(R.id.tvSupplierStatus);
        }
    }
}
