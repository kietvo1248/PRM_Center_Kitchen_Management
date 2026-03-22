package com.example.prm_center_kitchen_management.model.response;

import java.util.List;
public class InboundDetailResponse {
    private String id;
    private String status;
    private String note;
    private String createdAt;
    private SupplierInfo supplier;
    private CreatedBy createdBy;
    private List<Item> items;

    public String getId() { return id; }
    public String getStatus() { return status; }
    public String getNote() { return note; }
    public String getCreatedAt() { return createdAt; }
    public SupplierInfo getSupplier() { return supplier; }
    public CreatedBy getCreatedBy() { return createdBy; }
    public List<Item> getItems() { return items; }

    public static class SupplierInfo {
        private int id;
        private String name;
        private String contactName;
        private String phone;

        public String getName() { return name; }
        public String getPhone() { return phone; }
    }

    public static class CreatedBy {
        private String id;
        private String username;

        public String getUsername() { return username; }
    }

    public static class Item {
        private int id;
        private String quantity;
        private BatchInfo batch;

        public String getQuantity() { return quantity; }
        public BatchInfo getBatch() { return batch; }
    }

    public static class BatchInfo {
        private int id;
        private String batchCode;
        private String expiryDate;
        private String status;
        private ProductInfo product;

        public String getBatchCode() { return batchCode; }
        public String getExpiryDate() { return expiryDate; }
        public ProductInfo getProduct() { return product; }
    }

    public static class ProductInfo {
        private int id;
        private String name;
        private String sku;
        private String unit;

        public String getName() { return name; }
        public String getSku() { return sku; }
        public String getUnit() { return unit; }
    }
}
