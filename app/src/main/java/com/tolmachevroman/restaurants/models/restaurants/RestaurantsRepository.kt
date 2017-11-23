package com.tolmachevroman.restaurants.models.restaurants

import android.arch.lifecycle.LiveData
import android.util.Log
import com.tolmachevroman.restaurants.datasources.database.NetworkBoundResource
import com.tolmachevroman.restaurants.datasources.webservice.Resource
import com.tolmachevroman.restaurants.datasources.webservice.WebService
import com.tolmachevroman.restaurants.utils.AppExecutors
import com.tolmachevroman.restaurants.utils.Utils
import retrofit2.Call
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by romantolmachev on 22/11/2017.
 */
@Singleton
open class RestaurantsRepository @Inject constructor(val webService: WebService, val restaurantsDao: RestaurantsDao,
                                                val utils: Utils, val appExecutors: AppExecutors) {

    val TAG = "RestaurantRepository"

    open fun getRestaurants(cuisine: Int = 0): LiveData<Resource<List<Restaurant>>> {
        return object : NetworkBoundResource<List<Restaurant>>(appExecutors) {

            override fun saveNetworkCallResult(data: List<Restaurant>?) {
                Log.d(TAG, "saveNetworkCallResult")
                data?.filterNot {
                    (it.cuisine !in listOf(1, 2, 3)) or
                            it.name.isNullOrBlank() or
                            (it.lat == 0.0) or
                            (it.lng == 0.0)
                }?.forEach { restaurantsDao.insertRestaurant(it) }
            }

            override fun shouldLoadFromNetwork(data: List<Restaurant>?): Boolean {
                val shouldLoadFromNetwork = utils.hasConnection() && (data == null || data.isEmpty())
                Log.d(TAG, "shouldLoadFromNetwork: $shouldLoadFromNetwork")
                return shouldLoadFromNetwork
            }

            override fun loadFromDatabase(): LiveData<List<Restaurant>> {
                Log.d(TAG, "loadFromDatabase")
                return when (cuisine == 0) {
                    true -> restaurantsDao.queryRestaurants()
                    else -> restaurantsDao.queryRestaurantsByCuisine(cuisine)
                }
            }

            override fun createNetworkCall(): Call<List<Restaurant>> {
                Log.d(TAG, "createNetworkCall")
                return webService.getRestaurants()
            }
        }.asLiveData()
    }

}