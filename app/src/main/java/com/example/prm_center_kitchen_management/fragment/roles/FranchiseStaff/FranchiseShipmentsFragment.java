package com.example.prm_center_kitchen_management.fragment.roles.FranchiseStaff;

import android.app.AlertDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prm_center_kitchen_management.R;
import com.example.prm_center_kitchen_management.adapter.roles.FranchiseStaff.ShipmentAdapter;
import com.example.prm_center_kitchen_management.adapter.roles.FranchiseStaff.ShipmentDetailAdapter;
import com.example.prm_center_kitchen_management.api.ApiClient;
import com.example.prm_center_kitchen_management.api.ApiService;
import com.example.prm_center_kitchen_management.model.response.ApiResponse;
import com.example.prm_center_kitchen_management.model.response.PaginatedResponse;
import com.example.prm_center_kitchen_management.model.response.ReceiveAllResponse;
import com.example.prm_center_kitchen_management.model.response.Shipment;
import com.example.prm_center_kitchen_management.model.response.ShipmentDetailResponse;
import com.example.prm_center_kitchen_management.utils.SessionManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FranchiseShipmentsFragment extends Fragment {

    private RecyclerView rvShipments;
    private ShipmentAdapter adapter;
    private ApiService apiService;
    private SessionManager sessionManager;
    private ProgressBar progressBar;
    private EditText etSearch;
    private Button btnSearch;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Sử dụng layout fragment_shipment_list.xml của bạn
        View view = inflater.inflate(R.layout.fragment_shipment_list, container, false);

        // Ánh xạ View khớp với file XML fragment_shipment_list.xml của bạn
        rvShipments = view.findViewById(R.id.rvShipments);
        progressBar = view.findViewById(R.id.progressBar);
        etSearch = view.findViewById(R.id.etSearch);
        btnSearch = view.findViewById(R.id.btnSearch);

        sessionManager = new SessionManager(requireContext());
        apiService = ApiClient.getClient(requireContext()).create(ApiService.class);

        rvShipments.setLayoutManager(new LinearLayoutManager(getContext()));

        // Khởi tạo Adapter với interface onItemClick mới (Rút gọn)
        adapter = new ShipmentAdapter(new ArrayList<>(), shipment -> fetchAndShowShipmentDetail(shipment.getId()));
        rvShipments.setAdapter(adapter);

        if (btnSearch != null) {
            btnSearch.setOnClickListener(v -> loadShipments(etSearch.getText().toString().trim()));
        }

        // Tải danh sách mặc định khi mở màn hình
        loadShipments("");
        return view;
    }

    private void loadShipments(String keyword) {
        String storeId = sessionManager.getStoreId();
        if (storeId == null || storeId.isEmpty()) {
            Toast.makeText(getContext(), "Không tìm thấy mã cửa hàng!", Toast.LENGTH_SHORT).show();
            return;
        }

        setLoading(true);

        apiService.getMyStoreShipments(storeId, keyword, 1, 100, "DESC", null).enqueue(new Callback<ApiResponse<PaginatedResponse<Shipment>>>() {
            @Override
            public void onResponse(Call<ApiResponse<PaginatedResponse<Shipment>>> call, Response<ApiResponse<PaginatedResponse<Shipment>>> response) {
                setLoading(false);
                if (response.isSuccessful() && response.body() != null) {
                    PaginatedResponse<Shipment> paginated = response.body().getData();
                    if (paginated != null && paginated.getItems() != null) {
                        adapter.updateData(paginated.getItems());
                        if (paginated.getItems().isEmpty()) {
                            Toast.makeText(getContext(), "Chưa có chuyến hàng nào", Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    Toast.makeText(getContext(), "Lỗi tải dữ liệu", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<ApiResponse<PaginatedResponse<Shipment>>> call, Throwable t) {
                setLoading(false);
                Toast.makeText(getContext(), "Lỗi mạng", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Hàm lấy chi tiết chuyến hàng để hiện popup
    private void fetchAndShowShipmentDetail(String shipmentId) {
        setLoading(true);
        apiService.getShipmentDetail(shipmentId).enqueue(new Callback<ApiResponse<ShipmentDetailResponse>>() {
            @Override
            public void onResponse(Call<ApiResponse<ShipmentDetailResponse>> call, Response<ApiResponse<ShipmentDetailResponse>> response) {
                setLoading(false);
                if (response.isSuccessful() && response.body() != null && response.body().getData() != null) {
                    showDetailDialog(response.body().getData());
                } else {
                    Toast.makeText(getContext(), "Không tải được chi tiết", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<ApiResponse<ShipmentDetailResponse>> call, Throwable t) {
                setLoading(false);
                Toast.makeText(getContext(), "Lỗi kết nối khi tải chi tiết", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Hàm hiển thị Popup Chi tiết
    private void showDetailDialog(ShipmentDetailResponse detail) {
        View view = getLayoutInflater().inflate(R.layout.dialog_shipment_detail, null);
        AlertDialog dialog = new AlertDialog.Builder(requireContext())
                .setView(view)
                .create();

        TextView tvOrderId = view.findViewById(R.id.tvDetailOrderId);
        TextView tvStatus = view.findViewById(R.id.tvDetailStatus);
        RecyclerView rvItems = view.findViewById(R.id.rvShipmentItems);
        Button btnClose = view.findViewById(R.id.btnCloseDetail);

        // Nút xác nhận nằm trong Popup chi tiết
        Button btnReceiveAll = view.findViewById(R.id.btnReceiveAllInDetail);

        tvOrderId.setText(" Order ID: " + detail.getOrderId());
        tvStatus.setText("Trạng thái: " + detail.getStatus());

        // --- FIX NGHIỆP VỤ Ở ĐÂY ---
        // Chỉ hiện nút "Xác nhận nhận đủ hàng" CHỈ KHI status là "in_transit"
        if ("in_transit".equalsIgnoreCase(detail.getStatus())) {
            btnReceiveAll.setVisibility(View.VISIBLE);
            tvStatus.setTextColor(Color.parseColor("#F57C00")); // Cam
        } else {
            btnReceiveAll.setVisibility(View.GONE);
            tvStatus.setTextColor(Color.parseColor("#388E3C")); // Xanh lá nếu hoàn tất
        }

        // Đổ danh sách sản phẩm trong chuyến hàng
        rvItems.setLayoutManager(new LinearLayoutManager(getContext()));
        if (detail.getItems() != null) {
            rvItems.setAdapter(new ShipmentDetailAdapter(detail.getItems()));
        }

        // Sự kiện click nút xác nhận hàng
        btnReceiveAll.setOnClickListener(v -> {
            new AlertDialog.Builder(getContext())
                    .setTitle("Xác nhận nhận đủ hàng")
                    .setMessage("Bạn chắc chắn đã nhận ĐỦ hàng cho chuyến hàng này?")
                    .setPositiveButton("Xác nhận", (dialogInterface, which) -> {
                        // Đóng Popup chi tiết trước khi gọi API
                        dialog.dismiss();
                        String shipmentIdToSend = detail.getId().trim();

                        // 2. In ra màn hình để kiểm chứng
                        Toast.makeText(getContext(), "Đang duyệt Shipment ID: \n" + shipmentIdToSend, Toast.LENGTH_LONG).show();
                        Log.d("DEBUG_RECEIVE", "Gửi API duyệt hàng với Shipment ID: " + shipmentIdToSend);
                        Log.d("DEBUG_RECEIVE", "Order ID của chuyến này là: " + detail.getOrderId());

                        // 3. Gọi API
                        receiveAll(shipmentIdToSend);
                    })
                    .setNegativeButton("Hủy", null)
                    .show();
        });

        btnClose.setOnClickListener(v -> dialog.dismiss());
        dialog.show();
    }

    // Hàm gọi API Xác nhận nhận đủ hàng
    private void receiveAll(String shipmentId) {
        setLoading(true);
        apiService.receiveAllShipment(shipmentId).enqueue(new Callback<ApiResponse<ReceiveAllResponse>>() {
            @Override
            public void onResponse(Call<ApiResponse<ReceiveAllResponse>> call, Response<ApiResponse<ReceiveAllResponse>> response) {
                setLoading(false);
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(getContext(), "Xác nhận nhận hàng thành công!", Toast.LENGTH_SHORT).show();
                    // Refresh lại list bên ngoài để cập nhật trạng thái mới
                    loadShipments("");
                } else {
                    Toast.makeText(getContext(), "Lỗi xác nhận nhận hàng: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<ApiResponse<ReceiveAllResponse>> call, Throwable t) {
                setLoading(false);
                Toast.makeText(getContext(), "Lỗi mạng khi xác nhận", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Hàm tiện ích ẩn/hiện loading
    private void setLoading(boolean isLoading) {
        if (progressBar != null) {
            progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
        }
    }
}