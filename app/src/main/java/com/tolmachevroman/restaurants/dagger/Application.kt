package com.tolmachevroman.restaurants.dagger

import android.app.Activity
import android.app.Application
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import javax.inject.Inject

/**
 * Created by romantolmachev on 22/11/2017.
 */
class Application: Application(), HasActivityInjector {

    @Inject
    lateinit var activityInjector: DispatchingAndroidInjector<Activity>

     override fun onCreate() {
        super.onCreate()

    }

    override fun activityInjector(): AndroidInjector<Activity> = activityInjector
}