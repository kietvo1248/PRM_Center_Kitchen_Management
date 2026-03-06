package com.example.prm_center_kitchen_management.activity.roles;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import com.example.prm_center_kitchen_management.R;
import com.example.prm_center_kitchen_management.activity.base.BaseNavigationActivity;

//import com.example.prm_center_kitchen_management.activity.base.BaseActivity;
//import com.example.prm_center_kitchen_management.activity.auth.LoginActivity;
import com.example.prm_center_kitchen_management.fragment.roles.Manager.ManagerDashboardFragment;
import com.example.prm_center_kitchen_management.fragment.share.ProfileFragment;
import com.example.prm_center_kitchen_management.utils.SessionManager;


public class SupplyCoordinatorActivity extends BaseNavigationActivity {
    @Override
    protected int getContentViewId() {
        // Khai báo layout vỏ mà Activity này sẽ sử dụng
        return R.layout.activity_supply_coordinator_menu;
    }

    @Override
    protected void setupBottomNavigation() {

        bottomNav.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;
            int itemId = item.getItemId();

            if (itemId == R.id.nav_supply_coordinator_dashboard) {
                selectedFragment = new ManagerDashboardFragment();
            }
            else if (itemId == R.id.nav_profile) {
                selectedFragment = new ProfileFragment();
            }

            if (selectedFragment != null) {
                loadFragment(selectedFragment);
            }
            return true;
        });

        bottomNav.setSelectedItemId(R.id.nav_supply_coordinator_dashboard);
    }
}
