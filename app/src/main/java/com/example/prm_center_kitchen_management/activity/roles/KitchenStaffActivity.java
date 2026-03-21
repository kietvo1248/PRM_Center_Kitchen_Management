package com.example.prm_center_kitchen_management.activity.roles;

import androidx.fragment.app.Fragment;
import com.example.prm_center_kitchen_management.R;
import com.example.prm_center_kitchen_management.activity.base.BaseNavigationActivity;
import com.example.prm_center_kitchen_management.fragment.roles.KitchenStaff.InboundFragment;
import com.example.prm_center_kitchen_management.fragment.roles.KitchenStaff.KitchenInventoryFragment;
import com.example.prm_center_kitchen_management.fragment.roles.KitchenStaff.KitchenStaffDashboardFragment;
import com.example.prm_center_kitchen_management.fragment.roles.KitchenStaff.PickingListFragment;
import com.example.prm_center_kitchen_management.fragment.share.ProfileFragment;

public class KitchenStaffActivity extends BaseNavigationActivity {
    @Override
    protected int getContentViewId() {
        return R.layout.activity_kitchen_staff_menu;
    }

    @Override
    protected void setupBottomNavigation() {
        bottomNav.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;
            int itemId = item.getItemId();

            if (itemId == R.id.nav_kitchen_staff_dashboard) {
                selectedFragment = new KitchenStaffDashboardFragment();
            } else if (itemId == R.id.nav_inbound) {
                selectedFragment = new InboundFragment();
            } else if (itemId == R.id.nav_inventory) {
                selectedFragment = new KitchenInventoryFragment();
            } else if (itemId == R.id.nav_picking) {
                selectedFragment = new PickingListFragment();
            } else if (itemId == R.id.nav_profile) {
                selectedFragment = new ProfileFragment();
            }

            if (selectedFragment != null) {
                loadFragment(selectedFragment);
            }
            return true;
        });

        // Set default fragment
        bottomNav.setSelectedItemId(R.id.nav_kitchen_staff_dashboard);
    }
}
