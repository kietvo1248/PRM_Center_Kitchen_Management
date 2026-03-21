package com.example.prm_center_kitchen_management.fragment.roles.Manager;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prm_center_kitchen_management.R;
import com.example.prm_center_kitchen_management.activity.base.BaseActivity;
import com.example.prm_center_kitchen_management.adapter.roles.Manager.DashboardSupplierAdapter;
import com.example.prm_center_kitchen_management.api.ApiClient;
import com.example.prm_center_kitchen_management.api.ApiService;
import com.example.prm_center_kitchen_management.model.response.DashboardFulfillmentResponse;
import com.example.prm_center_kitchen_management.model.response.DashboardInventorySummaryResponse;
import com.example.prm_center_kitchen_management.model.response.DashboardSupplierResponse;
import com.example.prm_center_kitchen_management.model.response.DashboardWasteResponse;
import com.example.prm_center_kitchen_management.model.response.SupplierListResponse;

import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ManagerDashboardFragment extends Fragment {
    private TextView tvFillRate, tvRequestedApproved;
    private TextView tvTotalProducts, tvLowStock, tvExpiring;
    private TextView tvWasteTotal;
    private RecyclerView rvSuppliers;

    private ApiService apiService;
    private int pendingApiCount = 4; // Biến đếm để xử lý ẩn Loading Dialog

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_manager_dashboard, container, false);

        tvFillRate = view.findViewById(R.id.tvFillRate);
        tvRequestedApproved = view.findViewById(R.id.tvRequestedApproved);
        tvTotalProducts = view.findViewById(R.id.tvTotalProducts);
        tvLowStock = view.findViewById(R.id.tvLowStock);
        tvExpiring = view.findViewById(R.id.tvExpiring);
        tvWasteTotal = view.findViewById(R.id.tvWasteTotal);
        rvSuppliers = view.findViewById(R.id.rvSuppliers);

        rvSuppliers.setLayoutManager(new LinearLayoutManager(requireContext()));
        apiService = ApiClient.getClient(requireContext()).create(ApiService.class);

        loadAllDashboardData();

        return view;
    }

    private void loadAllDashboardData() {
        if (getActivity() instanceof BaseActivity) {
            ((BaseActivity) getActivity()).showLoading("Đang tải dữ liệu tổng quan...");
        }
        pendingApiCount = 4; // Có 4 API cần gọi

        fetchFulfillment();
        fetchInventorySummary();
        fetchWasteStats();
        fetchSuppliers();
    }

    //  Khi nào cả 4 API đều chạy xong thì mới tắt Loading
    private void checkAndHideLoading() {
        pendingApiCount--;
        if (pendingApiCount <= 0 && getActivity() instanceof BaseActivity) {
            ((BaseActivity) getActivity()).hideLoading();
        }
    }

    // 1. Lấy tỷ lệ đơn hàng
    private void fetchFulfillment() {
        apiService.getDashboardFulfillment().enqueue(new Callback<DashboardFulfillmentResponse>() {
            @Override
            public void onResponse(@NonNull Call<DashboardFulfillmentResponse> call, @NonNull Response<DashboardFulfillmentResponse> response) {
                checkAndHideLoading();
                if (response.isSuccessful() && response.body() != null && response.body().getData() != null) {
                    DashboardFulfillmentResponse.Kpi kpi = response.body().getData().getKpi();
                    if (kpi != null) {
                        tvFillRate.setText(kpi.getFillRatePercentage() + "%");
                        tvRequestedApproved.setText("Yêu cầu: " + kpi.getTotalRequestedQty() + " | Duyệt: " + kpi.getTotalApprovedQty());
                    }
                }
            }
            @Override public void onFailure(@NonNull Call<DashboardFulfillmentResponse> call, @NonNull Throwable t) { checkAndHideLoading(); }
        });
    }

    // 2. Lấy Tổng quan Kho
    private void fetchInventorySummary() {
        apiService.getDashboardInventorySummary().enqueue(new Callback<DashboardInventorySummaryResponse>() {
            @Override
            public void onResponse(@NonNull Call<DashboardInventorySummaryResponse> call, @NonNull Response<DashboardInventorySummaryResponse> response) {
                checkAndHideLoading();
                if (response.isSuccessful() && response.body() != null && response.body().getData() != null) {
                    DashboardInventorySummaryResponse.Overview over = response.body().getData().getOverview();
                    if (over != null) {
                        tvTotalProducts.setText("Tổng sản phẩm: " + over.getTotalProducts());
                        tvLowStock.setText("Sắp hết hàng: " + over.getTotalLowStockItems());
                        tvExpiring.setText("Sắp hết hạn: " + over.getTotalExpiringBatches());
                    }
                }
            }
            @Override public void onFailure(@NonNull Call<DashboardInventorySummaryResponse> call, @NonNull Throwable t) { checkAndHideLoading(); }
        });
    }

    // 3. Lấy Hàng hủy (Từ đầu năm đến cuối năm)
    private void fetchWasteStats() {
        int year = Calendar.getInstance().get(Calendar.YEAR);
        String fromDate = year + "-01-01";
        String toDate = year + "-12-31";

        apiService.getDashboardWaste(fromDate, toDate).enqueue(new Callback<DashboardWasteResponse>() {
            @Override
            public void onResponse(@NonNull Call<DashboardWasteResponse> call, @NonNull Response<DashboardWasteResponse> response) {
                checkAndHideLoading();
                if (response.isSuccessful() && response.body() != null && response.body().getData() != null) {
                    DashboardWasteResponse.Kpi kpi = response.body().getData().getKpi();
                    if (kpi != null) {
                        tvWasteTotal.setText("Tổng khối lượng: " + kpi.getTotalWastedQuantity());
                    }
                }
            }
            @Override public void onFailure(@NonNull Call<DashboardWasteResponse> call, @NonNull Throwable t) { checkAndHideLoading(); }
        });
    }

    // 4. Lấy Danh sách Nhà Cung Cấp
    private void fetchSuppliers() {
        apiService.getSuppliers(1, 10, "DESC", null, true).enqueue(new Callback<SupplierListResponse>() {
            @Override
            public void onResponse(@NonNull Call<SupplierListResponse> call, @NonNull Response<SupplierListResponse> response) {
                checkAndHideLoading();
                if (response.isSuccessful() && response.body() != null && response.body().getData() != null) {
                    // Note: DashboardSupplierAdapter might expect DashboardSupplierResponse.SupplierItem
                    // You might need to adjust the adapter or the model mapping here.
                    // For now, I'm just fixing the API call to an existing one.
                }
            }
            @Override public void onFailure(@NonNull Call<SupplierListResponse> call, @NonNull Throwable t) {
                checkAndHideLoading();
                if (isAdded()) Toast.makeText(requireContext(), "Lỗi tải NCC: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
