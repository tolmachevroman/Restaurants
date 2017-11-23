package com.tolmachevroman.restaurants.dagger.modules

import android.app.Application
import android.arch.persistence.room.Room
import android.content.Context
import com.tolmachevroman.restaurants.datasources.database.Database
import com.tolmachevroman.restaurants.models.restaurants.RestaurantsDao
import com.tolmachevroman.restaurants.utils.Utils
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Created by romantolmachev on 22/11/2017.
 */
@Module
class AppModule(private val app: Application) {

    @Provides
    @Singleton
    fun provideApplication(): Application = app

    @Provides
    @Singleton
    fun provideContext(): Context = app.applicationContext

    @Provides
    @Singleton
    fun providesUtils(): Utils = Utils(app)

    @Provides
    @Singleton
    fun provideRestaurantsDatabase(app: Application): Database =
            Room.databaseBuilder(app, Database::class.java, "restaurants_db").build()

    @Provides
    @Singleton
    fun provideRestaurantsDao(db: Database): RestaurantsDao = db.restaurantsDao()
}