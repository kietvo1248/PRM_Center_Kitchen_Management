package com.example.prm_center_kitchen_management.model.response;

public class RefreshTokenResponse {
    private int statusCode;
    private String message;
    private AuthData data;

    public AuthData getData() { return data; }
}
