package com.tolmachevroman.restaurants.dagger.modules

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.tolmachevroman.restaurants.dagger.ViewModelKey
import com.tolmachevroman.restaurants.viewmodels.RestaurantsViewModel
import com.tolmachevroman.restaurants.viewmodels.ViewModelFactory
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

/**
 * Created by romantolmachev on 23/11/2017.
 */
@Module
abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(RestaurantsViewModel::class)
    internal abstract fun bindRestaurantsViewModel(restaurantsViewModel: RestaurantsViewModel): ViewModel

    @Binds
    internal abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory
}