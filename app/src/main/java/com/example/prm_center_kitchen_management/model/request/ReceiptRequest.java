package com.example.prm_center_kitchen_management.model.request;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class ReceiptRequest {
    @SerializedName("supplierId")
    private Integer supplierId;

    public ReceiptRequest(Integer supplierId) {
        this.supplierId = supplierId;
    }

    public static class AddItemRequest {
        @SerializedName("productId")
        private Integer productId;
        
        @SerializedName("quantity")
        private Double quantity;

        public AddItemRequest(Integer productId, Double quantity) {
            this.productId = productId;
            this.quantity = quantity;
        }
    }
}
