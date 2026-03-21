package com.example.prm_center_kitchen_management.fragment.roles.SupplyCoordinator;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prm_center_kitchen_management.R;
import com.example.prm_center_kitchen_management.activity.base.BaseActivity;
import com.example.prm_center_kitchen_management.adapter.roles.SupplyCoordinator.CoordinatorOrderAdapter;
import com.example.prm_center_kitchen_management.api.ApiClient;
import com.example.prm_center_kitchen_management.api.ApiService;
import com.example.prm_center_kitchen_management.model.response.CoordinatorOrderResponse;
import com.example.prm_center_kitchen_management.model.response.CoordinatorOrderReviewResponse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CoordinatorOrderManagementFragment extends Fragment {
    private RecyclerView rvOrders;
    private TextView tvEmpty;
    private ApiService apiService;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_coordinator_order_management, container, false);
        rvOrders = v.findViewById(R.id.rvCoordinatorOrders);
        tvEmpty = v.findViewById(R.id.tvCoordinatorOrderEmpty);
        rvOrders.setLayoutManager(new LinearLayoutManager(getContext()));
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        apiService = ApiClient.getClient(requireContext()).create(ApiService.class);
        loadOrders();
    }

    private void loadOrders() {
        if (getActivity() instanceof BaseActivity) {
            ((BaseActivity) getActivity()).showLoading("Đang tải...");
        }

        // Sử dụng Diamond Operator <>
        apiService.getCoordinatorOrders(1, 50, "DESC").enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<CoordinatorOrderResponse> call, @NonNull Response<CoordinatorOrderResponse> response) {
                if (getActivity() instanceof BaseActivity) ((BaseActivity) getActivity()).hideLoading();

                if (response.isSuccessful() && response.body() != null) {
                    List<CoordinatorOrderResponse.OrderItem> items = response.body().getData().getItems();
                    if (items.isEmpty()) {
                        tvEmpty.setVisibility(View.VISIBLE);
                        rvOrders.setVisibility(View.GONE);
                    } else {
                        rvOrders.setAdapter(new CoordinatorOrderAdapter(items, item -> showReviewDialog(item)));
                        tvEmpty.setVisibility(View.GONE);
                        rvOrders.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<CoordinatorOrderResponse> call, @NonNull Throwable t) {
                if (getActivity() instanceof BaseActivity) ((BaseActivity) getActivity()).hideLoading();
            }
        });
    }

    // Bỏ qua cảnh báo cộng chuỗi trực tiếp
    @SuppressLint("SetTextI18n")
    private void showReviewDialog(CoordinatorOrderResponse.OrderItem order) {
        Dialog dialog = new Dialog(requireContext());
        dialog.setContentView(R.layout.dialog_order_detail_coordinator);

        if (dialog.getWindow() != null) {
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }

        TextView tvTitle = dialog.findViewById(R.id.tvDialogTitle);
        LinearLayout container = dialog.findViewById(R.id.llOrderItems);
        Button btnApprove = dialog.findViewById(R.id.btnApprove);
        Button btnReject = dialog.findViewById(R.id.btnReject);

        tvTitle.setText("REVIEW ĐƠN: #" + order.getId().substring(0, 8).toUpperCase());

        // ==========================================
        // THÊM ĐOẠN LOGIC KIỂM TRA TRẠNG THÁI Ở ĐÂY
        // ==========================================
        String status = order.getStatus().toLowerCase();
        if (status.equals("approved") || status.equals("rejected")) {
            // Nếu đã duyệt hoặc từ chối -> Ẩn 2 nút đi
            btnApprove.setVisibility(View.GONE);
            btnReject.setVisibility(View.GONE);
        } else {
            // Nếu là pending (hoặc trạng thái khác) -> Hiện 2 nút lên
            btnApprove.setVisibility(View.VISIBLE);
            btnReject.setVisibility(View.VISIBLE);
        }
        apiService.getCoordinatorOrderReview(order.getId()).enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<CoordinatorOrderReviewResponse> call, @NonNull Response<CoordinatorOrderReviewResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    container.removeAllViews();
                    for (CoordinatorOrderReviewResponse.ReviewItem item : response.body().getData().getItems()) {
                        View v = LayoutInflater.from(getContext()).inflate(R.layout.item_coordinator_review_product, container, false);

                        TextView name = v.findViewById(R.id.tvProductName);
                        TextView info = v.findViewById(R.id.tvProductInfo);

                        name.setText(item.getProductName());
                        if (!item.isCanFulfill()) {
                            name.setTextColor(android.graphics.Color.RED);
                        } else {
                            name.setTextColor(android.graphics.Color.BLACK);
                        }

                        info.setText("Yêu cầu: " + item.getRequestedQty() + " | Tồn kho: " + item.getCurrentStock());
                        container.addView(v);
                    }
                    dialog.show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<CoordinatorOrderReviewResponse> call, @NonNull Throwable t) {
                Toast.makeText(getContext(), "Lỗi tải chi tiết đơn hàng", Toast.LENGTH_SHORT).show();
            }
        });

        btnApprove.setOnClickListener(v -> {
            Map<String, Boolean> body = new HashMap<>();
            body.put("force_approve", true);
            apiService.approveCoordinatorOrder(order.getId(), body).enqueue(new Callback<>() {
                @Override
                public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                    dialog.dismiss();
                    loadOrders();
                    Toast.makeText(getContext(), "Đã duyệt đơn hàng", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                    Toast.makeText(getContext(), "Lỗi kết nối", Toast.LENGTH_SHORT).show();
                }
            });
        });

        btnReject.setOnClickListener(v -> {
            Map<String, String> body = new HashMap<>();
            body.put("reason", "Từ chối do không đủ tồn kho");

            apiService.rejectCoordinatorOrder(order.getId(), body).enqueue(new Callback<>() {
                @Override
                public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                    dialog.dismiss();
                    loadOrders();
                    Toast.makeText(getContext(), "Đã từ chối đơn hàng", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                    Toast.makeText(getContext(), "Lỗi kết nối", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }
}