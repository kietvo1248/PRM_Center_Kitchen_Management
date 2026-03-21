package com.example.prm_center_kitchen_management.model.response;

import com.google.gson.annotations.SerializedName;

public class ApiResponse<T> {
    @SerializedName("statusCode")
    private int statusCode;
    
    @SerializedName("message")
    private String message;
    
    @SerializedName("data")
    private T data;

    public int getStatusCode() { return statusCode; }
    public String getMessage() { return message; }
    public T getData() { return data; }
}
