package com.example.prm_center_kitchen_management.model.response;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class ShipmentDetailResponse {
    @SerializedName("id")
    private String id;

    @SerializedName("orderId")
    private String orderId;

    @SerializedName("status")
    private String status;
    private String createdAt;
    private OrderInfo order;
    private List<Item> items;

    // CÁC HÀM GETTER ĐỂ FRAGMENT GỌI
    public String getId() { return id; }
    public String getOrderId() { return orderId; }
    public String getStatus() { return status; }
    public String getCreatedAt() { return createdAt; }
    public OrderInfo getOrder() { return order; }
    public List<Item> getItems() { return items; }

    public static class OrderInfo {
        private String id;
        private String storeId;
        private String storeName;
        public String getId() { return id; }
        public String getStoreId() { return storeId; }
        public String getStoreName() { return storeName; }
    }

    public static class Item {
        private int batchId;
        private String batchCode;
        private int productId;
        private String productName;
        private String sku;
        private int quantity;
        private String expiryDate;
        private String imageUrl;

        public int getBatchId() { return batchId; }
        public String getBatchCode() { return batchCode; }
        public int getProductId() { return productId; }
        public String getProductName() { return productName; }
        public String getSku() { return sku; }
        public int getQuantity() { return quantity; }
        public String getExpiryDate() { return expiryDate; }
        public String getImageUrl() { return imageUrl; }
    }
}