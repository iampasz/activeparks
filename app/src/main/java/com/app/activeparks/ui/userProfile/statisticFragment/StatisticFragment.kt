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
import com.app.activeparks.util.extention.invisible
import com.app.activeparks.util.extention.visible
import com.app.activeparks.util.extention.visibleIf
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

    private var isCompareVisible = false

    private var firstWeek = ""
    private var secondWeek = ""
    private var firstWeekFrom = ""
    private var secondWeekFrom = ""
    private var firstWeekTo = ""
    private var secondWeekTo = ""

    private var firstMont = ""
    private var secondMont = ""
    private var firstMontFrom = ""
    private var secondMontFrom = ""
    private var firstMontTo = ""
    private var secondMontTo = ""

    private var firstYear = ""
    private var secondYear = ""
    private var firstYearFrom = ""
    private var secondYearFrom = ""
    private var firstYearTo = ""
    private var secondYearTo = ""

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
        with(binding) {
            tvFirstParametrs.text = DataHelper.getCurrentMontYear()

            rvTrainingInfo.apply {
                adapter = adapterInfoItem
                layoutManager = GridLayoutManager(requireContext(), 2)

                setStatisticList()
            }
            rvActivity.apply {
                adapter = adapterActivity
            }
        }
    }

    private fun setListener() {
        with(binding) {
            tvWeek.setOnClickListener {
                durationPicker = DurationPicker.WEEK
                setBackgroundTime(tvWeek, tvMonth, tvYear, tvAllTime)
                indispensabilityParameter()

                if (firstMont.isEmpty()) firstWeek = DataHelper.getCurrentWeek()
                tvFirstParametrs.text = firstWeek

                val (startDate, endDate) = firstWeek.split(" - ")

                if (firstWeekFrom.isEmpty()) firstWeekFrom = "${DataHelper.getCurrentYear()}.${startDate}"
                if (firstWeekTo.isEmpty()) firstWeekTo = "${DataHelper.getCurrentYear()}.${endDate}"

                viewModel.getStatistic(
                    firstWeekFrom,
                    firstWeekTo
                )

                if (isCompareVisible) {
                    if (secondWeekFrom.isNotEmpty()) {
                        tvSecondParametrs.text = secondWeek
                        viewModel.getStatistic(secondWeekFrom, secondWeekTo, false)
                    } else {
                        tvSecondParametrs.text = firstWeek
                        viewModel.getStatistic(firstWeekFrom, firstWeekTo, false)
                    }
                }
            }
            tvMonth.setOnClickListener {
                if (durationPicker != DurationPicker.MONTH) {
                    durationPicker = DurationPicker.MONTH
                    setBackgroundTime(tvMonth, tvWeek, tvYear, tvAllTime)
                    indispensabilityParameter()

                    if (firstMont.isEmpty()) firstMont = DataHelper.getCurrentMontYear()
                    tvFirstParametrs.text = firstMont

                    if (firstMontFrom.isEmpty()) firstMontFrom = DataHelper.getFirstDayOfYear()
                    if (firstMontTo.isEmpty()) firstMontTo = DataHelper.getLastDayOfYear()

                    viewModel.getStatistic(
                        firstMontFrom,
                        firstMontTo
                    )

                    if (isCompareVisible) {
                        if (secondMontFrom.isNotEmpty()) {
                            tvSecondParametrs.text = secondMont
                            viewModel.getStatistic(secondMontFrom, secondMontTo, false)
                        } else {
                            tvSecondParametrs.text = firstMont
                            viewModel.getStatistic(firstMontFrom, firstMontTo, false)
                        }
                    }
                }
            }
            tvYear.setOnClickListener {
                if (durationPicker != DurationPicker.YEAR) {
                    durationPicker = DurationPicker.YEAR
                    setBackgroundTime(tvYear, tvWeek, tvMonth, tvAllTime)

                    indispensabilityParameter()

                    if (firstYear.isEmpty()) firstYear = DataHelper.getCurrentYear()
                    tvFirstParametrs.text = firstYear
                    if (secondYear.isNotEmpty()) tvSecondParametrs.text = secondYear

                    if (firstYearFrom.isEmpty()) firstYearFrom = DataHelper.getFirstDayOfYear()
                    if (firstYearTo.isEmpty()) firstYearTo = DataHelper.getLastDayOfYear()

                    viewModel.getStatistic(
                        firstYearFrom,
                        firstYearTo
                    )

                    if (isCompareVisible) {
                        if (secondYearFrom.isNotEmpty()) {
                            tvSecondParametrs.text = secondYear
                            viewModel.getStatistic(secondYearFrom, secondYearTo, false)
                        } else {
                            tvSecondParametrs.text = firstYear
                            viewModel.getStatistic(firstYearFrom, firstYearTo, false)
                        }
                    }
                }
            }
            tvAllTime.setOnClickListener {
                durationPicker = DurationPicker.ALL
                setBackgroundTime(tvAllTime, tvWeek, tvMonth, tvYear)

                tvFirstParametrs.invisible()
                ivFMonth.invisible()
                tvSecondParametrs.invisible()
                ivSMonth.invisible()
                tvCompare.invisible()
                viewModel.apply {
                    getStatistic(DataHelper.getAllTime(), DataHelper.getCurrentDate())
                    secondListStatistic = listOf()
                }
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
                isCompareVisible = true
                tvCompare.gone()
                tvSecondParametrs.visible()
                ivFMonth.visible()
                ivSMonth.visible()

                when (durationPicker) {
                    DurationPicker.WEEK -> {
                        tvSecondParametrs.text = tvFirstParametrs.text
                        monthPicker = MonthPicker.SECOND
                        showDatePickerDialog()
                    }

                    DurationPicker.MONTH -> {
                        tvSecondParametrs.text = tvFirstParametrs.text
                        monthPicker = MonthPicker.SECOND
                        showDatePickerDialog()
                    }

                    DurationPicker.YEAR -> {
                        tvSecondParametrs.text = DataHelper.getCurrentYear()
                        monthPicker = MonthPicker.SECOND
                        showDatePickerDialog()
                    }

                    DurationPicker.ALL -> { }
                }
            }
        }
    }

    private fun FragmentUserProfileStatisticBinding.indispensabilityParameter() {
        tvFirstParametrs.visible()
        tvSecondParametrs.visibleIf(isCompareVisible)
        ivSMonth.visibleIf(isCompareVisible)
        ivFMonth.visibleIf(isCompareVisible)
        tvCompare.visibleIf(!isCompareVisible)
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
        when (durationPicker) {
            DurationPicker.WEEK -> { }

            DurationPicker.MONTH -> {
                val formattedDate = formatSelectedDate(year, month)
                when (monthPicker) {
                    MonthPicker.FIRST -> {
                        firstMont = formattedDate
                        binding.tvFirstParametrs.text = firstMont

                        firstMontFrom = DataHelper.getMonthFirstDate(month, year)
                        firstMontTo = DataHelper.getLastDayOfMonth(year, month)
                        viewModel.getStatistic(
                            firstMontFrom,
                            firstMontTo
                        )
                    }

                    MonthPicker.SECOND -> {
                        secondMont = formattedDate
                        binding.tvSecondParametrs.text = secondMont

                        secondMontFrom = DataHelper.getMonthFirstDate(month, year)
                        secondMontTo = DataHelper.getLastDayOfMonth(year, month)
                        viewModel.getStatistic(
                            secondMontFrom,
                            secondMontTo,
                            false
                        )
                    }
                }
            }

            DurationPicker.YEAR -> {
                when (monthPicker) {
                    MonthPicker.FIRST -> {
                        binding.tvFirstParametrs.text = year.toString()

                        firstYear = year.toString()
                        firstYearFrom = DataHelper.getFirstDayOfYear(year)
                        firstYearTo = DataHelper.getLastDayOfYear(year)

                        viewModel.getStatistic(firstYearFrom, firstYearTo)
                    }

                    MonthPicker.SECOND -> {
                        binding.tvSecondParametrs.text = year.toString()
                        secondYear = year.toString()

                        secondYearFrom = DataHelper.getFirstDayOfYear(year)
                        secondYearTo = DataHelper.getLastDayOfYear(year)

                        viewModel.getStatistic(secondYearFrom, secondYearTo, false)
                    }
                }
            }

            DurationPicker.ALL -> { }
        }
    }

    override fun onWeekSet(range: String, year: String) {
        val (startDate, endDate) = range.split(" - ")

        when (monthPicker) {
            MonthPicker.FIRST -> {
                binding.tvFirstParametrs.text = range

                firstWeek = range
                firstWeekFrom = "$year.${startDate}"
                firstWeekTo = "$year.${endDate}"

                viewModel.getStatistic(firstWeekFrom, firstWeekTo)
            }

            MonthPicker.SECOND -> {
                binding.tvSecondParametrs.text = range
                secondWeek = range

                secondWeekFrom = "$year.${startDate}"
                secondWeekTo = "$year.${endDate}"

                viewModel.getStatistic(secondWeekFrom, secondWeekTo, false)
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