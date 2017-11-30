package com.tolmachevroman.restaurants.views

import android.support.v7.app.AppCompatActivity
import com.tolmachevroman.restaurants.dagger.RestaurantsApplication

/**
 * Created by romantolmachev on 30/11/2017.
 */
abstract class AbstractActivity : AppCompatActivity() {

    override fun onDestroy() {
        super.onDestroy()
        val refWatcher = RestaurantsApplication.refWatcher(this)
        refWatcher.watch(this)
    }
}