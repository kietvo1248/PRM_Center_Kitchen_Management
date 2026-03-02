package com.example.prm_center_kitchen_management.utils;

//Sử dụng SharedPreferences để lưu JWT token, dùng cho các API sau này

import android.content.Context;
import android.content.SharedPreferences;
public class SessionManager {
    private static final String PREF_NAME = "KitchenAppSession";
    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;

    public SessionManager(Context context) {
        prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = prefs.edit();
    }

    public void saveAuthData(String token, String role, String storeId) {
        editor.putString("ACCESS_TOKEN", token);
        editor.putString("ROLE", role);
        editor.putString("STORE_ID", storeId);
        editor.apply();
    }

    public String getToken() { return prefs.getString("ACCESS_TOKEN", null); }
    public String getRole() { return prefs.getString("ROLE", null); }
    public void logout() { editor.clear().apply(); }
}
