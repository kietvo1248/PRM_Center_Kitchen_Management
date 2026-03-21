package com.example.prm_center_kitchen_management.model.response;

import com.google.gson.annotations.SerializedName;

public class Shipment {
    @SerializedName("id")
    private String id;

    @SerializedName("shipmentCode")
    private String shipmentCode;

    @SerializedName("status")
    private String status; // preparing, shipped, received, claimed

    @SerializedName("toStoreName")
    private String toStoreName;

    @SerializedName("shippedDate")
    private String shippedDate;

    @SerializedName("fromWarehouseId")
    private Integer fromWarehouseId;

    @SerializedName("toStoreId")
    private Integer toStoreId;

    // Getters
    public String getId() { return id; }
    public String getShipmentCode() { return shipmentCode; }
    public String getStatus() { return status; }
    public String getToStoreName() { return toStoreName; }
    public String getShippedDate() { return shippedDate; }
    public Integer getFromWarehouseId() { return fromWarehouseId; }
    public Integer getToStoreId() { return toStoreId; }
}
