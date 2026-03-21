package com.example.prm_center_kitchen_management.model.request;

import com.google.gson.annotations.SerializedName;

public class AddReceiptItemRequest {
    @SerializedName("productId")
    private int productId;

    @SerializedName("quantity")
    private double quantity;

    @SerializedName("mfgDate")
    private String mfgDate;

    @SerializedName("expDate")
    private String expDate;

    public AddReceiptItemRequest(int productId, double quantity, String mfgDate, String expDate) {
        this.productId = productId;
        this.quantity = quantity;
        this.mfgDate = mfgDate;
        this.expDate = expDate;
    }
}
