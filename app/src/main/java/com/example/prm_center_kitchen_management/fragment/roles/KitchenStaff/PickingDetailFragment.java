package com.example.prm_center_kitchen_management.fragment.roles.KitchenStaff;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.prm_center_kitchen_management.R;
import com.example.prm_center_kitchen_management.adapter.PickingItemAdapter;
import com.example.prm_center_kitchen_management.api.ApiClient;
import com.example.prm_center_kitchen_management.api.ApiService;
import com.example.prm_center_kitchen_management.model.request.FinalizeBulkRequest;
import com.example.prm_center_kitchen_management.model.response.ApiResponse;
import com.example.prm_center_kitchen_management.model.response.PickingTaskDetail;
import com.example.prm_center_kitchen_management.model.response.PickingTaskItem;
import com.example.prm_center_kitchen_management.model.response.ScanCheckResponse;
import com.example.prm_center_kitchen_management.model.response.SuggestedBatch;
import com.example.prm_center_kitchen_management.utils.ApiUiHelper;
import com.example.prm_center_kitchen_management.utils.FefoViolationParser;
import com.google.android.material.button.MaterialButton;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Chi tiết soạn hàng: hiển thị FEFO → Check lấy hàng (GET scan-check theo batchCode) → Duyệt đơn (finalize-bulk).
 */
public class PickingDetailFragment extends Fragment {

    private static final int FEFO_MAX_AUTO_RETRIES = 5;
    private static final String ARG_TASK_ID = "task_id";
    private String taskId;
    private ApiService apiService;
    private RecyclerView rvPickingItems;
    private PickingItemAdapter adapter;
    private TextView tvOrderHeader;
    private TextView tvShipment;
    private MaterialButton btnCheckBatch;
    private MaterialButton btnFinalize;
    private ProgressBar progressBar;
    private Toolbar toolbar;

    private PickingTaskDetail loadedDetail;

    /** Sau khi Check lấy hàng — lưu kèm batchCode để xử lý lỗi FEFO khi finalize */
    private final List<PickedEntry> pickedEntriesPrepared = new ArrayList<>();

    private static final class PickedEntry {
        final String batchCode;
        final int batchId;
        final double quantity;

        PickedEntry(String batchCode, int batchId, double quantity) {
            this.batchCode = batchCode;
            this.batchId = batchId;
            this.quantity = quantity;
        }
    }

    public static PickingDetailFragment newInstance(String taskId) {
        PickingDetailFragment fragment = new PickingDetailFragment();
        Bundle args = new Bundle();
        args.putString(ARG_TASK_ID, taskId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            taskId = getArguments().getString(ARG_TASK_ID);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_picking_detail, container, false);
        initViews(view);
        apiService = ApiClient.getClient(requireContext()).create(ApiService.class);
        loadTaskDetail();

        btnCheckBatch.setOnClickListener(v -> checkPickupBatches());
        btnFinalize.setOnClickListener(v -> finalizeOrder());
        toolbar.setNavigationOnClickListener(v -> requireActivity().getOnBackPressedDispatcher().onBackPressed());

        return view;
    }

    private void initViews(View view) {
        rvPickingItems = view.findViewById(R.id.rvPickingItems);
        tvOrderHeader = view.findViewById(R.id.tvOrderHeader);
        tvShipment = view.findViewById(R.id.tvShipment);
        btnCheckBatch = view.findViewById(R.id.btnCheckBatch);
        btnFinalize = view.findViewById(R.id.btnFinalizeShipment);
        progressBar = view.findViewById(R.id.progressBar);
        toolbar = view.findViewById(R.id.toolbar);
        rvPickingItems.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    private void setBusy(boolean busy) {
        progressBar.setVisibility(busy ? View.VISIBLE : View.GONE);
        btnCheckBatch.setEnabled(!busy);
        btnFinalize.setEnabled(!busy && !pickedEntriesPrepared.isEmpty());
    }

    private void loadTaskDetail() {
        setBusy(true);
        btnFinalize.setEnabled(false);
        pickedEntriesPrepared.clear();
        apiService.getPickingTaskDetail(taskId).enqueue(new Callback<ApiResponse<PickingTaskDetail>>() {
            @Override
            public void onResponse(@NonNull Call<ApiResponse<PickingTaskDetail>> call, @NonNull Response<ApiResponse<PickingTaskDetail>> response) {
                setBusy(false);
                if (!response.isSuccessful() || response.body() == null || response.body().getData() == null) {
                    if (response.code() == 401 || response.code() == 403) {
                        ApiUiHelper.toastHttpError(requireContext(), response);
                    } else {
                        Toast.makeText(getContext(), "Không tải được chi tiết soạn hàng", Toast.LENGTH_SHORT).show();
                    }
                    return;
                }
                loadedDetail = response.body().getData();
                tvOrderHeader.setText("Đơn: " + (loadedDetail.getOrderId() != null ? loadedDetail.getOrderId() : taskId));
                String ship = loadedDetail.getShipmentId();
                tvShipment.setText(ship != null ? "Shipment: " + ship : "");

                adapter = new PickingItemAdapter(loadedDetail.getItems());
                rvPickingItems.setAdapter(adapter);
                btnFinalize.setEnabled(false);
            }

            @Override
            public void onFailure(@NonNull Call<ApiResponse<PickingTaskDetail>> call, @NonNull Throwable t) {
                setBusy(false);
                Toast.makeText(getContext(), "Lỗi mạng: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    /** Gọi GET /warehouse/scan-check cho từng batchCode trong gợi ý FEFO, hiển thị thông tin lô. */
    private void checkPickupBatches() {
        if (loadedDetail == null) {
            Toast.makeText(getContext(), "Chưa có dữ liệu", Toast.LENGTH_SHORT).show();
            return;
        }
        List<String> codes = new ArrayList<>();
        List<Double> qtys = new ArrayList<>();
        List<PickingTaskItem> items = loadedDetail.getItems();
        if (items != null) {
            for (PickingTaskItem item : items) {
                List<SuggestedBatch> batches = item.getSuggestedBatches();
                if (batches == null) continue;
                for (SuggestedBatch sb : batches) {
                    if (sb != null && sb.getBatchCode() != null && !sb.getBatchCode().isEmpty()) {
                        codes.add(sb.getBatchCode());
                        qtys.add(sb.getQuantityToPick());
                    }
                }
            }
        }
        if (codes.isEmpty()) {
            Toast.makeText(getContext(), "Không có mã lô trong gợi ý FEFO", Toast.LENGTH_SHORT).show();
            return;
        }

        pickedEntriesPrepared.clear();
        StringBuilder log = new StringBuilder();
        setBusy(true);
        runScanCheckChain(codes, qtys, 0, log);
    }

    private void runScanCheckChain(List<String> codes, List<Double> qtys, int index, StringBuilder log) {
        if (index >= codes.size()) {
            setBusy(false);
            new AlertDialog.Builder(requireContext())
                    .setTitle("Kết quả kiểm tra lô")
                    .setMessage(log.length() > 0 ? log.toString() : "Không có dữ liệu")
                    .setPositiveButton(android.R.string.ok, null)
                    .show();
            return;
        }

        String batchCode = codes.get(index);
        double qty = qtys.get(index);

        apiService.verifyBatchScan(batchCode).enqueue(new Callback<ApiResponse<ScanCheckResponse>>() {
            @Override
            public void onResponse(
                    @NonNull Call<ApiResponse<ScanCheckResponse>> call,
                    @NonNull Response<ApiResponse<ScanCheckResponse>> response
            ) {
                if (response.isSuccessful() && response.body() != null && response.body().getData() != null) {
                    ScanCheckResponse d = response.body().getData();
                    if (d.getBatchId() != null) {
                        pickedEntriesPrepared.add(new PickedEntry(batchCode, d.getBatchId(), qty));
                        log.append("\n———\nMã lô: ").append(batchCode)
                                .append("\nSản phẩm: ").append(nvl(d.getProductName()))
                                .append("\nbatchId: ").append(d.getBatchId())
                                .append("\nHSD: ").append(nvl(d.getExpiryDate()))
                                .append("\nSL tồn: ").append(d.getQuantityPhysical() != null ? d.getQuantityPhysical() : "—")
                                .append("\nTrạng thái: ").append(nvl(d.getStatus()))
                                .append("\nSL lấy (FEFO): ").append(qty);
                    } else {
                        log.append("\n———\n").append(batchCode).append(": thiếu batchId trong phản hồi");
                    }
                } else {
                    if (response.code() == 401 || response.code() == 403) {
                        setBusy(false);
                        ApiUiHelper.toastHttpError(requireContext(), response);
                        return;
                    }
                    log.append("\n———\n").append(batchCode).append(": lỗi ").append(response.code());
                }
                runScanCheckChain(codes, qtys, index + 1, log);
            }

            @Override
            public void onFailure(@NonNull Call<ApiResponse<ScanCheckResponse>> call, @NonNull Throwable t) {
                log.append("\n———\n").append(batchCode).append(": ").append(t.getMessage());
                runScanCheckChain(codes, qtys, index + 1, log);
            }
        });
    }

    private static String nvl(String s) {
        return s != null ? s : "—";
    }

    private void finalizeOrder() {
        finalizeOrderInternal(0);
    }

    private void finalizeOrderInternal(int fefoRetryDepth) {
        if (loadedDetail == null || loadedDetail.getOrderId() == null) {
            Toast.makeText(getContext(), "Thiếu mã đơn", Toast.LENGTH_SHORT).show();
            return;
        }
        if (pickedEntriesPrepared.isEmpty()) {
            Toast.makeText(getContext(), "Vui lòng bấm \"Check lấy hàng\" trước", Toast.LENGTH_SHORT).show();
            return;
        }
        if (fefoRetryDepth > FEFO_MAX_AUTO_RETRIES) {
            Toast.makeText(getContext(), "Đã thử điều chỉnh FEFO quá nhiều lần. Vui lòng \"Check lấy hàng\" lại.", Toast.LENGTH_LONG).show();
            return;
        }

        List<FinalizeBulkRequest.PickedLine> lines = new ArrayList<>();
        for (PickedEntry e : pickedEntriesPrepared) {
            lines.add(new FinalizeBulkRequest.PickedLine(e.batchId, e.quantity));
        }
        FinalizeBulkRequest body = new FinalizeBulkRequest(
                Collections.singletonList(new FinalizeBulkRequest.OrderEntry(loadedDetail.getOrderId(), lines))
        );
        setBusy(true);
        apiService.finalizeShipments(body).enqueue(new Callback<ApiResponse<Void>>() {
            @Override
            public void onResponse(@NonNull Call<ApiResponse<Void>> call, @NonNull Response<ApiResponse<Void>> response) {
                setBusy(false);
                if (response.isSuccessful()) {
                    Toast.makeText(getContext(), "Đã duyệt đơn", Toast.LENGTH_SHORT).show();
                    requireActivity().getSupportFragmentManager().popBackStack();
                    return;
                }
                if (response.code() == 401 || response.code() == 403) {
                    ApiUiHelper.toastHttpError(requireContext(), response);
                    return;
                }
                showFinalizeFailureDialog(response, fefoRetryDepth);
            }

            @Override
            public void onFailure(@NonNull Call<ApiResponse<Void>> call, @NonNull Throwable t) {
                setBusy(false);
                Toast.makeText(getContext(), "Lỗi mạng", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /** Hiển thị lỗi duyệt đơn: FEFO → modal xác nhận + chọn/sửa mã lô; còn lại → modal thông báo. */
    private void showFinalizeFailureDialog(@NonNull Response<ApiResponse<Void>> response, int fefoRetryDepth) {
        int code = response.code();
        String msg = ApiUiHelper.parseErrorMessage(response);
        if (code == 400 && msg != null && fefoRetryDepth < FEFO_MAX_AUTO_RETRIES) {
            String[] codes = FefoViolationParser.parseWrongAndCorrectBatchCodes(msg);
            if (codes != null) {
                int idx = indexOfBatchCode(codes[0]);
                if (idx >= 0) {
                    showFefoConfirmDialog(msg, codes[0], codes[1], fefoRetryDepth);
                } else {
                    showInfoDialog(
                            "Vi phạm FEFO",
                            msg + "\n\nKhông tìm thấy mã lô sai trong danh sách đã check. Vui lòng \"Check lấy hàng\" lại."
                    );
                }
                return;
            }
        }
        showInfoDialog(
                code == 400 ? "Không duyệt được" : "Lỗi duyệt đơn",
                msg != null ? msg : "Duyệt đơn thất bại (" + code + ")"
        );
    }

    private void showInfoDialog(String title, String message) {
        if (getContext() == null) return;
        new AlertDialog.Builder(requireContext())
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, null)
                .show();
    }

    /**
     * Modal: nội dung lỗi FEFO + ô nhập mã lô (mặc định lô đề xuất). Chỉ sau khi xác nhận mới scan & duyệt lại.
     */
    private void showFefoConfirmDialog(String serverMessage, String wrongBatchCode, String suggestedBatchCode, int fefoRetryDepth) {
        if (getContext() == null) return;
        View content = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_fefo_confirm, null);
        TextView tvMsg = content.findViewById(R.id.tvFefoMessage);
        EditText etCode = content.findViewById(R.id.etFefoBatchCode);
        tvMsg.setText(serverMessage);
        if (suggestedBatchCode != null && !suggestedBatchCode.isEmpty()) {
            etCode.setText(suggestedBatchCode);
            etCode.setSelection(etCode.getText().length());
        }

        AlertDialog dialog = new AlertDialog.Builder(requireContext())
                .setTitle("Vi phạm FEFO")
                .setView(content)
                .setPositiveButton("Xác nhận & duyệt đơn", null)
                .setNegativeButton(android.R.string.cancel, null)
                .create();

        dialog.setOnShowListener(d -> {
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v -> {
                String userCode = etCode.getText().toString().trim();
                if (userCode.isEmpty()) {
                    etCode.setError("Nhập mã lô");
                    return;
                }
                dialog.dismiss();
                applyFefoFixAfterConfirm(wrongBatchCode, userCode, fefoRetryDepth);
            });
        });
        dialog.show();
    }

    private int indexOfBatchCode(String batchCode) {
        if (batchCode == null) return -1;
        for (int i = 0; i < pickedEntriesPrepared.size(); i++) {
            if (batchCode.equalsIgnoreCase(pickedEntriesPrepared.get(i).batchCode)) {
                return i;
            }
        }
        return -1;
    }

    /** Sau khi user xác nhận trong modal: thay lô, scan-check, gọi lại finalize. */
    private void applyFefoFixAfterConfirm(String wrongBatchCode, String newBatchCode, int fefoRetryDepth) {
        int idx = indexOfBatchCode(wrongBatchCode);
        if (idx < 0) {
            showInfoDialog("Lỗi", "Danh sách lô đã thay đổi. Vui lòng \"Check lấy hàng\" lại.");
            return;
        }
        double qty = pickedEntriesPrepared.get(idx).quantity;
        pickedEntriesPrepared.remove(idx);
        final int insertIdx = idx;

        setBusy(true);
        apiService.verifyBatchScan(newBatchCode).enqueue(new Callback<ApiResponse<ScanCheckResponse>>() {
            @Override
            public void onResponse(
                    @NonNull Call<ApiResponse<ScanCheckResponse>> call,
                    @NonNull Response<ApiResponse<ScanCheckResponse>> response
            ) {
                setBusy(false);
                if (response.code() == 401 || response.code() == 403) {
                    ApiUiHelper.toastHttpError(requireContext(), response);
                    return;
                }
                if (!response.isSuccessful() || response.body() == null || response.body().getData() == null) {
                    showInfoDialog("Không kiểm tra được lô", "Mã: " + newBatchCode + "\nVui lòng thử lại hoặc kiểm tra kết nối.");
                    return;
                }
                ScanCheckResponse d = response.body().getData();
                if (d.getBatchId() == null) {
                    showInfoDialog("Thiếu batchId", "Phản hồi không có batchId cho lô " + newBatchCode);
                    return;
                }
                pickedEntriesPrepared.add(insertIdx, new PickedEntry(newBatchCode, d.getBatchId(), qty));
                Toast.makeText(getContext(), "Đã cập nhật lô. Đang duyệt đơn…", Toast.LENGTH_SHORT).show();
                finalizeOrderInternal(fefoRetryDepth + 1);
            }

            @Override
            public void onFailure(@NonNull Call<ApiResponse<ScanCheckResponse>> call, @NonNull Throwable t) {
                setBusy(false);
                showInfoDialog("Lỗi mạng", "Không kiểm tra được lô " + newBatchCode + ".\n" + t.getMessage());
            }
        });
    }

}
