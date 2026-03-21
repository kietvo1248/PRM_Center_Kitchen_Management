package com.example.prm_center_kitchen_management.model.response;

import java.util.List;

public class CoordinatorShipmentResponse {
    private int statusCode;
    private Data data;

    public class Data {
        private List<ShipmentItem> items;
        public List<ShipmentItem> getItems() { return items; }
    }

    public class ShipmentItem {
        private String id;
        private String storeName;
        private String status;
        private String createdAt;

        public String getId() { return id; }
        public String getStoreName() { return storeName; }
        public String getStatus() { return status; }
        public String getCreatedAt() { return createdAt; }
    }
    public Data getData() { return data; }
    public int getStatusCode() { return statusCode; }
}
