package com.app.activeparks.ui.active.tab.map

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.app.activeparks.util.MapsViewControler
import com.technodreams.activeparks.databinding.FragmentMapActivityBinding

class MapActivityFragment : Fragment() {


    lateinit var binding: FragmentMapActivityBinding
    private var mapsViewController: MapsViewControler? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentMapActivityBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setCurrentLocation()
    }
    private fun setCurrentLocation() {
        mapsViewController = MapsViewControler(binding.mapview, context)
        mapsViewController?.homeView = true
    }
}