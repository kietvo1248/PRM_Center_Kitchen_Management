package com.example.prm_center_kitchen_management.activity;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import com.example.prm_center_kitchen_management.activity.auth.LoginActivity;
import com.example.prm_center_kitchen_management.activity.roles.AdminMenuActivity;
import com.example.prm_center_kitchen_management.activity.roles.FranchiseStaffActivity;
import com.example.prm_center_kitchen_management.activity.roles.KitchenStaffActivity;
import com.example.prm_center_kitchen_management.activity.roles.ManagerMenuActivity;
import com.example.prm_center_kitchen_management.activity.roles.SupplyCoordinatorActivity;
import com.example.prm_center_kitchen_management.utils.SessionManager;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Lưu ý: Không cần setContentView() vì trang này chỉ dùng để xử lý logic điều hướng rồi đóng luôn

        SessionManager sessionManager = new SessionManager(this);
        String token = sessionManager.getToken();
        String role = sessionManager.getRole();

        if (token != null && !token.isEmpty() && role != null) {
            // Đã đăng nhập -> Chuyển hướng theo Role
            routeToRoleActivity(role);
        } else {
            // Chưa đăng nhập -> Chuyển về màn hình Login
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }
    }

    private void routeToRoleActivity(String role) {
        Intent intent;
        switch (role) {
            case "admin":
                intent = new Intent(this, AdminMenuActivity.class);
                break;
            case "manager":
                intent = new Intent(this, ManagerMenuActivity.class);
                break;
            case "supply_coordinator":
                intent = new Intent(this, SupplyCoordinatorActivity.class);
                break;
            case "central_kitchen_staff":
                intent = new Intent(this, KitchenStaffActivity.class);
                break;
            case "franchise_store_staff":
                intent = new Intent(this, FranchiseStaffActivity.class);
                break;
            default:
                intent = new Intent(this, LoginActivity.class);
                break;
        }
        startActivity(intent);
        finish(); // Đóng Router lại để user không back về đây được
    }
}