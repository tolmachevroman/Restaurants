package com.tolmachevroman.restaurants.datasources.webservice

import com.tolmachevroman.restaurants.models.restaurants.Restaurant
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created by romantolmachev on 22/11/2017.
 */
interface WebService {
    @GET("restaurants")
    fun getRestaurants(@Query("_limit") limit: Int = 15): Call<List<Restaurant>>
}