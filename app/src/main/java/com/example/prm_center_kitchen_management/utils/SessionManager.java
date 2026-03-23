package com.example.prm_center_kitchen_management.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {
    private static final String PREF_NAME = "KitchenAppSession";
    private static final String KEY_TOKEN = "ACCESS_TOKEN";
    private static final String KEY_REFRESH_TOKEN = "REFRESH_TOKEN";
    private static final String KEY_ROLE = "ROLE";
    private static final String KEY_STORE_ID = "STORE_ID";
    
    private final SharedPreferences prefs;

    public SessionManager(Context context) {
        prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public void saveAuthData(String token, String role, String storeId) {
        prefs.edit()
            .putString(KEY_TOKEN, token)
            .putString(KEY_ROLE, role)
            .putString(KEY_STORE_ID, storeId)
            .apply();
    }

    public String getToken() { 
        return prefs.getString(KEY_TOKEN, null); 
    }
    
    public String getRole() { 
        return prefs.getString(KEY_ROLE, null); 
    }
    public String getStoreId() {
        return prefs.getString(KEY_STORE_ID, null);
    }


    public void saveAuthToken(String token) {
        prefs.edit().putString(KEY_TOKEN, token).apply();
    }

    public void saveRefreshToken(String refreshToken) {
        prefs.edit().putString(KEY_REFRESH_TOKEN, refreshToken).apply();
    }

    public String getRefreshToken() {
        return prefs.getString(KEY_REFRESH_TOKEN, null);
    }
    
    public void logout() { 
        prefs.edit().clear().apply();
    }
}
