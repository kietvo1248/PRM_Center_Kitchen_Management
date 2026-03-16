package com.example.prm_center_kitchen_management.model.request;

public class SupplierRequest {
    private String name;
    private String contactName;
    private String phone;
    private String address;
    private Boolean isActive;

    public SupplierRequest(String name, String contactName, String phone, String address, Boolean isActive) {
        this.name = name;
        this.contactName = contactName;
        this.phone = phone;
        this.address = address;
        this.isActive = isActive;
    }
}
