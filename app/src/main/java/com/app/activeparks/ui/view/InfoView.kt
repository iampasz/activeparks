package com.app.activeparks.ui.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.app.activeparks.ui.active.model.ActivityInfoItem
import com.app.activeparks.ui.active.model.InfoItem
import com.technodreams.activeparks.databinding.ViewActivityInfoBinding
import com.technodreams.activeparks.databinding.ViewInfoBinding

/**
 * Created by O.Dziuba on 28.10.2023.
 */
class InfoView constructor(
    context: Context, attrs: AttributeSet? = null
) : FrameLayout(context, attrs){

    private var binding: ViewInfoBinding
    private var item: InfoItem? = null


    init {
        binding = ViewInfoBinding.inflate(LayoutInflater.from(context), this, true)
    }

    fun setActivityInfoItem(item: InfoItem) {
        this.item = item

        setTitle(item.title)
        setDescription(item.description)
        setImg(item.img)
    }
    private fun setTitle(title: String) {
        binding.tvTitle.text = title
    }

    private fun setDescription(number: String) {
        binding.tvDescription.text = number
    }

    private fun setImg(id: Int) {
        binding.ivIcon.setImageResource(id)
    }




}