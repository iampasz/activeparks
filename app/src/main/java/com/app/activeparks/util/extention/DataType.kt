package com.app.activeparks.util.extention

import android.content.Context
import android.location.Address
import androidx.core.content.ContextCompat
import com.app.activeparks.data.model.track.PointsTrack
import com.app.activeparks.ui.active.model.ActivityInfoTrainingItem
import com.app.activeparks.util.MapsViewController
import com.technodreams.activeparks.R
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Polyline

/**
 * Created by O.Dziuba on 15.11.2023.
 */

//Double
fun Double.toInfo() = String.format("%.2f", this).replace(",", ".")

//FLoat
fun Float.toInfo() = String.format("%.2f", this).replace(",", ".")

//Int
fun Int?.toBoolean() = this == 1

//String
fun String.isPhone() = this.contains("+380")
fun String.setSex() = if (this == "male") "Чоловік" else "Жінка"
fun String.replaceAddress() = this.replace("вулиця", "вул.")
fun String.replacePhone() = this.replaceBrackets().replace(" ", "")
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
fun Long.getNumberForSpeak() = when (this / 1000) {
    2L -> "3"
    1L -> "2"
    else -> "1"
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

fun List<PointsTrack>.getTrackForBack(): List<List<Double>> {
    return this.map { point ->
        listOf(point.latitude, point.longitude)
    }
}


fun List<List<Double>>.getListForUI(): List<GeoPoint> {
    return map { coordinates ->
        if (coordinates.size >= 2) {
            GeoPoint(coordinates[0], coordinates[1])
        } else {
            throw IllegalArgumentException("Invalid coordinates format: $coordinates")
        }
    }
}

fun MutableList<PointsTrack>.drawActiveRoute(
    context: Context,
    mapview: MapView,
    mapsViewController: MapsViewController?
) {
    this.takeIf { it.isNotEmpty() }
        ?.apply {

            val line = Polyline()
            line.width = 12f
            line.color = ContextCompat.getColor(context, R.color.light_green)
            forEach {
                line.addPoint(GeoPoint(it.latitude, it.longitude))
                if (it.turn == "left" || it.turn == "right") {
                    mapsViewController?.addMarker(
                        it.latitude,
                        it.longitude,
                        R.drawable.ic_chekbox_on
                    )
                }
            }

            mapsViewController?.addMarker(
                first().latitude,
                first().longitude,
                R.drawable.ic_start_active_rout
            )
            mapsViewController?.addMarker(
                last().latitude,
                last().longitude,
                R.drawable.ic_end_active_rout
            )

            with(mapview) {
                overlayManager.add(line)
                invalidate()
            }
        }
}

