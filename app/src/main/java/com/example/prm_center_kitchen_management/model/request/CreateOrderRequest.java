package com.example.prm_center_kitchen_management.model.request;

import java.util.List;

public class CreateOrderRequest {
    private String deliveryDate;
    private List<OrderItemRequest> items;

    public CreateOrderRequest(String deliveryDate, List<OrderItemRequest> items) {
        this.deliveryDate = deliveryDate;
        this.items = items;
    }
    public static class OrderItemRequest {
        private int productId;
        private int quantity;

        public OrderItemRequest(int productId, int quantity) {
            this.productId = productId;
            this.quantity = quantity;
        }
    }
}
