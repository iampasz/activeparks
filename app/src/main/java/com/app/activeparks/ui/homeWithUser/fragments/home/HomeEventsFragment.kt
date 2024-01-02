package com.app.activeparks.ui.homeWithUser.fragments.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.app.activeparks.ui.event.activity.EventListActivity2
import com.app.activeparks.util.extention.gone
import com.app.activeparks.util.extention.visible
import com.technodreams.activeparks.databinding.FragmentHomeEventsBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeEventsFragment : Fragment() {

    private lateinit var binding: FragmentHomeEventsBinding
    val adapter = HomeEventsAdapter()
    private val viewModel: HomeEventsViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeEventsBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getEvents()
        initView()
        initListener()
        observe()
    }

    private fun initListener() {
        with(binding) {
            tvEventsCalendar.setOnClickListener {
                startActivity(Intent(requireActivity(), EventListActivity2::class.java))
            }
        }
    }

    private fun observe() {
        with(viewModel) {
            eventList.observe(viewLifecycleOwner) { response ->
                binding.pbEvents.gone()
                response?.takeIf { it.isNotEmpty() }
                    ?.apply {
                        adapter.list.submitList(this)
                    } ?: kotlin.run {
                    binding.tvMoreEvents.gone()
                    binding.tvNoEvents.visible()
                }
            }
        }
    }

    private fun initView() {
        binding.rvEvents.adapter = adapter
    }

}