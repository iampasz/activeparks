package com.app.activeparks.util.extention

import android.content.Context
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.technodreams.activeparks.R

/**
 * Created by O.Dziuba on 30.10.2023.
 */

fun Button.buttonSelectRight() {
    this?.background = this?.resources?.getDrawable(R.drawable.button_filter_select_right,null)
    this?.setTextColor(ContextCompat.getColor(this.context, R.color.white))
}
fun Button.buttonDeselectRight() {
    this?.background = this?.resources?.getDrawable(R.drawable.button_filter_deselect_right,null)
    this?.setTextColor(ContextCompat.getColor(this.context, R.color.color_park))
}

fun Button.buttonSelectLeft() {
    this?.background = this?.resources?.getDrawable(R.drawable.button_filter_select_left,null)
    this?.setTextColor(ContextCompat.getColor(this.context, R.color.white))
}
fun Button.buttonDeselectLeft() {
    this?.background = this?.resources?.getDrawable(R.drawable.button_filter_deselect_left,null)
    this?.setTextColor(ContextCompat.getColor(this.context, R.color.color_park))
}
