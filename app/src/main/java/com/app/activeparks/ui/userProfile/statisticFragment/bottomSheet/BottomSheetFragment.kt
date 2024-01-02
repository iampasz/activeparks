package com.app.activeparks.ui.userProfile.statisticFragment.bottomSheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.app.activeparks.ui.active.adapter.ActivityTypeAdapter
import com.app.activeparks.ui.active.model.TypeOfActivity
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.technodreams.activeparks.R
import com.technodreams.activeparks.databinding.FragmentActivityBottomSheetBinding


/**
 * Created by O.Dziuba on 22.12.2023.
 */
class BottomSheetFragment(
    private val itemSelected: (TypeOfActivity) -> Unit
) : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentActivityBottomSheetBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentActivityBottomSheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        iniRecycler()
    }

    private fun iniRecycler() {
        with(binding) {
            val adapter = ActivityTypeAdapter { item ->
                itemSelected(item)
                dismiss()
            }
            rvActivityType.adapter = adapter
            adapter.list.submitList(TypeOfActivity.getTypeOfActivityWithAll())
        }
    }
}