package com.example.prm_center_kitchen_management.activity.roles;

import android.os.Bundle;
import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.Fragment;
import com.example.prm_center_kitchen_management.R;
import com.example.prm_center_kitchen_management.activity.base.BaseActivity;
import com.example.prm_center_kitchen_management.fragment.roles.SupplyCoordinator.CoordinatorOrderManagementFragment;
import com.example.prm_center_kitchen_management.fragment.roles.SupplyCoordinator.CoordinatorShipmentManagementFragment;
import com.example.prm_center_kitchen_management.fragment.roles.SupplyCoordinator.SupplyCoordinatorDashboardFragment;
import com.example.prm_center_kitchen_management.fragment.share.ProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class SupplyCoordinatorActivity extends BaseActivity {

    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supply_coordinator_menu);

        // 1. Ánh xạ View
        bottomNavigationView = findViewById(R.id.bottom_navigation);

        // 2. Thiết lập Fragment mặc định (Dashboard)
        if (savedInstanceState == null) {
            replaceFragment(new SupplyCoordinatorDashboardFragment());
        }

        // 3. Xử lý điều hướng Bottom Navigation (Đã hợp nhất và sửa lỗi)
        setupNavigation();

        // 4. Cấu hình xử lý nút Back
        setupBackNavigation();
    }

    private void setupNavigation() {
        bottomNavigationView.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;
            int itemId = item.getItemId();

            // Khớp chính xác với ID trong file XML Menu của bạn
            if (itemId == R.id.nav_supply_coordinator_dashboard) {
                selectedFragment = new SupplyCoordinatorDashboardFragment();
            } else if (itemId == R.id.nav_coordinator_orders) {
                selectedFragment = new CoordinatorOrderManagementFragment();
            } else if (itemId == R.id.nav_coordinator_shipments) { // ĐÃ THÊM VẬN ĐƠN VÀO ĐÂY
                selectedFragment = new CoordinatorShipmentManagementFragment();
            } else if (itemId == R.id.nav_profile) {
                selectedFragment = new ProfileFragment();
            }

            if (selectedFragment != null) {
                replaceFragment(selectedFragment);
                return true;
            }
            return false;
        });
    }

    private void setupBackNavigation() {
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                int selectedId = bottomNavigationView.getSelectedItemId();
                if (selectedId != R.id.nav_supply_coordinator_dashboard) {
                    bottomNavigationView.setSelectedItemId(R.id.nav_supply_coordinator_dashboard);
                } else {
                    setEnabled(false);
                    getOnBackPressedDispatcher().onBackPressed();
                }
            }
        };
        getOnBackPressedDispatcher().addCallback(this, callback);
    }

    private void replaceFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }
}