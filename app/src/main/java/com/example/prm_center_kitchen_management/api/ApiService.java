package com.example.prm_center_kitchen_management.api;

import com.example.prm_center_kitchen_management.model.request.LoginRequest;
import com.example.prm_center_kitchen_management.model.request.RefreshTokenRequest;
import com.example.prm_center_kitchen_management.model.response.RefreshTokenResponse;
import com.example.prm_center_kitchen_management.model.request.ProfileUpdateRequest;
import com.example.prm_center_kitchen_management.model.response.LoginResponse;
import com.example.prm_center_kitchen_management.model.response.UserProfileResponse;
import com.example.prm_center_kitchen_management.model.response.CatalogResponse;
import com.example.prm_center_kitchen_management.model.response.ShipmentResponse;
import com.example.prm_center_kitchen_management.model.request.CreateOrderRequest;
import com.example.prm_center_kitchen_management.model.response.OrderResponse;
import com.example.prm_center_kitchen_management.model.response.OrderDetailResponse;
import com.example.prm_center_kitchen_management.model.response.InventoryResponse;
import com.example.prm_center_kitchen_management.model.response.DashboardSupplierResponse;
import com.example.prm_center_kitchen_management.model.response.DashboardWasteResponse;
import com.example.prm_center_kitchen_management.model.response.DashboardFulfillmentResponse;
import com.example.prm_center_kitchen_management.model.response.DashboardInventorySummaryResponse;
import com.example.prm_center_kitchen_management.model.response.StoreDetailResponse;
import com.example.prm_center_kitchen_management.model.response.StoreResponse;
import com.example.prm_center_kitchen_management.model.request.StoreRequest;
import com.example.prm_center_kitchen_management.model.request.SupplierRequest;
import com.example.prm_center_kitchen_management.model.response.SupplierListResponse;
import com.example.prm_center_kitchen_management.model.response.SupplierDetailResponse;
import com.example.prm_center_kitchen_management.model.request.ProductRequest;
import com.example.prm_center_kitchen_management.model.response.ProductListResponse;
import com.example.prm_center_kitchen_management.model.response.ProductDetailResponse;
import com.example.prm_center_kitchen_management.model.response.BaseUnitListResponse;


import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.DELETE;

public interface ApiService {
    // Auth & General
    @POST("auth/login")
    Call<LoginResponse> login(@Body LoginRequest request);

    @POST("auth/refresh-token")
    Call<RefreshTokenResponse> refreshToken(@Body RefreshTokenRequest request);

    @GET("auth/me")
    Call<UserProfileResponse> getProfile();

    @PATCH("auth/profile")
    Call<UserProfileResponse> updateProfile(@Body ProfileUpdateRequest request);

    // Catalog
    @GET("orders/catalog") // franchise staff
    Call<CatalogResponse> getCatalog(
            @Query("page") int page,
            @Query("limit") int limit,
            @Query("sortOrder") String sortOrder,
            @Query("isActive") boolean isActive
    );

    @GET("shipments/store/my") // franchise staff
    Call<ShipmentResponse> getMyShipments(
            @Query("page") int page,
            @Query("limit") int limit,
            @Query("sortOrder") String sortOrder,
            @Query("fromDate") String fromDate,
            @Query("toDate") String toDate
    );

    // Manage Order in my store
    @GET("orders/my-store")
    Call<OrderResponse> getStoreOrders(
            @Query("page") int page,
            @Query("limit") int limit,
            @Query("sortOrder") String sortOrder,
            @Query("status") String status
    );

    @GET("orders/{id}")
    Call<OrderDetailResponse> getOrderDetail(@Path("id") String id);

    @POST("orders")
    Call<ResponseBody> createOrder(@Body CreateOrderRequest request);

    @PATCH("orders/franchise/{id}/cancel")
    Call<ResponseBody> cancelOrder(@Path("id") String id);
    @POST("shipments/{id}/receive-all")
    Call<ResponseBody> receiveAllShipment(@Path("id") String id);

    @GET("inventory/store")
    Call<InventoryResponse> getStoreInventory(
            @Query("page") int page,
            @Query("limit") int limit,
            @Query("sortBy") String sortBy,
            @Query("sortOrder") String sortOrder,
            @Query("search") String search);


    // Manager

    @GET("orders/analytics/fulfillment-rate")
    Call<DashboardFulfillmentResponse> getDashboardFulfillment();

    @GET("inventory/analytics/summary")
    Call<DashboardInventorySummaryResponse> getDashboardInventorySummary();

    @GET("inventory/analytics/waste")
    Call<DashboardWasteResponse> getDashboardWaste(
            @Query("fromDate") String fromDate,
            @Query("toDate") String toDate
    );

    @GET("suppliers")
    Call<DashboardSupplierResponse> getDashboardSuppliers(
            @Query("page") int page,
            @Query("limit") int limit,
            @Query("sortOrder") String sortOrder,
            @Query("isActive") boolean isActive
    );

    // manage Store

    @GET("stores")
    Call<StoreResponse> getStores(
            @Query("page") int page,
            @Query("limit") int limit,
            @Query("sortOrder") String sortOrder,
            @Query("search") String search,
            @Query("isActive") Boolean isActive
    );

    @GET("stores/{id}")
    Call<StoreDetailResponse> getStoreDetail(@Path("id") String id);

    @POST("stores")
    Call<ResponseBody> createStore(@Body StoreRequest request);

    @PATCH("stores/{id}")
    Call<ResponseBody> updateStore(@Path("id") String id, @Body StoreRequest request);

    @DELETE("stores/{id}")
    Call<ResponseBody> deleteStore(@Path("id") String id);

    // manage Supplier
    @GET("suppliers")
    Call<SupplierListResponse> getSuppliers(
            @Query("page") int page,
            @Query("limit") int limit,
            @Query("sortOrder") String sortOrder,
            @Query("search") String search,
            @Query("isActive") Boolean isActive
    );

    @GET("suppliers/{id}")
    Call<SupplierDetailResponse> getSupplierDetail(@Path("id") int id);

    @POST("suppliers")
    Call<SupplierDetailResponse> createSupplier(@Body SupplierRequest request);

    @PATCH("suppliers/{id}")
    Call<SupplierDetailResponse> updateSupplier(@Path("id") int id, @Body SupplierRequest request);


    // manage product
    @GET("products")
    Call<ProductListResponse> getProducts(
            @Query("page") int page,
            @Query("limit") int limit,
            @Query("search") String search,
            @Query("sortOrder") String sortOrder
    );

    @GET("products/{id}")
    Call<ProductDetailResponse> getProductDetail(@Path("id") int id);

    @POST("products")
    Call<ProductDetailResponse> createProduct(@Body ProductRequest request);

    @PATCH("products/{id}")
    Call<ProductDetailResponse> updateProduct(@Path("id") int id, @Body ProductRequest request);

    @GET("base-units")
    Call<BaseUnitListResponse> getBaseUnits(
            @Query("page") int page,
            @Query("limit") int limit
    );

}
