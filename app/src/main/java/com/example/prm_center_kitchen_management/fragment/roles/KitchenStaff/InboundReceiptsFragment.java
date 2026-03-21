package com.example.prm_center_kitchen_management.fragment.roles.KitchenStaff;

import android.os.Bundle;
import android.util.Log;
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
import com.example.prm_center_kitchen_management.adapter.InboundReceiptAdapter;
import com.example.prm_center_kitchen_management.api.ApiClient;
import com.example.prm_center_kitchen_management.api.ApiService;
import com.example.prm_center_kitchen_management.model.response.ApiResponse;
import com.example.prm_center_kitchen_management.model.response.InboundReceipt;
import com.example.prm_center_kitchen_management.model.response.PaginatedResponse;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InboundReceiptsFragment extends Fragment implements InboundReceiptAdapter.OnItemClickListener {

    private static final String TAG = "InboundReceipts";
    private RecyclerView rvReceipts;
    private InboundReceiptAdapter adapter;
    private final List<InboundReceipt> receiptList = new ArrayList<>();
    private ProgressBar progressBar;
    private SwipeRefreshLayout swipeRefresh;
    private ApiService apiService;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_inbound_receipts, container, false);
        
        initViews(view);
        apiService = ApiClient.getClient(requireContext()).create(ApiService.class);
        
        loadReceipts();
        
        swipeRefresh.setOnRefreshListener(this::loadReceipts);
        
        return view;
    }

    private void initViews(View view) {
        rvReceipts = view.findViewById(R.id.rvReceipts);
        progressBar = view.findViewById(R.id.progressBar);
        swipeRefresh = view.findViewById(R.id.swipeRefresh);
        
        rvReceipts.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new InboundReceiptAdapter(receiptList, this);
        rvReceipts.setAdapter(adapter);
    }

    private void loadReceipts() {
        if (!swipeRefresh.isRefreshing()) progressBar.setVisibility(View.VISIBLE);
        
        // Page 1, limit 20, all status
        apiService.getInboundReceipts(1, 20, null).enqueue(new Callback<ApiResponse<PaginatedResponse<InboundReceipt>>>() {
            @Override
            public void onResponse(@NonNull Call<ApiResponse<PaginatedResponse<InboundReceipt>>> call, @NonNull Response<ApiResponse<PaginatedResponse<InboundReceipt>>> response) {
                progressBar.setVisibility(View.GONE);
                swipeRefresh.setRefreshing(false);
                
                // DEBUG: In ra dữ liệu thô từ Server
                if (response.raw() != null) {
                    Log.d(TAG, "RAW RESPONSE: " + response.raw().toString());
                }

                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse<PaginatedResponse<InboundReceipt>> apiResponse = response.body();
                    PaginatedResponse<InboundReceipt> page = apiResponse.getData();
                    List<InboundReceipt> data = page != null && page.getItems() != null ? page.getItems() : null;
                    
                    Log.d(TAG, "Status Code: " + apiResponse.getStatusCode());
                    Log.d(TAG, "Data size: " + (data != null ? data.size() : "null"));

                    if (data != null) {
                        receiptList.clear();
                        receiptList.addAll(data);
                        adapter.notifyDataSetChanged();
                    } else {
                        Log.w(TAG, "Data field is null in ApiResponse");
                    }
                } else {
                    Log.e(TAG, "Response Error: " + response.code() + " " + response.message());
                    try {
                        if (response.errorBody() != null) {
                            Log.e(TAG, "Error Body: " + response.errorBody().string());
                        }
                    } catch (Exception e) { e.printStackTrace(); }
                    Toast.makeText(getContext(), "Không thể tải danh sách phiếu nhập", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ApiResponse<PaginatedResponse<InboundReceipt>>> call, @NonNull Throwable t) {
                progressBar.setVisibility(View.GONE);
                swipeRefresh.setRefreshing(false);
                Log.e(TAG, "Network Error: ", t);
                Toast.makeText(getContext(), "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onItemClick(InboundReceipt receipt) {
        Toast.makeText(getContext(), "Mở phiếu: " + receipt.getReceiptCode(), Toast.LENGTH_SHORT).show();
    }
}
