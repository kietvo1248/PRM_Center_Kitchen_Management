package com.example.prm_center_kitchen_management.model.response;

import com.google.gson.annotations.SerializedName;
import java.util.List;

/** JSON lỗi từ backend (statusCode, message, errors) khi HTTP != 2xx */
public class ApiErrorBody {
    @SerializedName("statusCode")
    private int statusCode;

    @SerializedName("message")
    private String message;

    @SerializedName("errors")
    private List<Object> errors;

    public String getMessage() {
        return message;
    }
}
