package com.example.prm_center_kitchen_management.api;

import android.content.Context;
import android.content.Intent;
import com.example.prm_center_kitchen_management.activity.auth.LoginActivity;
import com.example.prm_center_kitchen_management.model.request.RefreshTokenRequest;
import com.example.prm_center_kitchen_management.model.response.RefreshTokenResponse;
import com.example.prm_center_kitchen_management.utils.SessionManager;
import java.io.IOException;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import java.util.concurrent.TimeUnit;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Route;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import java.io.IOException;

import okhttp3.Authenticator;


public class ApiClient {
    private static final String BASE_URL = "https://wdp301-api.onrender.com/wdp301-api/v1/";
    private static Retrofit retrofit = null;

    // Thêm Context vào tham số để lấy SharedPreferences
    public static Retrofit getClient(Context context) {
        if (retrofit == null) {
            SessionManager sessionManager = new SessionManager(context);

            // Middleware chặn lại mọi Request trước khi gửi đi
            Interceptor authInterceptor = new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    Request.Builder requestBuilder = chain.request().newBuilder();

                    // Lấy token từ Session và nhét vào Header
                    String token = sessionManager.getToken();
                    if (token != null && !token.isEmpty()) {
                        requestBuilder.addHeader("Authorization", "Bearer " + token);
                    }

                    return chain.proceed(requestBuilder.build());
                }
            };

            // 2. Authenticator để bắt lỗi 401 và tự động gọi API Refresh Token
            Authenticator tokenAuthenticator = new Authenticator() {
                @Override
                public Request authenticate(Route route, Response response) throws IOException {
                    // Nếu chính API refresh-token bị lỗi 401 -> Tránh vòng lặp vô hạn
                    if (response.request().url().encodedPath().contains("refresh-token")) {
                        return null;
                    }

                    String refreshToken = sessionManager.getRefreshToken();
                    if (refreshToken == null || refreshToken.isEmpty()) {
                        return null; // Không có refresh token, chịu thua
                    }

                    // TẠO RETROFIT MỚI ĐỂ GỌI ĐỒNG BỘ (SYNC) API REFRESH
                    ApiService apiService = new Retrofit.Builder()
                            .baseUrl(BASE_URL)
                            .addConverterFactory(GsonConverterFactory.create())
                            .build()
                            .create(ApiService.class);

                    retrofit2.Response<RefreshTokenResponse> refreshResponse =
                            apiService.refreshToken(new RefreshTokenRequest(refreshToken)).execute();

                    if (refreshResponse.isSuccessful() && refreshResponse.body() != null && refreshResponse.body().getData() != null) {
                        // Lấy Token mới thành công
                        String newAccessToken = refreshResponse.body().getData().getAccessToken();
                        String newRefreshToken = refreshResponse.body().getData().getRefreshToken();

                        // Cập nhật vào Session
                        sessionManager.saveAuthToken(newAccessToken);
                        sessionManager.saveRefreshToken(newRefreshToken);

                        // GẮN TOKEN MỚI VÀ TIẾP TỤC CHẠY LẠI API BỊ LỖI LÚC NÃY
                        return response.request().newBuilder()
                                .header("Authorization", "Bearer " + newAccessToken)
                                .build();
                    } else {
                        // Refresh Token cũng hết hạn hoặc lỗi -> Xóa session và văng ra màn hình Login
                        sessionManager.logout();

                        // Chuyển về màn hình đăng nhập (tuỳ chọn)
                        Intent intent = new Intent(context, LoginActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        context.startActivity(intent);

                        return null;
                    }
                }
            };

            // Gắn Middleware vào Http Client
            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(authInterceptor)
//                    .connectTimeout(30, TimeUnit.SECONDS)
//                    .readTimeout(30, TimeUnit.SECONDS)
//                    .writeTimeout(30, TimeUnit.SECONDS)
                    .build();

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(client) // Sử dụng client đã gắn Interceptor
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}