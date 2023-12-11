package com.app.activeparks.ui.active.model

import org.osmdroid.util.GeoPoint

data class ProfileState(

    val activeRoad: MutableList<GeoPoint> = mutableListOf(),
    var startPoint: String = "",
    var currentPulse: Int = 80,
    var pulseOnPause: Int = 75,
    var maxPulse: Int = 150,
    var pulseZone: PulseZone = PulseZone.getPulseZone().first(),
    var onSelectedTypeFromSetting: Boolean = false,
    var isPulseGadgetConnected: Boolean = true,
    var isAutoPause: Boolean = false,
    var isAudioHelper: Boolean = false,
    var isLazyStart: Boolean = false,
    var isTrainingStart: Boolean = false,
    var isPause: Boolean = false,
    var isAutoPulseZone: Boolean = false,
    var isAutoBlockScreen: Boolean = false,
)
