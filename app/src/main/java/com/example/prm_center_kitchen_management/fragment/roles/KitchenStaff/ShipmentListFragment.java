package com.example.prm_center_kitchen_management.fragment.roles.KitchenStaff;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.example.prm_center_kitchen_management.R;
import com.example.prm_center_kitchen_management.adapter.roles.KitchenStaff.ShipmentAdapter;
import com.example.prm_center_kitchen_management.api.ApiClient;
import com.example.prm_center_kitchen_management.api.ApiService;
import com.example.prm_center_kitchen_management.model.response.ApiResponse;
import com.example.prm_center_kitchen_management.model.response.Shipment;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShipmentListFragment extends Fragment {

    private RecyclerView rvShipments;
    private ShipmentAdapter adapter;
    private List<Shipment> shipmentList = new ArrayList<>();
    private ProgressBar progressBar;
    private SwipeRefreshLayout swipeRefresh;
    private ApiService apiService;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shipment_list, container, false);
        apiService = ApiClient.getClient(requireContext()).create(ApiService.class);

        initViews(view);
        loadShipments();

        // Bổ sung sự kiện vuốt để làm mới (nếu layout có hỗ trợ)
        if (swipeRefresh != null) {
            swipeRefresh.setOnRefreshListener(this::loadShipments);
        }

        return view;
    }

    private void initViews(View view) {
        rvShipments = view.findViewById(R.id.rvShipments);
        progressBar = view.findViewById(R.id.progressBar);
        swipeRefresh = view.findViewById(R.id.swipeRefresh);

        rvShipments.setLayoutManager(new LinearLayoutManager(getContext()));

        // FIX LỖI: Thêm OnItemClickListener vào Constructor của Adapter
        adapter = new ShipmentAdapter(shipmentList, shipment -> {
            Toast.makeText(getContext(), "Đang xem chuyến: " + shipment.getId(), Toast.LENGTH_SHORT).show();
            // Nếu bạn có làm chức năng Xem chi tiết cho Bếp Trung Tâm sau này thì gọi tại đây
        });

        rvShipments.setAdapter(adapter);
    }

    private void loadShipments() {
        if (progressBar != null) progressBar.setVisibility(View.VISIBLE);

        apiService.getKitchenShipments(null).enqueue(new Callback<ApiResponse<List<Shipment>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<Shipment>>> call, Response<ApiResponse<List<Shipment>>> response) {
                if (progressBar != null) progressBar.setVisibility(View.GONE);
                if (swipeRefresh != null) swipeRefresh.setRefreshing(false);

                if (response.isSuccessful() && response.body() != null) {
                    shipmentList = response.body().getData();
                    adapter.updateData(shipmentList);
                } else {
                    Toast.makeText(getContext(), "Lỗi tải chuyến hàng", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<Shipment>>> call, Throwable t) {
                if (progressBar != null) progressBar.setVisibility(View.GONE);
                if (swipeRefresh != null) swipeRefresh.setRefreshing(false);
                Toast.makeText(getContext(), "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}