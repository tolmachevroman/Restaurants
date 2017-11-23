package com.tolmachevroman.restaurants

import android.arch.core.executor.testing.InstantTaskExecutorRule
import android.arch.lifecycle.Observer
import android.arch.persistence.room.Room
import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import com.tolmachevroman.restaurants.datasources.database.Database
import com.tolmachevroman.restaurants.datasources.webservice.Resource
import com.tolmachevroman.restaurants.datasources.webservice.WebService
import com.tolmachevroman.restaurants.models.restaurants.Restaurant
import com.tolmachevroman.restaurants.models.restaurants.RestaurantsDao
import com.tolmachevroman.restaurants.models.restaurants.RestaurantsRepository
import com.tolmachevroman.restaurants.utils.AppExecutors
import com.tolmachevroman.restaurants.utils.Utils
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers
import org.mockito.Mock
import org.mockito.Mockito.timeout
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import utils.Error404NotFoundMatcher
import java.nio.charset.Charset
import java.util.*

/**
 * Created by romantolmachev on 22/11/2017.
 */
@RunWith(AndroidJUnit4::class)
class RestaurantsRepositoryTest {

    @Rule
    @JvmField
    var instantExecutor = InstantTaskExecutorRule()

    @Mock
    private lateinit var observer: Observer<Resource<List<Restaurant>>>

    private lateinit var mockWebServer: MockWebServer

    private lateinit var database: Database
    private lateinit var utils: Utils
    private lateinit var restaurantsDao: RestaurantsDao
    private lateinit var repository: RestaurantsRepository
    private lateinit var appExecutors: AppExecutors
    private lateinit var retrofit: Retrofit
    private lateinit var webService: WebService

    companion object {
        val TIMEOUT = 1000L
    }

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        val context = InstrumentationRegistry.getTargetContext()
        database = Room.inMemoryDatabaseBuilder(context, Database::class.java).allowMainThreadQueries().build()
        restaurantsDao = database.restaurantsDao()
        utils = Utils(context)
        appExecutors = AppExecutors()

        mockWebServer = MockWebServer()
        mockWebServer.start()

        retrofit = Retrofit.Builder()
                .addConverterFactory(MoshiConverterFactory.create())
                .baseUrl(mockWebServer.url("/").toString())
                .client(OkHttpClient())
                .build()
        webService = retrofit.create<WebService>(WebService::class.java)
        repository = RestaurantsRepository(webService, restaurantsDao, utils, appExecutors)
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    /**
     * Case 1: receive item with all valid fields.
     * Expected result: Resource(status = SUCCESS, data = [...], error = null)
     */
    @Test
    fun itemWithValidFieldsTest() {
        //given
        val content = this.javaClass.classLoader.getResource("get-restaurants-cases/valid.json").readText(Charset.forName("UTF-8"))
        mockWebServer.enqueue(MockResponse()
                .setResponseCode(200)
                .setBody(content))

        val restaurant = Restaurant(id = 1, cuisine = 1, name = "Mi Cevichazo", lat = -33.431756, lng = -70.578719, price = 8900,
                image = "https://www.atrapalo.cl/common/photo/res/44559/106814/ticr_0_0.jpg", description = "Todo el sabor del Perú está presente en el restaurante Mi Cevichazo.")

        //when
        repository.getRestaurants().observeForever(observer)
        //then
        verify(observer).onChanged(ArgumentMatchers.refEq(Resource.loading(null)))
        verify(observer, timeout(TIMEOUT)).onChanged(ArgumentMatchers.refEq(Resource.success(Collections.singletonList(restaurant))))
    }

    /**
     * Case 2: receive item with invalid name.
     * Expected result: Resource(status = SUCCESS, data = [], error = null)
     */
    @Test
    fun itemWithInvalidNameTest() {
        //given
        val content = this.javaClass.classLoader.getResource("get-restaurants-cases/invalid-name.json").readText(Charset.forName("UTF-8"))
        mockWebServer.enqueue(MockResponse()
                .setResponseCode(200)
                .setBody(content))
        //when
        repository.getRestaurants().observeForever(observer)
        //then
        verify(observer, timeout(TIMEOUT)).onChanged(ArgumentMatchers.refEq(Resource.success(emptyList())))
    }

    /**
     * Case 3: receive item with invalid cuisine.
     * Expected result: Resource(status = SUCCESS, data = [], error = null)
     */
    @Test
    fun itemWithInvalidCuisineTest() {
        //given
        val content = this.javaClass.classLoader.getResource("get-restaurants-cases/invalid-cuisine.json").readText(Charset.forName("UTF-8"))
        mockWebServer.enqueue(MockResponse()
                .setResponseCode(200)
                .setBody(content))
        //when
        repository.getRestaurants().observeForever(observer)
        //then
        verify(observer, timeout(TIMEOUT)).onChanged(ArgumentMatchers.refEq(Resource.success(emptyList())))
    }

    /**
     * Case 4: receive item with invalid latitude.
     * Expected result: Resource(status = SUCCESS, data = [], error = null)
     */
    @Test
    fun itemWithInvalidLatitudeTest() {
        //given
        val content = this.javaClass.classLoader.getResource("get-restaurants-cases/invalid-latitude.json").readText(Charset.forName("UTF-8"))
        mockWebServer.enqueue(MockResponse()
                .setResponseCode(200)
                .setBody(content))
        //when
        repository.getRestaurants().observeForever(observer)
        //then
        verify(observer, timeout(TIMEOUT)).onChanged(ArgumentMatchers.refEq(Resource.success(emptyList())))
    }

    /**
     * Case 5: receive 404 Not Found.
     * Expected result: Resource(status = Error, data = null, error = Error(code = 404, message = "Resource Not Found."))
     */
    @Test
    fun notFoundTest() {
        //given
        mockWebServer.enqueue(MockResponse()
                .setResponseCode(404)
                .setBody("Resource Not Found."))
        //when
        repository.getRestaurants().observeForever(observer)
        //then
        verify(observer, timeout(TIMEOUT)).onChanged(ArgumentMatchers.argThat(Error404NotFoundMatcher()))
    }

}