package com.app.activeparks.ui.active.fragments.type

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.app.activeparks.ui.active.ActiveViewModel
import com.app.activeparks.ui.active.adapter.ActivityTypeAdapter
import com.app.activeparks.ui.active.model.TypeOfActivity
import com.technodreams.activeparks.databinding.DialogActivityInfoBinding
import org.koin.androidx.viewmodel.ext.android.activityViewModel

class ActivityTypeFragment : Fragment() {

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

        val streetAdapter = ActivityTypeAdapter { item ->
            viewModel.activityState.activityType = item
            onBack()
        }
        binding.rvActivity.adapter = streetAdapter
        binding.ivBack.setOnClickListener {
            onBack()
        }
        val list = TypeOfActivity.getTypeOfActivity()
        list.forEach {
            if (it.id == viewModel.activityState.activityType.id) it.isSelected = true
        }
        streetAdapter.list.submitList(list)
    }

    private fun onBack() {
        requireActivity().onBackPressed()
    }
}