package com.example.prm_center_kitchen_management.api;

import com.example.prm_center_kitchen_management.model.request.LoginRequest;
import com.example.prm_center_kitchen_management.model.request.ProfileUpdateRequest;
import com.example.prm_center_kitchen_management.model.response.LoginResponse;
import com.example.prm_center_kitchen_management.model.response.UserProfileResponse;
import com.example.prm_center_kitchen_management.model.response.CatalogResponse;
import com.example.prm_center_kitchen_management.model.response.ShipmentResponse;
import com.example.prm_center_kitchen_management.model.request.CreateOrderRequest;
import com.example.prm_center_kitchen_management.model.response.OrderResponse;
import com.example.prm_center_kitchen_management.model.response.OrderDetailResponse;

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

}
