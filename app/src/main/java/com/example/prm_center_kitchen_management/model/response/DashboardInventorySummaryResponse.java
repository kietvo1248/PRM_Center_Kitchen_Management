package com.example.prm_center_kitchen_management.model.response;

public class DashboardInventorySummaryResponse {
    private DashboardInventoryData data;
    public DashboardInventoryData getData() { return data; }

    public static class DashboardInventoryData {
        private Overview overview;
        public Overview getOverview() { return overview; }
    }

    public static class Overview {
        private int totalProducts;
        private int totalLowStockItems;
        private int totalExpiringBatches;

        public int getTotalProducts() { return totalProducts; }
        public int getTotalLowStockItems() { return totalLowStockItems; }
        public int getTotalExpiringBatches() { return totalExpiringBatches; }
    }
}
