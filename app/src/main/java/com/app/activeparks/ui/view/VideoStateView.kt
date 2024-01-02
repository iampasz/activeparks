package com.app.activeparks.ui.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import com.app.activeparks.ui.userProfile.model.VideoState
import com.technodreams.activeparks.R
import com.technodreams.activeparks.databinding.ViewVideoStateBinding

/**
 * Created by O.Dziuba on 28.10.2023.
 */
class VideoStateView constructor(
    context: Context, attrs: AttributeSet? = null
) : FrameLayout(context, attrs) {

    private var binding: ViewVideoStateBinding


    init {
        binding = ViewVideoStateBinding.inflate(LayoutInflater.from(context), this, true)
    }

    fun setState(videoState: VideoState) {
        with(binding) {
            tvState.text = videoState.title
            tvState.setCompoundDrawablesWithIntrinsicBounds(videoState.img, 0, 0, 0)
            when (videoState) {
                VideoState.PUBLISHED -> {
                    tvState.apply {
                        setBackgroundResource(R.drawable.button_ap)
                        setTextColor(ContextCompat.getColor(this.context, R.color.white))
                    }
                }

                VideoState.MODERATION -> {
                    tvState.apply {
                        setBackgroundResource(R.drawable.button_black)
                        setTextColor(ContextCompat.getColor(this.context, R.color.white))
                    }
                }

                VideoState.DRAFT -> {
                    tvState.apply {
                        setBackgroundResource(R.drawable.button_gray_video)
                        setTextColor(ContextCompat.getColor(this.context, R.color.purple_500))
                    }
                }
            }
        }
    }
}