package com.example.prm_center_kitchen_management.model.response;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class PickingTaskItem {
    @SerializedName("productId")
    private int productId;

    @SerializedName("productName")
    private String productName;

    @SerializedName("requiredQty")
    private double requiredQty;

    @SerializedName("suggestedBatches")
    private List<SuggestedBatch> suggestedBatches;

    public int getProductId() { return productId; }
    public String getProductName() { return productName; }
    public double getQuantityRequested() { return requiredQty; }
    public List<SuggestedBatch> getSuggestedBatches() { return suggestedBatches; }
}
