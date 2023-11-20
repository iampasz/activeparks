package com.app.activeparks.ui.event

import android.content.Context
import android.location.Geocoder
import android.os.AsyncTask
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.osmdroid.bonuspack.location.GeocoderNominatim
import org.osmdroid.util.GeoPoint
import java.lang.ref.WeakReference
import java.util.Locale

class GeocodingAsyncTask(
    context: Context,
    private val coordinate: GeoPoint,
    private val callback: (String) -> Unit
) : AsyncTask<Void, Void, String>() {

    private val contextReference: WeakReference<Context> = WeakReference(context)

    override fun doInBackground(vararg params: Void?): String {
        val context = contextReference.get()
        if (context != null) {
            val geocoder = Geocoder(context, Locale.getDefault())
            try {
                val addresses = geocoder.getFromLocation(
                    coordinate.latitude,
                    coordinate.longitude,
                    1
                )

                if (addresses != null && addresses.isNotEmpty()) {
                    val address = addresses[0]
                    return address.getAddressLine(0) ?: ""
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return ""
    }

    override fun onPostExecute(result: String) {
        callback.invoke(result)
    }
}