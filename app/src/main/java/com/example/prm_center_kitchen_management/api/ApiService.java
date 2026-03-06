package com.example.prm_center_kitchen_management.api;

import com.example.prm_center_kitchen_management.model.request.LoginRequest;
import com.example.prm_center_kitchen_management.model.request.ProfileUpdateRequest;
import com.example.prm_center_kitchen_management.model.response.LoginResponse;
import com.example.prm_center_kitchen_management.model.response.UserProfileResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.DELETE;

public interface ApiService {
    @POST("auth/login")
    Call<LoginResponse> login(@Body LoginRequest request);

    @GET("auth/me")
    Call<UserProfileResponse> getProfile();

    @PATCH("auth/profile")
    Call<UserProfileResponse> updateProfile(@Body ProfileUpdateRequest request);
}
