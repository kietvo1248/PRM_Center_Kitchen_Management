package com.example.prm_center_kitchen_management.model.response;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class PickingTask {
    @SerializedName("id")
    private String id;

    /** Mã đơn hiển thị (ví dụ ORD123) — ưu tiên khi bind UI */
    @SerializedName("orderCode")
    private String orderCode;

    @SerializedName("orderId")
    private String orderId;

    @SerializedName("shipmentId")
    private String shipmentId;

    @SerializedName("status")
    private String status;

    @SerializedName("items")
    private List<PickingTaskItem> items;

    public String getId() { return id; }
    public String getOrderCode() { return orderCode; }
    public String getOrderId() { return orderId; }
    public String getShipmentId() { return shipmentId; }
    public String getStatus() { return status; }
    public List<PickingTaskItem> getItems() { return items; }
}
