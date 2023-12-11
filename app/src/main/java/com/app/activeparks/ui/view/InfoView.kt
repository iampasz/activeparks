package com.app.activeparks.ui.view

import android.content.Context
import android.util.AttributeSet
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.app.activeparks.ui.active.model.InfoItem
import com.app.activeparks.ui.active.model.getPulseInfo
import com.technodreams.activeparks.databinding.ViewInfoBinding

/**
 * Created by O.Dziuba on 28.10.2023.
 */
class InfoView constructor(
    context: Context, attrs: AttributeSet? = null
) : FrameLayout(context, attrs){

    private var binding: ViewInfoBinding
    private lateinit var item: InfoItem


    init {
        binding = ViewInfoBinding.inflate(LayoutInflater.from(context), this, true)
    }

    fun setActivityInfoItem(item: InfoItem) {
        this.item = item

        setTitle(item.unit)
        setDescription(item.description)
        setImg(item.img)
    }
    fun setPulseInfoItem(item: InfoItem) {
        this.item = item

        setTitle(item.getPulseInfo())
        setDescription(item.description)
        setImg(item.img)
    }

    fun getItem() = item
    fun setTitle(title: String) {
        binding.tvTitle.text = title
    }

    private fun setDescription(number: String) {
        binding.tvDescription.text = number
    }

    fun setImg(id: Int) {
        binding.ivIcon.setImageResource(id)
    }

    fun calculateDesiredWidth(): Float {
        val textView = binding.tvTitle
        val paint = textView.paint
        return paint.measureText(binding.tvTitle.text.toString())
    }




}