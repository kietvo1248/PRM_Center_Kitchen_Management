package com.example.prm_center_kitchen_management.model.response;

import com.google.gson.annotations.SerializedName;

public class KitchenDashboardStats {
    @SerializedName("totalPendingTasks")
    private int totalPendingTasks;

    @SerializedName("lowStockItems")
    private int lowStockItems;

    @SerializedName("nearExpiryBatches")
    private int nearExpiryBatches;

    public int getTotalPendingTasks() { return totalPendingTasks; }
    public int getLowStockItems() { return lowStockItems; }
    public int getNearExpiryBatches() { return nearExpiryBatches; }
}
