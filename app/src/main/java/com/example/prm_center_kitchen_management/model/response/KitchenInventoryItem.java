package com.example.prm_center_kitchen_management.model.response;

import com.google.gson.annotations.SerializedName;

public class KitchenInventoryItem {
    @SerializedName("productId")
    private int productId;
    
    @SerializedName("productName")
    private String productName;
    
    @SerializedName("sku")
    private String sku;
    
    @SerializedName("totalQuantity")
    private double totalQuantity;
    
    @SerializedName("unit")
    private String unit;

    // Getters
    public int getProductId() { return productId; }
    public String getProductName() { return productName; }
    public String getSku() { return sku; }
    public double getTotalQuantity() { return totalQuantity; }
    public String getUnit() { return unit; }
}
