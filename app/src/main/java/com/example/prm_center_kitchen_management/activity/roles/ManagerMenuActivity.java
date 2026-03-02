package com.example.prm_center_kitchen_management.activity.roles;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
//import androidx.appcompat.app.AppCompatActivity;
import com.example.prm_center_kitchen_management.R;
import com.example.prm_center_kitchen_management.activity.auth.LoginActivity;
import com.example.prm_center_kitchen_management.utils.SessionManager;
import com.example.prm_center_kitchen_management.activity.BaseActivity;

public class ManagerMenuActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_menu);

        Button btnLogout = findViewById(R.id.btnLogout);
        btnLogout.setOnClickListener(v -> {
            new SessionManager(this).logout(); // Xóa token
            startActivity(new Intent(this, LoginActivity.class)); // Về trang đăng nhập
            finish(); // Đóng trang hiện tại
        });
    }
}
