package com.example.prm_center_kitchen_management.model.response;

import com.google.gson.annotations.SerializedName;

/**
 * GET /warehouse/scan-check?batchCode= — xác thực lô trước khi soạn.
 */
public class ScanCheckResponse {
    @SerializedName("isValid")
    private Boolean isValid;

    @SerializedName("message")
    private String message;

    @SerializedName("productName")
    private String productName;

    @SerializedName("batchId")
    private Integer batchId;

    @SerializedName("batchCode")
    private String batchCode;

    @SerializedName("expiryDate")
    private String expiryDate;

    @SerializedName("quantityPhysical")
    private Double quantityPhysical;

    /** API doc: ví dụ "AVAILABLE" */
    @SerializedName("status")
    private String status;

    public String getMessage() { return message; }
    public String getProductName() { return productName; }
    public Integer getBatchId() { return batchId; }
    public String getBatchCode() { return batchCode; }
    public String getExpiryDate() { return expiryDate; }
    public Double getQuantityPhysical() { return quantityPhysical; }
    public String getStatus() { return status; }

    /**
     * Cho phép soạn khi backend trả cờ isValid hoặc trạng thái lô khả dụng.
     */
    public boolean isAllowedToPick() {
        if (Boolean.TRUE.equals(isValid)) return true;
        if (status != null && "AVAILABLE".equalsIgnoreCase(status.trim())) return true;
        return false;
    }
}
