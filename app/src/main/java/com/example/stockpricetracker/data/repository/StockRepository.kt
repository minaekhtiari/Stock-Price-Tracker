package com.example.stockpricetracker.data.repository

import com.example.stockpricetracker.data.model.StockItem
import com.example.stockpricetracker.network.StockApi
import javax.inject.Inject

class StockRepository @Inject constructor(private val stockApi: StockApi) : StockRepositoryImp {
    override suspend fun getTop5Items(): List<StockItem> {
        return stockApi.getTop5Items()
    }
}
