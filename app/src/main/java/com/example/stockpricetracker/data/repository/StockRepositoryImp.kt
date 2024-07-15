package com.example.stockpricetracker.data.repository

import com.example.stockpricetracker.data.model.StockItem

interface StockRepositoryImp {
    suspend fun getTop5Items(): List<StockItem>
}
