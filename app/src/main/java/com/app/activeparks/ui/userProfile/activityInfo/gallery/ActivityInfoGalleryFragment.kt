package com.app.activeparks.ui.userProfile.activityInfo.gallery

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.app.activeparks.ui.userProfile.activityInfo.ActivityInfoViewModel
import com.technodreams.activeparks.databinding.FragmentActivityInfoGalleryBinding
import org.koin.androidx.viewmodel.ext.android.activityViewModel

/**
 * Created by O.Dziuba on 22.01.2024.
 */
class ActivityInfoGalleryFragment : Fragment() {

    private lateinit var binding: FragmentActivityInfoGalleryBinding
    private val viewModel: ActivityInfoViewModel by activityViewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentActivityInfoGalleryBinding.inflate(inflater, container, false)

        return binding.root
    }
}