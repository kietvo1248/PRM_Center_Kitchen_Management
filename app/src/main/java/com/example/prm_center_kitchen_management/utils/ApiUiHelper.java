package com.example.prm_center_kitchen_management.utils;

import android.content.Context;
import android.widget.Toast;
import com.example.prm_center_kitchen_management.model.response.ApiErrorBody;
import com.google.gson.Gson;
import retrofit2.Response;

/**
 * Xử lý lỗi HTTP thống nhất cho Fragment/Activity (401/403 + message).
 */
public final class ApiUiHelper {

    private ApiUiHelper() {}

    /** Đọc {@code message} từ JSON body khi HTTP lỗi (4xx/5xx). */
    public static String parseErrorMessage(retrofit2.Response<?> response) {
        if (response == null || response.isSuccessful()) return null;
        okhttp3.ResponseBody eb = response.errorBody();
        if (eb == null) return null;
        try {
            String json = eb.string();
            ApiErrorBody err = new Gson().fromJson(json, ApiErrorBody.class);
            return err != null && err.getMessage() != null ? err.getMessage() : null;
        } catch (Exception ignored) {
            return null;
        }
    }

    public static void toastHttpError(Context context, Response<?> response) {
        if (context == null || response == null) return;
        int code = response.code();
        String msg;
        if (code == 401) {
            msg = "Phiên đăng nhập hết hạn. Vui lòng đăng nhập lại.";
        } else if (code == 403) {
            msg = "Bạn không có quyền thực hiện thao tác này.";
        } else {
            msg = "Lỗi máy chủ (" + code + ")";
        }
        Toast.makeText(context.getApplicationContext(), msg, Toast.LENGTH_LONG).show();
    }
}
