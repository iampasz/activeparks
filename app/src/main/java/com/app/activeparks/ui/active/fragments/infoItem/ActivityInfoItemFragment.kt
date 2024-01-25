package com.app.activeparks.ui.active.fragments.infoItem

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.app.activeparks.ui.active.ActiveViewModel
import com.app.activeparks.ui.active.adapter.ActivityInfoItemAdapter
import com.app.activeparks.ui.active.model.ActivityInfoTrainingItem
import com.technodreams.activeparks.databinding.DialogActivityInfoBinding
import org.koin.androidx.viewmodel.ext.android.activityViewModel

class ActivityInfoItemFragment(
    private val infoViewId: Int,
    private val firstId: Int,
    private val secondId: Int,
    private val onItemSelected: (ActivityInfoTrainingItem) -> Unit
) : Fragment() {

    lateinit var binding: DialogActivityInfoBinding
    private val viewModel: ActiveViewModel by activityViewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = DialogActivityInfoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = ActivityInfoItemAdapter(
        ) { item ->
            onItemSelected(item)
            onBack()
        }
        binding.rvActivity.adapter = adapter
        val types = viewModel.activityState.activityInfoItems
        types.forEach { it.isSelected = it.id == infoViewId }
        adapter.list.submitList(if (viewModel.activityState.activityType.id == 2) {
            types.filter { it.id != firstId && it.id != secondId }
                .filter { it.id != 11 }
        } else {
            types.filter { it.id != firstId && it.id != secondId }
                .filter {
                    it.isOutside == viewModel.activityState.activityType.isOutside
                            || it.id == 5 || it.id == 6 || it.id == 7 || it.id == 8
                }
        })

        binding.ivBack.setOnClickListener {
            onBack()
        }
    }

    private fun onBack() {
        requireActivity().onBackPressed()
    }
}