package com.tolmachevroman.restaurants.utils

import android.content.Context
import android.net.ConnectivityManager
import javax.inject.Singleton

/**
 * Created by romantolmachev on 22/11/2017.
 */
@Singleton
class Utils {

    fun hasConnection(context: Context): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = cm.activeNetworkInfo
        return activeNetwork != null && activeNetwork.isAvailable && activeNetwork.isConnected
    }

}