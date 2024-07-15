package com.example.stockpricetracker

import android.app.Application
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.lifecycle.viewModelScope
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.stockpricetracker.data.model.StockItem
import com.example.stockpricetracker.data.repository.StockRepositoryImp
import com.example.stockpricetracker.network.NetworkResult
import com.example.stockpricetracker.view.StockScreen
import com.example.stockpricetracker.view.viewModel.StockViewModel
import kotlinx.coroutines.launch
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class StockScreenTest {

    @get:Rule
    //Initializes the ComposeTestRule.
    val composeTestRule = createComposeRule()

    private lateinit var viewModel: FakeStockViewModel
    private lateinit var repository: FakeStockRepository

    @Before
    fun setup() {
        repository = FakeStockRepository()
        viewModel = FakeStockViewModel(Application(), repository)

        // Set up the Compose test environment
        composeTestRule.setContent {
            StockScreen(viewModel = viewModel)
        }
    }

    @Test
    fun stockScreen_displaysStockItems() {
        // Check if the stock items are displayed

        composeTestRule.onNodeWithText("Item 1").assertIsDisplayed()
        composeTestRule.onNodeWithText("€100.0000").assertIsDisplayed()

        composeTestRule.onNodeWithText("Item 2").assertIsDisplayed()
        composeTestRule.onNodeWithText("€200.0000").assertIsDisplayed()

        composeTestRule.onNodeWithText("Item 3").assertIsDisplayed()
        composeTestRule.onNodeWithText("€300.0000").assertIsDisplayed()

        composeTestRule.onNodeWithText("Item 4").assertIsDisplayed()
        composeTestRule.onNodeWithText("€400.0000").assertIsDisplayed()

        composeTestRule.onNodeWithText("Item 5").assertIsDisplayed()
        composeTestRule.onNodeWithText("€500.0000").assertIsDisplayed()
    }
}


class FakeStockRepository : StockRepositoryImp {
    override suspend fun getTop5Items(): List<StockItem> {
        return listOf(
            StockItem(1, "Item 1", 100.0),
            StockItem(2, "Item 2", 200.0),
            StockItem(3, "Item 3", 300.0),
            StockItem(4, "Item 4", 400.0),
            StockItem(5, "Item 5", 500.0)
        )
    }
}

class FakeStockViewModel(
    application: Application,
    private val repository: FakeStockRepository
) : StockViewModel(repository, application) {
    init {
        viewModelScope.launch {
            val items = repository.getTop5Items()
            _networkState.value = NetworkResult.Success(items)
            updateStockItems(items)
        }
    }

    override val stockItems = listOf(
        StockItem(1, "Item 1", 100.0),
        StockItem(2, "Item 2", 200.0),
        StockItem(3, "Item 3", 300.0),
        StockItem(4, "Item 4", 400.0),
        StockItem(5, "Item 5", 500.0)
    )
}