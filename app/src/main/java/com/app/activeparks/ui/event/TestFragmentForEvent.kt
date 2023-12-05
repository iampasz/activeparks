package com.app.activeparks.ui.event

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.technodreams.activeparks.databinding.FragmentEventCreateBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class TestFragmentForEvent : Fragment() {

    lateinit var binding: FragmentEventCreateBinding
    private val testEventViewModel: TestEventViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEventCreateBinding
            .inflate(inflater, container, false)

        return binding.root
    }


}