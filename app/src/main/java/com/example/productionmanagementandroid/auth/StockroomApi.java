package com.example.productionmanagementandroid.auth;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface StockroomApi {
    @GET("/api/list/stockrooms")
    Call<List<Stockroom>> getStockrooms();
}