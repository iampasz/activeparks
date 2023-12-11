package com.app.activeparks.util.extention

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager

/**
 * Created by O.Dziuba on 06.12.2023.
 */
class Keyboard {
    companion object {
        fun hideKeyboard(context: Context, view: View) {
            val inputMethodManager =
                context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

            inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }
}