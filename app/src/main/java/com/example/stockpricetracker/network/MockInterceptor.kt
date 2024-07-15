package com.example.stockpricetracker.network

import android.content.Context
import com.example.stockpricetracker.R
import com.example.stockpricetracker.data.model.StockItem
import com.google.gson.Gson
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.Protocol
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import kotlin.random.Random


class MockInterceptor(context: Context) : Interceptor {
    private val stockItems = mutableListOf(
        StockItem(1, context.getString(R.string.item_1), 200.0),
        StockItem(2, context.getString(R.string.item_2), 240.0),
        StockItem(3, context.getString(R.string.item_3), 125.0),
        StockItem(4, context.getString(R.string.item_4), 140.0),
        StockItem(5, context.getString(R.string.item_5), 190.0),
        StockItem(6, context.getString(R.string.item_6), 100.0),
        StockItem(7, context.getString(R.string.item_7), 101.0),
        StockItem(8, context.getString(R.string.item_8), 120.0),
        StockItem(9, context.getString(R.string.item_9), 130.0),
        StockItem(10, context.getString(R.string.item_10), 180.0)
    )

    override fun intercept(chain: Interceptor.Chain): Response {
        //Simulating Price Changes
        stockItems.forEach { item ->
            val change = Random.nextDouble(-5.0, 5.0)
            //the previousPrice is updated with the change,
            //the price is adjusted accordingly.
            item.previousPrice = change
            item.price += change
        }

        val top5Items = stockItems.sortedByDescending { it.price }.take(5)
        // Serialize top5Items to JSON string
        val responseString = Gson().toJson(top5Items)
        //mock response
        return Response.Builder()
            .code(200)
            .message(responseString)
            .request(chain.request())
            .protocol(Protocol.HTTP_1_1)
            .body(responseString.toResponseBody("application/json".toMediaTypeOrNull()))
            .build()
    }
}
