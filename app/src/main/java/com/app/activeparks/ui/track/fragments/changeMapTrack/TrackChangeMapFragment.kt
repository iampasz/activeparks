package com.app.activeparks.ui.track.fragments.changeMapTrack

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.technodreams.activeparks.databinding.FragmentEditTrackBinding

class TrackChangeMapFragment  : Fragment() {

    lateinit var binding: FragmentEditTrackBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentEditTrackBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setListener()
    }
    private fun setListener() {
        with(binding) {

            ivBack.setOnClickListener {
                requireActivity().onBackPressed()
            }

        }
    }

}