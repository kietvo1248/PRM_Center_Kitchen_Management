package com.example.prm_center_kitchen_management.model.response;

import com.google.gson.annotations.SerializedName;
public class AddReceiptItemResponse {
    @SerializedName("batchId")
    private int batchId;

    @SerializedName("batchCode")
    private String batchCode;

    @SerializedName("manufactureDate")
    private String manufactureDate;

    @SerializedName("expiryDate")
    private String expiryDate;

    @SerializedName("warning")
    private String warning;

    public int getBatchId() { return batchId; }
    public String getBatchCode() { return batchCode; }
    public String getManufactureDate() { return manufactureDate; }
    public String getExpiryDate() { return expiryDate; }
    public String getWarning() { return warning; }
}
