package com.example.prm_center_kitchen_management.model.response;

public class DashboardWasteResponse {
    private DashboardWasteData data;
    public DashboardWasteData getData() { return data; }

    public static class DashboardWasteData {
        private Kpi kpi;
        public Kpi getKpi() { return kpi; }
    }

    public static class Kpi {
        private double totalWastedQuantity;
        private String period;

        public double getTotalWastedQuantity() { return totalWastedQuantity; }
        public String getPeriod() { return period; }
    }
}
