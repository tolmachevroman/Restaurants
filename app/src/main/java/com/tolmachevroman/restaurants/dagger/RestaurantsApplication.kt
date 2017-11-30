package com.tolmachevroman.restaurants.dagger

import android.app.Activity
import android.app.Application
import android.content.Context
import com.squareup.leakcanary.LeakCanary
import com.squareup.leakcanary.RefWatcher
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
class RestaurantsApplication : Application(), HasActivityInjector {

    @Inject
    lateinit var activityInjector: DispatchingAndroidInjector<Activity>

    lateinit var refWatcher: RefWatcher

    companion object {
        @JvmStatic fun refWatcher(context: Context): RefWatcher =
                (context.applicationContext as RestaurantsApplication).refWatcher
    }

     override fun onCreate() {
        super.onCreate()

         if (LeakCanary.isInAnalyzerProcess(this)) {
             // This process is dedicated to LeakCanary for heap analysis.
             // You should not init your app in this process.
             return
         }
         refWatcher = LeakCanary.install(this)

         DaggerAppComponent
                 .builder()
                 .appModule(AppModule(this))
                 .netModule(NetModule(BuildConfig.URL))
                 .build()
                 .inject(this)

    }

    override fun activityInjector(): AndroidInjector<Activity> = activityInjector
}