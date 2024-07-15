package com.example.stockpricetracker

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import junit.framework.TestCase.assertNotNull
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class ApplicationProviderTest {

    @Test
    fun testApplicationProvider() {

        val appContext = ApplicationProvider.getApplicationContext<Context>()
        //Confirms that the context is not null
        assertNotNull(appContext)
    }
}