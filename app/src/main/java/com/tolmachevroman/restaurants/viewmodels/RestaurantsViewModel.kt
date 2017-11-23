package com.tolmachevroman.restaurants.viewmodels

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations
import android.arch.lifecycle.ViewModel
import com.tolmachevroman.restaurants.datasources.webservice.Resource
import com.tolmachevroman.restaurants.models.restaurants.Restaurant
import com.tolmachevroman.restaurants.models.restaurants.RestaurantsRepository
import javax.inject.Inject

/**
 * Created by romantolmachev on 23/11/2017.
 */
class RestaurantsViewModel @Inject constructor(private val repository: RestaurantsRepository) : ViewModel() {

    var initialized = false

    var cuisineInput: MutableLiveData<Int> = MutableLiveData()

    val restaurants: LiveData<Resource<List<Restaurant>>> = Transformations.switchMap(cuisineInput) { cuisine ->
        initialized = true; repository.getRestaurants(cuisine)
    }

}