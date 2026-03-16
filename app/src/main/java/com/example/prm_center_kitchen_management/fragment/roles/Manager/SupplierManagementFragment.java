package com.example.prm_center_kitchen_management.fragment.roles.Manager;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prm_center_kitchen_management.R;
import com.example.prm_center_kitchen_management.adapter.roles.Manager.SupplierAdapter;
import com.example.prm_center_kitchen_management.api.ApiClient;
import com.example.prm_center_kitchen_management.api.ApiService;
import com.example.prm_center_kitchen_management.model.request.SupplierRequest;
import com.example.prm_center_kitchen_management.model.response.Supplier;
import com.example.prm_center_kitchen_management.model.response.SupplierDetailResponse;
import com.example.prm_center_kitchen_management.model.response.SupplierListResponse;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
public class SupplierManagementFragment extends Fragment {
    private EditText etSearch;
    private Spinner spSort, spActive;
    private RecyclerView rvSuppliers;
    private SupplierAdapter adapter;
    private ApiService apiService;

    // Dữ liệu bộ lọc
    private final String[] sortLabels = {"Mới nhất (DESC)", "Cũ nhất (ASC)"};
    private final String[] sortValues = {"DESC", "ASC"};
    private final String[] statusLabels = {"Tất cả", "Đang hoạt động", "Ngừng hoạt động"};
    private final Boolean[] statusValues = {null, true, false};

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_supplier_management, container, false);

        apiService = ApiClient.getClient(requireContext()).create(ApiService.class);

        etSearch = view.findViewById(R.id.etSearchSupplier);
        spSort = view.findViewById(R.id.spSortOrder);
        spActive = view.findViewById(R.id.spIsActive);
        rvSuppliers = view.findViewById(R.id.rvSuppliers);
        Button btnSearch = view.findViewById(R.id.btnSearchSupplier);
        FloatingActionButton fabAdd = view.findViewById(R.id.fabAddSupplier);

        // Setup RecyclerView
        rvSuppliers.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new SupplierAdapter();
        rvSuppliers.setAdapter(adapter);

        setupFilters();

        btnSearch.setOnClickListener(v -> fetchSuppliers());
        fabAdd.setOnClickListener(v -> showFormDialog(null));

        return view;
    }

    private void setupFilters() {
        ArrayAdapter<String> sortAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_dropdown_item, sortLabels);
        spSort.setAdapter(sortAdapter);

        ArrayAdapter<String> statusAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_dropdown_item, statusLabels);
        spActive.setAdapter(statusAdapter);

        AdapterView.OnItemSelectedListener filterListener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                fetchSuppliers();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        };

        spSort.setOnItemSelectedListener(filterListener);
        spActive.setOnItemSelectedListener(filterListener);
    }

    private void fetchSuppliers() {
        String searchTxt = etSearch.getText().toString().trim();
        String searchParam = searchTxt.isEmpty() ? null : searchTxt;
        String sortOrder = sortValues[spSort.getSelectedItemPosition()];
        Boolean isActive = statusValues[spActive.getSelectedItemPosition()];

        // Mặc định gọi page 1, limit 50 để lấy đủ danh sách thao tác
        apiService.getSuppliers(1, 50, sortOrder, searchParam, isActive).enqueue(new Callback<SupplierListResponse>() {
            @Override
            public void onResponse(Call<SupplierListResponse> call, Response<SupplierListResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().getData() != null) {
                    adapter.setSuppliers(response.body().getData().getItems(), supplier -> getSupplierDetail(supplier.getId()));
                }
            }
            @Override
            public void onFailure(Call<SupplierListResponse> call, Throwable t) {
                Toast.makeText(getContext(), "Lỗi tải dữ liệu", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getSupplierDetail(int id) {
        apiService.getSupplierDetail(id).enqueue(new Callback<SupplierDetailResponse>() {
            @Override
            public void onResponse(Call<SupplierDetailResponse> call, Response<SupplierDetailResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    showDetailDialog(response.body().getData());
                }
            }
            @Override
            public void onFailure(Call<SupplierDetailResponse> call, Throwable t) {
                Toast.makeText(getContext(), "Lỗi lấy chi tiết", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showDetailDialog(Supplier supplier) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_supplier_detail, null);
        builder.setView(view);
        AlertDialog dialog = builder.create();

        TextView tvName = view.findViewById(R.id.tvDetName);
        TextView tvContact = view.findViewById(R.id.tvDetContact);
        TextView tvPhone = view.findViewById(R.id.tvDetPhone);
        TextView tvAddress = view.findViewById(R.id.tvDetAddress);
        TextView tvStatus = view.findViewById(R.id.tvDetStatus);
        Button btnEdit = view.findViewById(R.id.btnEditSupplier);
        Button btnClose = view.findViewById(R.id.btnCloseDetail);

        tvName.setText(supplier.getName());
        tvContact.setText("Liên hệ: " + supplier.getContactName());
        tvPhone.setText("SĐT: " + supplier.getPhone());
        tvAddress.setText("Địa chỉ: " + supplier.getAddress());
        tvStatus.setText("Trạng thái: " + (supplier.isActive() ? "Đang hoạt động" : "Ngừng hoạt động"));

        btnClose.setOnClickListener(v -> dialog.dismiss());
        btnEdit.setOnClickListener(v -> {
            dialog.dismiss();
            showFormDialog(supplier);
        });

        dialog.show();
    }

    private void showFormDialog(@Nullable Supplier supplier) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_supplier_form, null);
        builder.setView(view);
        AlertDialog dialog = builder.create();

        TextView tvTitle = view.findViewById(R.id.tvFormTitle);
        EditText etName = view.findViewById(R.id.etName);
        EditText etContact = view.findViewById(R.id.etContactName);
        EditText etPhone = view.findViewById(R.id.etPhone);
        EditText etAddress = view.findViewById(R.id.etAddress);
        CheckBox cbIsActive = view.findViewById(R.id.cbIsActive);
        Button btnSave = view.findViewById(R.id.btnSaveSupplier);

        boolean isUpdate = (supplier != null);
        if (isUpdate) {
            tvTitle.setText("Cập nhật Nhà Cung Cấp");
            etName.setText(supplier.getName());
            etContact.setText(supplier.getContactName());
            etPhone.setText(supplier.getPhone());
            etAddress.setText(supplier.getAddress());
            cbIsActive.setChecked(supplier.isActive());
        }

        btnSave.setOnClickListener(v -> {
            String name = etName.getText().toString().trim();
            String contact = etContact.getText().toString().trim();
            String phone = etPhone.getText().toString().trim();
            String address = etAddress.getText().toString().trim();
            boolean isActive = cbIsActive.isChecked();

            if (name.isEmpty() || contact.isEmpty() || phone.isEmpty() || address.isEmpty()) {
                Toast.makeText(getContext(), "Vui lòng nhập đủ thông tin", Toast.LENGTH_SHORT).show();
                return;
            }

            SupplierRequest request = new SupplierRequest(name, contact, phone, address, isActive);

            if (isUpdate) {
                // UPDATE (PATCH)
                apiService.updateSupplier(supplier.getId(), request).enqueue(new Callback<SupplierDetailResponse>() {
                    @Override
                    public void onResponse(Call<SupplierDetailResponse> call, Response<SupplierDetailResponse> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(getContext(), "Cập nhật thành công!", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                            fetchSuppliers(); // Refresh list
                        }
                    }
                    @Override
                    public void onFailure(Call<SupplierDetailResponse> call, Throwable t) {}
                });
            } else {
                // CREATE (POST)
                apiService.createSupplier(request).enqueue(new Callback<SupplierDetailResponse>() {
                    @Override
                    public void onResponse(Call<SupplierDetailResponse> call, Response<SupplierDetailResponse> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(getContext(), "Tạo mới thành công!", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                            fetchSuppliers(); // Refresh list
                        }
                    }
                    @Override
                    public void onFailure(Call<SupplierDetailResponse> call, Throwable t) {}
                });
            }
        });

        dialog.show();
    }
}
