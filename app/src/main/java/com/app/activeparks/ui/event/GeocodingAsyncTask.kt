package com.app.activeparks.ui.event

import android.content.Context
import android.location.Geocoder
import org.osmdroid.util.GeoPoint
import java.util.Locale


 class GeocodingAsyncTask {

    fun fetchData(
        context: Context,
        coordinate: GeoPoint
    ): String {

        val geocoder = Geocoder(context, Locale.getDefault())
        var address = ""
        try {
            val addresses = geocoder.getFromLocation(
                coordinate.latitude,
                coordinate.longitude,
                1
            )

            if (addresses != null) {
                address = addresses[0].getAddressLine(0) ?: ""
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return address
    }
}