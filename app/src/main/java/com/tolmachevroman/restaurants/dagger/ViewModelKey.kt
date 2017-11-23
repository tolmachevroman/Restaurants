package com.tolmachevroman.restaurants.dagger

import android.arch.lifecycle.ViewModel
import dagger.MapKey
import kotlin.reflect.KClass

/**
 * Created by romantolmachev on 23/11/2017.
 */
@MapKey
annotation class ViewModelKey(val value: KClass<out ViewModel>)