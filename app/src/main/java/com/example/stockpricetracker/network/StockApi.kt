package com.example.stockpricetracker.network

import com.example.stockpricetracker.data.model.StockItem
import retrofit2.http.GET

interface StockApi {
    @GET("stocks")
    suspend fun getTop5Items(): List<StockItem>
}
