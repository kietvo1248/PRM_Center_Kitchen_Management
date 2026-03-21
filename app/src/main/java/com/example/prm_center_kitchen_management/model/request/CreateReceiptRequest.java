package com.example.prm_center_kitchen_management.model.request;

import com.google.gson.annotations.SerializedName;

public class CreateReceiptRequest {
    @SerializedName("supplierId")
    private int supplierId;

    @SerializedName("note")
    private String note;

    public CreateReceiptRequest(int supplierId) {
        this.supplierId = supplierId;
    }

    public CreateReceiptRequest(int supplierId, String note) {
        this.supplierId = supplierId;
        this.note = note;
    }

    public int getSupplierId() {
        return supplierId;
    }
}
