package com.example.prm_center_kitchen_management.model.request;

import com.google.gson.annotations.SerializedName;

public class ScanCheckRequest {
    @SerializedName("batchCode")
    private String batchCode;

    @SerializedName("pickingTaskId")
    private String pickingTaskId;

    public ScanCheckRequest(String batchCode, String pickingTaskId) {
        this.batchCode = batchCode;
        this.pickingTaskId = pickingTaskId;
    }
}
