package com.example.prm_center_kitchen_management.activity.roles;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import com.example.prm_center_kitchen_management.R;
import com.example.prm_center_kitchen_management.activity.BaseActivity;
import com.example.prm_center_kitchen_management.activity.auth.LoginActivity;
import com.example.prm_center_kitchen_management.utils.SessionManager;

public class AdminMenuActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_menu);

        Button btnLogout = findViewById(R.id.btnLogout);
        btnLogout.setOnClickListener(v -> {
            // demo BaseActivity
            // 1. Gọi hàm hiển thị Loading từ BaseActivity
            showLoading("Đang đăng xuất...");

            // Giả lập độ trễ mạng 1.5 giây để nhìn thấy vòng xoay
            new Handler().postDelayed(() -> {
                // 2. Ẩn Loading
                hideLoading();

                // 3. Hiển thị thông báo Toast từ BaseActivity
                showToast("Đã đăng xuất an toàn!");

                // Xóa token và về trang Login
                new SessionManager(this).logout();
                startActivity(new Intent(this, LoginActivity.class));
                finish();
            }, 1500);
        });
    }
}