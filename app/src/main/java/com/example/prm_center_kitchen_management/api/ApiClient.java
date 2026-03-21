package com.example.prm_center_kitchen_management.api;

import android.content.Context;
import android.content.Intent;
import com.example.prm_center_kitchen_management.activity.auth.LoginActivity;
import com.example.prm_center_kitchen_management.model.request.RefreshTokenRequest;
import com.example.prm_center_kitchen_management.model.response.RefreshTokenResponse;
import com.example.prm_center_kitchen_management.utils.SessionManager;
import java.io.IOException;
import okhttp3.Authenticator;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Route;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import java.util.concurrent.TimeUnit;

public class ApiClient {
    private static final String BASE_URL = "https://wdp301-api.vercel.app/wdp301-api/v1/";
    private static volatile Retrofit retrofit = null;
    private static volatile ApiService refreshService = null;
    private static final Object LOCK = new Object();

    public static Retrofit getClient(Context context) {
        if (retrofit == null) {
            synchronized (LOCK) {
                if (retrofit == null) {
                    retrofit = createRetrofit(context.getApplicationContext());
                }
            }
        }
        return retrofit;
    }

    private static Retrofit createRetrofit(Context context) {
        SessionManager sessionManager = new SessionManager(context);

        Interceptor authInterceptor = chain -> {
            String token = sessionManager.getToken();
            Request.Builder builder = chain.request().newBuilder();
            if (token != null && !token.isEmpty()) {
                builder.header("Authorization", "Bearer " + token);
            }
            return chain.proceed(builder.build());
        };

        Authenticator authenticator = (route, response) -> {
            // Limit retries to avoid infinite loops
            if (responseCount(response) >= 2) return null;
            
            // Do not attempt to refresh if the refresh token call itself failed
            if (response.request().url().encodedPath().contains("auth/refresh-token")) {
                return null;
            }

            synchronized (LOCK) {
                String latestToken = sessionManager.getToken();
                String requestToken = response.request().header("Authorization");
                if (requestToken != null && requestToken.startsWith("Bearer ")) {
                    requestToken = requestToken.substring(7);
                }

                // If token was already updated by another concurrent thread, retry with the new one
                if (latestToken != null && !latestToken.equals(requestToken)) {
                    return response.request().newBuilder()
                            .header("Authorization", "Bearer " + latestToken)
                            .build();
                }

                String refreshToken = sessionManager.getRefreshToken();
                if (refreshToken == null || refreshToken.isEmpty()) {
                    logout(context, sessionManager);
                    return null;
                }

                // Lazy init refresh service to avoid multiple Retrofit instances
                if (refreshService == null) {
                    refreshService = new Retrofit.Builder()
                            .baseUrl(BASE_URL)
                            .addConverterFactory(GsonConverterFactory.create())
                            .build()
                            .create(ApiService.class);
                }

                try {
                    // Synchronous refresh call
                    retrofit2.Response<RefreshTokenResponse> res = 
                        refreshService.refreshToken(new RefreshTokenRequest(refreshToken)).execute();

                    if (res.isSuccessful() && res.body() != null && res.body().getData() != null) {
                        String newToken = res.body().getData().getAccessToken();
                        String newRefresh = res.body().getData().getRefreshToken();
                        
                        // Update storage
                        sessionManager.saveAuthToken(newToken);
                        sessionManager.saveRefreshToken(newRefresh);
                        
                        // Retry the original request with the new token
                        return response.request().newBuilder()
                                .header("Authorization", "Bearer " + newToken)
                                .build();
                    } else {
                        // Refresh failed (e.g., refresh token expired)
                        logout(context, sessionManager);
                        return null;
                    }
                } catch (IOException e) {
                    // Network error during refresh
                    return null;
                }
            }
        };

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(authInterceptor)
                .authenticator(authenticator)
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .build();

        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    private static int responseCount(Response response) {
        int result = 1;
        while ((response = response.priorResponse()) != null) result++;
        return result;
    }

    private static synchronized void logout(Context context, SessionManager sessionManager) {
        // Only trigger logout if we haven't already cleared the session
        if (sessionManager.getToken() == null && sessionManager.getRefreshToken() == null) {
            return;
        }

        sessionManager.logout();
        Intent intent = new Intent(context, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
    }
}
