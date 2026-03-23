package com.example.prm_center_kitchen_management.fragment.roles.FranchiseStaff;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prm_center_kitchen_management.R;
import com.example.prm_center_kitchen_management.adapter.roles.FranchiseStaff.CatalogAdapter;
import com.example.prm_center_kitchen_management.adapter.roles.FranchiseStaff.ShipmentAdapter;
import com.example.prm_center_kitchen_management.api.ApiClient;
import com.example.prm_center_kitchen_management.api.ApiService;
import com.example.prm_center_kitchen_management.model.response.CatalogResponse;
import com.example.prm_center_kitchen_management.model.response.Shipment; // Đã đổi thành Shipment
import com.example.prm_center_kitchen_management.model.response.PaginatedResponse;
import com.example.prm_center_kitchen_management.model.response.ApiResponse;
import com.example.prm_center_kitchen_management.utils.SessionManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FranchiseStaffDashboardFragment extends Fragment {

    private RecyclerView rvCatalog, rvShipments;
    private CatalogAdapter catalogAdapter;
    private ShipmentAdapter shipmentAdapter;
    private ApiService apiService;
    private SessionManager sessionManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_franchise_staff_dashboard, container, false);

        rvCatalog = view.findViewById(R.id.rvCatalog);
        rvShipments = view.findViewById(R.id.rvShipments);

        rvCatalog.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        rvShipments.setLayoutManager(new LinearLayoutManager(getContext()));

        apiService = ApiClient.getClient(requireContext()).create(ApiService.class);
        sessionManager = new SessionManager(requireContext());

        loadCatalog();
        loadShipments();

        return view;
    }

    private void loadCatalog() {
        apiService.getCatalog(1, 10, "DESC", true).enqueue(new Callback<CatalogResponse>() {
            @Override
            public void onResponse(Call<CatalogResponse> call, Response<CatalogResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // response.body() chính là CatalogResponse
                    CatalogResponse catalogResponse = response.body();

                    // Kiểm tra null an toàn và đi sâu vào CatalogData để gọi getItems()
                    if (catalogResponse.getData() != null && catalogResponse.getData().getItems() != null) {
                        catalogAdapter = new CatalogAdapter(catalogResponse.getData().getItems());
                        rvCatalog.setAdapter(catalogAdapter);
                    }
                }
            }

            @Override
            public void onFailure(Call<CatalogResponse> call, Throwable t) {
                if (isAdded()) {
                    Toast.makeText(requireContext(), "Lỗi tải danh mục: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void loadShipments() {
        String storeId = sessionManager.getStoreId();
        if (storeId == null || storeId.isEmpty()) return;

        // FIX LỖI: Dùng API mới và Model Shipment
        apiService.getMyStoreShipments(storeId, "", 1, 5, "DESC", null).enqueue(new Callback<ApiResponse<PaginatedResponse<Shipment>>>() {
            @Override
            public void onResponse(Call<ApiResponse<PaginatedResponse<Shipment>>> call, Response<ApiResponse<PaginatedResponse<Shipment>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    PaginatedResponse<Shipment> paginated = response.body().getData();

                    if (paginated != null && paginated.getItems() != null) {
                        // FIX LỖI: Cập nhật hàm khởi tạo Adapter (chỉ còn 1 listener)
                        shipmentAdapter = new ShipmentAdapter(paginated.getItems(), shipment -> {
                            Toast.makeText(requireContext(), "Vui lòng qua tab Giao Nhận để xem chi tiết", Toast.LENGTH_SHORT).show();
                        });
                        rvShipments.setAdapter(shipmentAdapter);
                    } else {
                        if (isAdded()) Toast.makeText(requireContext(), "Không thể tải chuyến hàng", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<PaginatedResponse<Shipment>>> call, Throwable t) {
                if (isAdded()) Toast.makeText(requireContext(), "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}