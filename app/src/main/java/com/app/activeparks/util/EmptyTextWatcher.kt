package com.app.activeparks.util

import android.text.Editable
import android.text.TextWatcher

/**
 * Created by O.Dziuba on 08.11.2023.
 */
open class EmptyTextWatcher: TextWatcher {
    override fun afterTextChanged(s: Editable?) {
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
    }
}