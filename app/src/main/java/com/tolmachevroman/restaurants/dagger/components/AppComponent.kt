package com.tolmachevroman.restaurants.dagger.components

import android.app.Application
import com.tolmachevroman.restaurants.dagger.modules.AppModule
import com.tolmachevroman.restaurants.dagger.modules.BuildersModule
import com.tolmachevroman.restaurants.dagger.modules.NetModule
import dagger.Component
import dagger.android.AndroidInjectionModule
import javax.inject.Singleton

/**
 * Created by romantolmachev on 22/11/2017.
 */
@Singleton
@Component(modules = arrayOf(AndroidInjectionModule::class, BuildersModule::class, AppModule::class, NetModule::class))
interface AppComponent {
    fun inject(app: Application)
}