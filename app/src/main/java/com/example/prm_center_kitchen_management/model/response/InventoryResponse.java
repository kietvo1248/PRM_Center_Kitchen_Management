package com.example.prm_center_kitchen_management.model.response;

import java.util.List;
public class InventoryResponse {
    private int statusCode;
    private String message;
    private InventoryData data;
    public int getStatusCode() { return statusCode; }
    public String getMessage() { return message; }
    public InventoryData getData() { return data; }

    public static class InventoryData {
        private List<InventoryItem> items;
        public List<InventoryItem> getItems() { return items; }

    }

    public static class InventoryItem {
        private int inventoryId;
        private String productName;
        private String sku;
        private String batchCode;
        private int quantity;
        private String expiryDate;
        private String unit;
        private String imageUrl;

        public int getInventoryId() { return inventoryId; }
        public String getProductName() { return productName; }
        public String getSku() { return sku; }
        public String getBatchCode() { return batchCode; }
        public int getQuantity() { return quantity; }
        public String getExpiryDate() { return expiryDate; }
        public String getUnit() { return unit; }
        public String getImageUrl() { return imageUrl; }
    }
}
