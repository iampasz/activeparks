package com.app.activeparks.ui.active.tab.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.app.activeparks.ui.active.adapte.ActivityTypeAdapter
import com.app.activeparks.ui.active.model.InfoItem
import com.app.activeparks.ui.active.model.TypeOfActivity
import com.app.activeparks.ui.view.InfoView
import com.app.activeparks.util.dialog.DialogViewBuilder
import com.technodreams.activeparks.R
import com.technodreams.activeparks.databinding.FragmentSettingsActivityBinding

class SettingsActivityFragment : Fragment() {

    lateinit var binding: FragmentSettingsActivityBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentSettingsActivityBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            tvTypeActivity.setOnClickListener {
                showDialogActivityType(TypeOfActivity.getTypeOfActivity().first())
            }
            tvPulseZone.setOnClickListener {
                showDialogPulseZone()
            }
        }
    }


    private fun showDialogActivityType(item: TypeOfActivity) {
        DialogViewBuilder(requireContext()).build().apply {
            setContentView(R.layout.dialog_activity_type)
            findViewById<TextView>(R.id.tvTitle).setOnClickListener {
                dismiss()
            }

            val streetAdapter = ActivityTypeAdapter { }
            findViewById<RecyclerView>(R.id.rvActivityStreet).adapter = streetAdapter
            val list = TypeOfActivity.getTypeOfActivity()
            list.first().isSelected = true
            streetAdapter.list.submitList(list)
            show()
        }
    }

    private fun  showDialogPulseZone() {
        DialogViewBuilder(requireContext()).build().apply {
            setContentView(R.layout.dialog_pulse_zone)
            findViewById<TextView>(R.id.tvTitle).setOnClickListener {
                dismiss()
            }

            val infoImg = findViewById<ImageView>(R.id.vPulseZoneInfo)
            val infoTitle = findViewById<TextView>(R.id.tvPulseInfoTitle)
            val vInfoOne = findViewById<InfoView>(R.id.vInfoOne)
                vInfoOne.setActivityInfoItem(InfoItem.getPulseInfo()[0])
            val vInfoTwo = findViewById<InfoView>(R.id.vInfoTwo)
                vInfoTwo.setActivityInfoItem(InfoItem.getPulseInfo()[1])

            val tvZone6 = findViewById<TextView>(R.id.tvZone6)
            val tvZone5 = findViewById<TextView>(R.id.tvZone5)
            val tvZone4 = findViewById<TextView>(R.id.tvZone4)
            val tvZone3 = findViewById<TextView>(R.id.tvZone3)
            val tvZone2 = findViewById<TextView>(R.id.tvZone2)

            tvZone6.setOnClickListener {
                changeZoneInfo(tvZone6, infoImg, infoTitle)
            }
            tvZone5.setOnClickListener {
                changeZoneInfo(tvZone5, infoImg, infoTitle)
            }
            tvZone4.setOnClickListener {
                changeZoneInfo(tvZone4, infoImg, infoTitle)
            }
            tvZone3.setOnClickListener {
                changeZoneInfo(tvZone3, infoImg, infoTitle)
            }
            tvZone2.setOnClickListener {
                changeZoneInfo(tvZone2, infoImg, infoTitle)
            }

            show()
        }

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