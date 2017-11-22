package com.tolmachevroman.restaurants

import com.tolmachevroman.restaurants.utils.AppExecutors
import java.util.concurrent.Executor


/**
 * Created by romantolmachev on 22/11/2017.
 */
class InstantAppExecutors : AppExecutors(instant, instant, instant) {
    companion object {
        private val instant: Executor = Executor { command -> command.run() }
    }


}