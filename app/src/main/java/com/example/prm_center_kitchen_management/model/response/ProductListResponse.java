package com.example.prm_center_kitchen_management.model.response;

import java.util.List;
public class ProductListResponse {
    private Data data;
    public Data getData() { return data; }
    public static class Data {
        private List<Product> items;
        public List<Product> getItems() { return items; }
    }
}
