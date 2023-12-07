package com.app.activeparks.util

import android.content.Context
import android.location.Geocoder
import android.os.AsyncTask
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.osmdroid.util.GeoPoint
import java.util.Locale

//class GeocodingAsyncTask(
//    private val context: Context,
//    private val coordinate: GeoPoint,
//    private val callback: (String) -> Unit
//) {
//
//      fun fetchDataAsync() {
//
//
//        GlobalScope.launch(Dispatchers.Main) {
//            val result = doInBackground()
//            callback(result)
//        }
//    }
//
//    private  fun doInBackground(): String {
//        val geocoder = Geocoder(context, Locale.getDefault())
//        var address = ""
//        try {
//            val addresses = geocoder.getFromLocation(
//                coordinate.latitude,
//                coordinate.longitude,
//                1
//            )
//
//            if (addresses != null && addresses.isNotEmpty()) {
//                address = addresses[0].getAddressLine(0) ?: ""
//            }
//        } catch (e: Exception) {
//            e.printStackTrace()
//        }
//        return address
//    }
//}

class GeocodingAsyncTask {

    suspend fun fetchData(
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