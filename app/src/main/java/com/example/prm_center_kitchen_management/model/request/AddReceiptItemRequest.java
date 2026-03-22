package com.example.prm_center_kitchen_management.model.request;

import com.google.gson.annotations.SerializedName;

public class AddReceiptItemRequest {
    @SerializedName("productId")
    private int productId;

    @SerializedName("quantity")
    private double quantity;


    public AddReceiptItemRequest(int productId, double quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }
}
