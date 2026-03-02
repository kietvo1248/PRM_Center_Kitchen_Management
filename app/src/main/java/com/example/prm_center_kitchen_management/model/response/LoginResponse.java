package com.example.prm_center_kitchen_management.model.response;

public class LoginResponse {
    private int statusCode;
    private String message;
    private AuthData data;
    public LoginResponse(int statusCode, String message, AuthData data) {
        this.statusCode = statusCode;
        this.message = message;
        this.data = data;
    }

    public int getStatusCode() { return statusCode; }
    public String getMessage() { return message; }
    public AuthData getData() { return data; }
}
