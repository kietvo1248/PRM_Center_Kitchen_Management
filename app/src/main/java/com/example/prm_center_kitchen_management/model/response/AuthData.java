package com.example.prm_center_kitchen_management.model.response;

public class AuthData {
    private String userId;
    private String email;
    private String username;
    private String role;
    private String storeId;
    private String accessToken;
    private String refreshToken;
    public AuthData(String userId, String email, String username, String role, String storeId, String accessToken, String refreshToken) {
        this.userId = userId;
        this.email = email;
        this.username = username;
        this.role = role;
        this.storeId = storeId;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
    public String getRole() { return role; }
    public String getAccessToken() { return accessToken; }
    public String getRefreshToken() { return refreshToken; }
    public String getStoreId() { return storeId; }
}
