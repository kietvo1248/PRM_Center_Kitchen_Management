package com.example.prm_center_kitchen_management.model.response;

import java.util.List;
public class DashboardSupplierResponse {
    private DashboardSupplierData data;
    public DashboardSupplierData getData() { return data; }

    public static class DashboardSupplierData {
        private List<SupplierItem> items;
        public List<SupplierItem> getItems() { return items; }
    }

    public static class SupplierItem {
        private String name;
        private String contactName;
        private String phone;

        public String getName() { return name; }
        public String getContactName() { return contactName; }
        public String getPhone() { return phone; }
    }
}
