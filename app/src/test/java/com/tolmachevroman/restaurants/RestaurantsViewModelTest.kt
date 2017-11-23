package com.tolmachevroman.restaurants

import android.arch.core.executor.testing.InstantTaskExecutorRule
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Observer
import com.tolmachevroman.restaurants.datasources.webservice.Resource
import com.tolmachevroman.restaurants.models.restaurants.Restaurant
import com.tolmachevroman.restaurants.models.restaurants.RestaurantsRepository
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.ArgumentMatchers
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations
import com.tolmachevroman.restaurants.viewmodels.RestaurantsViewModel

/**
 * Created by romantolmachev on 23/11/2017.
 */
@RunWith(JUnit4::class)
class RestaurantsViewModelTest {

    @Rule
    @JvmField
    var instantExecutor = InstantTaskExecutorRule()

    @Mock
    lateinit var repository: RestaurantsRepository

    @Mock
    lateinit var observer: Observer<Resource<List<Restaurant>>>

    lateinit var restaurantsViewModel: RestaurantsViewModel

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        restaurantsViewModel = RestaurantsViewModel(repository)
    }

    /**
     * When cuisineInput is set to 0, return all restaurants
     */
    @Test
    fun getRestaurantsFromRepository() {
        val restaurant1 = Restaurant(1, 1, "Fake Restaurant", 0.0, 0.0, 1000,
                "image", "description")
        val restaurant2 = Restaurant(2, 2, "Fake Restaurant 2", 0.0, 0.0, 1000,
                "image", "description")
        val restaurant3 = Restaurant(3, 1, "Fake Restaurant 3", 0.0, 0.0, 1000,
                "image", "description")
        val restaurants = MutableLiveData<Resource<List<Restaurant>>>()
        val resource = Resource.success(listOf(restaurant1, restaurant2, restaurant3))
        restaurants.value = resource

        Mockito.`when`(repository.getRestaurants()).thenReturn(restaurants)
        restaurantsViewModel.restaurants.observeForever(observer)

        Assert.assertFalse(restaurantsViewModel.initialized)

        restaurantsViewModel.cuisineInput.value = 0

        Assert.assertTrue(restaurantsViewModel.initialized)
        verify(observer).onChanged(ArgumentMatchers.refEq(resource))

    }

    /**
     * When cuisineInput is set to 1, return all restaurants where cuisine param is 1
     */
    @Test
    fun getRestaurantsByCuisineFromRepository() {
        val restaurant1 = Restaurant(1, 1, "Fake Restaurant", 0.0, 0.0, 1000,
                "image", "description")
        val restaurant3 = Restaurant(3, 1, "Fake Restaurant 3", 0.0, 0.0, 1000,
                "image", "description")
        val restaurants = MutableLiveData<Resource<List<Restaurant>>>()
        val resource = Resource.success(listOf(restaurant1, restaurant3))
        restaurants.value = resource

        Mockito.`when`(repository.getRestaurants(1)).thenReturn(restaurants)
        restaurantsViewModel.restaurants.observeForever(observer)

        Assert.assertFalse(restaurantsViewModel.initialized)

        restaurantsViewModel.cuisineInput.value = 1

        Assert.assertTrue(restaurantsViewModel.initialized)
        verify(observer).onChanged(ArgumentMatchers.refEq(resource))

    }
}