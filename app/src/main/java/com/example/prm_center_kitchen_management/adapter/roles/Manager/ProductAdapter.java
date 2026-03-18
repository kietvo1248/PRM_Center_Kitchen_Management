package com.example.prm_center_kitchen_management.adapter.roles.Manager;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.prm_center_kitchen_management.R;
import com.example.prm_center_kitchen_management.model.response.Product;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import java.util.List;
public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {
    private List<Product> list = new ArrayList<>();
    private OnItemClickListener listener;

    public interface OnItemClickListener { void onItemClick(Product product); }

    public void setProducts(List<Product> products, OnItemClickListener listener) {
        this.list = products;
        this.listener = listener;
        notifyDataSetChanged();
    }

    @NonNull @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Product p = list.get(position);
        holder.tvName.setText(p.getName());
        holder.tvSku.setText("SKU: " + p.getSku());
        holder.tvUnit.setText("ĐVT: " + p.getBaseUnitName() + " | HSD: " + p.getShelfLifeDays() + " ngày");

        holder.tvStatus.setText(p.isActive() ? "Hoạt động" : "Ngừng bán");
        holder.tvStatus.setTextColor(p.isActive() ? Color.parseColor("#388E3C") : Color.RED);

        if (p.getImageUrl() != null && !p.getImageUrl().isEmpty()) {
            Picasso.get().load(p.getImageUrl()).placeholder(R.mipmap.ic_launcher).into(holder.ivImage);
        } else {
            holder.ivImage.setImageResource(R.mipmap.ic_launcher);
        }

        holder.itemView.setOnClickListener(v -> { if (listener != null) listener.onItemClick(p); });
    }

    @Override public int getItemCount() { return list != null ? list.size() : 0; }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivImage; TextView tvName, tvSku, tvUnit, tvStatus;
        public ViewHolder(View itemView) {
            super(itemView);
            ivImage = itemView.findViewById(R.id.ivProductImage);
            tvName = itemView.findViewById(R.id.tvProductName);
            tvSku = itemView.findViewById(R.id.tvProductSku);
            tvUnit = itemView.findViewById(R.id.tvProductUnit);
            tvStatus = itemView.findViewById(R.id.tvProductStatus);
        }
    }

}
