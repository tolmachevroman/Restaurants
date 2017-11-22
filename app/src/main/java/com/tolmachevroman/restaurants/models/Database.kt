package com.tolmachevroman.restaurants.models

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import com.tolmachevroman.restaurants.models.restaurants.Restaurant

/**
 * Created by romantolmachev on 22/11/2017.
 */
@Database(entities = arrayOf(Restaurant::class), version = 1)
abstract class Database : RoomDatabase() {

}