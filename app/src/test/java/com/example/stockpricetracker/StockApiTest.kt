package com.example.stockpricetracker

import com.example.stockpricetracker.data.model.StockItem
import com.example.stockpricetracker.network.StockApi
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.test.runTest
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class StockApiTest {
    //Moshi instance for JSON serialization and deserialization
    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    @Test
    fun `getTop5Items returns list of stock items`() = runTest {
        //mock data
        val mockItems = listOf(
            StockItem(1, "Item 1", 100.0),
            StockItem(2, "Item 2", 200.0),
            StockItem(3, "Item 3", 300.0),
            StockItem(4, "Item 4", 400.0),
            StockItem(5, "Item 5", 500.0)
        )
        //Mock Web Server Setup
        //simulate an HTTP server
        val server = MockWebServer()
        server.enqueue(
            MockResponse()
                .setBody(
                    moshi.adapter<List<StockItem>>(
                        Types.newParameterizedType(
                            List::class.java,
                            StockItem::class.java
                        )
                    ).toJson(mockItems)
                )
                .setResponseCode(200)
        )
        server.start()

        val retrofit = Retrofit.Builder()
            .baseUrl(server.url("/"))
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()

        //API Call and Assertion

        val api = retrofit.create(StockApi::class.java)

        val response = api.getTop5Items()
   // Verifying the response using assertions ensures that the API returns the expected data
        assertEquals(mockItems, response)

        server.shutdown()
    }
}




