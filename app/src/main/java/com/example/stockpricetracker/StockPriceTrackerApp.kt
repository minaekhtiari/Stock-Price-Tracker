package com.example.stockpricetracker

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class StockPriceTrackerApp :Application()

//The Application class in Android is a base class that contains global application state for your app.
// It's instantiated before any other class when the process for your application/package is created.


//@HiltAndroidApp, you're signaling Hilt to start the code generation process for the components it manages.
// This includes generating a base class for your Application,
// which handles the creation and management of the dependency graph.

//that dependencies are available throughout the entire application's lifecycle.