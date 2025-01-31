package com.example.productionmanagementandroid.auth;

import com.google.gson.annotations.SerializedName;

// ログインレスポンスデータ
public class LoginResponse {
    @SerializedName("access_token")
    private String accessToken;

    public String getAccessToken() {
        return accessToken;
    }
}