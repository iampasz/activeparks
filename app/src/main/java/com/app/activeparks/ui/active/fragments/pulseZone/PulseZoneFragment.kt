package com.app.activeparks.ui.active.fragments.pulseZone

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.NumberPicker
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.app.activeparks.ui.active.ActiveViewModel
import com.app.activeparks.ui.active.model.InfoItem
import com.app.activeparks.ui.active.model.PulseZone
import com.technodreams.activeparks.R
import com.technodreams.activeparks.databinding.DialogPulseZoneBinding
import org.koin.androidx.viewmodel.ext.android.activityViewModel

class PulseZoneFragment : Fragment() {

    lateinit var binding: DialogPulseZoneBinding
    private val viewModel: ActiveViewModel by activityViewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = DialogPulseZoneBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val pulseZoneList = PulseZone.getPulseZone()

        with(binding) {
            vInfoOne.setPulseInfoItem(InfoItem.pausePulse())
            vInfoTwo.setPulseInfoItem(InfoItem.maxPulse())

            tvZone6.setOnClickListener {
                changeZoneInfo(tvZone6, vPulseZoneInfo, tvPulseInfoTitle)
                setPulseZone(pulseZoneList[0])
            }
            tvZone5.setOnClickListener {
                changeZoneInfo(tvZone5, vPulseZoneInfo, tvPulseInfoTitle)
                setPulseZone(pulseZoneList[1])
            }
            tvZone4.setOnClickListener {
                changeZoneInfo(tvZone4, vPulseZoneInfo, tvPulseInfoTitle)
                setPulseZone(pulseZoneList[2])
            }
            tvZone3.setOnClickListener {
                changeZoneInfo(tvZone3, vPulseZoneInfo, tvPulseInfoTitle)
                setPulseZone(pulseZoneList[3])
            }
            tvZone2.setOnClickListener {
                changeZoneInfo(tvZone2, vPulseZoneInfo, tvPulseInfoTitle)
                setPulseZone(pulseZoneList[4])
            }

            tvSelectZone6.setOnClickListener {
                setPulseZoneValue(tvSelectZone6, 175)
            }

            tvSelectZone5.setOnClickListener {
                setPulseZoneValue(tvSelectZone5, 150)
            }

            tvSelectZone4.setOnClickListener {
                setPulseZoneValue(tvSelectZone4, 125)
            }

            tvSelectZone3.setOnClickListener {
                setPulseZoneValue(tvSelectZone3, 100)
            }

            tvSelectZone2.setOnClickListener {
                setPulseZoneValue(tvSelectZone2, 75)
            }

            ivBack.setOnClickListener {
                requireActivity().onBackPressed()
            }

            swAuto.apply {
                isChecked = viewModel.activityState.isAutoPulseZone
                setOnCheckedChangeListener { _, isChecked ->
                    viewModel.activityState.isAutoPulseZone = isChecked
                }
            }
            val pulseZone = viewModel.activityState.pulseZone
            tvPulseInfoTitle.text = when (pulseZone.id) {
                0 -> {
                    pulseZone.title
                }

                1 -> {
                    pulseZone.title
                }

                2 -> {
                    pulseZone.title
                }

                3 -> {
                    pulseZone.title
                }

                4 -> {
                    pulseZone.title
                }

                else -> {
                    "Unknown type"
                }
            }
            vPulseZoneInfo.setImageResource(
                when (pulseZone.id) {
                    0 -> {
                        pulseZone.background
                    }

                    1 -> {
                        pulseZone.background
                    }

                    2 -> {
                        pulseZone.background
                    }

                    3 -> {
                        pulseZone.background
                    }

                    4 -> {
                        pulseZone.background
                    }

                    else -> {
                        R.drawable.view_puls_level_1
                    }
                }
            )
        }
    }


    private fun setPulseZoneValue(textView: TextView, value: Int) {
        val numberPicker = NumberPicker(requireContext())
        numberPicker.minValue = 70
        numberPicker.maxValue = 200
        numberPicker.value = value

        val dialog = androidx.appcompat.app.AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.tv_select_pulse_zone_value))
            .setView(numberPicker)
            .setPositiveButton(getString(R.string.tv_ok)) { _, _ ->
                textView.text =
                    getString(R.string.tv_pulse_zone_picker, numberPicker.value.toString())
            }
            .create()

        dialog.show()
    }

    private fun setPulseZone(pulseZone: PulseZone) {
        viewModel.activityState.pulseZone = pulseZone
    }

    private fun changeZoneInfo(
        tvZone6: TextView,
        infoImg: ImageView,
        infoTitle: TextView
    ) {
        tvZone6.background?.let { bg -> infoImg.setImageDrawable(bg) }
        tvZone6.text?.let { t -> infoTitle.text = t }
    }
}