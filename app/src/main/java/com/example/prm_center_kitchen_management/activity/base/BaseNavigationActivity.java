package com.example.prm_center_kitchen_management.activity.base;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import com.example.prm_center_kitchen_management.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public abstract class BaseNavigationActivity extends BaseActivity {
    protected BottomNavigationView bottomNav;

    // Yêu cầu Activity con phải truyền vào ID của layout nó muốn dùng
    protected abstract int getContentViewId();
    protected abstract void setupBottomNavigation();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Thay vì fix cứng, ta set layout được truyền từ Activity con
        setContentView(getContentViewId());

        bottomNav = findViewById(R.id.bottom_navigation);

        // Bắt buộc các Activity con phải tự cài đặt Menu riêng
        setupBottomNavigation();
    }

    // Hàm tiện ích để chuyển đổi Fragment nhanh chóng
    protected void loadFragment(Fragment fragment) {
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();
        }
    }
}