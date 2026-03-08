package com.example.prm_center_kitchen_management.adapter.roles.FranchiseStaff;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prm_center_kitchen_management.R;
import com.example.prm_center_kitchen_management.model.response.CatalogResponse;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class CatalogAdapter extends RecyclerView.Adapter<CatalogAdapter.ViewHolder> {
    private final List<CatalogResponse.ProductItem> list;

    public CatalogAdapter(List<CatalogResponse.ProductItem> list) {
        this.list = list != null ? list : new ArrayList<>();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_catalog_product, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CatalogResponse.ProductItem item = list.get(position);
        holder.tvName.setText(item.getName());
        holder.tvSku.setText(String.format("SKU: %s", item.getSku()));

        if (item.getImageUrl() != null && !item.getImageUrl().isEmpty()) {
            Picasso.get().load(item.getImageUrl())
                    .placeholder(R.mipmap.ic_launcher)
                    .error(R.mipmap.ic_launcher)
                    .into(holder.ivProduct);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivProduct;
        TextView tvName, tvSku;

        public ViewHolder(View itemView) {
            super(itemView);
            ivProduct = itemView.findViewById(R.id.ivProduct);
            tvName = itemView.findViewById(R.id.tvProductName);
            tvSku = itemView.findViewById(R.id.tvProductSku);
        }
    }
}
