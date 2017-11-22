package com.tolmachevroman.restaurants.datasources.database

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import com.tolmachevroman.restaurants.models.restaurants.Restaurant
import com.tolmachevroman.restaurants.models.restaurants.RestaurantsDao

/**
 * Created by romantolmachev on 22/11/2017.
 */
@Database(entities = arrayOf(Restaurant::class), version = 1)
abstract class Database : RoomDatabase() {
    abstract fun restaurantsDao(): RestaurantsDao
}