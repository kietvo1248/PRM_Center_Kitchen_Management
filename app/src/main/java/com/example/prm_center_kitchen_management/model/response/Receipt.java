package com.example.prm_center_kitchen_management.model.response;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class Receipt {
    @SerializedName("id")
    private String id;

    @SerializedName("receiptCode")
    private String receiptCode;

    @SerializedName("supplierId")
    private Integer supplierId;

    @SerializedName("status")
    private String status; // draft, pending, completed

    @SerializedName("items")
    private List<ReceiptItem> items;

    // Getters
    public String getId() { return id; }
    public String getReceiptCode() { return receiptCode; }
    public Integer getSupplierId() { return supplierId; }
    public String getStatus() { return status; }
    public List<ReceiptItem> getItems() { return items; }

    public static class ReceiptItem {
        @SerializedName("productId")
        private Integer productId;
        
        @SerializedName("quantity")
        private Double quantity;
        
        @SerializedName("batchCode")
        private String batchCode;

        public Integer getProductId() { return productId; }
        public Double getQuantity() { return quantity; }
        public String getBatchCode() { return batchCode; }
    }
}
