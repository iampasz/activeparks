package com.app.activeparks.ui.userProfile.activityInfo.statistic

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.app.activeparks.ui.active.model.ActivityInfoTrainingItem
import com.app.activeparks.ui.userProfile.activityInfo.ActivityInfoViewModel
import com.technodreams.activeparks.databinding.FragmentActivityInfoStatisticBinding
import org.koin.androidx.viewmodel.ext.android.activityViewModel

/**
 * Created by O.Dziuba on 22.01.2024.
 */
class ActivityInfoStatisticFragment : Fragment() {

    private lateinit var binding: FragmentActivityInfoStatisticBinding
    private val viewModel: ActivityInfoViewModel by activityViewModel()
    private val adapterInfoItem = ActivityInfoTrainingAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentActivityInfoStatisticBinding.inflate(inflater, container, false)

        initView()
        observe()

        return binding.root
    }

    private fun observe() {
        with(viewModel) {
            activity.observe(viewLifecycleOwner) { activity ->
                activity?.let { item ->
                    val list = ActivityInfoTrainingItem.getActivityInfoItem()

                    list[0].number = item.speed.toString()
                    list[1].number = item.speed.toString()
                    list[2].number = item.speedMax.toString()
                    list[3].number = item.distance.toString()
                    list[4].number = item.calories.toString()
                    list[5].number = item.bpm.toString()
                    val bpm = (item.minBpm?.let { item.maxBpm?.minus(it) })?.div(2)
                    list[6].number = bpm.toString()
                    list[7].number = item.maxBpm.toString()
                    list[8].number = item.minBpm.toString()
                    list[9].number = item.temp.toString()
                    list[10].number = item.hight.toString()
                    list[11].number = item.steps.toString()
                    list[12].number = item.altitudeMin.toString()

                    when(item.activityType.toInt()) {
                        0, 1, 3 -> {
                            list.removeAt(14)
                            list.removeAt(13)
                            list.removeAt(0)
                        }
                        2 -> {
                            list.removeAt(14)
                            list.removeAt(13)
                            list.removeAt(11)
                            list.removeAt(0)
                        }
                        4 -> {
                            list.removeAt(14)
                            list.removeAt(13)
                            list.removeAt(11)
                            list.removeAt(10)
                            list.removeAt(9)
                            list.removeAt(3)
                            list.removeAt(2)
                            list.removeAt(1)
                            list.removeAt(0)
                        }
                    }


                    adapterInfoItem.list.submitList(list)
                }
            }
        }
    }

    private fun initView() {
        with(binding) {

            rvInfoView.apply {
                adapter = adapterInfoItem
                layoutManager = GridLayoutManager(requireContext(), 2)
            }
        }
    }
}