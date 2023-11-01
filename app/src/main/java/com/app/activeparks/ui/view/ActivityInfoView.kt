package com.app.activeparks.ui.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.app.activeparks.ui.active.model.ActivityInfoItem
import com.technodreams.activeparks.databinding.ViewActivityInfoBinding

/**
 * Created by O.Dziuba on 28.10.2023.
 */
class ActivityInfoView constructor(
    context: Context, attrs: AttributeSet? = null
) : FrameLayout(context, attrs){

    private var binding: ViewActivityInfoBinding
    private var item: ActivityInfoItem? = null


    init {
        binding = ViewActivityInfoBinding.inflate(LayoutInflater.from(context), this, true)
    }

    fun setActivityInfoItem(item: ActivityInfoItem) {
        this.item = item

        setTitle(item.title)
    }

    fun getActivityInfoItem() = item

    fun setNumber(number: String) {
        binding.tvNumber.text = number
    }

    private fun setTitle(title: String) {
        binding.tvTitle.text = title
    }



}