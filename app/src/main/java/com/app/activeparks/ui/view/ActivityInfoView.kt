package com.app.activeparks.ui.view

import android.content.Context
import android.text.Html
import android.text.Html.FROM_HTML_MODE_LEGACY
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.app.activeparks.ui.active.model.ActivityInfoTrainingItem
import com.technodreams.activeparks.databinding.ViewActivityInfoBinding

/**
 * Created by O.Dziuba on 28.10.2023.
 */
class ActivityInfoView constructor(
    context: Context, attrs: AttributeSet? = null
) : FrameLayout(context, attrs) {

    private var binding: ViewActivityInfoBinding
    private var item: ActivityInfoTrainingItem? = null


    init {
        binding = ViewActivityInfoBinding.inflate(LayoutInflater.from(context), this, true)
    }

    fun setActivityInfoItem(item: ActivityInfoTrainingItem) {
        this.item = item

        setDescription("${item.description}<br>${item.unit}")
        setNumber(item.number)
    }

    fun getActivityInfoItem() = item

    fun setNumber(number: String) {
        binding.tvNumber.text = number
    }

    private fun setDescription(title: String) {
        binding.tvTitle.apply {
            text = Html.fromHtml(title, FROM_HTML_MODE_LEGACY)
            gravity = Gravity.CENTER
        }
    }
}
