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
        ) { item -> onItemSelected(item) }
        binding.rvActivity.adapter = adapter
        val types = viewModel.activityInfoItems
        types.forEach { if (it.id == infoViewId) it.isSelected = true }
        adapter.list.submitList(types.filter { it.id != firstId && it.id != secondId })

        binding.ivBack.setOnClickListener {
            requireActivity().onBackPressed()
        }
    }
}