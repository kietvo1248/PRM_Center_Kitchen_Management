package com.example.prm_center_kitchen_management.model.response;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class InventoryItem {
    @SerializedName("productId")
    private Integer productId;

    @SerializedName("productName")
    private String productName;

    @SerializedName("sku")
    private String sku;

    @SerializedName("totalQty")
    private Double totalQty;

    @SerializedName("batches")
    private List<Batch> batches;

    // Getters
    public Integer getProductId() { return productId; }
    public String getProductName() { return productName; }
    public String getSku() { return sku; }
    public Double getTotalQty() { return totalQty; }
    public List<Batch> getBatches() { return batches; }
}
