package com.app.activeparks.ui.homeWithUser.fragments.event

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.app.activeparks.MainActivity
import com.app.activeparks.ui.event.activity.EventActivityOld
import com.app.activeparks.ui.event.fragments.EventListFragment
import com.app.activeparks.util.extention.gone
import com.app.activeparks.util.extention.visible
import com.technodreams.activeparks.databinding.FragmentHomeEventsBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeEventsFragment : Fragment() {

    private lateinit var binding: FragmentHomeEventsBinding
    val adapter = HomeEventsAdapter{
        startActivity(Intent(activity, EventActivityOld::class.java).putExtra("id", it.id))
    }
    private val viewModel: HomeEventsViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeEventsBinding.inflate(inflater, container, false)
        return binding.root
    }
    @SuppressLint("SetTextI18n")
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
                (requireActivity() as MainActivity).openFragment(EventListFragment())
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