package com.example.prm_center_kitchen_management.model.response;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class PickingListResponse {
    private int statusCode;
    private String message;
    private PickingData data;

    public int getStatusCode() { return statusCode; }
    public String getMessage() { return message; }
    public PickingData getData() { return data; }

    public static class PickingData {
        @SerializedName("shipment_id")
        private String shipmentId;

        @SerializedName("order_id")
        private String orderId;

        @SerializedName("store_name")
        private String storeName;

        private String status;
        private List<PickingItem> items;

        public String getShipmentId() { return shipmentId; }
        public String getOrderId() { return orderId; }
        public String getStoreName() { return storeName; }
        public String getStatus() { return status; }
        public List<PickingItem> getItems() { return items; }
    }

    public static class PickingItem {
        @SerializedName("product_name")
        private String productName;

        private String sku;

        @SerializedName("batch_code")
        private String batchCode;

        private String quantity;

        @SerializedName("expiry_date")
        private String expiryDate;

        @SerializedName("image_url")
        private String imageUrl;

        public String getProductName() { return productName; }
        public String getSku() { return sku; }
        public String getBatchCode() { return batchCode; }
        public String getQuantity() { return quantity; }
        public String getExpiryDate() { return expiryDate; }
        public String getImageUrl() { return imageUrl; }
    }
}