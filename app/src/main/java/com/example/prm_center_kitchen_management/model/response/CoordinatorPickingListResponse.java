package com.example.prm_center_kitchen_management.model.response;

import java.util.List;

public class CoordinatorPickingListResponse {
    private int statusCode;
    private PickingData data;

    public class PickingData {
        private List<PickingItem> items;
        public List<PickingItem> getItems() { return items; }
    }

    public class PickingItem {
        private String product_name;
        private String sku;
        private String batch_code;
        private String quantity;
        private String image_url;

        public String getProductName() { return product_name; }
        public String getSku() { return sku; }
        public String getBatchCode() { return batch_code; }
        public String getQuantity() { return quantity; }
        public String getImageUrl() { return image_url; }
    }
    public PickingData getData() { return data; }
    public int getStatusCode() { return statusCode; }
}