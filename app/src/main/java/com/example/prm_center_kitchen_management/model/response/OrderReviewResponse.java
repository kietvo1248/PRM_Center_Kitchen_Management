package com.example.prm_center_kitchen_management.model.response;

import java.util.List;

public class OrderReviewResponse {
    private int statusCode;
    private String message;
    private ReviewData data;

    public int getStatusCode() { return statusCode; }
    public String getMessage() { return message; }
    public ReviewData getData() { return data; }

    public static class ReviewData {
        private String orderId;
        private String storeName;
        private String status;
        private List<ReviewItem> items;

        public String getOrderId() { return orderId; }
        public String getStoreName() { return storeName; }
        public String getStatus() { return status; }
        public List<ReviewItem> getItems() { return items; }
    }

    public static class ReviewItem {
        private int productId;
        private String productName;
        private int requestedQty;
        private int currentStock;
        private boolean canFulfill;

        public int getProductId() { return productId; }
        public String getProductName() { return productName; }
        public int getRequestedQty() { return requestedQty; }
        public int getCurrentStock() { return currentStock; }
        public boolean canFulfill() { return canFulfill; }
    }
}