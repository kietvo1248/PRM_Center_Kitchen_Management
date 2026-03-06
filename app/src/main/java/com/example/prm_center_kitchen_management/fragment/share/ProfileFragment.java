package com.example.prm_center_kitchen_management.fragment.share;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.content.Intent;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.prm_center_kitchen_management.R;
import com.example.prm_center_kitchen_management.activity.base.BaseActivity;
import com.example.prm_center_kitchen_management.api.ApiClient;
import com.example.prm_center_kitchen_management.api.ApiService;
import com.example.prm_center_kitchen_management.model.request.ProfileUpdateRequest;
import com.example.prm_center_kitchen_management.model.response.UserProfileResponse;
import com.example.prm_center_kitchen_management.utils.SessionManager;
import com.example.prm_center_kitchen_management.activity.auth.LoginActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileFragment extends Fragment {
    private TextView tvUsername, tvEmail, tvPhone, tvRole, tvStatus;
    private Button btnOpenUpdate, btnLogout;
    private ApiService apiService;

    //lấy data gốc để edit
    private UserProfileResponse.UserProfileData currentUserData;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        tvUsername = view.findViewById(R.id.tvUsername);
        tvEmail = view.findViewById(R.id.tvEmail);
        tvPhone = view.findViewById(R.id.tvPhone);
        tvRole = view.findViewById(R.id.tvRole);
        tvStatus = view.findViewById(R.id.tvStatus);
        btnOpenUpdate = view.findViewById(R.id.btnOpenUpdate);
        btnLogout = view.findViewById(R.id.btnLogout);

        apiService = ApiClient.getClient(requireContext()).create(ApiService.class);
        fetchUserProfile();
        btnOpenUpdate.setOnClickListener(v -> showUpdateDialog());
        btnLogout.setOnClickListener(v -> {
            new SessionManager(requireContext()).logout();
            startActivity(new Intent(requireActivity(), LoginActivity.class));
            requireActivity().finish();
        });

        return view;
    }

    private void fetchUserProfile() {
        if (getActivity() instanceof BaseActivity) {
            ((BaseActivity) getActivity()).showLoading("Đang tải thông tin...");
        }

        apiService.getProfile().enqueue(new Callback<UserProfileResponse>() {
            @Override
            public void onResponse(@NonNull Call<UserProfileResponse> call, @NonNull Response<UserProfileResponse> response) {
                if (getActivity() instanceof BaseActivity) {
                    ((BaseActivity) getActivity()).hideLoading();
                }

                if (response.isSuccessful() && response.body() != null && response.body().getStatusCode() == 200) {
                    currentUserData = response.body().getData();
                    if (currentUserData != null) {
                        updateUI(currentUserData);
                    }
                } else {
                    if (isAdded()) {
                        Toast.makeText(requireContext(), "Lấy thông tin thất bại!", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<UserProfileResponse> call, @NonNull Throwable t) {
                if (getActivity() instanceof BaseActivity) {
                    ((BaseActivity) getActivity()).handleApiError(t);
                }
            }
        });
    }

    private void updateUI(UserProfileResponse.UserProfileData data) {
        if (data == null) return;

        tvUsername.setText("Họ tên: " + (data.getUsername() != null ? data.getUsername() : ""));
        tvEmail.setText("Email: " + (data.getEmail() != null ? data.getEmail() : ""));
        tvRole.setText("Vai trò: " + (data.getRole() != null ? data.getRole().toUpperCase() : ""));
        tvStatus.setText("Trạng thái: " + (data.getStatus() != null ? data.getStatus() : ""));

        if (data.getPhone() != null && !data.getPhone().isEmpty()) {
            tvPhone.setText("SĐT: " + data.getPhone());
        } else {
            tvPhone.setText("SĐT: Chưa cập nhật");
        }
    }

    private void showUpdateDialog() {
        if(currentUserData == null) return;

        Dialog dialog = new Dialog(requireContext());
        dialog.setContentView(R.layout.dialog_update_profile);
        dialog.setCancelable(true);

        EditText edtFullName = dialog.findViewById(R.id.edtFullName);
        EditText edtPhone = dialog.findViewById(R.id.edtPhone);
        Button btnSave = dialog.findViewById(R.id.btnSave);
        Button btnCancel = dialog.findViewById(R.id.btnCancel);

        edtFullName.setText(currentUserData.getUsername());
        edtPhone.setText(currentUserData.getPhone());

        btnSave.setOnClickListener(v -> {
            String fullName = edtFullName.getText().toString().trim();
            String phone = edtPhone.getText().toString().trim();

            if (fullName.isEmpty() || phone.isEmpty()) {
                Toast.makeText(requireContext(), "Vui lòng nhập đủ thông tin!", Toast.LENGTH_SHORT).show();
                return;
            }
            updateUserProfile(fullName, phone, dialog);
            //ProfileUpdateRequest request = new ProfileUpdateRequest(fullName, phone);
        });

        btnCancel.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }

    private void updateUserProfile(String fullName, String phone, Dialog dialog) {
        if (getActivity() instanceof BaseActivity) {
            ((BaseActivity) getActivity()).showLoading("Đang Cập Nhật...");
        }
        ProfileUpdateRequest request = new ProfileUpdateRequest(fullName, phone);
        apiService.updateProfile(request).enqueue(new Callback<UserProfileResponse>() {
            @Override
            public void onResponse(Call<UserProfileResponse> call, Response<UserProfileResponse> response) {
                if (getActivity() instanceof BaseActivity) {
                    ((BaseActivity) getActivity()).hideLoading();
                }

                if (response.isSuccessful() && response.body() != null && response.body().getStatusCode() == 200) {
                    Toast.makeText(requireContext(), "Cập nhật thành công!", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                    // Lấy cục data mới từ response đập thẳng vào UI để khỏi gọi GET API lại lần nữa
                    currentUserData = response.body().getData();
                    updateUI(currentUserData);
                } else {
                    Toast.makeText(requireContext(), "Cập nhật thất bại!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UserProfileResponse> call, Throwable t) {
                if (getActivity() instanceof BaseActivity) {
                    ((BaseActivity) getActivity()).handleApiError(t);
                }
            }
        });
    }

}

