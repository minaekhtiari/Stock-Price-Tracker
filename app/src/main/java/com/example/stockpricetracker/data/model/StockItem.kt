package com.example.stockpricetracker.data.model

import com.example.stockpricetracker.utils.ChangeColor

data class StockItem(
    val id: Int,
    val name: String,
    var price: Double,
    var previousPrice: Double = 0.0,
    var priceChangeColor: ChangeColor = ChangeColor.Neutral
)
