package com.example.prm_center_kitchen_management.model.response;

import java.util.List;
public class StoreDetailResponse {
    private StoreDetailData data;
    public StoreDetailData getData() { return data; }

    public static class StoreDetailData {
        private String id;
        private String name;
        private String address;
        private String managerName;
        private String phone;
        private boolean isActive;
        private List<Warehouse> warehouses;

        public String getId() { return id; }
        public String getName() { return name; }
        public String getAddress() { return address; }
        public String getManagerName() { return managerName; }
        public String getPhone() { return phone; }
        public boolean isActive() { return isActive; }
        public List<Warehouse> getWarehouses() { return warehouses; }
    }

    public static class Warehouse {
        private String name;
        public String getName() { return name; }
    }
}
