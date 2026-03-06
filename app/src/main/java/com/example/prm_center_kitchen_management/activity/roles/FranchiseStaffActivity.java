package com.example.prm_center_kitchen_management.activity.roles;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.fragment.app.Fragment;
import com.example.prm_center_kitchen_management.R;
import com.example.prm_center_kitchen_management.activity.base.BaseNavigationActivity;


import com.example.prm_center_kitchen_management.fragment.roles.Manager.ManagerDashboardFragment;
import com.example.prm_center_kitchen_management.fragment.share.ProfileFragment;

import com.example.prm_center_kitchen_management.activity.auth.LoginActivity;
import com.example.prm_center_kitchen_management.utils.SessionManager;
import com.example.prm_center_kitchen_management.activity.base.BaseActivity;

public class FranchiseStaffActivity extends BaseNavigationActivity{
    @Override
    protected int getContentViewId() {
        return R.layout.activity_franchise_staff_menu;
    }

    @Override
    protected void setupBottomNavigation() {
        // Menu đã được nhúng sẵn trong XML (app:menu="@menu/menu_manager") nên không cần .inflateMenu nữa

        bottomNav.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;
            int itemId = item.getItemId();

            if (itemId == R.id.nav_franchise_staff_dashboard) {
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

        bottomNav.setSelectedItemId(R.id.nav_franchise_staff_dashboard);
    }

}
