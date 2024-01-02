package com.app.activeparks.ui.userProfile.heals

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.technodreams.activeparks.databinding.FragmentUserProfileHealsBinding

/**
 * Created by O.Dziuba on 21.12.2023.
 */
class HealsFragmentUserProfile : Fragment() {

    private lateinit var binding: FragmentUserProfileHealsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUserProfileHealsBinding.inflate(inflater, container, false)

        return binding.root
    }
}