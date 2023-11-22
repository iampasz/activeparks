package com.app.activeparks.ui.event

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.technodreams.activeparks.databinding.FragmentChangeRouteBinding
import com.technodreams.activeparks.databinding.FragmentEventCreateBinding

class FragmentChangeRoute : Fragment() {

    lateinit var binding: FragmentChangeRouteBinding
    private lateinit var viewModel: EventViewModel


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentChangeRouteBinding
            .inflate(inflater, container, false)

        viewModel = EventViewModel()

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }
}