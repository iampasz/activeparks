package com.app.activeparks.ui.homeWithUser.fragments.location

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.app.activeparks.util.extention.gone
import com.technodreams.activeparks.databinding.FragmentHomeLocationBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeLocationFragment : Fragment() {

    private lateinit var binding: FragmentHomeLocationBinding
    val adapter = HomeLocationAdapter()
    private val viewModel: HomeLocationViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeLocationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getParks()
        initView()
        observe()
    }

    private fun observe() {
        with(viewModel) {
            parksList.observe(viewLifecycleOwner) { response ->
                binding.pbParks.gone()
                response?.items?.takeIf { it.isNotEmpty() }
                    ?.apply {
                        adapter.list.submitList(this)
                    }
            }
        }
    }

    private fun initView() {
        binding.rvLocations.adapter = adapter
    }

}