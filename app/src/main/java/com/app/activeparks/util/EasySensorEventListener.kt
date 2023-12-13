package com.app.activeparks.util

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener

/**
 * Created by O.Dziuba on 13.12.2023.
 */
interface EasySensorEventListener : SensorEventListener {
    override fun onSensorChanged(event: SensorEvent) { }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) { }
}