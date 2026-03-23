package com.example.prm_center_kitchen_management.model.response;

import java.util.List;

public class ShipmentResponse {
    private List<ShipmentItem> items;
    public List<ShipmentItem> getItems() { return items; }

    public static class ShipmentItem {
        private String id;
        private String orderId;
        private String status; // pending, preparing, in_transit, completed, cancelled
        private String createdAt;
        private String toStoreId;

        public String getId() { return id; }
        public String getOrderId() { return orderId; }
        public String getStatus() { return status; }
        public String getCreatedAt() { return createdAt; }
        public String getToStoreId() { return toStoreId; }
    }
}