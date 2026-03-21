package com.example.prm_center_kitchen_management.model.response;

import com.google.gson.annotations.SerializedName;

public class WasteReport {
    @SerializedName("batchCode")
    private String batchCode;

    @SerializedName("productName")
    private String productName;

    @SerializedName("quantity")
    private double quantity;

    @SerializedName("reason")
    private String reason;

    @SerializedName("wasteDate")
    private String wasteDate;

    public String getBatchCode() { return batchCode; }
    public String getProductName() { return productName; }
    public double getQuantity() { return quantity; }
    public String getReason() { return reason; }
    public String getWasteDate() { return wasteDate; }
}
