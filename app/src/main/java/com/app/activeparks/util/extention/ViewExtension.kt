package com.app.activeparks.util.extention

import android.content.Context
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat

/**
 * Created by O.Dziuba on 30.10.2023.
 */

fun View?.visible() {
    this?.visibility = View.VISIBLE
}
fun Context.visible(vararg view: View) {
    view.forEach {
        it.visible()
    }
}

fun View?.visibleIf(isVisible: Boolean) {
    this?.visibility = if (isVisible) View.VISIBLE else View.GONE
}

fun View?.invisible() {
    this?.visibility = View.INVISIBLE
}

fun View?.gone() {
    this?.visibility = View.GONE
}
fun Context.gone(vararg view: View) {
    view.forEach {
        it.gone()
    }
}

fun View?.enable() {
    this?.isEnabled = true
}

fun View?.enableIf(isEnable: Boolean) {
    this?.isEnabled = isEnable
}

fun Context.enableIf(isEnable: Boolean, vararg views: View) {
    views.forEach {
        it.enableIf(isEnable)
    }
}

fun View?.disable() {
    this?.isEnabled = false
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

fun TextView.setPulseZone(title: String) {
    this.apply {
        text = title
        layoutParams.apply {
            (this as LinearLayout.LayoutParams).weight = 10f
            requestLayout()
        }
    }
}