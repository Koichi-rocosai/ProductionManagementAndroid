package com.example.productionmanagementandroid.auth;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface StockroomApi {
    String STOCKROOMS_ENDPOINT = "/api/list/stockrooms"; // 定数として定義

    @GET(STOCKROOMS_ENDPOINT)
    Call<List<Stockroom>> getStockrooms();
}