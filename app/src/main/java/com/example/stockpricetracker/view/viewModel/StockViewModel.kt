package com.example.stockpricetracker.view.viewModel

import android.app.Application
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.stockpricetracker.R
import com.example.stockpricetracker.data.model.StockItem
import com.example.stockpricetracker.data.repository.StockRepositoryImp
import com.example.stockpricetracker.network.NetworkResult
import com.example.stockpricetracker.utils.AppConstant.UPDATE_INTERVAL
import com.example.stockpricetracker.utils.ChangeColor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
open class StockViewModel @Inject constructor(
    private val repository: StockRepositoryImp,
    private val application: Application
) : ViewModel() {
    val _networkState = MutableLiveData<NetworkResult<List<StockItem>>>()
    val networkState: LiveData<NetworkResult<List<StockItem>>> get() = _networkState

    private val _stockItems = mutableStateListOf<StockItem>()
    open val stockItems: List<StockItem> get() = _stockItems
    private var updateJob: Job? = null
    private var previousPrices: MutableMap<String, Double> =
        mutableMapOf() // Map to store previous prices

    private val updateInterval: Long = UPDATE_INTERVAL

    init {
        //Initially fetches the items
        fetchTop5Items()
        startPeriodicUpdates()
    }

    fun fetchTop5Items() {
        viewModelScope.launch {
            _networkState.value = NetworkResult.Loading
            try {
                val newItems = repository.getTop5Items()
                updatePriceChanges(newItems)
                updateStockItems(newItems)
                _networkState.value = NetworkResult.Success(newItems)
            } catch (e: Exception) {
                _networkState.value =
                    NetworkResult.Error(e.message ?: application.getString(R.string.unknown_error))
            }
        }
    }

    private fun startPeriodicUpdates() {
        updateJob = viewModelScope.launch {
            while (isActive) {
                fetchTop5Items()
                delay(updateInterval)
            }
        }
    }

    fun refreshItems() {
        fetchTop5Items()
    }

    override fun onCleared() {
        super.onCleared()
        updateJob?.cancel()
    }

    private fun updatePriceChanges(newItems: List<StockItem>) {
        newItems.forEach { newItem ->
            val previousPrice = previousPrices[newItem.id.toString()] ?: newItem.price
            newItem.priceChangeColor = when {
                newItem.price > previousPrice -> ChangeColor.Increase
                newItem.price < previousPrice -> ChangeColor.Decrease
                else -> ChangeColor.Neutral
            }
            previousPrices[newItem.id.toString()] = newItem.price
        }
    }

    fun updateStockItems(newItems: List<StockItem>) {
        _stockItems.clear()
        _stockItems.addAll(newItems)
    }
}

