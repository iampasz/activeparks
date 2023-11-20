package com.app.activeparks.util.extention

import android.location.Address
import com.app.activeparks.ui.active.model.ActivityInfoTrainingItem

/**
 * Created by O.Dziuba on 15.11.2023.
 */

//Double
fun Double.toInfo() = String.format("%.2f", this).replace(",", ".")

//FLoat
fun Float.toInfo() = String.format("%.2f", this).replace(",", ".")

//String
fun String.replaceAddress() = this.replace("вулиця", "вул.")
fun String.replaceBrackets() = this.replace("(", "").replace(")", "")
fun String.replaceNull() = this.replace("null", "")

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

fun List<ActivityInfoTrainingItem>.filterOutside() = this.filter { !it.isPulseGadget }
