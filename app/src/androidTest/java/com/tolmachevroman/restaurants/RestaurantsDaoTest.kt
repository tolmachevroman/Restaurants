package com.tolmachevroman.restaurants

import android.arch.core.executor.testing.InstantTaskExecutorRule
import android.arch.lifecycle.Observer
import android.arch.persistence.room.Room
import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import com.tolmachevroman.restaurants.datasources.database.Database
import com.tolmachevroman.restaurants.models.restaurants.Restaurant
import com.tolmachevroman.restaurants.models.restaurants.RestaurantsDao
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations

/**
 * Created by romantolmachev on 22/11/2017.
 */
@RunWith(AndroidJUnit4::class)
class RestaurantsDaoTest {

    @Rule
    @JvmField
    var instantExecutor = InstantTaskExecutorRule()

    @Mock
    private lateinit var observer: Observer<List<Restaurant>>

    private lateinit var database: Database
    private lateinit var restaurantsDao: RestaurantsDao

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        val context = InstrumentationRegistry.getTargetContext()
        database = Room.inMemoryDatabaseBuilder(context, Database::class.java).allowMainThreadQueries().build()
        restaurantsDao = database.restaurantsDao()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun queryRestaurantsTest() {
        //given
        val restaurant = Restaurant(1, 1, "Fake Restaurant", 0.0, 0.0, 1000,
                "image", "description")
        restaurantsDao.insertRestaurant(restaurant)
        //when
        restaurantsDao.queryRestaurants().observeForever(observer)
        //then
        verify(observer).onChanged(listOf(restaurant))
    }

    @Test
    fun queryRestaurantsByCuisineTest() {
        //given
        val restaurant1 = Restaurant(1, 1, "Fake Restaurant", 0.0, 0.0, 1000,
                "image", "description")
        val restaurant2 = Restaurant(2, 2, "Fake Restaurant 2", 0.0, 0.0, 1000,
                "image", "description")
        val restaurant3 = Restaurant(3, 1, "Fake Restaurant 3", 0.0, 0.0, 1000,
                "image", "description")
        restaurantsDao.insertRestaurant(restaurant1)
        restaurantsDao.insertRestaurant(restaurant2)
        restaurantsDao.insertRestaurant(restaurant3)
        //when
        restaurantsDao.queryRestaurantsByCuisine(1).observeForever(observer)
        //then
        verify(observer).onChanged(listOf(restaurant1, restaurant3))
    }

}