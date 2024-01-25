package com.app.activeparks.ui.userProfile.activities

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.app.activeparks.data.model.activity.ActivityItemResponse
import com.app.activeparks.ui.userProfile.model.ActivityItemType
import com.app.activeparks.ui.userProfile.model.DurationPicker
import com.app.activeparks.util.MonthYearPickerDialog
import com.app.activeparks.util.extention.DataHelper
import com.app.activeparks.util.extention.invisible
import com.app.activeparks.util.extention.visible
import com.technodreams.activeparks.R
import com.technodreams.activeparks.databinding.FragmentAllActivitiesBinding
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.Calendar

/**
 * Created by O.Dziuba on 17.01.2024.
 */
class AllActivitiesFragment : Fragment(), MonthYearPickerDialog.OnDateSetListener {

    private lateinit var binding: FragmentAllActivitiesBinding
    private val viewModel: AllActivitiesViewModel by viewModel()
    private var durationPicker = DurationPicker.YEAR

    private var week = ""
    private var weekFrom = ""
    private var weekTo = ""

    private var mont = ""
    private var montFrom = ""
    private var montTo = ""

    private var year = ""
    private var yearFrom = ""
    private var yearTo = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentAllActivitiesBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setListener()
        observe()
        viewModel.getActivities(DataHelper.getFirstDayOfYear(), DataHelper.getCurrentDate())
    }

    private fun observe() {
        with(viewModel) {
            activities.observe(viewLifecycleOwner) {
                it?.let {
                    val adapter = ActivityAdapterWIthHeader {}
                    binding.rvActivity.adapter = adapter
                    val items = mutableListOf<ActivityItemType>()
                    when (durationPicker) {
                        DurationPicker.WEEK, DurationPicker.MONTH, DurationPicker.YEAR -> {
                            items.add(ActivityItemType.Header("${binding.tvParametrs.text}"))
                            addedItems(items, it)
                        }

                        DurationPicker.ALL -> {

                        }
                    }
                    adapter.setData(items)
                }
            }
        }
    }

    private fun addedItems(
        items: MutableList<ActivityItemType>,
        list: List<ActivityItemResponse>
    ) {
        list.forEach {
            items.add(ActivityItemType.Item(it))
        }
    }

    private fun setListener() {
        with(binding) {
            tvWeek.setOnClickListener {
                tvParametrs.visible()
                durationPicker = DurationPicker.WEEK
                setBackgroundTime(tvWeek, tvMonth, tvYear, tvAllTime)

                if (week.isEmpty()) week = DataHelper.getCurrentWeek()
                tvParametrs.text = week

                val (startDate, endDate) = week.split(" - ")

                if (weekFrom.isEmpty()) weekFrom =
                    "${DataHelper.getCurrentYear()}.${startDate}"
                if (weekTo.isEmpty()) weekTo = "${DataHelper.getCurrentYear()}.${endDate}"

                viewModel.getActivities(
                    weekFrom,
                    weekTo
                )
            }
            tvMonth.setOnClickListener {
                tvParametrs.visible()
                if (durationPicker != DurationPicker.MONTH) {
                    durationPicker = DurationPicker.MONTH
                    setBackgroundTime(tvMonth, tvWeek, tvYear, tvAllTime)

                    if (mont.isEmpty()) mont = DataHelper.getCurrentMontYear()
                    tvParametrs.text = mont

                    if (montFrom.isEmpty()) montFrom = DataHelper.getFirstDayOfYear()
                    if (montTo.isEmpty()) montTo = DataHelper.getLastDayOfYear()

                    viewModel.getActivities(
                        montFrom,
                        montTo
                    )
                }
            }
            tvYear.setOnClickListener {
                tvParametrs.visible()
                if (durationPicker != DurationPicker.YEAR) {
                    durationPicker = DurationPicker.YEAR
                    setBackgroundTime(tvYear, tvWeek, tvMonth, tvAllTime)

                    if (year.isEmpty()) year = DataHelper.getCurrentYear()
                    tvParametrs.text = year

                    if (yearFrom.isEmpty()) yearFrom = DataHelper.getFirstDayOfYear()
                    if (yearTo.isEmpty()) yearTo = DataHelper.getLastDayOfYear()

                    viewModel.getActivities(
                        yearFrom,
                        yearTo
                    )
                }
            }
            tvAllTime.setOnClickListener {
                durationPicker = DurationPicker.ALL
                setBackgroundTime(tvAllTime, tvWeek, tvMonth, tvYear)

                tvParametrs.invisible()
                viewModel.apply {
                    getActivities(DataHelper.getAllTime(), DataHelper.getCurrentDate())
                }
            }
            tvParametrs.setOnClickListener {
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
            this@AllActivitiesFragment,
            currentYear,
            currentMonth,
            durationPicker
        )

        monthYearPickerDialog.show()
    }


    private fun setBackgroundTime(vararg views: View) {
        views.forEach {
            it.setBackgroundResource(0)
        }
        views.first().setBackgroundResource(R.drawable.time_light_green)
    }

    override fun onDateSet(month: Int, year: Int) {
        when (durationPicker) {
            DurationPicker.WEEK -> {}

            DurationPicker.MONTH -> {
                val formattedDate = MonthYearPickerDialog.formatSelectedDate(year, month)

                mont = formattedDate
                binding.tvParametrs.text = mont

                montFrom = DataHelper.getMonthFirstDate(month, year)
                montTo = DataHelper.getLastDayOfMonth(year, month)
                viewModel.getActivities(
                    montFrom,
                    montTo
                )
            }

            DurationPicker.YEAR -> {

                binding.tvParametrs.text = year.toString()

                this.year = year.toString()
                yearFrom = DataHelper.getFirstDayOfYear(year)
                yearTo = DataHelper.getLastDayOfYear(year)

                viewModel.getActivities(yearFrom, yearTo)
            }

            DurationPicker.ALL -> {}
        }
    }

    override fun onWeekSet(range: String, year: String) {
        val (startDate, endDate) = range.split(" - ")

        binding.tvParametrs.text = range

        week = range
        weekFrom = "$year.${startDate}"
        weekTo = "$year.${endDate}"

        viewModel.getActivities(weekFrom, weekTo)
    }
}