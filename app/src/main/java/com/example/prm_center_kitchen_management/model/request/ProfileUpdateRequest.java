package com.example.prm_center_kitchen_management.model.request;

public class ProfileUpdateRequest {
    private String fullName;
    private String phone;

    public ProfileUpdateRequest(String fullName, String phone) {
        this.fullName = fullName;
        this.phone = phone;
    }
}
