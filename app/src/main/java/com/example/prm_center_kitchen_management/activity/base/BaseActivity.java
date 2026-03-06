package com.example.prm_center_kitchen_management.activity.base;

import android.app.ProgressDialog;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class BaseActivity extends AppCompatActivity{
    private ProgressDialog progressDialog;

    // Hiển thị Toast nhanh chóng
    public void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    // Hiển thị vòng xoay Loading
    public void showLoading(String message) {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setCancelable(false);
        }
        progressDialog.setMessage(message);
        if (!progressDialog.isShowing()) {
            progressDialog.show();
        }
    }

    // Ẩn vòng xoay Loading
    public void hideLoading() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    // Xử lý lỗi hệ thống/mạng
    public void handleApiError(Throwable t) {
        hideLoading();
        showToast("Lỗi hệ thống: " + t.getMessage());
    }
}

/**
 * MỤC ĐÍCH CỦA BASE ACTIVITY:
 * * 1. Đóng vai trò là Lớp Cha (Base Class) cho tất cả các Activity khác trong dự án.
 * 2. DRY (Don't Repeat Yourself): Gom toàn bộ các logic
 * và UI dùng chung vào một chỗ để tránh việc copy-paste code lặp lại.
 * 3. Quản lý tập trung các tác vụ phổ biến như:
 * - Hiển thị/Ẩn vòng xoay Loading (Progress Dialog).
 * - Hiển thị thông báo nhanh (Toast).
 * - Xử lý lỗi ngoại lệ từ API (Handle Exception).
 * 4. Giúp các Activity con (kế thừa từ nó) trở nên ngắn gọn, sạch sẽ và dễ bảo trì hơn.
 */
