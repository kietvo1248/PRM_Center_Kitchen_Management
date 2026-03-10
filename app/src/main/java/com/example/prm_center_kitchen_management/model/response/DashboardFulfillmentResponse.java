package com.example.prm_center_kitchen_management.model.response;

public class DashboardFulfillmentResponse {
    private DashboardFulfillmentData data;
    public DashboardFulfillmentData getData() { return data; }

    public static class DashboardFulfillmentData {
        private Kpi kpi;
        public Kpi getKpi() { return kpi; }
    }

    public static class Kpi {
        private double fillRatePercentage;
        private int totalRequestedQty;
        private int totalApprovedQty;

        public double getFillRatePercentage() { return fillRatePercentage; }
        public int getTotalRequestedQty() { return totalRequestedQty; }
        public int getTotalApprovedQty() { return totalApprovedQty; }
    }
}
