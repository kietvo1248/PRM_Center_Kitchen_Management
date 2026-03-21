package com.example.prm_center_kitchen_management.activity.auth;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

//import androidx.appcompat.app.AppCompatActivity;

import com.example.prm_center_kitchen_management.R;
import com.example.prm_center_kitchen_management.api.ApiClient;
import com.example.prm_center_kitchen_management.api.ApiService;
import com.example.prm_center_kitchen_management.model.request.LoginRequest;
import com.example.prm_center_kitchen_management.model.response.LoginResponse;
import com.example.prm_center_kitchen_management.model.response.AuthData;
import com.example.prm_center_kitchen_management.utils.SessionManager;
import com.example.prm_center_kitchen_management.activity.base.BaseActivity;

// Thêm imports cho các Activity phân quyền
import com.example.prm_center_kitchen_management.activity.roles.AdminMenuActivity;
import com.example.prm_center_kitchen_management.activity.roles.ManagerMenuActivity;
import com.example.prm_center_kitchen_management.activity.roles.SupplyCoordinatorActivity;
import com.example.prm_center_kitchen_management.activity.roles.KitchenStaffActivity;
import com.example.prm_center_kitchen_management.activity.roles.FranchiseStaffActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends BaseActivity {

    private EditText edtEmail, edtPassword;
    private Button btnLogin;
    private SessionManager sessionManager;
    private AuthData authData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edtEmail = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtPassword);
        btnLogin = findViewById(R.id.btnLogin);
        sessionManager = new SessionManager(this);

        // Nạp sẵn data test
//        edtEmail.setText("admin@gmail.com");
//        edtPassword.setText("pass123456789");

        btnLogin.setOnClickListener(v -> attemptLogin());
    }

    private void attemptLogin() {
        String email = edtEmail.getText().toString().trim();
        String pass = edtPassword.getText().toString().trim();

        if (email.isEmpty() || pass.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }

        //ApiService apiService = ApiClient.getClient().create(ApiService.class);
        ApiService apiService = ApiClient.getClient(this).create(ApiService.class);
        LoginRequest request = new LoginRequest(email, pass);

        // Gọi API bất đồng bộ
        apiService.login(request).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                android.util.Log.d("API_DEBUG", "HTTP Code: " + response.code());

                if (response.isSuccessful() && response.body() != null) {
                    LoginResponse loginResponse = response.body();
                    int internalCode = loginResponse.getStatusCode();
                    android.util.Log.d("API_DEBUG", "JSON Status Code: " + internalCode);

                    if (internalCode == 201 || internalCode == 200) {
                        // --- ĐĂNG NHẬP THÀNH CÔNG ---
                        authData = loginResponse.getData();
                        String role = authData.getRole();

                        sessionManager.saveAuthData(authData.getAccessToken(), role, authData.getStoreId());
                        sessionManager.saveAuthToken(authData.getAccessToken());
                        sessionManager.saveRefreshToken(authData.getRefreshToken());

                        Toast.makeText(LoginActivity.this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();
                        routeToRoleActivity(role);
                    } else {
                        // --- THÀNH CÔNG HTTP NHƯNG LỖI LOGIC (Ví dụ: 400 trong JSON) ---
                        android.util.Log.e("API_DEBUG", "Logic Error: " + loginResponse.getMessage());
                        Toast.makeText(LoginActivity.this, "Lỗi: " + loginResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // --- LỖI HTTP (401 Unauthorized, 404 Not Found, 500 Server Error) ---
                    String errorMsg = "Sai tài khoản hoặc mật khẩu!";
                    if (response.code() == 500) errorMsg = "Lỗi server (500), vui lòng thử lại sau";

                    android.util.Log.e("API_DEBUG", "HTTP Error: " + response.code());
                    Toast.makeText(LoginActivity.this, errorMsg, Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Toast.makeText(LoginActivity.this, "Lỗi kết nối mạng: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Router điều phối theo Role
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
                Toast.makeText(this, "Role không hợp lệ!", Toast.LENGTH_SHORT).show();
                return;
        }
        startActivity(intent);
        finish(); // Đóng LoginActivity
    }
}