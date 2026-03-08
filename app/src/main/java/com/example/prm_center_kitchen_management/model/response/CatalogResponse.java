package com.example.prm_center_kitchen_management.model.response;

import java.util.List;

public class CatalogResponse {
    private int statusCode;
    private String message;
    private CatalogData data;

    public int getStatusCode() {
        return statusCode;
    }

    public String getMessage() {
        return message;
    }

    public CatalogData getData() {
        return data;
    }

    public static class CatalogData {
        private List<ProductItem> items;

        public List<ProductItem> getItems() {
            return items;
        }
    }

    public static class ProductItem {
        private int id;
        private String sku;
        private String name;
        private String imageUrl;
        private boolean isActive;

        public int getId() {
            return id;
        }

        public String getSku() {
            return sku;
        }

        public String getName() {
            return name;
        }

        public String getImageUrl() {
            return imageUrl;
        }

        public boolean isActive() {
            return isActive;
        }
    }
}
