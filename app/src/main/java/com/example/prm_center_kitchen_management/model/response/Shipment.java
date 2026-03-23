package com.example.prm_center_kitchen_management.model.response;

import com.google.gson.annotations.SerializedName;

public class Shipment {
    @SerializedName("id")
    private String id;

    @SerializedName("orderId")
    private String orderId;

    @SerializedName("storeName")
    private String storeName;

    @SerializedName("status")
    private String status;

    @SerializedName("shipDate")
    private String shipDate;

    @SerializedName("createdAt")
    private String createdAt;

    // Getters
    public String getId() { return id; }
    public String getOrderId() { return orderId; }
    public String getStoreName() { return storeName; }
    public String getStatus() { return status; }
    public String getShipDate() { return shipDate; }
    public String getCreatedAt() { return createdAt; }
}