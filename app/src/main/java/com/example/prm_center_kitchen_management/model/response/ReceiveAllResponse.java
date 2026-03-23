package com.example.prm_center_kitchen_management.model.response;

public class ReceiveAllResponse {
    private String message;
    private String shipmentId;
    private String status;
    private boolean hasDiscrepancy;
    private String claimId;

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public String getShipmentId() { return shipmentId; }
    public void setShipmentId(String shipmentId) { this.shipmentId = shipmentId; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public boolean isHasDiscrepancy() { return hasDiscrepancy; }
    public void setHasDiscrepancy(boolean hasDiscrepancy) { this.hasDiscrepancy = hasDiscrepancy; }
    public String getClaimId() { return claimId; }
    public void setClaimId(String claimId) { this.claimId = claimId; }
}
