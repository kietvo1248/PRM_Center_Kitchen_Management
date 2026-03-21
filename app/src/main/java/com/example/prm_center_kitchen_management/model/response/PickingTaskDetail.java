package com.example.prm_center_kitchen_management.model.response;

import com.google.gson.annotations.SerializedName;
import java.util.List;

/**
 * GET /warehouse/picking-tasks/{id} — data: orderId, shipmentId, items (FEFO suggestedBatches).
 */
public class PickingTaskDetail {

    @SerializedName("orderId")
    private String orderId;

    @SerializedName("shipmentId")
    private String shipmentId;

    @SerializedName("items")
    private List<PickingTaskItem> items;

    public String getOrderId() { return orderId; }
    public String getShipmentId() { return shipmentId; }
    public List<PickingTaskItem> getItems() { return items; }
}
