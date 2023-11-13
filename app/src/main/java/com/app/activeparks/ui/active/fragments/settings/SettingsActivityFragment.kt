package com.app.activeparks.ui.active.fragments.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.app.activeparks.ui.active.ActiveViewModel
import com.app.activeparks.ui.active.fragments.pulseGadget.PulseGadgetFragment
import com.app.activeparks.ui.active.fragments.pulseZone.PulseZoneFragment
import com.app.activeparks.ui.active.fragments.type.ActivityTypeFragment
import com.app.activeparks.util.extention.enableIf
import com.technodreams.activeparks.databinding.FragmentSettingsActivityBinding
import org.koin.androidx.viewmodel.ext.android.activityViewModel

class SettingsActivityFragment : Fragment() {

    lateinit var binding: FragmentSettingsActivityBinding
    private val viewModel: ActiveViewModel by activityViewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentSettingsActivityBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setListener()
        observe()
    }

    private fun observe() {
        viewModel.updateUI.observe(viewLifecycleOwner) {
            with(binding) {
                setEnableViews()
            }
        }
    }

    private fun setListener() {
        with(binding) {
            tvTypeActivity.setOnClickListener {
                viewModel.activityState.onSelectedTypeFromSetting = true
                viewModel.navigateTo(ActivityTypeFragment())
            }
            tvPulseZone.setOnClickListener {
                viewModel.activityState.onSelectedTypeFromSetting = true
                viewModel.navigateTo(PulseZoneFragment())
            }
            tvPulseDevise.setOnClickListener {
                viewModel.activityState.onSelectedTypeFromSetting = true
                viewModel.navigateTo(PulseGadgetFragment())
            }

            scAutoPause.apply {
                isChecked = viewModel.activityState.isAutoPause
                setOnCheckedChangeListener { _, isChecked ->
                    viewModel.activityState.isAutoPause = isChecked
                }
            }
            scAudio.apply {
                isChecked = viewModel.activityState.isAudioHelper
                setOnCheckedChangeListener { _, isChecked ->
                    viewModel.activityState.isAudioHelper = isChecked
                }
            }
            scLateStart.apply {
                isChecked = viewModel.activityState.isLazyStart
                setOnCheckedChangeListener { _, isChecked ->
                    viewModel.activityState.isLazyStart = isChecked
                }
            }
            scAutoBlock.apply {
                isChecked = viewModel.activityState.isAutoBlockScreen
                setOnCheckedChangeListener { _, isChecked ->
                    viewModel.activityState.isAutoBlockScreen = isChecked
                }
            }

            setEnableViews()
        }
    }

    private fun FragmentSettingsActivityBinding.setEnableViews() {
        tvTypeActivity.enableIf(!viewModel.activityState.isTrainingStart)
        tvPulseZone.enableIf(!viewModel.activityState.isTrainingStart)
        tvPulseDevise.enableIf(!viewModel.activityState.isTrainingStart)
        scAutoPause.enableIf(!viewModel.activityState.isTrainingStart)
        scAudio.enableIf(!viewModel.activityState.isTrainingStart)
        scLateStart.enableIf(!viewModel.activityState.isTrainingStart)
        scAutoBlock.enableIf(!viewModel.activityState.isTrainingStart)
    }
}