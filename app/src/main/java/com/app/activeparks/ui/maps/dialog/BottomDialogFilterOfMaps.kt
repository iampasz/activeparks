package com.app.activeparks.ui.maps.dialog

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.app.activeparks.data.model.sportsgrounds.ItemSportsground
import com.app.activeparks.ui.park.ParkActivity
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.technodreams.activeparks.R

class BottomDialogFilterOfMaps : BottomSheetDialogFragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(
            R.layout.dialog_bottom_filter_map, container,
            false
        )

        return view
    }

    companion object {
        fun newInstance(): BottomDialogFilterOfMaps {
            return BottomDialogFilterOfMaps()
        }
    }
}