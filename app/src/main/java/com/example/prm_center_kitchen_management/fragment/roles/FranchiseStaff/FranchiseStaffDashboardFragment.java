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
import com.example.prm_center_kitchen_management.activity.base.BaseActivity;
import com.example.prm_center_kitchen_management.adapter.roles.FranchiseStaff.CatalogAdapter;
import com.example.prm_center_kitchen_management.adapter.roles.FranchiseStaff.ShipmentAdapter;
import com.example.prm_center_kitchen_management.api.ApiClient;
import com.example.prm_center_kitchen_management.api.ApiService;
import com.example.prm_center_kitchen_management.model.response.CatalogResponse;
import com.example.prm_center_kitchen_management.model.response.ShipmentResponse;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
public class FranchiseStaffDashboardFragment extends Fragment{
    private RecyclerView rvCatalog, rvShipments;
    private CatalogAdapter catalogAdapter;
    private ShipmentAdapter shipmentAdapter;
    private ApiService apiService;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_franchise_staff_dashboard, container, false);

        rvCatalog = view.findViewById(R.id.rvCatalog);
        rvShipments = view.findViewById(R.id.rvShipments);

        // Setup LayoutManager: Catalog vuốt ngang, Shipment vuốt dọc
        rvCatalog.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
        rvShipments.setLayoutManager(new LinearLayoutManager(requireContext()));

        apiService = ApiClient.getClient(requireContext()).create(ApiService.class);

        // Gọi API song song
        fetchCatalog();
        fetchShipments();

        return view;
    }

    private void fetchCatalog() {
        if (getActivity() instanceof BaseActivity) {
            ((BaseActivity) getActivity()).showLoading("Đang tải dữ liệu...");
        }

        apiService.getCatalog(1, 10, "ASC", true).enqueue(new Callback<CatalogResponse>() {
            @Override
            public void onResponse(@NonNull Call<CatalogResponse> call, @NonNull Response<CatalogResponse> response) {
                if (getActivity() instanceof BaseActivity) ((BaseActivity) getActivity()).hideLoading();

                if (response.isSuccessful() && response.body() != null && response.body().getStatusCode() == 200) {
                    List<CatalogResponse.ProductItem> items = response.body().getData().getItems();
                    catalogAdapter = new CatalogAdapter(items);
                    rvCatalog.setAdapter(catalogAdapter);
                }
            }

            @Override
            public void onFailure(@NonNull Call<CatalogResponse> call, @NonNull Throwable t) {
                if (getActivity() instanceof BaseActivity) ((BaseActivity) getActivity()).hideLoading();
                // Bọc isAdded() để tránh lỗi văng app nếu user chuyển tab nhanh khi API đang gọi
                if (isAdded()) {
                    Toast.makeText(requireContext(), "Lỗi tải sản phẩm: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void fetchShipments() {
        // Lấy ngày đầu năm và ngày hiện tại
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Calendar cal = Calendar.getInstance();
        String toDate = sdf.format(cal.getTime());
        cal.set(Calendar.DAY_OF_YEAR, 1);
        String fromDate = sdf.format(cal.getTime());

        apiService.getMyShipments(1, 10, "DESC", fromDate, toDate).enqueue(new Callback<ShipmentResponse>() {
            @Override
            public void onResponse(@NonNull Call<ShipmentResponse> call, @NonNull Response<ShipmentResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().getStatusCode() == 200) {
                    List<ShipmentResponse.ShipmentItem> items = response.body().getData().getItems();
                    shipmentAdapter = new ShipmentAdapter(items);
                    rvShipments.setAdapter(shipmentAdapter);
                }
            }

            @Override
            public void onFailure(@NonNull Call<ShipmentResponse> call, @NonNull Throwable t) {
                if (isAdded()) {
                    Toast.makeText(requireContext(), "Lỗi tải lịch sử kho: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
