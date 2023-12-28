package com.app.activeparks.ui.active.model


data class PulseZoneBorders(
    val easyMin: Int = 60,
    val easyMax: Int = 90,
    val fatBurningMin: Int = 91,
    val fatBurningMax: Int = 120,
    val aerobicMin: Int = 121,
    val aerobicMax: Int = 140,
    val anaerobicMin: Int = 141,
    val anaerobicMax: Int = 199,
    val upperBorderMin: Int = 200,
    val upperBorderMax: Int = 250,
)
