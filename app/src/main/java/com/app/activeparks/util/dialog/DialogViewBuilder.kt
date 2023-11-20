package com.app.activeparks.util.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.ViewGroup
import android.view.Window

class DialogViewBuilder(context: Context) : Dialog(context), IDialogViewBuilder {

    override fun build(): Dialog {
        return Dialog(context, androidx.appcompat.R.style.Base_Theme_AppCompat_Dialog_Alert).apply {
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            setCancelable(false)
            window?.apply {
                setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                attributes.gravity = Gravity.CENTER
                attributes.width = ViewGroup.LayoutParams.MATCH_PARENT
            }
        }
    }
}