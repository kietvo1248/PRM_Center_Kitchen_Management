package com.example.prm_center_kitchen_management.fragment.roles.FranchiseStaff;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prm_center_kitchen_management.R;
import com.example.prm_center_kitchen_management.activity.base.BaseActivity;
import com.example.prm_center_kitchen_management.adapter.roles.FranchiseStaff.StoreOrderAdapter;
import com.example.prm_center_kitchen_management.api.ApiClient;
import com.example.prm_center_kitchen_management.api.ApiService;
import com.example.prm_center_kitchen_management.model.request.CreateOrderRequest;
import com.example.prm_center_kitchen_management.model.response.CatalogResponse;
import com.example.prm_center_kitchen_management.model.response.OrderDetailResponse;
import com.example.prm_center_kitchen_management.model.response.OrderResponse;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FranchiseStoreOrdersFragment extends Fragment{
    private Spinner spinnerStatus, spinnerSort;
    private RecyclerView rvOrders;
    private StoreOrderAdapter orderAdapter;
    private ApiService apiService;
    private String currentStatus = null;
    private String currentSort = "DESC";
    private String selectedDeliveryDate = null;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_franchise_store_orders, container, false);

        spinnerStatus = view.findViewById(R.id.spinnerStatus);
        spinnerSort = view.findViewById(R.id.spinnerSort);
        rvOrders = view.findViewById(R.id.rvOrders);
        view.findViewById(R.id.fabAddOrder).setOnClickListener(v -> showCreateOrderDialog());

        rvOrders.setLayoutManager(new LinearLayoutManager(requireContext()));
        apiService = ApiClient.getClient(requireContext()).create(ApiService.class);

        setupFilters();
        return view;

    }

    private void setupFilters() {
        // Setup Status Spinner
        String[] statuses = {"Tất cả", "PENDING", "APPROVED", "DELIVERING", "COMPLETED", "CANCELLED"};
        ArrayAdapter<String> statusAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_dropdown_item, statuses);
        spinnerStatus.setAdapter(statusAdapter);

        // Setup Sort Spinner
        String[] sorts = {"Mới nhất (DESC)", "Cũ nhất (ASC)"};
        ArrayAdapter<String> sortAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_dropdown_item, sorts);
        spinnerSort.setAdapter(sortAdapter);

        spinnerStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                currentStatus = position == 0 ? null : statuses[position].toLowerCase();
                fetchOrders();
            }
            @Override public void onNothingSelected(AdapterView<?> parent) {}
        });

        spinnerSort.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                currentSort = position == 0 ? "DESC" : "ASC";
                fetchOrders();
            }
            @Override public void onNothingSelected(AdapterView<?> parent) {}
        });
    }
    private void fetchOrders() {
        apiService.getStoreOrders(1, 50, currentSort, currentStatus).enqueue(new Callback<OrderResponse>() {
            @Override
            public void onResponse(@NonNull Call<OrderResponse> call, @NonNull Response<OrderResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<OrderResponse.OrderItem> items = response.body().getData() != null ? response.body().getData().getItems() : new ArrayList<>();

                    // Sử dụng Adapter đã được tách riêng
                    orderAdapter = new StoreOrderAdapter(items, item -> showOrderDetail(item.getId()));
                    rvOrders.setAdapter(orderAdapter);
                }
            }
            @Override public void onFailure(@NonNull Call<OrderResponse> call, @NonNull Throwable t) {
                if(isAdded()) Toast.makeText(requireContext(), "Lỗi tải kho: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void showOrderDetail(String orderId) {
        if(getActivity() instanceof BaseActivity) ((BaseActivity)getActivity()).showLoading("Đang tải chi tiết...");
        apiService.getOrderDetail(orderId).enqueue(new Callback<OrderDetailResponse>() {
            @Override
            public void onResponse(@NonNull Call<OrderDetailResponse> call, @NonNull Response<OrderDetailResponse> response) {
                if(getActivity() instanceof BaseActivity) ((BaseActivity)getActivity()).hideLoading();
                if (response.isSuccessful() && response.body() != null) {
                    openDetailDialog(response.body().getData());
                }
            }
            @Override public void onFailure(@NonNull Call<OrderDetailResponse> call, @NonNull Throwable t) {
                if(getActivity() instanceof BaseActivity) ((BaseActivity)getActivity()).hideLoading();
                if(isAdded()) Toast.makeText(requireContext(), "Lỗi kết nối", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void openDetailDialog(OrderDetailResponse.OrderDetailData data) {
        Dialog dialog = new Dialog(requireContext());
        dialog.setContentView(R.layout.dialog_order_detail);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        TextView tvTitle = dialog.findViewById(R.id.tvDetailTitle);
        TextView tvStatus = dialog.findViewById(R.id.tvDetailStatus);
        LinearLayout layoutItems = dialog.findViewById(R.id.layoutItemsContainer);
        Button btnCancel = dialog.findViewById(R.id.btnCancelOrder);
        Button btnReceive = dialog.findViewById(R.id.btnReceiveOrder);

        tvTitle.setText("Mã Đơn: " + data.getId().substring(0, 8) + "...");
        tvStatus.setText("Trạng thái: " + data.getStatus().toUpperCase());

        // Hiện nút hủy nếu trạng thái pending
        if (!"pending".equalsIgnoreCase(data.getStatus())) {
            btnCancel.setVisibility(View.GONE);
        }
        if ("delivering".equalsIgnoreCase(data.getStatus()) || "in_transit".equalsIgnoreCase(data.getStatus())) {
            btnReceive.setVisibility(View.VISIBLE);
        } else {
            btnReceive.setVisibility(View.GONE);
        }

        // Đổ danh sách sản phẩm bằng code tự động vào ScrollView
        if (data.getItems() != null) {
            for (OrderDetailResponse.DetailItem item : data.getItems()) {
                TextView tvItem = new TextView(requireContext());
                tvItem.setText("- " + item.getProduct().getName() + " (SL: " + item.getQuantityRequested() + ")");
                tvItem.setPadding(0, 8, 0, 8);
                tvItem.setTextSize(14f);
                layoutItems.addView(tvItem);
            }
        }

        dialog.findViewById(R.id.btnCloseDetail).setOnClickListener(v -> dialog.dismiss());
        btnCancel.setOnClickListener(v -> {
            cancelOrder(data.getId());
            dialog.dismiss();
        });


        dialog.show();
    }


    // Create, Cancel

    private void showCreateOrderDialog() {
        if(getActivity() instanceof BaseActivity) ((BaseActivity)getActivity()).showLoading("Đang lấy DS sản phẩm...");

        // Cần gọi API Catalog trước để lấy list sản phẩm cho Dropdown
        apiService.getCatalog(1, 50, "ASC", true).enqueue(new Callback<CatalogResponse>() {
            @Override
            public void onResponse(@NonNull Call<CatalogResponse> call, @NonNull Response<CatalogResponse> response) {
                if(getActivity() instanceof BaseActivity) ((BaseActivity)getActivity()).hideLoading();
                if (response.isSuccessful() && response.body() != null && response.body().getData() != null) {
                    openCreateDialog(response.body().getData().getItems());
                }
            }
            @Override public void onFailure(@NonNull Call<CatalogResponse> call, @NonNull Throwable t) {
                if(getActivity() instanceof BaseActivity) ((BaseActivity)getActivity()).hideLoading();
            }
        });
    }

    private void openCreateDialog(List<CatalogResponse.ProductItem> products) {
        Dialog dialog = new Dialog(requireContext());
        dialog.setContentView(R.layout.dialog_create_order);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        Spinner spinnerProducts = dialog.findViewById(R.id.spinnerProducts);
        EditText edtQuantity = dialog.findViewById(R.id.edtQuantity);
        Button btnSelectDate = dialog.findViewById(R.id.btnSelectDate);

        // Map list tên hiển thị
        List<String> productNames = new ArrayList<>();
        for (CatalogResponse.ProductItem p : products) productNames.add(p.getName());
        spinnerProducts.setAdapter(new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_dropdown_item, productNames));

        btnSelectDate.setOnClickListener(v -> {
            Calendar c = Calendar.getInstance();
            DatePickerDialog dpd = new DatePickerDialog(requireContext(), (view, year, month, dayOfMonth) -> {
                Calendar selected = Calendar.getInstance();
                selected.set(year, month, dayOfMonth);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'00:00:00.000'Z'", Locale.getDefault());
                selectedDeliveryDate = sdf.format(selected.getTime());
                btnSelectDate.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
            }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));

            // Ép chọn ngày tương lai ít nhất 1 ngày
            dpd.getDatePicker().setMinDate(System.currentTimeMillis() + 24 * 60 * 60 * 1000);
            dpd.show();
        });

        dialog.findViewById(R.id.btnCancel).setOnClickListener(v -> dialog.dismiss());

        dialog.findViewById(R.id.btnSubmit).setOnClickListener(v -> {
            if (selectedDeliveryDate == null || edtQuantity.getText().toString().isEmpty()) {
                Toast.makeText(requireContext(), "Vui lòng điền đủ thông tin!", Toast.LENGTH_SHORT).show();
                return;
            }

            int quantity = Integer.parseInt(edtQuantity.getText().toString());
            int productId = products.get(spinnerProducts.getSelectedItemPosition()).getId();

            CreateOrderRequest.OrderItemRequest reqItem = new CreateOrderRequest.OrderItemRequest(productId, quantity);
            CreateOrderRequest request = new CreateOrderRequest(selectedDeliveryDate, Collections.singletonList(reqItem));

            if(getActivity() instanceof BaseActivity) ((BaseActivity)getActivity()).showLoading("Đang tạo đơn...");
            apiService.createOrder(request).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                    if(getActivity() instanceof BaseActivity) ((BaseActivity)getActivity()).hideLoading();
                    if(response.isSuccessful()) {
                        Toast.makeText(requireContext(), "Tạo đơn thành công!", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                        fetchOrders();
                    } else {
                        Toast.makeText(requireContext(), "Tạo đơn thất bại!", Toast.LENGTH_SHORT).show();
                    }
                }
                @Override public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                    if(getActivity() instanceof BaseActivity) ((BaseActivity)getActivity()).hideLoading();
                }
            });
        });
        dialog.show();
    }

    private void cancelOrder(String orderId) {
        apiService.cancelOrder(orderId).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if(response.isSuccessful()) {
                    if(isAdded()) Toast.makeText(requireContext(), "Đã hủy đơn hàng!", Toast.LENGTH_SHORT).show();
                    fetchOrders(); // Load lại list
                } else {
                    if(isAdded()) Toast.makeText(requireContext(), "Không thể hủy đơn này!", Toast.LENGTH_SHORT).show();
                }
            }
            @Override public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {}
        });
    }

}
