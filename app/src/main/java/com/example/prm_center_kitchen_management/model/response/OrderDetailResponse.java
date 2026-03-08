package com.example.prm_center_kitchen_management.model.response;
import java.util.List;


public class OrderDetailResponse {
    private int statusCode;
    private OrderDetailData data;
    public int getStatusCode() { return statusCode; }
    public OrderDetailData getData() { return data; }

    public class OrderDetailData {
        private String id;
        private String status;
        private String deliveryDate;
        private List<DetailItem> items;

        public String getId() {
            return id;
        }

        public String getStatus() {
            return status;
        }

        public String getDeliveryDate() {
            return deliveryDate;
        }

        public List<DetailItem> getItems() {
            return items;
        }

    }

    public class DetailItem {
        private String quantityRequested;
        private Product product;
        public String getQuantityRequested() { return quantityRequested; }
        public Product getProduct() { return product; }

    }

    public class Product{
        private String name;
        private String imageUrl;
        public String getName() { return name; }
        public String getImageUrl() { return imageUrl; }
    }

}
