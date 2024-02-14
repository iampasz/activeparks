package com.app.activeparks.util

import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.util.Log
import java.io.IOException
import java.util.Locale


@Suppress("DEPRECATION")
class GeocodingAsyncTask {

    fun getCityName(context: Context?, latitude: Double, longitude: Double): String? {
        val geocoder = Geocoder(context!!, Locale.getDefault())
        var cityName: String? = null
        try {
            val addresses: List<Address>? = geocoder.getFromLocation(latitude, longitude, 1)
            if (!addresses.isNullOrEmpty()) {
                cityName = addresses[0].locality
            }
        } catch (e: IOException) {
            e.printStackTrace()
            Log.e("LocationHelper", "Error getting city name: " + e.message)
        }

        return cityName
    }
}