package com.app.activeparks.util
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText


/**
 * Created by O.Dziuba on 17.01.2024.
 */
class PhoneNumberMaskWatcher(private val editText: EditText,
    private val job: () -> Unit) : TextWatcher {


    private var isDeleting = false

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        isDeleting = count > after
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        if (isDeleting) return

        val digits = s?.filter { it.isDigit() }

        if (digits.isNullOrEmpty()) {
            editText.setText("+38 (0")
            editText.setSelection(editText.text.length)
            return
        }

        val formattedText = buildString {
            append("+38 (0")
            if (digits.length > 2) {
                append(digits.drop(3).take(2))
                append(") ")
                if (digits.length > 5) {
                    append(" ")
                    append(digits.drop(5).take(3))
                    if (digits.length > 8) {
                        append(" ")
                        append(digits.drop(8).take(2))
                        if (digits.length > 10) {
                            append(" ")
                            append(digits.drop(10).take(2))
                        }
                    }
                }
            }
        }

        if (formattedText != s?.toString()) {
            editText.removeTextChangedListener(this)
            editText.setText(formattedText)
            editText.setSelection(formattedText.length)
            editText.addTextChangedListener(this)
        }
    }

    override fun afterTextChanged(s: Editable?) {
        job()
    }
}
