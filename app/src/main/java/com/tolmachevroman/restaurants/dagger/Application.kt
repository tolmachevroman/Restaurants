package com.tolmachevroman.restaurants.dagger

import android.app.Activity
import android.app.Application
import com.tolmachevroman.restaurants.BuildConfig
import com.tolmachevroman.restaurants.dagger.components.DaggerAppComponent
import com.tolmachevroman.restaurants.dagger.modules.AppModule
import com.tolmachevroman.restaurants.dagger.modules.NetModule
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

         DaggerAppComponent
                 .builder()
                 .appModule(AppModule(this))
                 .netModule(NetModule(BuildConfig.URL))
                 .build()
                 .inject(this)

    }

    override fun activityInjector(): AndroidInjector<Activity> = activityInjector
}