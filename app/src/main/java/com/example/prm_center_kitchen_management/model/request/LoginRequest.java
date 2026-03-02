package com.example.prm_center_kitchen_management.model.request;

public class LoginRequest {
    private String email;
    private String password;
    public LoginRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
