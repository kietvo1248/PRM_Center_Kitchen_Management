package com.example.prm_center_kitchen_management.model.request;

import com.google.gson.annotations.SerializedName;
import java.util.List;

/**
 * PATCH /warehouse/shipments/finalize-bulk — chốt xuất kho theo đơn đã soạn.
 */
public class FinalizeBulkRequest {

    @SerializedName("orders")
    private List<OrderEntry> orders;

    public FinalizeBulkRequest(List<OrderEntry> orders) {
        this.orders = orders;
    }

    public List<OrderEntry> getOrders() { return orders; }

    public static class OrderEntry {
        @SerializedName("orderId")
        private String orderId;

        @SerializedName("pickedItems")
        private List<PickedLine> pickedItems;

        public OrderEntry(String orderId, List<PickedLine> pickedItems) {
            this.orderId = orderId;
            this.pickedItems = pickedItems;
        }
    }

    public static class PickedLine {
        @SerializedName("batchId")
        private int batchId;

        @SerializedName("quantity")
        private double quantity;

        public PickedLine(int batchId, double quantity) {
            this.batchId = batchId;
            this.quantity = quantity;
        }
    }
}
