package com.example.productionmanagementandroid.auth;

// ログインレスポンスデータ
public class LoginResponse {
    private String access_token;
    private String token_type;

    public String getAccessToken() {
        return access_token;
    }
}
