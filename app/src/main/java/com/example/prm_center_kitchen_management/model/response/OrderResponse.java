package com.example.prm_center_kitchen_management.model.response;

import java.util.List;

public class OrderResponse {
    private int statusCode;
    private OrderData data;
    public int getStatusCode() { return statusCode; }
    public OrderData getData() { return data; }

    public class OrderData {
        private List<OrderItem> items;
        public List<OrderItem> getItems() { return items; }
    }

    public class OrderItem {
        private String id;
        private String status;
        private String deliveryDate;
        private String createdAt;
        public String getId() { return id; }
        public String getStatus() { return status; }
        public String getDeliveryDate() { return deliveryDate; }
        public String getCreatedAt() { return createdAt; }
    }

}
