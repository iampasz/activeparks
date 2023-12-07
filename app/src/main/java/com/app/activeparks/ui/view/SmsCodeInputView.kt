package com.app.activeparks.ui.view

import android.content.Context
import android.graphics.Color
import android.text.Editable
import android.util.AttributeSet
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import androidx.constraintlayout.widget.ConstraintLayout
import com.app.activeparks.util.EasyTextWatcher
import com.technodreams.activeparks.R


/**
 * Created by O.Dziuba on 22.11.2023.
 */

class SmsCodeInputView : ConstraintLayout {

    private lateinit var editTexts: Array<EditText>
    private lateinit var indicators: Array<View>
    private lateinit var codeCompleteListener: CodeCompleteListener

    interface CodeCompleteListener {
        fun onCodeComplete(code: String)
        fun notCodeComplete()
    }

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init()
    }

    private fun init() {
        LayoutInflater.from(context).inflate(R.layout.view_sms_code_input, this, true)

        editTexts = arrayOf(
            findViewById(R.id.editText1),
            findViewById(R.id.editText2),
            findViewById(R.id.editText3),
            findViewById(R.id.editText4),
            findViewById(R.id.editText5),
            findViewById(R.id.editText6)
        )

        indicators = arrayOf(
            findViewById(R.id.indicator1),
            findViewById(R.id.indicator2),
            findViewById(R.id.indicator3),
            findViewById(R.id.indicator4),
            findViewById(R.id.indicator5),
            findViewById(R.id.indicator6)
        )

        setupEditTexts()
    }


    private fun setupEditTexts() {
        for (i in editTexts.indices) {
            editTexts[i].apply {

                val indicator = indicators[i]

                filters = arrayOf(android.text.InputFilter.LengthFilter(1))

                setOnEditorActionListener { _, actionId, event ->
                    if (actionId == EditorInfo.IME_ACTION_NEXT || (event != null && event.keyCode == KeyEvent.KEYCODE_ENTER)) {
                        if (i < editTexts.size - 1) {
                            editTexts[i + 1].requestFocus()
                        }
                        return@setOnEditorActionListener true
                    }
                    false
                }

                setOnKeyListener { _, keyCode, event ->
                    if (keyCode == KeyEvent.KEYCODE_DEL && event.action == KeyEvent.ACTION_DOWN) {
                        if (text.isEmpty() && i > 0) {
                            editTexts[i - 1].requestFocus()
                        }
                    }
                    false
                }

                addTextChangedListener(object : EasyTextWatcher() {

                    override fun afterTextChanged(s: Editable?) {
                        indicator.setBackgroundColor(if (s.isNullOrEmpty()) Color.BLACK else Color.GREEN)
                        checkCodeComplete()

                        if (s?.length == 1 && i < editTexts.size - 1) {
                            editTexts[i + 1].requestFocus()
                        }
                    }
                })
            }
        }
    }

    private fun checkCodeComplete() {
        if (editTexts.all { it.text.isNotEmpty() }) {
            codeCompleteListener.onCodeComplete(getCode())
        } else {
            codeCompleteListener.notCodeComplete()
        }
    }

    fun setCodeCompleteListener(listener: CodeCompleteListener) {
        this.codeCompleteListener = listener
    }

    fun getCode(): String {
        val stringBuilder = StringBuilder()
        for (editText in editTexts) {
            stringBuilder.append(editText.text.toString())
        }
        return stringBuilder.toString()
    }

    fun clearAllFields() {
        for (editText in editTexts) {
            editText.text.clear()
        }
        editTexts[0].requestFocus()
    }
}
