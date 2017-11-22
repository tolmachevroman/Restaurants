package com.tolmachevroman.restaurants.models.restaurants

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy.REPLACE
import android.arch.persistence.room.Query

/**
 * Created by romantolmachev on 22/11/2017.
 */
@Dao
interface RestaurantsDao {

    @Query("SELECT * FROM restaurant")
    fun queryRestaurants(): LiveData<List<Restaurant>>

    @Query("SELECT * FROM restaurant WHERE cuisine LIKE :cuisine")
    fun queryRestaurantsByCuisine(cuisine: Int): LiveData<List<Restaurant>>

    @Insert(onConflict = REPLACE)
    fun insertRestaurant(restaurant: Restaurant)
}