package com.tolmachevroman.restaurants

import android.content.Context
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.net.NetworkInfo
import com.tolmachevroman.restaurants.utils.Utils
import junit.framework.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mockito



/**
 * Created by romantolmachev on 22/11/2017.
 */
@RunWith(JUnit4::class)
class UtilsTest {

    @Test
    fun hasConnectionTest() {
        val context = Mockito.mock<Context>(Context::class.java)
        val connManager = Mockito.mock(ConnectivityManager::class.java)
        val networkInfo = Mockito.mock(NetworkInfo::class.java)
        val packageManager = Mockito.mock(PackageManager::class.java)
        val utils = Utils(context)

        Mockito.`when`(context.packageManager).thenReturn(packageManager)
        Mockito.`when`(context.getSystemService(Context.CONNECTIVITY_SERVICE)).thenReturn(connManager)
        Mockito.`when`(connManager.activeNetworkInfo).thenReturn(networkInfo)
        Mockito.`when`(networkInfo.isAvailable).thenReturn(true)
        Mockito.`when`(networkInfo.isConnected).thenReturn(true)

        Assert.assertTrue(utils.hasConnection())

        Mockito.`when`(networkInfo.isConnected).thenReturn(false)

        Assert.assertFalse(utils.hasConnection())
    }

}