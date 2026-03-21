package com.example.prm_center_kitchen_management.fragment.roles.KitchenStaff;

import android.os.Bundle;
import android.util.Log;
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
import com.example.prm_center_kitchen_management.adapter.roles.KitchenStaff.WasteReportAdapter;
import com.example.prm_center_kitchen_management.api.ApiClient;
import com.example.prm_center_kitchen_management.api.ApiService;
import com.example.prm_center_kitchen_management.model.response.ApiResponse;
import com.example.prm_center_kitchen_management.model.response.InventorySummary;
import com.example.prm_center_kitchen_management.model.response.WasteReport;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class KitchenStaffDashboardFragment extends Fragment {

    private TextView tvPendingTasks, tvLowStock, tvNearExpiry;
    private RecyclerView rvWasteReports;
    private WasteReportAdapter adapter;
    private List<WasteReport> wasteReports = new ArrayList<>();
    private ApiService apiService;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_kitchen_staff_dashboard, container, false);
        initViews(view);
        // Dùng requireContext() an toàn hơn getContext()
        apiService = ApiClient.getClient(requireContext()).create(ApiService.class);

        loadStats();
        loadWasteReports();

        return view;
    }

    private void initViews(View view) {
        tvPendingTasks = view.findViewById(R.id.tvPendingTasks);
        tvLowStock = view.findViewById(R.id.tvLowStock);
        tvNearExpiry = view.findViewById(R.id.tvNearExpiry);
        rvWasteReports = view.findViewById(R.id.rvWasteReports);

        rvWasteReports.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new WasteReportAdapter(wasteReports);
        rvWasteReports.setAdapter(adapter);
    }

    private void loadStats() {
        apiService.getInventorySummary().enqueue(new Callback<ApiResponse<InventorySummary>>() {
            @Override
            public void onResponse(Call<ApiResponse<InventorySummary>> call, Response<ApiResponse<InventorySummary>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    InventorySummary summary = response.body().getData();
                    if (summary != null) {
                        tvPendingTasks.setText(String.valueOf(summary.getTotalPendingTasks()));
                        tvLowStock.setText(String.valueOf(summary.getLowStockItems()));
                        tvNearExpiry.setText(String.valueOf(summary.getNearExpiryBatches()));
                    } else {
                        Toast.makeText(getContext(), "Cấu trúc API thống kê không khớp (Data Null)", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getContext(), "Lỗi tải thống kê: " + response.code(), Toast.LENGTH_SHORT).show();
                    Log.e("API_ERROR", "Thống kê lỗi: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<InventorySummary>> call, Throwable t) {
                if (isAdded()) {
                    Toast.makeText(getContext(), "Mất kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void loadWasteReports() {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String toDate = sdf.format(cal.getTime());
        cal.add(Calendar.DAY_OF_YEAR, -7);
        String fromDate = sdf.format(cal.getTime());

        apiService.getExpiryAlerts(fromDate, toDate).enqueue(new Callback<ApiResponse<List<WasteReport>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<WasteReport>>> call, Response<ApiResponse<List<WasteReport>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<WasteReport> newReports = response.body().getData();
                    if (newReports != null) {
                        wasteReports = newReports;
                        adapter.updateData(wasteReports);
                    } else {
                        Toast.makeText(getContext(), "Cấu trúc API Báo cáo không khớp (Data Null)", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getContext(), "Lỗi tải báo cáo Waste: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<WasteReport>>> call, Throwable t) {
                if (isAdded()) {
                    Toast.makeText(getContext(), "Lỗi kết nối Waste Report", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}