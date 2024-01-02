package com.app.activeparks.ui.userProfile.statisticFragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.app.activeparks.ui.userProfile.model.DurationPicker
import com.app.activeparks.ui.userProfile.model.MonthPicker
import com.app.activeparks.ui.userProfile.model.PairActivityInfoTrainingMapper
import com.app.activeparks.ui.userProfile.statisticFragment.adapter.ActivityAdapter
import com.app.activeparks.ui.userProfile.statisticFragment.adapter.PairActivityInfoTrainingAdapter
import com.app.activeparks.ui.userProfile.statisticFragment.bottomSheet.BottomSheetFragment
import com.app.activeparks.util.MonthYearPickerDialog
import com.app.activeparks.util.MonthYearPickerDialog.Companion.formatSelectedDate
import com.app.activeparks.util.extention.DataHelper
import com.app.activeparks.util.extention.gone
import com.app.activeparks.util.extention.visible
import com.technodreams.activeparks.R
import com.technodreams.activeparks.databinding.FragmentUserProfileStatisticBinding
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.Calendar


/**
 * Created by O.Dziuba on 21.12.2023.
 */
class StatisticFragment : Fragment(), MonthYearPickerDialog.OnDateSetListener {

    private lateinit var binding: FragmentUserProfileStatisticBinding
    private val viewModel: StatisticViewModel by viewModel()
    private val adapterInfoItem = PairActivityInfoTrainingAdapter()
    private val adapterActivity = ActivityAdapter {}
    private var monthPicker = MonthPicker.FIRST
    private var durationPicker = DurationPicker.MONTH

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUserProfileStatisticBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getActivities()

        setListener()
        initViews()
        observe()

        viewModel.getStatistic(DataHelper.getCurrentMonthFirstDate(), DataHelper.getCurrentDate())
    }

    private fun observe() {
        with(viewModel) {
            activities.observe(viewLifecycleOwner) {
                it?.let {
                    adapterActivity.list.submitList(it)

                }
            }
            update.observe(viewLifecycleOwner) {
                setStatisticList()
            }
        }
    }

    private fun initViews() {
        binding.rvTrainingInfo.apply {
            adapter = adapterInfoItem
            layoutManager = GridLayoutManager(requireContext(), 2)

            setStatisticList()
        }
        binding.rvActivity.apply {
            adapter = adapterActivity
        }
    }

    private fun setListener() {
        with(binding) {
            tvWeek.setOnClickListener {
                setBackgroundTime(tvWeek, tvMonth, tvYear, tvAllTime)
                durationPicker = DurationPicker.WEEK
            }
            tvMonth.setOnClickListener {
                durationPicker = DurationPicker.MONTH
                setBackgroundTime(tvMonth, tvWeek, tvYear, tvAllTime)
            }
            tvYear.setOnClickListener {
                durationPicker = DurationPicker.YEAR
                setBackgroundTime(tvYear, tvWeek, tvMonth, tvAllTime)
            }
            tvAllTime.setOnClickListener {
                durationPicker = DurationPicker.ALL
                setBackgroundTime(tvAllTime, tvWeek, tvMonth, tvYear)
            }

            tvActivity.setOnClickListener {
                val dialog = BottomSheetFragment { item ->
                    tvActivity.apply {
                        text = item.title
                        setCompoundDrawablesWithIntrinsicBounds(item.img, 0, 0, 0)
                    }
                }
                dialog.show(childFragmentManager, "")
            }

            tvFirstParametrs.setOnClickListener {
                monthPicker = MonthPicker.FIRST
                showDatePickerDialog()
            }

            tvSecondParametrs.setOnClickListener {
                monthPicker = MonthPicker.SECOND
                showDatePickerDialog()
            }

            tvCompare.setOnClickListener {
                tvCompare.gone()
                tvSecondParametrs.visible()
                ivFMonth.visible()
                ivSMonth.visible()
                tvSecondParametrs.text = tvFirstParametrs.text
                monthPicker = MonthPicker.SECOND
                showDatePickerDialog()
            }
        }
    }

    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val currentYear = calendar.get(Calendar.YEAR)
        val currentMonth = calendar.get(Calendar.MONTH)

        val monthYearPickerDialog = MonthYearPickerDialog(
            requireContext(),
            this@StatisticFragment,
            currentYear,
            currentMonth,
            durationPicker
        )

        monthYearPickerDialog.show()
    }

    override fun onDateSet(month: Int, year: Int) {
        val formattedDate = formatSelectedDate(year, month)
        when (monthPicker) {
            MonthPicker.FIRST -> {
                binding.tvFirstParametrs.text = formattedDate
                viewModel.getStatistic(DataHelper.getMonthFirstDate(month, year), DataHelper.getCurrentDate())
            }

            MonthPicker.SECOND -> {
                binding.tvSecondParametrs.text = formattedDate
                viewModel.getStatistic(DataHelper.getCurrentMonthFirstDate(), DataHelper.getCurrentDate(), false)
            }
        }
    }

    private fun setStatisticList() {
        adapterInfoItem.list.submitList(
            PairActivityInfoTrainingMapper.map(
                viewModel.firstListStatistic,
                viewModel.secondListStatistic
            )
        )
    }

    private fun setBackgroundTime(vararg views: View) {
        views.forEach {
            it.setBackgroundResource(0)
        }
        views.first().setBackgroundResource(R.drawable.time_light_green)
    }
}