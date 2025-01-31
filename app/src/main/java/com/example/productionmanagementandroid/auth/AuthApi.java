package com.example.productionmanagementandroid.auth;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

// 認証用の API インターフェース
@SuppressWarnings("UnimplementedDeclaration")
public interface AuthApi {
    @Headers("Content-Type: application/json")
    @POST("api/Auth/token")
    Call<LoginResponse> login(@Body LoginRequest request);
}