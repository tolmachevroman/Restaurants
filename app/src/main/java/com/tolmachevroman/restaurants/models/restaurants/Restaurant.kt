package com.tolmachevroman.restaurants.models.restaurants

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import java.io.Serializable

/**
 * Created by romantolmachev on 22/11/2017.
 *
 *
    {
    "id": 1,
    "cuisine": 1,
    "restaurant_name": "Mi Cevichazo",
    "lat": -33.431756,
    "lng": -70.578719,
    "price": 8900,
    "image": "",
    "description": "Todo el sabor del Perú está presente en el restaurante Mi Cevichazo."
    }
 *
 * IMPORTANT: Provide default values or explicitly check for non-null values before passing to DAO
 *
 */
@Entity
data class Restaurant(@PrimaryKey val id: Int, val cuisine: Int?, val name: String?,
                      val lat: Double, val lng: Double, val price: Int,
                      val image: String = "", val description: String = "") : Serializable