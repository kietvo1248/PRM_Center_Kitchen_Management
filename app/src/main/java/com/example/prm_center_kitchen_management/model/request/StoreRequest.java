package com.example.prm_center_kitchen_management.model.request;

public class StoreRequest {
    private String name;
    private String address;
    private String phone;
    private String managerName;

    public StoreRequest(String name, String address, String phone, String managerName) {
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.managerName = managerName;
    }
}
