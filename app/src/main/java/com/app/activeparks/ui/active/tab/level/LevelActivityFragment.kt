package com.app.activeparks.ui.active.tab.level

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.app.activeparks.ui.active.model.ActivityInfoTrainingItem
import com.app.activeparks.ui.active.model.LevelOfActivity
import com.app.activeparks.ui.active.model.TypeOfActivity
import com.app.activeparks.util.extention.gone
import com.technodreams.activeparks.databinding.FragmentLevelActivityBinding

class LevelActivityFragment : Fragment() {

    lateinit var binding: FragmentLevelActivityBinding

    companion object {
        private var instance: LevelActivityFragment? = null
        private const val item = "ITEM_ID"
        fun newInstance(typeOfActivity: TypeOfActivity): LevelActivityFragment {
            return instance ?: kotlin.run {
                val f = LevelActivityFragment()

                f.apply {
                    arguments = Bundle().apply {
                        putInt(item, typeOfActivity.id)

                    }
                }
                f
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentLevelActivityBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.apply {
            val id = getInt(item)
            val adapterInfoItem = ActivityInfoTrainingAdapter{}
            binding.rvTrainingInfo.apply {
                adapter = adapterInfoItem
                layoutManager = GridLayoutManager(requireContext(), 2)
            }
            if (id == 4) {
                val adapter = ActivityLevelAdapter {
                }
                binding.rvActivityStreet.adapter = adapter
                adapter.list.submitList(LevelOfActivity.getLevelOfActivity())

                adapterInfoItem.list.submitList(ActivityInfoTrainingItem.getActivityInfoItem().filter { !it.isOutside })
            } else {
                binding.gList.gone()
                adapterInfoItem.list.submitList(ActivityInfoTrainingItem.getActivityInfoItem().filter { it.isOutside })

            }
        }

    }
}