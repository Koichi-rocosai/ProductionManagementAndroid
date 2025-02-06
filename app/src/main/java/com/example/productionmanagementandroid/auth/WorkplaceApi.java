package com.example.productionmanagementandroid.auth;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;

public interface WorkplaceApi {
    @GET("/api/list/workplaces")
    Call<List<Workplace>> getWorkplaces(@Header("Authorization") String token);
}