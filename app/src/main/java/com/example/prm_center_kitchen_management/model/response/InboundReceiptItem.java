package com.example.prm_center_kitchen_management.model.response;

import com.google.gson.annotations.SerializedName;

public class InboundReceiptItem {
    @SerializedName("productId")
    private int productId;
    
    @SerializedName("productName")
    private String productName;
    
    @SerializedName("quantity")
    private int quantity;
    
    @SerializedName("batchCode")
    private String batchCode;

    public int getProductId() { return productId; }
    public String getProductName() { return productName; }
    public int getQuantity() { return quantity; }
    public String getBatchCode() { return batchCode; }
}
