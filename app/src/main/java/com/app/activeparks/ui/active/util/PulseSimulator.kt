package com.app.activeparks.ui.active.util

import android.os.Handler
import android.os.Looper
import kotlin.random.Random

class PulseSimulator(private val pulseListener: (Int) -> Unit) {
    private var isIncreasing = true
    private var currentPulse = 78

    private val handler = Handler(Looper.getMainLooper())
    private val pulseChangeRunnable = object : Runnable {
        override fun run() {
            if (isIncreasing) {
                currentPulse += Random.nextInt(1, 5)
                if (currentPulse >= 160) {
                    currentPulse = 160
                    isIncreasing = false
                }
            } else {
                currentPulse -= Random.nextInt(1, 5)
                if (currentPulse <= 78) {
                    currentPulse = 78
                    isIncreasing = true
                }
            }
            pulseListener.invoke(currentPulse)
            handler.postDelayed(this, 2000)
        }
    }

    fun startSimulation() {
        pulseListener.invoke(currentPulse)
        handler.postDelayed(pulseChangeRunnable, 2000)
    }

    fun stopSimulation() {
        handler.removeCallbacks(pulseChangeRunnable)
    }
}