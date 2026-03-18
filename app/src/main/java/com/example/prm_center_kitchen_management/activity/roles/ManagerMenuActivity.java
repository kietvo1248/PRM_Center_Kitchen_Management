package com.example.prm_center_kitchen_management.activity.roles;

import androidx.fragment.app.Fragment;
import com.example.prm_center_kitchen_management.R;
import com.example.prm_center_kitchen_management.activity.base.BaseNavigationActivity;

// Nhớ import đúng đường dẫn mới của các Fragment
import com.example.prm_center_kitchen_management.fragment.roles.Manager.ManagerDashboardFragment;
import com.example.prm_center_kitchen_management.fragment.roles.Manager.StoreManagementFragment;
import com.example.prm_center_kitchen_management.fragment.roles.Manager.SupplierManagementFragment;
import com.example.prm_center_kitchen_management.fragment.roles.Manager.ProductManagerFragment;
import com.example.prm_center_kitchen_management.fragment.share.ProfileFragment;

public class ManagerMenuActivity extends BaseNavigationActivity {

    @Override
    protected int getContentViewId() {
        // Khai báo layout vỏ mà Activity này sẽ sử dụng
        return R.layout.activity_manager_menu;
    }

    @Override
    protected void setupBottomNavigation() {
        // Menu đã được nhúng sẵn trong XML (app:menu="@menu/menu_manager") nên không cần .inflateMenu nữa

        // Bắt sự kiện khi click vào các Tab
        bottomNav.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;
            int itemId = item.getItemId();

            if (itemId == R.id.nav_manager_dashboard) {
                // Gọi Fragment Tổng quan
                selectedFragment = new ManagerDashboardFragment();
            } else if (itemId == R.id.nav_manager_store) {
                selectedFragment = new StoreManagementFragment();

            }else if (itemId == R.id.nav_supplier_management) {
                selectedFragment = new SupplierManagementFragment();
            }else if(itemId == R.id.nav_product_management){
                selectedFragment = new ProductManagerFragment();
            }

            else if (itemId == R.id.nav_profile) {
                // Gọi Fragment Cá nhân dùng chung
                selectedFragment = new ProfileFragment();
            }

            // Chuyển Fragment
            if (selectedFragment != null) {
                loadFragment(selectedFragment);
            }
            return true;
        });

        // Mặc định khi vừa vào App thì chọn tab Tổng quan
        bottomNav.setSelectedItemId(R.id.nav_manager_dashboard);
    }
}