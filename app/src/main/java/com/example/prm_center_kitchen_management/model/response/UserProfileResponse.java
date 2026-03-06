package com.example.prm_center_kitchen_management.model.response;

public class UserProfileResponse {
    private int statusCode;
    private String message;
    private UserProfileData data;

    public int getStatusCode() { return statusCode; }
    public String getMessage() { return message; }
    public UserProfileData getData() { return data; }


    public static class UserProfileData{
        private String id;
        private String email;
        private String username; // Là fullName
        private String role;
        private String status;
        private String phone;

        public String getEmail() { return email; }
        public String getUsername() { return username; }
        public String getRole() { return role; }
        public String getStatus() { return status; }
        public String getPhone() { return phone; }
    }
}
