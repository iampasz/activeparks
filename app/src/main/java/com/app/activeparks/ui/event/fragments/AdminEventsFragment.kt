package com.app.activeparks.ui.event.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.activeparks.data.model.sportevents.ItemEvent
import com.app.activeparks.ui.event.adapter.AdminEventsAdapter
import com.technodreams.activeparks.databinding.FragmentAdminEventsBinding

class AdminEventsFragment : Fragment() {

    lateinit var binding: FragmentAdminEventsBinding
    private val viewModel: MainEventViewModel by activityViewModels()


    var adminEventsAdapter: AdminEventsAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAdminEventsBinding
            .inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("ObsoleteSdkInt")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getAdminEvents()
        viewModel.eventList.observe(viewLifecycleOwner) { response ->
            response.items?.takeIf { it.isNotEmpty() }
                ?.apply {
                    initView(this)
                } ?: kotlin.run {
            }
        }
    }

    private fun initView(itemEventList:List<ItemEvent>) {

        binding.rvAdminEvents.layoutManager = LinearLayoutManager(requireContext())
        adminEventsAdapter = AdminEventsAdapter(requireContext(), itemEventList)
        binding.rvAdminEvents.adapter = adminEventsAdapter
    }
}