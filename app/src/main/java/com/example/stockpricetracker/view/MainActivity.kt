package com.example.stockpricetracker.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // display views behind the status and navigation bar
        enableEdgeToEdge()
        setContent {
            StockScreen()
        }
    }
}

//@Preview(showBackground = true)
//@Composable
//fun StockScreenPreview() {
//    StockPriceTrackerTheme {
//    }
//}