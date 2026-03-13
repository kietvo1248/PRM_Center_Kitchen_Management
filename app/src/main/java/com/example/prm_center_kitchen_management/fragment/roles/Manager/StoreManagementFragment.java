package com.example.prm_center_kitchen_management.fragment.roles.Manager;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prm_center_kitchen_management.R;
import com.example.prm_center_kitchen_management.activity.base.BaseActivity;
import com.example.prm_center_kitchen_management.adapter.roles.Manager.StoreAdapter;
import com.example.prm_center_kitchen_management.api.ApiClient;
import com.example.prm_center_kitchen_management.api.ApiService;
import com.example.prm_center_kitchen_management.model.request.StoreRequest;
import com.example.prm_center_kitchen_management.model.response.StoreDetailResponse;
import com.example.prm_center_kitchen_management.model.response.StoreResponse;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
public class StoreManagementFragment extends Fragment{
    private RecyclerView rvStores;
    private SearchView searchView;
    private StoreAdapter adapter;
    private ApiService apiService;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_store_management, container, false);

        rvStores = view.findViewById(R.id.rvStores);
        searchView = view.findViewById(R.id.searchView);
        view.findViewById(R.id.fabAddStore).setOnClickListener(v -> showStoreFormDialog(null));

        rvStores.setLayoutManager(new LinearLayoutManager(requireContext()));
        apiService = ApiClient.getClient(requireContext()).create(ApiService.class);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                fetchStores(query);
                searchView.clearFocus();
                return true;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.isEmpty()) fetchStores(null);
                return true;
            }
        });

        fetchStores(null);
        return view;
    }

    // --- 1. LẤY DANH SÁCH ---
    private void fetchStores(String search) {
        if(getActivity() instanceof BaseActivity) ((BaseActivity)getActivity()).showLoading("Đang tải DS cửa hàng...");

        // Truyền null cho các param không bắt buộc
        apiService.getStores(1, 50, "DESC", search, null).enqueue(new Callback<StoreResponse>() {
            @Override
            public void onResponse(@NonNull Call<StoreResponse> call, @NonNull Response<StoreResponse> response) {
                if(getActivity() instanceof BaseActivity) ((BaseActivity)getActivity()).hideLoading();
                if (response.isSuccessful() && response.body() != null) {
                    List<StoreResponse.StoreItem> items = response.body().getData().getItems();
                    adapter = new StoreAdapter(items, storeId -> showStoreDetail(storeId));
                    rvStores.setAdapter(adapter);
                }
            }
            @Override public void onFailure(@NonNull Call<StoreResponse> call, @NonNull Throwable t) {
                if(getActivity() instanceof BaseActivity) ((BaseActivity)getActivity()).hideLoading();
            }
        });
    }

    // --- 2. XEM CHI TIẾT ---
    private void showStoreDetail(String storeId) {
        if(getActivity() instanceof BaseActivity) ((BaseActivity)getActivity()).showLoading("Đang tải chi tiết...");
        apiService.getStoreDetail(storeId).enqueue(new Callback<StoreDetailResponse>() {
            @Override
            public void onResponse(@NonNull Call<StoreDetailResponse> call, @NonNull Response<StoreDetailResponse> response) {
                if(getActivity() instanceof BaseActivity) ((BaseActivity)getActivity()).hideLoading();
                if (response.isSuccessful() && response.body() != null) {
                    openDetailDialog(response.body().getData());
                }
            }
            @Override public void onFailure(@NonNull Call<StoreDetailResponse> call, @NonNull Throwable t) {
                if(getActivity() instanceof BaseActivity) ((BaseActivity)getActivity()).hideLoading();
            }
        });
    }

    private void openDetailDialog(StoreDetailResponse.StoreDetailData data) {
        Dialog dialog = new Dialog(requireContext());
        dialog.setContentView(R.layout.dialog_store_detail);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        TextView tvName = dialog.findViewById(R.id.tvDetailName);
        TextView tvAddress = dialog.findViewById(R.id.tvDetailAddress);
        TextView tvManager = dialog.findViewById(R.id.tvDetailManager);
        TextView tvPhone = dialog.findViewById(R.id.tvDetailPhone);
        TextView tvWarehouses = dialog.findViewById(R.id.tvDetailWarehouses);

        tvName.setText(data.getName());
        tvAddress.setText("Địa chỉ: " + data.getAddress());
        tvManager.setText("Quản lý: " + (data.getManagerName() != null ? data.getManagerName() : "N/A"));
        tvPhone.setText("SĐT: " + (data.getPhone() != null ? data.getPhone() : "N/A"));

        // Hiển thị danh sách kho
        if (data.getWarehouses() != null && !data.getWarehouses().isEmpty()) {
            StringBuilder whStr = new StringBuilder("Kho liên kết:\n");
            for (StoreDetailResponse.Warehouse wh : data.getWarehouses()) {
                whStr.append("- ").append(wh.getName()).append("\n");
            }
            tvWarehouses.setText(whStr.toString().trim());
        } else {
            tvWarehouses.setText("Chưa có kho liên kết.");
        }

        dialog.findViewById(R.id.btnClose).setOnClickListener(v -> dialog.dismiss());

        // Nút Cập nhật (Đóng detail, mở form edit)
        dialog.findViewById(R.id.btnEditStore).setOnClickListener(v -> {
            dialog.dismiss();
            showStoreFormDialog(data); // Truyền data cũ vào Form
        });

        // Nút Xóa/Ngưng hoạt động
        Button btnDelete = dialog.findViewById(R.id.btnDeleteStore);
        if (!data.isActive()) btnDelete.setVisibility(View.GONE); // Đã ngưng thì ẩn nút
        btnDelete.setOnClickListener(v -> {
            dialog.dismiss();
            deleteStore(data.getId());
        });

        dialog.show();
    }

    // --- 3 & 4. FORM TẠO / CẬP NHẬT ---
    private void showStoreFormDialog(@Nullable StoreDetailResponse.StoreDetailData existingData) {
        Dialog dialog = new Dialog(requireContext());
        dialog.setContentView(R.layout.dialog_store_form);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        TextView tvTitle = dialog.findViewById(R.id.tvFormTitle);
        EditText edtName = dialog.findViewById(R.id.edtName);
        EditText edtAddress = dialog.findViewById(R.id.edtAddress);
        EditText edtPhone = dialog.findViewById(R.id.edtPhone);
        EditText edtManager = dialog.findViewById(R.id.edtManager);

        boolean isUpdate = (existingData != null);

        // Nếu là Update -> Bơm dữ liệu cũ vào Form
        if (isUpdate) {
            tvTitle.setText("CẬP NHẬT CỬA HÀNG");
            edtName.setText(existingData.getName());
            edtAddress.setText(existingData.getAddress());
            if (existingData.getPhone() != null) edtPhone.setText(existingData.getPhone());
            if (existingData.getManagerName() != null) edtManager.setText(existingData.getManagerName());
        }

        dialog.findViewById(R.id.btnFormCancel).setOnClickListener(v -> dialog.dismiss());
        dialog.findViewById(R.id.btnFormSubmit).setOnClickListener(v -> {
            String name = edtName.getText().toString().trim();
            String address = edtAddress.getText().toString().trim();
            String phone = edtPhone.getText().toString().trim();
            String manager = edtManager.getText().toString().trim();

            if (name.isEmpty() || address.isEmpty()) {
                Toast.makeText(requireContext(), "Vui lòng nhập Tên và Địa chỉ!", Toast.LENGTH_SHORT).show();
                return;
            }

            StoreRequest request = new StoreRequest(name, address, phone, manager);
            if(getActivity() instanceof BaseActivity) ((BaseActivity)getActivity()).showLoading("Đang xử lý...");

            if (isUpdate) {
                // Gọi PATCH
                apiService.updateStore(existingData.getId(), request).enqueue(new Callback<ResponseBody>() {
                    @Override public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                        handleFormResponse(dialog, response, "Cập nhật thành công!");
                    }
                    @Override public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) { handleFormFailure(); }
                });
            } else {
                // Gọi POST
                apiService.createStore(request).enqueue(new Callback<ResponseBody>() {
                    @Override public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                        handleFormResponse(dialog, response, "Tạo cửa hàng thành công!");
                    }
                    @Override public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) { handleFormFailure(); }
                });
            }
        });

        dialog.show();
    }

    private void handleFormResponse(Dialog dialog, Response<ResponseBody> response, String successMsg) {
        if(getActivity() instanceof BaseActivity) ((BaseActivity)getActivity()).hideLoading();
        if (response.isSuccessful()) {
            if(isAdded()) Toast.makeText(requireContext(), successMsg, Toast.LENGTH_SHORT).show();
            dialog.dismiss();
            fetchStores(null); // Load lại danh sách
        } else {
            if(isAdded()) Toast.makeText(requireContext(), "Thao tác thất bại!", Toast.LENGTH_SHORT).show();
        }
    }

    private void handleFormFailure() {
        if(getActivity() instanceof BaseActivity) ((BaseActivity)getActivity()).hideLoading();
        if(isAdded()) Toast.makeText(requireContext(), "Lỗi mạng!", Toast.LENGTH_SHORT).show();
    }

    // --- 5. XÓA (NGƯNG HOẠT ĐỘNG) ---
    private void deleteStore(String storeId) {
        if(getActivity() instanceof BaseActivity) ((BaseActivity)getActivity()).showLoading("Đang xử lý...");
        apiService.deleteStore(storeId).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if(getActivity() instanceof BaseActivity) ((BaseActivity)getActivity()).hideLoading();
                if(response.isSuccessful()) {
                    if(isAdded()) Toast.makeText(requireContext(), "Đã ngưng hoạt động cửa hàng!", Toast.LENGTH_SHORT).show();
                    fetchStores(null); // Tải lại để cập nhật màu đỏ
                }
            }
            @Override public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                handleFormFailure();
            }
        });
    }
}
