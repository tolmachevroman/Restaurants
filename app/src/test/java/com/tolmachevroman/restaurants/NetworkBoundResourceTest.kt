package com.tolmachevroman.restaurants

import android.arch.core.executor.testing.InstantTaskExecutorRule
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Observer
import com.google.gson.Gson
import com.tolmachevroman.restaurants.datasources.database.NetworkBoundResource
import com.tolmachevroman.restaurants.datasources.webservice.Resource
import com.tolmachevroman.restaurants.utils.AppExecutors
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.*
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.ArgumentMatchers
import org.mockito.Mock
import org.mockito.Mockito.reset
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import utils.Error404NotFoundMatcher
import java.util.concurrent.atomic.AtomicBoolean
import java.util.function.Function


/**
 * Created by romantolmachev on 22/11/2017.
 */
@RunWith(JUnit4::class)
class NetworkBoundResourceTest {

    @Rule
    @JvmField
    var instantExecutor = InstantTaskExecutorRule()

    companion object {
        data class Foo(val value: Int)

        interface FooWebService {
            @GET("foo")
            fun getFoo(): Call<Foo>
        }
    }

    @Mock
    private lateinit var observer: Observer<Resource<Foo>>

    private lateinit var appExecutors: AppExecutors
    private lateinit var networkBoundResource: NetworkBoundResource<Foo>
    private lateinit var dbData: MutableLiveData<Foo>

    private lateinit var saveNetworkCallResult: Function<Foo?, Unit>
    private lateinit var shouldLoadFromNetwork: Function<Foo?, Boolean>
    private lateinit var createNetworkCall: Function<Unit, Call<Foo>>

    private lateinit var fooWebService: FooWebService
    private lateinit var mockWebServer: MockWebServer

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        appExecutors = InstantAppExecutors()
        dbData = MutableLiveData()
        networkBoundResource = object : NetworkBoundResource<Foo>(appExecutors) {
            override fun saveNetworkCallResult(data: Foo?) {
//                println("saveNetworkCallResult")
                saveNetworkCallResult.apply(data)
            }

            override fun shouldLoadFromNetwork(data: Foo?): Boolean {
//                println("shouldLoadFromNetwork")
                return shouldLoadFromNetwork.apply(data)
            }

            override fun loadFromDatabase(): LiveData<Foo> {
//                println("loadFromDatabase")
                return dbData
            }

            override fun createNetworkCall(): Call<Foo> {
//                println("createNetworkCall")
                return createNetworkCall.apply(Unit)
            }
        }

        mockWebServer = MockWebServer()
        mockWebServer.start()

        fooWebService = Retrofit.Builder()
                .addConverterFactory(MoshiConverterFactory.create())
                .baseUrl(mockWebServer.url("/"))
                .client(OkHttpClient())
                .build()
                .create(FooWebService::class.java)
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    /**
     * Resource.loading(null) -> [shouldLoadFromNetwork() -> true] -> [createNetworkCall() -> networkFoo]
     * -> [loadFromDatabase() -> Resource.success(networkFoo)]
     */
    @Test
    fun successWithNetwork() {

        val saved = AtomicBoolean(false)
        shouldLoadFromNetwork = Function { foo -> foo == null }
        saveNetworkCallResult = Function { foo -> saved.set(true); dbData.value = foo; }

        networkBoundResource.asLiveData().observeForever(observer)
        verify(observer).onChanged(ArgumentMatchers.refEq(Resource.loading(null)))
        reset(observer)

        val networkFoo = Foo(1)
        createNetworkCall = Function { _ ->  fooWebService.getFoo()}

        mockWebServer.enqueue(MockResponse()
                .setResponseCode(200)
                .setBody(Gson().toJson(networkFoo)))

        dbData.value = null
        Assert.assertTrue(saved.get())
        verify(observer).onChanged(ArgumentMatchers.refEq(Resource.success(networkFoo)))
    }

    /**
     * Resource.loading(null) -> [shouldLoadFromNetwork -> false] -> [loadFromDatabase -> Resource.success(dbFoo)]
     */
    @Test
    fun successShouldNotLoadFromNetwork() {

        val saved = AtomicBoolean(false)
        val dbFoo = Foo(1)
        dbData.value = dbFoo
        shouldLoadFromNetwork = Function { foo -> foo == null }
        saveNetworkCallResult = Function { _ -> saved.set(true) }

        networkBoundResource.asLiveData().observeForever(observer)
        verify(observer).onChanged(ArgumentMatchers.refEq(Resource.loading(null)))

        Assert.assertFalse(saved.get())
        verify(observer).onChanged(ArgumentMatchers.refEq(Resource.success(dbFoo)))
    }

    /**
     * Resource.loading(null) -> [shouldLoadFromNetwork() -> true] -> [createNetworkCall() -> 404]
     * -> [loadFromDatabase() -> Resource.error()]
     */
    @Test
    fun failureWithNetwork() {

        val saved = AtomicBoolean(false)
        shouldLoadFromNetwork = Function { foo -> foo == null }
        saveNetworkCallResult = Function { foo -> saved.set(true); dbData.value = foo; }

        networkBoundResource.asLiveData().observeForever(observer)
        verify(observer).onChanged(ArgumentMatchers.refEq(Resource.loading(null)))
        reset(observer)

        createNetworkCall = Function { _ -> fooWebService.getFoo() }

        mockWebServer.enqueue(MockResponse()
                .setResponseCode(404))

        dbData.value = null
        Assert.assertFalse(saved.get())
        verify(observer).onChanged(ArgumentMatchers.argThat(Error404NotFoundMatcher()))
    }

}