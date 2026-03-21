package com.example.prm_center_kitchen_management.model.response;

import com.google.gson.annotations.SerializedName;

public class SuggestedBatch {
    @SerializedName("batchId")
    private Integer batchId;

    @SerializedName("batchCode")
    private String batchCode;

    /** API sample dùng qtyToPick */
    @SerializedName(value = "qtyToPick", alternate = {"quantityToPick"})
    private double qtyToPick;

    @SerializedName(value = "expiry", alternate = {"expDate"})
    private String expiry;

    public Integer getBatchId() { return batchId; }
    public String getBatchCode() { return batchCode; }
    public double getQuantityToPick() { return qtyToPick; }
    public String getExpDate() { return expiry; }
}
