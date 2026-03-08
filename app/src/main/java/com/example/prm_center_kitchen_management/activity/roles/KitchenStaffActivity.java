package com.example.prm_center_kitchen_management.activity.roles;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.fragment.app.Fragment;
import com.example.prm_center_kitchen_management.activity.base.BaseNavigationActivity;
import com.example.prm_center_kitchen_management.fragment.roles.KitchenStaff.KitchenStaffDashboardFragment;
import com.example.prm_center_kitchen_management.fragment.share.ProfileFragment;
import com.example.prm_center_kitchen_management.R;
import com.example.prm_center_kitchen_management.activity.auth.LoginActivity;
import com.example.prm_center_kitchen_management.utils.SessionManager;
import com.example.prm_center_kitchen_management.activity.base.BaseActivity;

public class KitchenStaffActivity extends BaseNavigationActivity {
    @Override
    protected int getContentViewId() {
        // Khai báo layout vỏ mà Activity này sẽ sử dụng
        return R.layout.activity_kitchen_staff_menu;
    }

    @Override
    protected void setupBottomNavigation() {

        bottomNav.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;
            int itemId = item.getItemId();

            if (itemId == R.id.nav_kitchen_staff_dashboard) {
                selectedFragment = new KitchenStaffDashboardFragment();
            }
            else if (itemId == R.id.nav_profile) {
                selectedFragment = new ProfileFragment();
            }

            if (selectedFragment != null) {
                loadFragment(selectedFragment);
            }
            return true;
        });

        bottomNav.setSelectedItemId(R.id.nav_kitchen_staff_dashboard);
    }
}
