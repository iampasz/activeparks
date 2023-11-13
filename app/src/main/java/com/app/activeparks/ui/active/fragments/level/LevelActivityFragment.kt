package com.app.activeparks.ui.active.fragments.level

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.app.activeparks.ui.active.ActiveViewModel
import com.app.activeparks.ui.active.model.ActivityInfoTrainingItem
import com.app.activeparks.ui.active.model.InfoItem
import com.app.activeparks.ui.active.model.LevelOfActivity
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapterInfoItem = ActivityInfoTrainingAdapter {}
        binding.rvTrainingInfo.apply {
            adapter = adapterInfoItem
            layoutManager = GridLayoutManager(requireContext(), 2)
        }
        val adapter = ActivityLevelAdapter {
            viewModel.activityState.activityTypeOutside = it
        }
        binding.rvActivityStreet.adapter = adapter

        viewModel.updateUI.observe(viewLifecycleOwner) {
            if (viewModel.activityState.isTrainingStart) {
                with(binding) {
                    gPulse.visibleIf(viewModel.activityState.isPulseGadgetConnected)
                    gTrainingInfo.visible()

                    if (viewModel.activityState.isPulseGadgetConnected) {
                        vInfoOne.setActivityInfoItem(InfoItem(0, "70 уд/хв", "Мінімальний"))
                        vInfoTwo.setActivityInfoItem(InfoItem(1, "100 уд/хв", "Середній"))
                        vInfoThree.setActivityInfoItem(InfoItem(2, "145 уд/хв", "Максимальний"))
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
                        }

                    }
                }

                if (viewModel.activityState.activityType.isInclude) {
                    val list = viewModel.activityState.activityTypeOutside.copy(
                        isSelected = true
                    )
                    adapter.list.submitList(listOf(list))
                    binding.tvTitleRecyclerView.invisible()
                    binding.rvActivityStreet.visible()
                    adapterInfoItem.list.submitList(
                        ActivityInfoTrainingItem.getActivityInfoItem().filter { !it.isOutside })
                } else {
                    binding.gLevelActivity.gone()
                    adapterInfoItem.list.submitList(
                        ActivityInfoTrainingItem.getActivityInfoItem()
                    )
                }

            } else {
                if (viewModel.activityState.activityType.isInclude) {
                    val types = LevelOfActivity.getLevelOfActivity()
                    types.forEach {
                        if (it.id == viewModel.activityState.activityTypeOutside.id) it.isSelected =
                            true
                    }
                    adapter.list.submitList(types)


                    with(binding) {
                        gLevelActivity.visible()
                        gPulse.gone()
                        gTrainingInfo.gone()
                    }
                } else {
                    with(binding) {
                        gLevelActivity.gone()
                        gPulse.gone()
                        gTrainingInfo.gone()
                    }
                }
            }
        }
    }
}