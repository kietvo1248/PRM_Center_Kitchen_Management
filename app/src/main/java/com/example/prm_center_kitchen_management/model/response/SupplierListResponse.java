package com.example.prm_center_kitchen_management.model.response;

import java.util.List;
public class SupplierListResponse {
    private int statusCode;
    private String message;
    private Data data;

    public Data getData() { return data; }

    public static class Data {
        private List<Supplier> items;
        public List<Supplier> getItems() { return items; }
    }
}
