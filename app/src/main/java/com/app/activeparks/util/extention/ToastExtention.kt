package com.app.activeparks.util.extention

import android.content.Context
import android.widget.Toast

    fun toast(context: Context, message: String) =
        Toast.makeText(
            context,
            message,
            Toast.LENGTH_SHORT
        ).show()
