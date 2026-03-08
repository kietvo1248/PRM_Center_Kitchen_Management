package com.example.prm_center_kitchen_management.model.response;

import java.util.List;

public class ShipmentResponse {
    private int statusCode;
    private String message;
    private ShipmentData data;

    public int getStatusCode() { return statusCode; }
    public String getMessage() { return message; }
    public ShipmentData getData() { return data; }

    public static class ShipmentData {
        private List<ShipmentItem> items;
        public List<ShipmentItem> getItems() { return items; }
    }

    public static class ShipmentItem {
        private String id;
        private String orderId;
        private String storeName;
        private String status;
        private String shipDate;
        private String createdAt;

        public String getId() { return id; }
        public String getOrderId() { return orderId; }
        public String getStoreName() { return storeName; }
        public String getStatus() { return status; }
        public String getShipDate() { return shipDate; }
        public String getCreatedAt() { return createdAt; }
    }
}
