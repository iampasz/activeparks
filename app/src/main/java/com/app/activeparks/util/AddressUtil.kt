package com.app.activeparks.util

import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.location.Location
import com.app.activeparks.util.extention.getAddress
import com.technodreams.activeparks.R
import java.io.IOException
import java.util.Locale

/**
 * Created by O.Dziuba on 04.12.2023.
 */
class AddressOfDoubleUtil {
    companion object {
        fun getAddressFromLocation(context: Context, latitude: Double, longitude: Double): String {
            val geocoder = Geocoder(context, Locale.getDefault())
            val addresses: List<Address>?

            try {
                addresses = geocoder.getFromLocation(latitude, longitude, 1)
                if (!addresses.isNullOrEmpty()) {
                    val address = addresses[0]
                    val addressStringBuilder = StringBuilder()

                    for (i in 0..address.maxAddressLineIndex) {
                        addressStringBuilder.append(address.getAddressLine(i)).append(" ")
                    }

                    return address.getAddress()
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }

            return context.getString(R.string.tv_address_not_found)
        }
    }
}