package com.example.stockpricetracker.view


import android.annotation.SuppressLint
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.Modifier
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.stockpricetracker.view.viewModel.StockViewModel
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.stockpricetracker.R
import com.example.stockpricetracker.data.model.StockItem
import com.example.stockpricetracker.network.NetworkResult
import com.example.stockpricetracker.utils.ChangeColor
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState

@Composable
fun StockScreen(viewModel: StockViewModel = hiltViewModel()) {
    val networkState by viewModel.networkState.observeAsState(NetworkResult.Loading)
    val isRefreshing = networkState is NetworkResult.Loading
    val stockItems = viewModel.stockItems

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.custom_green))
            .padding(top = dimensionResource(id = R.dimen.padding_large))

    ) {
        val textFontSizeMedium = dimensionResource(id = R.dimen.font_size_medium).value.sp
        Text(
            stringResource(R.string.stock_price_tracker),
            fontSize = textFontSizeMedium,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            modifier = Modifier
                .align(Alignment.TopCenter)

        )

        SwipeRefreshLayout(
            isRefreshing = isRefreshing,
            onRefresh = {
                viewModel.refreshItems()
            }
        ) {

            if (isRefreshing && stockItems.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else {
                StockList(stockItems)
            }
        }

    }
}


@Composable
fun StockList(stockItems: List<StockItem>) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = dimensionResource(id = R.dimen.padding_medium))
    ) {
        items(stockItems, key = { it.id }) { stockItem ->
            StockItemView(stockItem)
        }
    }
}

@SuppressLint("DefaultLocale")
@Composable
fun StockItemView(item: StockItem) {
    val color by animateColorAsState(
        targetValue = when (item.priceChangeColor) {
            ChangeColor.Increase -> colorResource(id = R.color.price_green)
            ChangeColor.Decrease -> Color.Red
            ChangeColor.Neutral -> Color.Black
        }, label = ""
    )
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(dimensionResource(id = R.dimen.padding_extra_small)),
        elevation = CardDefaults.cardElevation(defaultElevation = dimensionResource(id = R.dimen.card_elevation)),
        colors = CardDefaults.cardColors(containerColor = colorResource(id = R.color.custom_white))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimensionResource(id = R.dimen.padding_extra_medium))
        ) {
            val textFontSizeSmall = dimensionResource(id = R.dimen.font_size_small).value.sp
            Text(
                text = item.name,
                modifier = Modifier
                    .weight(1f)
                    .padding(start = dimensionResource(id = R.dimen.padding_small)),
                fontSize = textFontSizeSmall,
                fontWeight = FontWeight.Light
            )
            Text(
                text = String.format("€%.4f", item.price),
                color = color,
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .padding(end = dimensionResource(id = R.dimen.padding_small))
            )
        }
    }
}

@Composable
fun SwipeRefreshLayout(
    isRefreshing: Boolean,
    onRefresh: () -> Unit,
    content: @Composable () -> Unit
) {
    val swipeRefreshState = rememberSwipeRefreshState(isRefreshing)
    SwipeRefresh(
        state = swipeRefreshState,
        onRefresh = onRefresh
    ) {
        content()
    }
}
