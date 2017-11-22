package com.tolmachevroman.restaurants.utils

import android.os.Handler
import android.os.Looper
import java.util.concurrent.Executor
import java.util.concurrent.Executors
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by romantolmachev on 22/11/2017.
 */
@Singleton
open class AppExecutors(private val mDiskIO: Executor, private val mNetworkIO: Executor, private val mMainThread: Executor) {

    @Inject constructor() : this(Executors.newSingleThreadExecutor(), Executors.newFixedThreadPool(3),
            MainThreadExecutor())

    fun diskIO(): Executor = mDiskIO

    fun networkIO(): Executor = mNetworkIO

    fun mainThread(): Executor = mMainThread

    private class MainThreadExecutor : Executor {
        private val mainThreadHandler = Handler(Looper.getMainLooper())

        override fun execute(command: Runnable) {
            mainThreadHandler.post(command)
        }
    }
}