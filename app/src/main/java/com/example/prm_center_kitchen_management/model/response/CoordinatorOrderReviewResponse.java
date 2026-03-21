package com.example.prm_center_kitchen_management.model.response;

import java.util.List;

public class CoordinatorOrderReviewResponse {
    private Data data;
    public class Data {
        private String storeName;
        private List<ReviewItem> items;
        public List<ReviewItem> getItems() { return items; }
        public String getStoreName() { return storeName; }
    }
    public class ReviewItem {
        private String productName;
        private int requestedQty;
        private int currentStock;
        private boolean canFulfill;

        public String getProductName() { return productName; }
        public int getRequestedQty() { return requestedQty; }
        public int getCurrentStock() { return currentStock; }
        public boolean isCanFulfill() { return canFulfill; }
    }
    public Data getData() { return data; }
}