package com.example.prm_center_kitchen_management.fragment.roles.SupplyCoordinator;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
import com.example.prm_center_kitchen_management.adapter.roles.SupplyCoordinator.CoordinatorShipmentAdapter;
import com.example.prm_center_kitchen_management.api.ApiClient;
import com.example.prm_center_kitchen_management.api.ApiService;
import com.example.prm_center_kitchen_management.model.response.CoordinatorPickingListResponse;
import com.example.prm_center_kitchen_management.model.response.CoordinatorShipmentResponse;
import com.squareup.picasso.Picasso;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
public class CoordinatorShipmentManagementFragment extends Fragment {
    private RecyclerView rvShipments;
    private TextView tvEmpty;
    private ApiService apiService;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_coordinator_shipment_management, container, false);
        rvShipments = v.findViewById(R.id.rvCoordinatorShipments);
        tvEmpty = v.findViewById(R.id.tvCoordinatorEmpty);
        rvShipments.setLayoutManager(new LinearLayoutManager(getContext()));
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        apiService = ApiClient.getClient(requireContext()).create(ApiService.class);
        loadShipments();
    }

    private void loadShipments() {
        if (getActivity() instanceof BaseActivity) ((BaseActivity) getActivity()).showLoading("Đang tải...");
        apiService.getCoordinatorShipments(1, 50, "DESC").enqueue(new Callback<CoordinatorShipmentResponse>() {
            @Override
            public void onResponse(Call<CoordinatorShipmentResponse> call, Response<CoordinatorShipmentResponse> response) {
                if (getActivity() instanceof BaseActivity) ((BaseActivity) getActivity()).hideLoading();
                if (response.isSuccessful() && response.body() != null) {
                    List<CoordinatorShipmentResponse.ShipmentItem> items = response.body().getData().getItems();
                    if (items.isEmpty()) {
                        tvEmpty.setVisibility(View.VISIBLE);
                        rvShipments.setVisibility(View.GONE);
                    } else {
                        rvShipments.setAdapter(new CoordinatorShipmentAdapter(items, item -> showPickingList(item)));
                        tvEmpty.setVisibility(View.GONE);
                        rvShipments.setVisibility(View.VISIBLE);
                    }
                }
            }
            @Override public void onFailure(Call<CoordinatorShipmentResponse> call, Throwable t) {
                if (getActivity() instanceof BaseActivity) ((BaseActivity) getActivity()).hideLoading();
            }
        });
    }

    private void showPickingList(CoordinatorShipmentResponse.ShipmentItem shipment) {
        Dialog dialog = new Dialog(requireContext());
        dialog.setContentView(R.layout.dialog_order_detail_coordinator);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        ((TextView) dialog.findViewById(R.id.tvDialogTitle)).setText("PICKING LIST: #" + shipment.getId().substring(0, 8).toUpperCase());
        LinearLayout container = dialog.findViewById(R.id.llOrderItems);
        if (dialog.findViewById(R.id.btnApprove) != null) dialog.findViewById(R.id.btnApprove).setVisibility(View.GONE);
        if (dialog.findViewById(R.id.btnReject) != null) dialog.findViewById(R.id.btnReject).setVisibility(View.GONE);

        apiService.getCoordinatorPickingList(shipment.getId()).enqueue(new Callback<CoordinatorPickingListResponse>() {
            @Override
            public void onResponse(Call<CoordinatorPickingListResponse> call, Response<CoordinatorPickingListResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    container.removeAllViews();
                    for (CoordinatorPickingListResponse.PickingItem item : response.body().getData().getItems()) {
                        View v = LayoutInflater.from(getContext()).inflate(R.layout.item_picking_product, container, false);
                        ((TextView) v.findViewById(R.id.tvProductName)).setText(item.getProductName());
                        ((TextView) v.findViewById(R.id.tvProductInfo)).setText("SKU: " + item.getSku() + " | Lô: " + item.getBatchCode());
                        ((TextView) v.findViewById(R.id.tvQuantity)).setText("x" + item.getQuantity());
                        Picasso.get().load(item.getImageUrl()).into((ImageView) v.findViewById(R.id.ivProductThumb));
                        container.addView(v);
                    }
                    dialog.show();
                }
            }
            @Override public void onFailure(Call<CoordinatorPickingListResponse> call, Throwable t) {
                Toast.makeText(getContext(), "Lỗi tải dữ liệu", Toast.LENGTH_SHORT).show();
            }
        });
    }
}