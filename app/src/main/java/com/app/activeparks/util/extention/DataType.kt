package com.app.activeparks.util.extention

import android.location.Address
import com.app.activeparks.ui.active.model.ActivityInfoTrainingItem
import org.osmdroid.util.GeoPoint

/**
 * Created by O.Dziuba on 15.11.2023.
 */

//Double
fun Double.toInfo() = String.format("%.2f", this).replace(",", ".")

//FLoat
fun Float.toInfo() = String.format("%.2f", this).replace(",", ".")

//String
fun String.isPhone() = this.contains("+380")
fun String.setSex() = if (this == "male") "Чоловік" else "Жінка"
fun String.replaceAddress() = this.replace("вулиця", "вул.")
fun String.replaceBrackets() = this.replace("(", "").replace(")", "")
fun String.replaceNull() = this.replace("null", "")
fun String.timeToSeconds(): Int {
    val timeRegex = Regex("""(\d{2}):(\d{2}):(\d{2})""")

    val matchResult = timeRegex.find(this)
    return if (matchResult != null) {
        val (hours, minutes, seconds) = matchResult.destructured
        hours.toInt() * 3600 + minutes.toInt() * 60 + seconds.toInt()
    } else {
        val minutesRegex = Regex("""(\d{2}):(\d{2})""")
        val minutesMatchResult = minutesRegex.find(this)
        if (minutesMatchResult != null) {
            val (minutes, seconds) = minutesMatchResult.destructured
            minutes.toInt() * 60 + seconds.toInt()
        } else {
            return 0
        }
    }
}


//Address
fun Address.getAddress() =
    "м. ${this.locality}, ${this.thoroughfare} ${this.subThoroughfare}".replaceAddress()

//Long
fun Long.getStingForSpeak() = when (this / 1000) {
    2L -> "Три"
    1L -> "Два"
    else -> "Один"
}

//List
fun List<ActivityInfoTrainingItem>.filterInside() =
    this.filter { !it.isOutside }.filter { !it.isPulseGadget }


fun List<ActivityInfoTrainingItem>.filterOutside(activeTypeId: Int): List<ActivityInfoTrainingItem> {
    return if (activeTypeId == 2) {
        this
            .filter { !it.isPulseGadget }
            .filter { it.id != 11 }
    } else {
        this
            .filter { !it.isPulseGadget }
    }
}

fun List<GeoPoint>.getListForBack(): List<List<Double>> {
    return this.map { geoPoint ->
        listOf(geoPoint.latitude, geoPoint.longitude)
    }
}

