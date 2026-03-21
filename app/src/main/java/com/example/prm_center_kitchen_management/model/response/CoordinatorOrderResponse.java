package com.example.prm_center_kitchen_management.model.response;

import java.util.List;

public class CoordinatorOrderResponse {
    private int statusCode;
    private Data data;

    public class Data {
        private List<OrderItem> items;
        public List<OrderItem> getItems() { return items; }
    }

    public class OrderItem {
        private String id;
        private String status;
        private String createdAt;
        private Store store;

        public String getId() { return id; }
        public String getStatus() { return status; }
        public String getCreatedAt() { return createdAt; }
        public Store getStore() { return store; }
    }

    public class Store {
        private String name;
        public String getName() { return name; }
    }
    public Data getData() { return data; }
}