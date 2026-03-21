package com.example.prm_center_kitchen_management.fragment.roles.KitchenStaff;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.example.prm_center_kitchen_management.R;
import com.example.prm_center_kitchen_management.adapter.BatchAdapter;
import com.example.prm_center_kitchen_management.adapter.KitchenInventoryAdapter;
import com.example.prm_center_kitchen_management.api.ApiClient;
import com.example.prm_center_kitchen_management.api.ApiService;
import com.example.prm_center_kitchen_management.model.request.InventoryAdjustmentRequest;
import com.example.prm_center_kitchen_management.model.response.ApiResponse;
import com.example.prm_center_kitchen_management.model.response.Batch;
import com.example.prm_center_kitchen_management.model.response.KitchenInventoryItem;
import com.example.prm_center_kitchen_management.model.response.PaginatedResponse;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class KitchenInventoryFragment extends Fragment implements KitchenInventoryAdapter.OnItemClickListener {

    private static final String TAG = "KitchenInventory";
    private RecyclerView rvInventory;
    private KitchenInventoryAdapter adapter;
    private final List<KitchenInventoryItem> inventoryList = new ArrayList<>();
    private ProgressBar progressBar;
    private SwipeRefreshLayout swipeRefresh;
    private SearchView searchView;
    private ApiService apiService;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_kitchen_inventory, container, false);
        initViews(view);
        apiService = ApiClient.getClient(requireContext()).create(ApiService.class);
        loadInventory(null);

        swipeRefresh.setOnRefreshListener(() -> loadInventory(searchView.getQuery().toString()));
        setupSearch();
        return view;
    }

    private void initViews(View view) {
        rvInventory = view.findViewById(R.id.rvInventory);
        progressBar = view.findViewById(R.id.progressBar);
        swipeRefresh = view.findViewById(R.id.swipeRefresh);
        searchView = view.findViewById(R.id.searchView);

        rvInventory.setLayoutManager(new LinearLayoutManager(getContext()));
        // Khởi tạo adapter với list rỗng ban đầu
        adapter = new KitchenInventoryAdapter(inventoryList, this);
        rvInventory.setAdapter(adapter);
    }

    private void setupSearch() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                loadInventory(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.isEmpty()) loadInventory(null);
                return true;
            }
        });
    }

    private void loadInventory(String query) {
        if (!swipeRefresh.isRefreshing()) progressBar.setVisibility(View.VISIBLE);
        
        apiService.getKitchenInventory(1, 50, query, "productName").enqueue(new Callback<ApiResponse<PaginatedResponse<KitchenInventoryItem>>>() {
            @Override
            public void onResponse(@NonNull Call<ApiResponse<PaginatedResponse<KitchenInventoryItem>>> call, @NonNull Response<ApiResponse<PaginatedResponse<KitchenInventoryItem>>> response) {
                progressBar.setVisibility(View.GONE);
                swipeRefresh.setRefreshing(false);
                
                if (response.isSuccessful() && response.body() != null && response.body().getData() != null) {
                    List<KitchenInventoryItem> items = response.body().getData().getItems();
                    Log.d(TAG, "Loaded items: " + (items != null ? items.size() : 0));
                    
                    if (items != null) {
                        inventoryList.clear();
                        inventoryList.addAll(items);
                        adapter.notifyDataSetChanged();
                    }
                } else {
                    Log.e(TAG, "API Error: " + response.code());
                    Toast.makeText(getContext(), "Lỗi tải dữ liệu: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ApiResponse<PaginatedResponse<KitchenInventoryItem>>> call, @NonNull Throwable t) {
                progressBar.setVisibility(View.GONE);
                swipeRefresh.setRefreshing(false);
                Log.e(TAG, "Network Error: ", t);
                Toast.makeText(getContext(), "Lỗi kết nối mạng", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onCheckBatches(KitchenInventoryItem item) {
        showBatchesBottomSheet(item);
    }

    private void showBatchesBottomSheet(KitchenInventoryItem item) {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(requireContext());
        View bottomSheetView = getLayoutInflater().inflate(R.layout.fragment_inbound_list, null);
        
        RecyclerView rvBatches = bottomSheetView.findViewById(R.id.rvInboundReceipts);
        bottomSheetView.findViewById(R.id.fabCreateReceipt).setVisibility(View.GONE);
        bottomSheetView.findViewById(R.id.swipeRefresh).setEnabled(false);
        
        rvBatches.setLayoutManager(new LinearLayoutManager(getContext()));
        
        apiService.getProductBatches(item.getProductId()).enqueue(new Callback<ApiResponse<List<Batch>>>() {
            @Override
            public void onResponse(@NonNull Call<ApiResponse<List<Batch>>> call, @NonNull Response<ApiResponse<List<Batch>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Batch> batches = response.body().getData();
                    if (batches != null) {
                        Collections.sort(batches, (b1, b2) -> b1.getExpDate().compareTo(b2.getExpDate()));
                        BatchAdapter batchAdapter = new BatchAdapter(batches, batch -> {
                            bottomSheetDialog.dismiss();
                            showAdjustmentDialog(batch);
                        });
                        rvBatches.setAdapter(batchAdapter);
                        bottomSheetDialog.setContentView(bottomSheetView);
                        bottomSheetDialog.show();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ApiResponse<List<Batch>>> call, @NonNull Throwable t) {
                Toast.makeText(getContext(), "Không thể tải danh sách lô hàng", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showAdjustmentDialog(Batch batch) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_adjustment, null);
        builder.setView(dialogView);

        AutoCompleteTextView actvReason = dialogView.findViewById(R.id.actvReason);
        TextInputEditText etAdjustmentQty = dialogView.findViewById(R.id.etAdjustmentQty);
        MaterialButton btnConfirm = dialogView.findViewById(R.id.btnConfirmAdjustment);

        String[] reasons = {"damaged", "expired", "lost"};
        ArrayAdapter<String> reasonAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_dropdown_item_1line, reasons);
        actvReason.setAdapter(reasonAdapter);

        AlertDialog dialog = builder.create();

        btnConfirm.setOnClickListener(v -> {
            String qtyStr = etAdjustmentQty.getText().toString();
            if (qtyStr.isEmpty()) {
                etAdjustmentQty.setError("Vui lòng nhập số lượng");
                return;
            }

            double adjustmentQty = Double.parseDouble(qtyStr);
            if (adjustmentQty > batch.getQuantity()) {
                etAdjustmentQty.setError("Không thể điều chỉnh vượt quá số lượng hiện có (" + batch.getQuantity() + ")");
                return;
            }

            InventoryAdjustmentRequest request = new InventoryAdjustmentRequest(batch.getBatchCode(), adjustmentQty, actvReason.getText().toString());
            apiService.adjustInventory(request).enqueue(new Callback<ApiResponse<Void>>() {
                @Override
                public void onResponse(@NonNull Call<ApiResponse<Void>> call, @NonNull Response<ApiResponse<Void>> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(getContext(), "Điều chỉnh kho thành công", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                        loadInventory(searchView.getQuery().toString());
                    } else {
                        Toast.makeText(getContext(), "Điều chỉnh thất bại!", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ApiResponse<Void>> call, @NonNull Throwable t) {
                    Toast.makeText(getContext(), "Lỗi mạng!", Toast.LENGTH_SHORT).show();
                }
            });
        });

        dialog.show();
    }
}
