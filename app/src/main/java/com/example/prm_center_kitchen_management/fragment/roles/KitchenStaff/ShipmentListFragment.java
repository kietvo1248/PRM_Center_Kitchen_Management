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
import com.example.prm_center_kitchen_management.adapter.ShipmentAdapter;
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
        initViews(view);
        apiService = ApiClient.getClient(getContext()).create(ApiService.class);
        
        loadShipments();
        
        swipeRefresh.setOnRefreshListener(this::loadShipments);
        
        return view;
    }

    private void initViews(View view) {
        rvShipments = view.findViewById(R.id.rvShipments);
        progressBar = view.findViewById(R.id.progressBar);
        swipeRefresh = view.findViewById(R.id.swipeRefresh);

        rvShipments.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new ShipmentAdapter(shipmentList);
        rvShipments.setAdapter(adapter);
    }

    private void loadShipments() {
        progressBar.setVisibility(View.VISIBLE);
        // Lấy tất cả shipment của bếp trung tâm
        apiService.getKitchenShipments(null).enqueue(new Callback<ApiResponse<List<Shipment>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<Shipment>>> call, Response<ApiResponse<List<Shipment>>> response) {
                progressBar.setVisibility(View.GONE);
                swipeRefresh.setRefreshing(false);
                if (response.isSuccessful() && response.body() != null) {
                    shipmentList = response.body().getData();
                    adapter.updateData(shipmentList);
                } else {
                    Toast.makeText(getContext(), "Failed to load shipments", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<Shipment>>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                swipeRefresh.setRefreshing(false);
                Toast.makeText(getContext(), "Network error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
