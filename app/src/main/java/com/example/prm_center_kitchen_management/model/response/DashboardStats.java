package com.example.prm_center_kitchen_management.model.response;

import com.google.gson.annotations.SerializedName;

public class DashboardStats {
    @SerializedName("pendingTasksCount")
    private int pendingTasksCount;

    @SerializedName("lowStockCount")
    private int lowStockCount;

    @SerializedName("nearExpiryCount")
    private int nearExpiryCount;

    public int getPendingTasksCount() { return pendingTasksCount; }
    public int getLowStockCount() { return lowStockCount; }
    public int getNearExpiryCount() { return nearExpiryCount; }
}
