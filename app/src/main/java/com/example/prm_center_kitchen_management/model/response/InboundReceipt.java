package com.example.prm_center_kitchen_management.model.response;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class InboundReceipt {
    @SerializedName("id")
    private String id;

    @SerializedName("receiptCode")
    private String receiptCode;

    @SerializedName("supplierId")
    private int supplierId;

    @SerializedName("status")
    private String status; // draft, pending, completed

    @SerializedName("items")
    private List<InboundReceiptItem> items;

    @SerializedName("createdAt")
    private String createdAt;

    // Getters
    public String getId() { return id; }
    public String getReceiptCode() { return receiptCode; }
    public int getSupplierId() { return supplierId; }
    public String getStatus() { return status; }
    public List<InboundReceiptItem> getItems() { return items; }
    public String getCreatedAt() { return createdAt; }
}
