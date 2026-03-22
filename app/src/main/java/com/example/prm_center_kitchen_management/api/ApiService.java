package com.example.prm_center_kitchen_management.api;

import com.example.prm_center_kitchen_management.model.request.LoginRequest;
import com.example.prm_center_kitchen_management.model.request.RefreshTokenRequest;
import com.example.prm_center_kitchen_management.model.response.CoordinatorOrderResponse;
import com.example.prm_center_kitchen_management.model.response.CoordinatorOrderReviewResponse;
import com.example.prm_center_kitchen_management.model.response.CoordinatorPickingListResponse;
import com.example.prm_center_kitchen_management.model.response.CoordinatorShipmentResponse;
import com.example.prm_center_kitchen_management.model.response.OrderReviewResponse;
import com.example.prm_center_kitchen_management.model.response.PickingListResponse;
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

// Kitchen Models
import com.example.prm_center_kitchen_management.model.response.ApiResponse;
import com.example.prm_center_kitchen_management.model.response.InboundReceipt;
import com.example.prm_center_kitchen_management.model.response.InboundDetailResponse;
import com.example.prm_center_kitchen_management.model.response.AddReceiptItemResponse;
import com.example.prm_center_kitchen_management.model.request.CreateReceiptRequest;
import com.example.prm_center_kitchen_management.model.request.AddReceiptItemRequest;
import com.example.prm_center_kitchen_management.model.response.KitchenInventoryItem;
import com.example.prm_center_kitchen_management.model.response.Batch;
import com.example.prm_center_kitchen_management.model.request.InventoryAdjustmentRequest;
import com.example.prm_center_kitchen_management.model.response.PickingTask;
import com.example.prm_center_kitchen_management.model.response.PickingTaskDetail;
import com.example.prm_center_kitchen_management.model.response.ScanCheckResponse;
import com.example.prm_center_kitchen_management.model.request.FinalizeBulkRequest;
import com.example.prm_center_kitchen_management.model.response.Shipment;
import com.example.prm_center_kitchen_management.model.response.InventorySummary;
import com.example.prm_center_kitchen_management.model.response.WasteAnalyticsData;
import com.example.prm_center_kitchen_management.model.response.WasteReport;
import com.example.prm_center_kitchen_management.model.response.PaginatedResponse;

import java.util.List;

import java.util.Map;

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
    // --- Auth & General ---
    @POST("auth/login")
    Call<LoginResponse> login(@Body LoginRequest request);

    @POST("auth/refresh-token")
    Call<RefreshTokenResponse> refreshToken(@Body RefreshTokenRequest request);

    @GET("auth/me")
    Call<UserProfileResponse> getProfile();

    @PATCH("auth/profile")
    Call<UserProfileResponse> updateProfile(@Body ProfileUpdateRequest request);

    // --- Module 8: Inbound Logistics (Nhập kho) ---
    /** data = { items, meta } theo tài liệu phân trang */
    @GET("inbound/receipts")
    Call<ApiResponse<PaginatedResponse<InboundReceipt>>> getInboundReceipts(
            @Query("page") int page,
            @Query("limit") int limit,
            @Query("status") String status
    );

    @GET("inbound/receipts/{id}") // Lấy chi tiết phiếu nhập
    Call<ApiResponse<InboundDetailResponse>> getReceiptDetail(@Path("id") String receiptId);

    @POST("inbound/receipts")
    Call<ApiResponse<InboundReceipt>> createReceipt(@Body CreateReceiptRequest request);

    @POST("inbound/receipts/{id}/items")
    Call<ApiResponse<AddReceiptItemResponse>> addReceiptItem(
            @Path("id") String receiptId,
            @Body AddReceiptItemRequest request
    );

    @PATCH("inbound/receipts/{id}/complete")
    Call<ApiResponse<Void>> completeReceipt(@Path("id") String receiptId);

    // --- Module 9: Inventory & Analytics (Central Kitchen Staff) ---
    /** FRONTEND_API: GET /inventory/kitchen/summary */
    @GET("inventory/kitchen/summary")
    Call<ApiResponse<PaginatedResponse<KitchenInventoryItem>>> getKitchenInventory(
            @Query("page") int page,
            @Query("limit") int limit,
            @Query("search") String search,
            @Query("sortBy") String sortBy
    );

    @GET("products/{id}/batches")
    Call<ApiResponse<List<Batch>>> getProductBatches(@Path("id") int productId);

    @POST("inventory/adjustments")
    Call<ApiResponse<Void>> adjustInventory(@Body InventoryAdjustmentRequest request);

    @GET("inventory/analytics/summary")
    Call<ApiResponse<InventorySummary>> getInventorySummary();

    @GET("inventory/analytics/waste")
    Call<ApiResponse<WasteAnalyticsData>> getExpiryAlerts(
            @Query("fromDate") String from,
            @Query("toDate") String to
    );

    // --- Module 11: Warehouse Operation (Picking) ---
    /**
     * Danh sách tác vụ soạn — data = {@code { items, meta }} (phân trang).
     * Ví dụ: ?page=1&amp;limit=10&amp;sortBy=status&amp;sortOrder=DESC
     */
    @GET("warehouse/picking-tasks")
    Call<ApiResponse<PaginatedResponse<PickingTask>>> getPickingTasks(
            @Query("page") int page,
            @Query("limit") int limit,
            @Query("status") String status,
            @Query("search") String search,
            @Query("date") String date,
            @Query("sortBy") String sortBy,
            @Query("sortOrder") String sortOrder
    );

    @GET("warehouse/picking-tasks/{id}")
    Call<ApiResponse<PickingTaskDetail>> getPickingTaskDetail(@Path("id") String taskId);

    @GET("warehouse/scan-check") // Verify QR Code
    Call<ApiResponse<ScanCheckResponse>> verifyBatchScan(@Query("batchCode") String batchCode);

    @PATCH("warehouse/shipments/finalize-bulk")
    Call<ApiResponse<Void>> finalizeShipments(@Body FinalizeBulkRequest request);

    // --- Module 12: Shipment ---
    @GET("shipments") // Kitchen Source
    Call<ApiResponse<List<Shipment>>> getKitchenShipments(@Query("status") String status);


    // --- Existing endpoints (Maintained for backward compatibility) ---
    @GET("orders/catalog")
    Call<CatalogResponse> getCatalog(@Query("page") int page, @Query("limit") int limit, @Query("sortOrder") String sortOrder, @Query("isActive") boolean isActive);

    @GET("shipments/store/my")
    Call<ShipmentResponse> getMyShipments(@Query("page") int page, @Query("limit") int limit, @Query("sortOrder") String sortOrder, @Query("fromDate") String fromDate, @Query("toDate") String toDate);

    @GET("orders/my-store")
    Call<OrderResponse> getStoreOrders(@Query("page") int page, @Query("limit") int limit, @Query("sortOrder") String sortOrder, @Query("status") String status);

    @GET("orders/{id}")
    Call<OrderDetailResponse> getOrderDetail(@Path("id") String id);

    @POST("orders")
    Call<ResponseBody> createOrder(@Body CreateOrderRequest request);

    @PATCH("orders/franchise/{id}/cancel")
    Call<ResponseBody> cancelOrder(@Path("id") String id);

    @POST("shipments/{id}/receive-all")
    Call<ResponseBody> receiveAllShipment(@Path("id") String id);

    @GET("inventory/store")
    Call<InventoryResponse> getStoreInventory(@Query("page") int page, @Query("limit") int limit, @Query("sortBy") String sortBy, @Query("sortOrder") String sortOrder, @Query("search") String search);

    @GET("orders/analytics/fulfillment-rate")
    Call<DashboardFulfillmentResponse> getDashboardFulfillment();

    @GET("inventory/analytics/summary")
    Call<DashboardInventorySummaryResponse> getDashboardInventorySummary();

    @GET("inventory/analytics/waste")
    Call<DashboardWasteResponse> getDashboardWaste(@Query("fromDate") String fromDate, @Query("toDate") String toDate);

    @GET("suppliers")
    Call<SupplierListResponse> getSuppliers(@Query("page") int page, @Query("limit") int limit, @Query("sortOrder") String sortOrder, @Query("search") String search, @Query("isActive") Boolean isActive);

    @GET("suppliers/dashboard") // Added specifically for dashboard usage if needed or just use regular suppliers
    Call<DashboardSupplierResponse> getDashboardSuppliers(
            @Query("page") int page,
            @Query("limit") int limit,
            @Query("sortOrder") String sortOrder,
            @Query("isActive") boolean isActive
    );

    @GET("suppliers/{id}")
    Call<SupplierDetailResponse> getSupplierDetail(@Path("id") int id);

    @POST("suppliers")
    Call<SupplierDetailResponse> createSupplier(@Body SupplierRequest request);

    @PATCH("suppliers/{id}")
    Call<SupplierDetailResponse> updateSupplier(@Path("id") int id, @Body SupplierRequest request);

    @GET("products")
    Call<ProductListResponse> getProducts(@Query("page") int page, @Query("limit") int limit, @Query("search") String search, @Query("sortOrder") String sortOrder);

    @GET("products/{id}")
    Call<ProductDetailResponse> getProductDetail(@Path("id") int id);

    @POST("products")
    Call<ProductDetailResponse> createProduct(@Body ProductRequest request);

    @PATCH("products/{id}")
    Call<ProductDetailResponse> updateProduct(@Path("id") int id, @Body ProductRequest request);

    @GET("base-units")
    Call<BaseUnitListResponse> getBaseUnits(@Query("page") int page, @Query("limit") int limit);

    // --- Store Management ---
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
        // Lấy danh sách đơn hàng cho Coordinator
        @GET("orders")
        Call<CoordinatorOrderResponse> getCoordinatorOrders(
                @Query("page") int page,
                @Query("limit") int limit,
                @Query("sortOrder") String sortOrder
        );

    @GET("orders/coordinator/{id}/review")
    Call<CoordinatorOrderReviewResponse> getCoordinatorOrderReview(@Path("id") String orderId);

    @PATCH("orders/coordinator/{id}/approve")
    Call<Void> approveCoordinatorOrder(@Path("id") String orderId, @Body java.util.Map<String, Boolean> body);

    @PATCH("orders/coordinator/{id}/reject")
    Call<Void> rejectCoordinatorOrder(@Path("id") String orderId, @Body java.util.Map<String, String> body);
    @GET("shipments")
    Call<CoordinatorShipmentResponse> getCoordinatorShipments(
            @Query("page") int page,
            @Query("limit") int limit,
            @Query("sortOrder") String sortOrder
    );

    @GET("shipments/{id}/picking-list")
    Call<CoordinatorPickingListResponse> getCoordinatorPickingList(@Path("id") String shipmentId);
}
