package com.app.activeparks.ui.active.fragments.pulseZone

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.NumberPicker
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.app.activeparks.data.model.registration.PulseZoneRequest
import com.app.activeparks.ui.active.ActiveViewModel
import com.app.activeparks.ui.active.model.InfoItem
import com.app.activeparks.ui.active.model.PulseZone
import com.technodreams.activeparks.R
import com.technodreams.activeparks.databinding.DialogPulseZoneBinding
import org.koin.androidx.viewmodel.ext.android.activityViewModel

class PulseZoneFragment : Fragment() {

    lateinit var binding: DialogPulseZoneBinding
    private val viewModel: ActiveViewModel by activityViewModel()

    private val minPauseZone = 60

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

        viewModel.getPulseZone()

        setListener(pulseZoneList)
        observe()
    }

    private fun observe() {
        with(binding) {
            viewModel.pulseZoneRequest.observe(viewLifecycleOwner) {

                it?.let {
                    viewModel.pulseZone.upperBorder = it.upperBorder
                    viewModel.pulseZone.anaerobic = it.anaerobic
                    viewModel.pulseZone.aerobic = it.aerobic
                    viewModel.pulseZone.fatBurning = it.fatBurning
                    viewModel.pulseZone.easy = it.easy
                    viewModel.pulseZone.pausePulse = it.pausePulse

                    if (viewModel.activityState.isAutoPulseZone) {
                        setAutoPulseZone()
                    } else {
                        changePulseZone(it)
                    }
                } ?: kotlin.run {
                    setDefaultZone()
                }
            }
        }
    }

    private fun DialogPulseZoneBinding.setDefaultZone() {
        vInfoOne.setPulseInfoItem(InfoItem.pausePulse())
        vInfoTwo.setPulseInfoItem(InfoItem.maxPulse())
    }

    @SuppressLint("SetTextI18n")
    private fun DialogPulseZoneBinding.changePulseZone(it: PulseZoneRequest) {
        tvSelectZone6.text = "${it.upperBorder} уд/хв"
        tvSelectZone5.text = "${it.anaerobic} уд/хв"
        tvSelectZone4.text = "${it.aerobic} уд/хв"
        tvSelectZone3.text = "${it.fatBurning} уд/хв"
        tvSelectZone2.text = "${it.easy} уд/хв"

        vInfoOne.setPulseInfoItem(InfoItem.pausePulse(it.pausePulse))
        vInfoTwo.setPulseInfoItem(InfoItem.maxPulse(it.upperBorder))
    }

    private fun setListener(pulseZoneList: List<PulseZone>) {
        with(binding) {
            setDefaultZone()
            vInfoOne.setOnClickListener {
                if (viewModel.isAuth) {
                    setPulseZoneValue(
                        tvSelectZone6,
                        minPauseZone,
                        minPauseZone,
                        viewModel.pulseZone.upperBorder
                    ) {
                        viewModel.pulseZone.pausePulse = it
                        viewModel.savePulseZone()
                        vInfoOne.setPulseInfoItem(InfoItem.pausePulse(it))
                    }
                }
            }

            vInfoTwo.setOnClickListener {
                if (viewModel.isAuth) {
                    setPulseZoneValue(
                        tvSelectZone6,
                        viewModel.pulseZone.upperBorder
                    ) {
                        viewModel.pulseZone.upperBorder = it
                        viewModel.savePulseZone()
                        vInfoTwo.setPulseInfoItem(InfoItem.maxPulse(it))
                    }
                }
            }

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
                if (!viewModel.activityState.isAutoPulseZone && viewModel.isAuth) {
                    setPulseZoneValue(
                        tvSelectZone6,
                        viewModel.pulseZone.upperBorder,
                        viewModel.pulseZone.anaerobic + 1,
                        viewModel.pulseZone.upperBorder
                    ) {
                        viewModel.pulseZone.upperBorder = it
                        viewModel.savePulseZone()
                    }
                }
            }

            tvSelectZone5.setOnClickListener {
                if (!viewModel.activityState.isAutoPulseZone && viewModel.isAuth) {
                    setPulseZoneValue(
                        tvSelectZone5,
                        viewModel.pulseZone.anaerobic,
                        viewModel.pulseZone.aerobic + 1,
                        viewModel.pulseZone.upperBorder - 1
                    ) {
                        viewModel.pulseZone.anaerobic = it
                        viewModel.savePulseZone()
                    }
                }
            }

            tvSelectZone4.setOnClickListener {
                if (!viewModel.activityState.isAutoPulseZone && viewModel.isAuth) {
                    setPulseZoneValue(
                        tvSelectZone4,
                        viewModel.pulseZone.aerobic,
                        viewModel.pulseZone.fatBurning + 1,
                        viewModel.pulseZone.anaerobic - 1
                    ) {
                        viewModel.pulseZone.aerobic = it
                        viewModel.savePulseZone()
                    }
                }
            }

            tvSelectZone3.setOnClickListener {
                if (!viewModel.activityState.isAutoPulseZone && viewModel.isAuth) {
                    setPulseZoneValue(
                        tvSelectZone3,
                        viewModel.pulseZone.fatBurning,
                        viewModel.pulseZone.easy + 1,
                        viewModel.pulseZone.aerobic - 1
                    ) {
                        viewModel.pulseZone.fatBurning = it
                        viewModel.savePulseZone()
                    }
                }
            }

            tvSelectZone2.setOnClickListener {
                if (!viewModel.activityState.isAutoPulseZone && viewModel.isAuth) {
                    setPulseZoneValue(
                        tvSelectZone2, viewModel.pulseZone.easy,
                        viewModel.pulseZone.easy + 1,
                        viewModel.pulseZone.fatBurning - 1
                    ) {
                        viewModel.pulseZone.easy = it
                        viewModel.savePulseZone()
                    }
                }
            }

            ivBack.setOnClickListener {
                requireActivity().onBackPressed()
            }

            swAuto.apply {
                isChecked = viewModel.activityState.isAutoPulseZone
                setOnCheckedChangeListener { _, isChecked ->
                    if (viewModel.isAuth) {
                        viewModel.activityState.isAutoPulseZone = isChecked
                        if (isChecked) {
                            setAutoPulseZone()
                        } else {
                            changePulseZone(viewModel.pulseZone)
                        }
                    }
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

    private fun DialogPulseZoneBinding.setAutoPulseZone() {
        viewModel.pulseZoneRequest.value?.let {
            changePulseZone(
                PulseZoneRequest.getAutoPulseZone(
                    viewModel.age, it.pausePulse
                )
            )
        }
    }


    private fun setPulseZoneValue(
        textView: TextView,
        value: Int,
        minValue: Int? = null,
        maxValue: Int? = null,
        update: (Int) -> Unit
    ) {
        val numberPicker = NumberPicker(requireContext())
        numberPicker.minValue = if (minValue == null || minValue < 0 ) 75 else minValue
        numberPicker.maxValue = if (maxValue == null || maxValue < 0 ) 200 else maxValue
        numberPicker.value = value
        numberPicker.wrapSelectorWheel = false

        val dialog = androidx.appcompat.app.AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.tv_select_pulse_zone_value))
            .setView(numberPicker)
            .setPositiveButton(getString(R.string.tv_ok)) { _, _ ->
                textView.text =
                    getString(R.string.tv_pulse_zone_picker, numberPicker.value.toString())
                update(numberPicker.value)
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