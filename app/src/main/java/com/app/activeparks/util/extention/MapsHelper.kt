package com.app.activeparks.util.extention

import com.app.activeparks.data.model.track.PointsTrack
import org.osmdroid.util.GeoPoint
import kotlin.random.Random

/**
 * Created by O.Dziuba on 31.01.2024.
 */


fun generateRandomRoute(center: GeoPoint, numPoints: Int, maxDistance: Double): List<PointsTrack> {
    val route = mutableListOf<PointsTrack>()
    for (i in 0 until numPoints) {
        val randomLatOffset = Random.nextDouble(-maxDistance, maxDistance)
        val randomLonOffset = Random.nextDouble(-maxDistance, maxDistance)
        val randomPoint = GeoPoint(center.latitude + randomLatOffset, center.longitude + randomLonOffset)
        route.add(PointsTrack(
            randomPoint.latitude,
            randomPoint.longitude,
            if (i/3 == 1) "left" else ""
        ))
    }
    return route
}