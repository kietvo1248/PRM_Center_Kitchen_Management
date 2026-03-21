package com.example.prm_center_kitchen_management.model.request;

import com.google.gson.annotations.SerializedName;

public class InventoryAdjustmentRequest {
    @SerializedName("batchCode")
    private String batchCode;
    
    @SerializedName("adjustmentQty")
    private double adjustmentQty;
    
    @SerializedName("reason")
    private String reason; // e.g., "damaged", "expired"

    public InventoryAdjustmentRequest(String batchCode, double adjustmentQty, String reason) {
        this.batchCode = batchCode;
        this.adjustmentQty = adjustmentQty;
        this.reason = reason;
    }
}
