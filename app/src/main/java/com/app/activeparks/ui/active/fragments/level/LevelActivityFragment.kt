package com.app.activeparks.ui.active.fragments.level

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.app.activeparks.ui.active.ActiveViewModel
import com.app.activeparks.ui.active.model.LevelOfActivity
import com.app.activeparks.ui.active.model.PulseZone
import com.app.activeparks.util.extention.filterInside
import com.app.activeparks.util.extention.filterOutside
import com.app.activeparks.util.extention.gone
import com.app.activeparks.util.extention.invisible
import com.app.activeparks.util.extention.setPulseZone
import com.app.activeparks.util.extention.visible
import com.app.activeparks.util.extention.visibleIf
import com.technodreams.activeparks.databinding.FragmentLevelActivityBinding
import org.koin.androidx.viewmodel.ext.android.activityViewModel

class LevelActivityFragment : Fragment() {

    lateinit var binding: FragmentLevelActivityBinding
    private val viewModel: ActiveViewModel by activityViewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentLevelActivityBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapterInfoItem = ActivityInfoTrainingAdapter {}
        binding.rvTrainingInfo.apply {
            adapter = adapterInfoItem
            layoutManager = GridLayoutManager(requireContext(), 2)
        }

        viewModel.updateActivityInfoTrainingItem.observe(viewLifecycleOwner) { isUpdate ->
            if (isUpdate) {
                binding.tvPulseNumber.text = viewModel.activityState.currentPulse.toString()
                changePulseZone(viewModel.activityState.currentPulse)
                if (viewModel.activityState.activityType.isInclude) {
                    adapterInfoItem.list.submitList(
                        viewModel.activityState.activityInfoItems.filterInside()
                    )
                } else {
                    adapterInfoItem.list.submitList(
                        viewModel.activityState.activityInfoItems.filterOutside(
                            viewModel.activityState.activityType.id
                        )
                    )
                }
                adapterInfoItem.notifyDataSetChanged()
            }
        }
        val adapter = ActivityLevelAdapter {
            viewModel.activityState.activityTypeOutside = it
        }
        binding.rvActivityStreet.adapter = adapter

        viewModel.updateUI.observe(viewLifecycleOwner) {
            with(binding) {
                gPulse.visibleIf(viewModel.activityState.isPulseGadgetConnected)
                rvTrainingInfo.visible()

                if (viewModel.activityState.isPulseGadgetConnected) {
                    vInfoOne.setPulseInfoItem(viewModel.activityState.activityPulseItems[0])
                    vInfoTwo.setPulseInfoItem(viewModel.activityState.activityPulseItems[1])
                    vInfoThree.setPulseInfoItem(viewModel.activityState.activityPulseItems[2])
                }

                llPulseZone.apply {
                    for (i in 0 until childCount) {
                        getChildAt(i).apply {
                            (this as TextView).text = ""
                            layoutParams.apply {
                                (this as LinearLayout.LayoutParams).weight = 1f
                            }
                        }
                    }
                    val pulseZone = viewModel.activityState.pulseZone
                    when (pulseZone.id) {
                        0 -> {
                            tvSix.setPulseZone(pulseZone.title)
                        }

                        1 -> {
                            tvFive.setPulseZone(pulseZone.title)
                        }

                        2 -> {
                            tvFor.setPulseZone(pulseZone.title)
                        }

                        3 -> {
                            tvThree.setPulseZone(pulseZone.title)
                        }

                        4 -> {
                            tvTwo.setPulseZone(pulseZone.title)
                        }

                        5 -> {
                            tvOne.setPulseZone(pulseZone.title)
                        }
                    }

                }
            }

            if (viewModel.activityState.activityType.isInclude) {
                val list = if (viewModel.activityState.isTrainingStart) {
                    binding.tvTitleRecyclerView.invisible()
                    listOf(
                        viewModel.activityState.activityTypeOutside.copy(
                            isSelected = true
                        )
                    )
                } else {
                    binding.tvTitleRecyclerView.visible()
                    val types = LevelOfActivity.getLevelOfActivity()
                    types.forEach {
                        if (it.id == viewModel.activityState.activityTypeOutside.id) it.isSelected =
                            true
                    }
                    types
                }
                adapter.list.submitList(list)
                binding.rvActivityStreet.visible()
                adapterInfoItem.list.submitList(viewModel.activityState.activityInfoItems.filterInside())
            } else {
                binding.tvTitleRecyclerView.gone()
                binding.gLevelActivity.gone()
                adapterInfoItem.list.submitList(
                    viewModel.activityState.activityInfoItems.filterOutside(
                        viewModel.activityState.activityType.id
                    )
                )
            }
        }
    }

    private fun changePulseZone(number: Int) {
        with(viewModel) {
            when {
                number < pulseZone.pausePulse -> {
                    if (activityState.pulseZone.id != 5) {
                        activityState.pulseZone = PulseZone.getPulseZone()[5]
                        updateUI.value = true
                    }
                }

                number in viewModel.pulseZone.pausePulse + 1..viewModel.pulseZone.easy -> {
                    if (activityState.pulseZone.id != 4) {
                        activityState.pulseZone = PulseZone.getPulseZone()[4]
                        updateUI.value = true
                    }

                }

                number in viewModel.pulseZone.easy + 1..viewModel.pulseZone.anaerobic -> {
                    if (activityState.pulseZone.id != 3) {
                        viewModel.activityState.pulseZone = PulseZone.getPulseZone()[3]
                        updateUI.value = true
                    }

                }

                number in viewModel.pulseZone.anaerobic + 1..viewModel.pulseZone.aerobic -> {
                    if (activityState.pulseZone.id != 2) {
                        viewModel.activityState.pulseZone = PulseZone.getPulseZone()[2]
                        updateUI.value = true
                    }

                }

                number in viewModel.pulseZone.anaerobic + 1..viewModel.pulseZone.aerobic -> {
                    if (activityState.pulseZone.id != 1) {
                        activityState.pulseZone = PulseZone.getPulseZone()[1]
                        updateUI.value = true
                    }

                }

                number > viewModel.pulseZone.aerobic -> {
                    if (activityState.pulseZone.id != 0) {
                        activityState.pulseZone = PulseZone.getPulseZone()[0]
                        updateUI.value = true
                    }

                }
            }
        }
    }
}