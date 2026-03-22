package com.example.prm_center_kitchen_management.model.response;

import java.util.List;
public class WasteAnalyticsData {
    private Kpi kpi;
    private List<WasteReport> details;

    public Kpi getKpi() { return kpi; }
    public List<WasteReport> getDetails() { return details; }

    public static class Kpi {
        private double totalWastedQuantity;
        private String period;

        public double getTotalWastedQuantity() { return totalWastedQuantity; }
        public String getPeriod() { return period; }
    }
}
