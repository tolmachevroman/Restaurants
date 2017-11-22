package com.tolmachevroman.restaurants

import android.arch.core.executor.testing.InstantTaskExecutorRule
import com.tolmachevroman.restaurants.datasources.webservice.WebService
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.*
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.nio.charset.Charset

/**
 * Created by romantolmachev on 22/11/2017.
 */
@RunWith(JUnit4::class)
class WebServiceTest {

    @Rule
    @JvmField
    var instantExecutor = InstantTaskExecutorRule()

    private lateinit var webService: WebService
    private lateinit var mockWebServer: MockWebServer

    @Before
    fun setup() {
        mockWebServer = MockWebServer()
        mockWebServer.start()

        webService = Retrofit.Builder()
                .addConverterFactory(MoshiConverterFactory.create())
                .baseUrl(mockWebServer.url("/"))
                .client(OkHttpClient())
                .build()
                .create(WebService::class.java)
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun getRestaurantsTest() {
        val content = this.javaClass.classLoader.getResource("get-restaurants.json").readText(Charset.forName("UTF-8"))
        mockWebServer.enqueue(MockResponse()
                .setResponseCode(200)
                .setBody(content))
        val response = webService.getRestaurants().execute()
        Assert.assertTrue(response.isSuccessful)
        Assert.assertNotNull(response.body())
        Assert.assertEquals(response.body()!!.first().id, 1)
    }
}