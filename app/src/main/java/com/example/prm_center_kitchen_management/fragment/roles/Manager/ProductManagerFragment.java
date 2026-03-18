package com.example.prm_center_kitchen_management.fragment.roles.Manager;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prm_center_kitchen_management.R;
import com.example.prm_center_kitchen_management.adapter.roles.Manager.ProductAdapter;
import com.example.prm_center_kitchen_management.api.ApiClient;
import com.example.prm_center_kitchen_management.api.ApiService;
import com.example.prm_center_kitchen_management.model.request.ProductRequest;
import com.example.prm_center_kitchen_management.model.response.BaseUnit;
import com.example.prm_center_kitchen_management.model.response.BaseUnitListResponse;
import com.example.prm_center_kitchen_management.model.response.Product;
import com.example.prm_center_kitchen_management.model.response.ProductDetailResponse;
import com.example.prm_center_kitchen_management.model.response.ProductListResponse;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductManagerFragment extends Fragment {
    private RecyclerView rvProducts;
    private ProductAdapter productAdapter;
    private EditText etSearch;
    private final List<BaseUnit> baseUnitList = new ArrayList<>();
    private ApiService apiService;

    @NonNull
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_product_manager, container, false);
        apiService = ApiClient.getClient(requireContext()).create(ApiService.class);
        etSearch = view.findViewById(R.id.etSearchProduct);
        Button btnSearch = view.findViewById(R.id.btnSearchProduct);
        FloatingActionButton fabAdd = view.findViewById(R.id.fabAddProduct);
        rvProducts = view.findViewById(R.id.rvProducts);

        rvProducts.setLayoutManager(new LinearLayoutManager(getContext()));
        productAdapter = new ProductAdapter();
        rvProducts.setAdapter(productAdapter);

        fetchBaseUnits();
        fetchProducts();

        btnSearch.setOnClickListener(v -> fetchProducts());
        fabAdd.setOnClickListener(v -> showFormDialog(null));

        return view;
    }

    private void fetchBaseUnits() {
        apiService.getBaseUnits(1, 100).enqueue(new Callback<BaseUnitListResponse>() {
            @Override
            public void onResponse(Call<BaseUnitListResponse> call, Response<BaseUnitListResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().getData() != null) {
                    baseUnitList.clear();
                    baseUnitList.addAll(response.body().getData().getItems());
                }
            }
            @Override public void onFailure(Call<BaseUnitListResponse> call, Throwable t) {}
        });
    }

    private void fetchProducts() {
        String searchTxt = etSearch.getText().toString().trim();
        String searchParam = searchTxt.isEmpty() ? null : searchTxt;

        apiService.getProducts(1, 50, searchParam, "DESC").enqueue(new Callback<ProductListResponse>() {
            @Override
            public void onResponse(Call<ProductListResponse> call, Response<ProductListResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().getData() != null) {
                    productAdapter.setProducts(response.body().getData().getItems(), product -> getProductDetail(product.getId()));
                }
            }
            @Override
            public void onFailure(Call<ProductListResponse> call, Throwable t) {
                if (getContext() != null)
                    Toast.makeText(getContext(), "Lỗi tải sản phẩm", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getProductDetail(int id) {
        apiService.getProductDetail(id).enqueue(new Callback<ProductDetailResponse>() {
            @Override
            public void onResponse(Call<ProductDetailResponse> call, Response<ProductDetailResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    showDetailDialog(response.body().getData());
                }
            }
            @Override public void onFailure(Call<ProductDetailResponse> call, Throwable t) {}
        });
    }

    private void showDetailDialog(Product product) {
        if (getContext() == null) return;
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_product_detail, null);
        builder.setView(view);
        AlertDialog dialog = builder.create();

        ImageView ivImage = view.findViewById(R.id.ivDetImage);
        TextView tvName = view.findViewById(R.id.tvDetName);
        TextView tvSku = view.findViewById(R.id.tvDetSku);
        TextView tvUnit = view.findViewById(R.id.tvDetUnit);
        TextView tvShelfLife = view.findViewById(R.id.tvDetShelfLife);
        Button btnEdit = view.findViewById(R.id.btnEditProduct);
        Button btnClose = view.findViewById(R.id.btnCloseDetail);

        tvName.setText(product.getName());
        tvSku.setText("Mã SKU: " + product.getSku());
        tvUnit.setText("Đơn vị tính: " + (product.getBaseUnitName() != null ? product.getBaseUnitName() : "N/A"));
        tvShelfLife.setText("Hạn sử dụng: " + product.getShelfLifeDays() + " ngày");

        if (product.getImageUrl() != null && !product.getImageUrl().isEmpty()) {
            Picasso.get().load(product.getImageUrl()).into(ivImage);
        }

        btnClose.setOnClickListener(v -> dialog.dismiss());
        btnEdit.setOnClickListener(v -> {
            dialog.dismiss();
            showFormDialog(product);
        });

        dialog.show();
    }

    private void showFormDialog(@Nullable Product product) {
        if (getContext() == null) return;

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_product_form, null);
        builder.setView(view);
        AlertDialog dialog = builder.create();

        TextView tvTitle = view.findViewById(R.id.tvFormTitle);
        EditText etName = view.findViewById(R.id.etName);
        EditText etShelfLife = view.findViewById(R.id.etShelfLife);
        Spinner spBaseUnit = view.findViewById(R.id.spBaseUnit);
        Button btnSave = view.findViewById(R.id.btnSaveProduct);

        ArrayAdapter<BaseUnit> unitAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_dropdown_item, baseUnitList);
        spBaseUnit.setAdapter(unitAdapter);

        boolean isUpdate = (product != null);
        if (isUpdate) {
            tvTitle.setText("Cập nhật Sản Phẩm");
            etName.setText(product.getName());
            etShelfLife.setText(String.valueOf(product.getShelfLifeDays()));

            // Tìm và chọn đơn vị tính mặc định
            spBaseUnit.post(() -> {
                if (baseUnitList != null && !baseUnitList.isEmpty()) {
                    for (int i = 0; i < baseUnitList.size(); i++) {
                        BaseUnit unit = baseUnitList.get(i);
                        boolean match = false;
                        
                        Integer pUnitId = product.getBaseUnitId();
                        if (pUnitId != null && unit.getId() == pUnitId.intValue()) {
                            match = true;
                        } else if (product.getBaseUnitName() != null && unit.getName().trim().equalsIgnoreCase(product.getBaseUnitName().trim())) {
                            match = true;
                        }
                        
                        if (match) {
                            spBaseUnit.setSelection(i);
                            break;
                        }
                    }
                }
            });
        }

        btnSave.setOnClickListener(v -> {
            String name = etName.getText().toString().trim();
            String shelfLifeStr = etShelfLife.getText().toString().trim();
            BaseUnit selectedUnit = (BaseUnit) spBaseUnit.getSelectedItem();

            if (name.isEmpty() || shelfLifeStr.isEmpty() || selectedUnit == null) {
                Toast.makeText(getContext(), "Vui lòng nhập đủ thông tin", Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                int shelfLife = Integer.parseInt(shelfLifeStr);
                String imageUrl = isUpdate ? product.getImageUrl() : null;
                ProductRequest request = new ProductRequest(name, selectedUnit.getId(), shelfLife, imageUrl);

                if (isUpdate) {
                    apiService.updateProduct(product.getId(), request).enqueue(new Callback<ProductDetailResponse>() {
                        @Override
                        public void onResponse(Call<ProductDetailResponse> call, Response<ProductDetailResponse> response) {
                            if (response.isSuccessful()) {
                                Toast.makeText(getContext(), "Cập nhật thành công!", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                                fetchProducts();
                            } else {
                                Toast.makeText(getContext(), "Cập nhật thất bại", Toast.LENGTH_SHORT).show();
                            }
                        }
                        @Override public void onFailure(Call<ProductDetailResponse> call, Throwable t) {
                            Toast.makeText(getContext(), "Lỗi kết nối", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    apiService.createProduct(request).enqueue(new Callback<ProductDetailResponse>() {
                        @Override
                        public void onResponse(Call<ProductDetailResponse> call, Response<ProductDetailResponse> response) {
                            if (response.isSuccessful()) {
                                Toast.makeText(getContext(), "Tạo mới thành công!", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                                fetchProducts();
                            } else {
                                Toast.makeText(getContext(), "Tạo mới thất bại", Toast.LENGTH_SHORT).show();
                            }
                        }
                        @Override public void onFailure(Call<ProductDetailResponse> call, Throwable t) {
                            Toast.makeText(getContext(), "Lỗi kết nối", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            } catch (NumberFormatException e) {
                Toast.makeText(getContext(), "Hạn sử dụng phải là số", Toast.LENGTH_SHORT).show();
            }
        });

        dialog.show();
    }
}
