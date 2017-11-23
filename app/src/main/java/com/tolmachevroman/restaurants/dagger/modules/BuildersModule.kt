package com.tolmachevroman.restaurants.dagger.modules

import com.tolmachevroman.restaurants.views.RestaurantsMapActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * Created by romantolmachev on 22/11/2017.
 */
@Module
abstract class BuildersModule {

    @ContributesAndroidInjector
    abstract fun contributeRestaurantsMapsActivity(): RestaurantsMapActivity
}