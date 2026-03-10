package com.example.prm_center_kitchen_management.fragment.roles.FranchiseStaff;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.example.prm_center_kitchen_management.adapter.roles.FranchiseStaff.StoreInventoryAdapter;
import com.example.prm_center_kitchen_management.api.ApiClient;
import com.example.prm_center_kitchen_management.api.ApiService;
import com.example.prm_center_kitchen_management.model.response.InventoryResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FranchiseStoreInventoryFragment extends Fragment {
    private RecyclerView rvInventory;
    private TextView tvEmptyState;
    private SearchView searchView;
    private StoreInventoryAdapter adapter;
    private ApiService apiService;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_franchise_store_inventory, container, false);

        rvInventory = view.findViewById(R.id.rvInventory);
        tvEmptyState = view.findViewById(R.id.tvEmptyState);
        searchView = view.findViewById(R.id.searchView);

        rvInventory.setLayoutManager(new LinearLayoutManager(requireContext()));
        apiService = ApiClient.getClient(requireContext()).create(ApiService.class);

        // Bắt sự kiện khi gõ chữ vào thanh tìm kiếm
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                fetchInventory(query); // Gọi API khi bấm nút "Enter/Search" trên bàn phím
                searchView.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Nếu người dùng xóa hết chữ thì load lại danh sách mặc định
                if (newText.isEmpty()) {
                    fetchInventory(null);
                }
                return true;
            }
        });

            fetchInventory(null);

        return view;
    }

        private void fetchInventory(String searchKeyword) {
            if (getActivity() instanceof BaseActivity) {
                ((BaseActivity) getActivity()).showLoading("Đang tải kho...");
            }

            // Truyền null cho tham số không dùng (Retrofit sẽ tự động bỏ qua)
            apiService.getStoreInventory(1, 50, null, "DESC", searchKeyword).enqueue(new Callback<InventoryResponse>() {
                @Override
                public void onResponse(@NonNull Call<InventoryResponse> call, @NonNull Response<InventoryResponse> response) {
                    if (getActivity() instanceof BaseActivity) ((BaseActivity) getActivity()).hideLoading();

                    if (response.isSuccessful() && response.body() != null && response.body().getStatusCode() == 200) {
                        List<InventoryResponse.InventoryItem> items = response.body().getData().getItems();

                        if (items != null && !items.isEmpty()) {
                            adapter = new StoreInventoryAdapter(items);
                            rvInventory.setAdapter(adapter);
                            rvInventory.setVisibility(View.VISIBLE);
                            tvEmptyState.setVisibility(View.GONE);
                        } else {
                            showEmptyState();
                        }
                    } else {
                        showEmptyState();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<InventoryResponse> call, @NonNull Throwable t) {
                    if (getActivity() instanceof BaseActivity) ((BaseActivity) getActivity()).hideLoading();
                    showEmptyState();
                    if (isAdded()) {
                        Toast.makeText(requireContext(), "Lỗi mạng: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    private void showEmptyState() {
        rvInventory.setVisibility(View.GONE);
        tvEmptyState.setVisibility(View.VISIBLE);
    }

}

