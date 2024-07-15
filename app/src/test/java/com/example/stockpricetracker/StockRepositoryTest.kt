package com.example.stockpricetracker

import com.example.stockpricetracker.data.model.StockItem
import com.example.stockpricetracker.data.repository.StockRepository
import com.example.stockpricetracker.data.repository.StockRepositoryImp
import com.example.stockpricetracker.network.StockApi
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`

class StockRepositoryTest {

    private lateinit var repository: StockRepositoryImp
    private val api = mock(StockApi::class.java)

    @Before
    fun setUp() {
        repository = StockRepository(api)
    }
//runBlocking :run the test in a coroutine scope,
//necessary for testing suspend fun
    @Test
    fun `getTop5Items returns items from API`(): Unit = runBlocking {
        val mockItems = listOf(
            StockItem(1, "Item 1", 100.0),
            StockItem(2, "Item 2", 200.0),
            StockItem(3, "Item 3", 300.0),
            StockItem(4, "Item 4", 400.0),
            StockItem(5, "Item 5", 500.0)
        )
        `when`(api.getTop5Items()).thenReturn(mockItems)

        val result = repository.getTop5Items()

        assertEquals(mockItems, result)
        verify(api, times(1)).getTop5Items()
    }
}


