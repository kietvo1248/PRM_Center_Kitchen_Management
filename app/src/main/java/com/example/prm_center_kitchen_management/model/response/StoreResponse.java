package com.example.prm_center_kitchen_management.model.response;

import java.util.List;
public class StoreResponse {
    private int statusCode;
    private StoreData data;
    public StoreData getData() { return data; }

    public static class StoreData {
        private List<StoreItem> items;
        public List<StoreItem> getItems() { return items; }
    }

    public static class StoreItem {
        private String id;
        private String name;
        private String address;
        private String managerName;
        private String phone;
        private boolean isActive;

        public String getId() { return id; }
        public String getName() { return name; }
        public String getAddress() { return address; }
        public String getManagerName() { return managerName; }
        public String getPhone() { return phone; }
        public boolean isActive() { return isActive; }
    }
}
