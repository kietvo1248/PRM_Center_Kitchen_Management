package com.example.prm_center_kitchen_management.fragment.roles.KitchenStaff;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
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
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.example.prm_center_kitchen_management.R;
import com.example.prm_center_kitchen_management.adapter.roles.KitchenStaff.InboundReceiptAdapter;
import com.example.prm_center_kitchen_management.api.ApiClient;
import com.example.prm_center_kitchen_management.api.ApiService;
import com.example.prm_center_kitchen_management.model.request.AddReceiptItemRequest;
import com.example.prm_center_kitchen_management.model.request.CreateReceiptRequest;
import com.example.prm_center_kitchen_management.model.response.AddReceiptItemResponse;
import com.example.prm_center_kitchen_management.model.response.ApiResponse;
import com.example.prm_center_kitchen_management.model.response.InboundReceipt;
import com.example.prm_center_kitchen_management.model.response.PaginatedResponse;
import com.example.prm_center_kitchen_management.model.response.Product;
import com.example.prm_center_kitchen_management.model.response.ProductListResponse;
import com.example.prm_center_kitchen_management.model.response.Supplier;
import com.example.prm_center_kitchen_management.model.response.SupplierListResponse;
import com.example.prm_center_kitchen_management.utils.ApiUiHelper;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Module 8 — Nhập kho: danh sách phiếu + tạo draft + thêm dòng (MFG/EXP) + chốt hoàn tất.
 */
public class InboundFragment extends Fragment implements InboundReceiptAdapter.OnItemClickListener {

    private RecyclerView rvInboundReceipts;
    private InboundReceiptAdapter adapter;
    private final List<InboundReceipt> receiptList = new ArrayList<>();
    private ProgressBar progressBar;
    private SwipeRefreshLayout swipeRefresh;
    private FloatingActionButton fabCreateReceipt;
    private ApiService apiService;

    private List<Supplier> suppliersCache = new ArrayList<>();
    private List<Product> productsCache = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_inbound_list, container, false);
        initViews(view);
        apiService = ApiClient.getClient(requireContext()).create(ApiService.class);
        loadReceipts();

        swipeRefresh.setOnRefreshListener(this::loadReceipts);
        fabCreateReceipt.setOnClickListener(v -> showPickSupplierThenCreate());

        return view;
    }

    private void initViews(View view) {
        rvInboundReceipts = view.findViewById(R.id.rvInboundReceipts);
        progressBar = view.findViewById(R.id.progressBar);
        swipeRefresh = view.findViewById(R.id.swipeRefresh);
        fabCreateReceipt = view.findViewById(R.id.fabCreateReceipt);

        rvInboundReceipts.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new InboundReceiptAdapter(receiptList, this);
        rvInboundReceipts.setAdapter(adapter);
    }

    private void setLoading(boolean loading) {
        if (swipeRefresh != null && swipeRefresh.isRefreshing()) {
            progressBar.setVisibility(View.GONE);
            return;
        }
        progressBar.setVisibility(loading ? View.VISIBLE : View.GONE);
    }

    private void loadReceipts() {
        if (!swipeRefresh.isRefreshing()) {
            progressBar.setVisibility(View.VISIBLE);
        }
        apiService.getInboundReceipts(1, 50, null).enqueue(new Callback<ApiResponse<PaginatedResponse<InboundReceipt>>>() {
            @Override
            public void onResponse(@NonNull Call<ApiResponse<PaginatedResponse<InboundReceipt>>> call, @NonNull Response<ApiResponse<PaginatedResponse<InboundReceipt>>> response) {
                progressBar.setVisibility(View.GONE);
                swipeRefresh.setRefreshing(false);
                if (response.isSuccessful() && response.body() != null && response.body().getData() != null) {
                    PaginatedResponse<InboundReceipt> page = response.body().getData();
                    List<InboundReceipt> items = page.getItems() != null ? page.getItems() : new ArrayList<>();
                    receiptList.clear();
                    receiptList.addAll(items);
                    adapter.notifyDataSetChanged();
                } else {
                    if (response.code() == 401 || response.code() == 403) {
                        ApiUiHelper.toastHttpError(requireContext(), response);
                    } else {
                        Toast.makeText(getContext(), "Không tải được danh sách phiếu nhập", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ApiResponse<PaginatedResponse<InboundReceipt>>> call, @NonNull Throwable t) {
                progressBar.setVisibility(View.GONE);
                swipeRefresh.setRefreshing(false);
                Toast.makeText(getContext(), "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    /** B1: chọn NCC → POST phiếu draft */
    private void showPickSupplierThenCreate() {
        setLoading(true);
        apiService.getSuppliers(1, 100, "DESC", null, true).enqueue(new Callback<SupplierListResponse>() {
            @Override
            public void onResponse(@NonNull Call<SupplierListResponse> call, @NonNull Response<SupplierListResponse> response) {
                setLoading(false);
                if (!response.isSuccessful() || response.body() == null || response.body().getData() == null
                        || response.body().getData().getItems() == null) {
                    if (response.code() == 401 || response.code() == 403) {
                        ApiUiHelper.toastHttpError(requireContext(), response);
                    } else {
                        Toast.makeText(getContext(), "Không tải được nhà cung cấp", Toast.LENGTH_SHORT).show();
                    }
                    return;
                }
                suppliersCache = response.body().getData().getItems();
                String[] labels = new String[suppliersCache.size()];
                for (int i = 0; i < suppliersCache.size(); i++) {
                    labels[i] = suppliersCache.get(i).getName() + " (#" + suppliersCache.get(i).getId() + ")";
                }
                View form = getLayoutInflater().inflate(R.layout.dialog_pick_supplier, null);
                AutoCompleteTextView actv = form.findViewById(R.id.actvSupplier);
                TextInputEditText etNote = form.findViewById(R.id.etNote);
                actv.setAdapter(new ArrayAdapter<>(requireContext(), android.R.layout.simple_dropdown_item_1line, labels));

                new AlertDialog.Builder(requireContext())
                        .setTitle("Tạo phiếu nhập (draft)")
                        .setView(form)
                        .setPositiveButton("Tạo", (d, w) -> {
                            int idx = -1;
                            String picked = actv.getText() != null ? actv.getText().toString() : "";
                            for (int i = 0; i < labels.length; i++) {
                                if (labels[i].equals(picked)) {
                                    idx = i;
                                    break;
                                }
                            }
                            if (idx < 0) {
                                Toast.makeText(getContext(), "Chọn nhà cung cấp", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            int sid = suppliersCache.get(idx).getId();
                            String note = etNote.getText() != null ? etNote.getText().toString().trim() : null;
                            createDraftReceipt(sid, TextUtils.isEmpty(note) ? null : note);
                        })
                        .setNegativeButton(android.R.string.cancel, null)
                        .show();
            }

            @Override
            public void onFailure(@NonNull Call<SupplierListResponse> call, @NonNull Throwable t) {
                setLoading(false);
                Toast.makeText(getContext(), "Lỗi tải NCC", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void createDraftReceipt(int supplierId, String note) {
        setLoading(true);
        CreateReceiptRequest req = note != null ? new CreateReceiptRequest(supplierId, note) : new CreateReceiptRequest(supplierId);
        apiService.createReceipt(req).enqueue(new Callback<ApiResponse<InboundReceipt>>() {
            @Override
            public void onResponse(@NonNull Call<ApiResponse<InboundReceipt>> call, @NonNull Response<ApiResponse<InboundReceipt>> response) {
                setLoading(false);
                if (response.isSuccessful() && response.body() != null && response.body().getData() != null) {
                    InboundReceipt created = response.body().getData();
                    Toast.makeText(getContext(), "Đã tạo phiếu nháp", Toast.LENGTH_SHORT).show();
                    loadReceipts();
                    showAddItemDialog(created.getId());
                } else {
                    if (response.code() == 401 || response.code() == 403) {
                        ApiUiHelper.toastHttpError(requireContext(), response);
                    } else {
                        Toast.makeText(getContext(), "Không tạo được phiếu", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ApiResponse<InboundReceipt>> call, @NonNull Throwable t) {
                setLoading(false);
                Toast.makeText(getContext(), "Lỗi mạng", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /** B2: thêm dòng hàng — POST items (nhận batchCode để in QR) */
    private void showAddItemDialog(String receiptId) {
        setLoading(true);
        apiService.getProducts(1, 100, null, "DESC").enqueue(new Callback<ProductListResponse>() {
            @Override
            public void onResponse(@NonNull Call<ProductListResponse> call, @NonNull Response<ProductListResponse> response) {
                setLoading(false);
                if (!response.isSuccessful() || response.body() == null || response.body().getData() == null
                        || response.body().getData().getItems() == null) {
                    Toast.makeText(getContext(), "Không tải được sản phẩm", Toast.LENGTH_SHORT).show();
                    return;
                }
                productsCache = response.body().getData().getItems();
                showAddItemForm(receiptId);
            }

            @Override
            public void onFailure(@NonNull Call<ProductListResponse> call, @NonNull Throwable t) {
                setLoading(false);
                Toast.makeText(getContext(), "Lỗi tải sản phẩm", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showAddItemForm(String receiptId) {
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_add_batch, null);
        AutoCompleteTextView actvProduct = dialogView.findViewById(R.id.actvProduct);
        TextInputEditText etQuantity = dialogView.findViewById(R.id.etQuantity);
        MaterialButton btnSubmit = dialogView.findViewById(R.id.btnSubmit);

        String[] names = new String[productsCache.size()];
        for (int i = 0; i < productsCache.size(); i++) {
            names[i] = productsCache.get(i).getName() + " (#" + productsCache.get(i).getId() + ")";
        }
        actvProduct.setAdapter(new ArrayAdapter<>(requireContext(), android.R.layout.simple_dropdown_item_1line, names));


        AlertDialog dialog = new AlertDialog.Builder(requireContext())
                .setView(dialogView)
                .create();

        btnSubmit.setOnClickListener(v -> {
            String qtyStr = etQuantity.getText() != null ? etQuantity.getText().toString().trim() : "";
            if (qtyStr.isEmpty()) {
                Toast.makeText(getContext(), "Nhập số lượng", Toast.LENGTH_SHORT).show();
                return;
            }
            double quantity;
            try {
                quantity = Double.parseDouble(qtyStr.replace(",", "."));
            } catch (NumberFormatException e) {
                Toast.makeText(getContext(), "Số lượng không hợp lệ", Toast.LENGTH_SHORT).show();
                return;
            }

            int productId = -1;
            String picked = actvProduct.getText() != null ? actvProduct.getText().toString() : "";
            for (int i = 0; i < names.length; i++) {
                if (names[i].equals(picked)) {
                    productId = productsCache.get(i).getId();
                    break;
                }
            }
            if (productId < 0) {
                Toast.makeText(getContext(), "Chọn sản phẩm", Toast.LENGTH_SHORT).show();
                return;
            }


            setLoading(true);
            AddReceiptItemRequest request = new AddReceiptItemRequest(productId, quantity);
            apiService.addReceiptItem(receiptId, request).enqueue(new Callback<ApiResponse<AddReceiptItemResponse>>() {
                @Override
                public void onResponse(@NonNull Call<ApiResponse<AddReceiptItemResponse>> call, @NonNull Response<ApiResponse<AddReceiptItemResponse>> response) {
                    setLoading(false);
                    if (response.isSuccessful() && response.body() != null && response.body().getData() != null) {
                        AddReceiptItemResponse data = response.body().getData();
                        dialog.dismiss();
                        showBatchQrDialog(data.getBatchCode(), data.getWarning(), receiptId);
                        loadReceipts();
                    } else {
                        if (response.code() == 401 || response.code() == 403) {
                            ApiUiHelper.toastHttpError(requireContext(), response);
                        } else {
                            Toast.makeText(getContext(), "Thêm dòng thất bại", Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ApiResponse<AddReceiptItemResponse>> call, @NonNull Throwable t) {
                    setLoading(false);
                    Toast.makeText(getContext(), "Lỗi mạng", Toast.LENGTH_SHORT).show();
                }
            });
        });

        dialog.show();
    }



    private void showBatchQrDialog(String batchCode, String warning, String receiptId) {
        String msg = "Mã lô (QR): " + batchCode;
        if (!TextUtils.isEmpty(warning)) {
            msg += "\n\n" + warning;
        }
        new AlertDialog.Builder(requireContext())
                .setTitle("In tem QR")
                .setMessage(msg)
                .setPositiveButton("Thêm dòng khác", (d, w) -> showAddItemDialog(receiptId))
                .setNegativeButton("Hoàn tất nhập kho", (d, w) -> completeReceipt(receiptId))
                .setNeutralButton("Đóng", null)
                .show();
    }

    /** B3: PATCH complete — tồn kho + batch available */
    private void completeReceipt(String receiptId) {
        setLoading(true);
        apiService.completeReceipt(receiptId).enqueue(new Callback<ApiResponse<Void>>() {
            @Override
            public void onResponse(@NonNull Call<ApiResponse<Void>> call, @NonNull Response<ApiResponse<Void>> response) {
                setLoading(false);
                if (response.isSuccessful()) {
                    Toast.makeText(getContext(), "Đã hoàn tất nhập kho (completed)", Toast.LENGTH_LONG).show();
                    loadReceipts();
                } else {
                    if (response.code() == 401 || response.code() == 403) {
                        ApiUiHelper.toastHttpError(requireContext(), response);
                    } else {
                        Toast.makeText(getContext(), "Không chốt được phiếu", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ApiResponse<Void>> call, @NonNull Throwable t) {
                setLoading(false);
                Toast.makeText(getContext(), "Lỗi mạng", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onItemClick(InboundReceipt receipt) {
        if (receipt == null) return;
        String st = receipt.getStatus() != null ? receipt.getStatus() : "";
        if (!"draft".equalsIgnoreCase(st)) {
            Toast.makeText(getContext(), "Phiếu không ở trạng thái draft — không thêm dòng tại đây.", Toast.LENGTH_SHORT).show();
            return;
        }
        new AlertDialog.Builder(requireContext())
                .setTitle(receipt.getReceiptCode() != null ? receipt.getReceiptCode() : "Phiếu nhập")
                .setItems(new CharSequence[]{"Thêm dòng hàng", "Hoàn tất nhập kho"}, (d, which) -> {
                    if (which == 0) {
                        showAddItemDialog(receipt.getId());
                    } else {
                        completeReceipt(receipt.getId());
                    }
                })
                .show();
    }
}
