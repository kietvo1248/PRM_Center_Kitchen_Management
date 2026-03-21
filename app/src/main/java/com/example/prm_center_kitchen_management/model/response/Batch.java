package com.example.prm_center_kitchen_management.model.response;

import com.google.gson.annotations.SerializedName;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class Batch {
    @SerializedName("id")
    private String id;

    @SerializedName("batchCode")
    private String batchCode;

    @SerializedName("productId")
    private Integer productId;

    @SerializedName("quantity")
    private Double quantity;

    @SerializedName("mfgDate")
    private String mfgDate;

    @SerializedName("expDate")
    private String expDate;

    @SerializedName("status")
    private String status;

    // Getters
    public String getId() { return id; }
    public String getBatchCode() { return batchCode; }
    public Integer getProductId() { return productId; }
    public Double getQuantity() { return quantity; }
    public String getMfgDate() { return mfgDate; }
    public String getExpDate() { return expDate; }
    public String getStatus() { return status; }

    /**
     * Logic helper: Trả về trạng thái hết hạn dựa trên expDate
     * 🔴 EXPIRED: Đã hết hạn hoặc còn < 3 ngày.
     * 🟡 WARNING: Còn < 7 ngày.
     * 🟢 SAFE: An toàn.
     */
    public ExpiryStatus getExpiryStatus() {
        if (expDate == null || expDate.isEmpty()) return ExpiryStatus.SAFE;

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            Date expiryDate = sdf.parse(expDate);
            Date currentDate = new Date();

            if (expiryDate == null) return ExpiryStatus.SAFE;

            long diffInMillies = expiryDate.getTime() - currentDate.getTime();
            long diffInDays = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);

            if (diffInDays < 3) {
                return ExpiryStatus.EXPIRED;
            } else if (diffInDays < 7) {
                return ExpiryStatus.WARNING;
            } else {
                return ExpiryStatus.SAFE;
            }
        } catch (ParseException e) {
            return ExpiryStatus.SAFE;
        }
    }
}
