package com.app.activeparks.util.extention

import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat

/**
 * Created by O.Dziuba on 30.10.2023.
 */

fun View?.visible() {
    this?.visibility = View.VISIBLE
}

fun View?.invisible() {
    this?.visibility = View.INVISIBLE
}

fun View?.gone() {
    this?.visibility = View.GONE
}

fun TextView.setTint(color: Int) {
    for (drawable in this.compoundDrawables) {
        if (drawable != null) {
            drawable.colorFilter =
                PorterDuffColorFilter(
                    ContextCompat.getColor(
                        this.context,
                        color
                    ), PorterDuff.Mode.SRC_IN
                )
        }
    }
}