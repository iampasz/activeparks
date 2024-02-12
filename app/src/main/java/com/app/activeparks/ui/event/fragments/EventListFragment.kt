package com.app.activeparks.ui.event.fragments

import android.annotation.SuppressLint
import android.content.Context.LOCATION_SERVICE
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.app.activeparks.MainActivity
import com.app.activeparks.data.model.calendar.CalendarModel
import com.app.activeparks.data.storage.Preferences
import com.app.activeparks.ui.event.activity.EventFragment
import com.app.activeparks.ui.event.adapter.EventsListAdapter
import com.app.activeparks.ui.event.interfaces.OnItemClickListener
import com.app.activeparks.ui.event.viewmodel.EventViewModel
import com.app.activeparks.ui.homeWithUser.fragments.event.HomeEventsViewModel
import com.app.activeparks.util.extention.gone
import com.app.activeparks.util.extention.mainAddFragment
import com.app.activeparks.util.extention.removeFragment
import com.app.activeparks.util.extention.visible
import com.applandeo.materialcalendarview.CalendarWeekDay
import com.applandeo.materialcalendarview.EventDay
import com.applandeo.materialcalendarview.listeners.OnDayClickListener
import com.google.android.material.tabs.TabLayout
import com.technodreams.activeparks.R
import com.technodreams.activeparks.databinding.FragmentEventsBinding
import org.koin.androidx.viewmodel.ext.android.activityViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Objects


class EventListFragment : Fragment(), LocationListener, OnItemClickListener {

    lateinit var binding: FragmentEventsBinding
    private val viewModel: EventViewModel by activityViewModel()
    private val homeEventsViewModel: HomeEventsViewModel by viewModel()
    private var locationManager: LocationManager? = null
    private lateinit var preferences: Preferences

    val adapter = EventsListAdapter{
        val bundle = Bundle()
        bundle.putString("EVENT_ID", it.id)
        val eventFragment = EventFragment()
        eventFragment.arguments = bundle

        mainAddFragment((requireActivity() as MainActivity), eventFragment)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEventsBinding
            .inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        update()
        observe()
        initListener()
        initView()
    }

    override fun onLocationChanged(location: Location) {
        val latitude = location.latitude
        val longitude = location.longitude
        viewModel.filterCordinate(latitude, longitude)
    }

    @SuppressLint("CommitTransaction")
    override fun onItemClick(position: Int) {
        val bundle = Bundle()
        val eventFragment = EventFragment()
        eventFragment.arguments = bundle
        mainAddFragment((requireActivity() as MainActivity), eventFragment)
    }

    private fun showCreateEventButton() {
        if (preferences.token == null || preferences.token.isEmpty()) {
            binding.createEvent.gone()
        } else {
            binding.createEvent.visible()
        }
    }

    private fun initToolBar() {

        val myTab = binding.selectFilter.newTab()
        myTab.setCustomView(R.layout.custom_tab)
        myTab.customView?.findViewById<TextView>(R.id.tabText)?.text = "ЗА ВІДСТАННЮ"
        myTab.customView?.findViewById<LinearLayout>(R.id.mybg)
            ?.setBackgroundResource(R.drawable.background_green)

        val myTab2 = binding.selectFilter.newTab()
        myTab2.setCustomView(R.layout.custom_tab)
        myTab2.customView?.findViewById<TextView>(R.id.tabText)?.text = "ЗА ЧАСОМ"

        val myTab3 = binding.selectFilter.newTab()
        myTab3.setCustomView(R.layout.custom_tab)
        myTab3.customView?.findViewById<TextView>(R.id.tabText)?.text = "АКСТУАЛЬНЕ"

        binding.selectFilter.addTab(myTab)
        binding.selectFilter.addTab(myTab2)
        binding.selectFilter.addTab(myTab3)

        binding.selectFilter.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                tab?.customView?.findViewById<LinearLayout>(R.id.mybg)
                    ?.setBackgroundResource(R.drawable.background_green)
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                tab?.customView?.findViewById<LinearLayout>(R.id.mybg)
                    ?.setBackgroundResource(R.drawable.background_gray_w_stroke)
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }

        })
    }

    private fun initView(){
        preferences = Preferences(requireContext())
        showCreateEventButton()
        locationManager = requireActivity().getSystemService(LOCATION_SERVICE) as LocationManager

        binding.findEvents.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
            }
        })
        initToolBar()

        binding.listEvents.adapter = adapter
    }

    private fun observe(){

        homeEventsViewModel.eventDayList.observe(viewLifecycleOwner){
            result ->
            adapter.list.submitList(result.newGetting)
        }

        viewModel.calendar.observe(
            viewLifecycleOwner
        ) { calendarItem: CalendarModel? ->
            calendarItem?.let {
                this.setMapperAdapter(
                    it
                )
            }
        }
    }

    private fun initListener(){
        binding.closed.setOnClickListener {
            parentFragmentManager.removeFragment(this)
        }
        binding.createEvent.setOnClickListener {
            (requireActivity() as MainActivity).openFragment(FragmentEventCreate())
        }

        binding.calendarView.setOnDayClickListener(object : OnDayClickListener {
            override fun onDayClick(eventDay: EventDay) {
                val cal = eventDay.calendar
                @SuppressLint("SimpleDateFormat") val dateFormat =
                    SimpleDateFormat("yyyy-MM-dd")


                homeEventsViewModel.getEventsForDate(dateFormat.format(cal.time), dateFormat.format(cal.time))
            }
        })


    }

    @SuppressLint("SimpleDateFormat")
    fun getCurrentDate(): String {
        val currentDate = Date()
        val formatter = SimpleDateFormat("yyyy-MM-dd")
        return formatter.format(currentDate)
    }

    fun update(){

        val today = getCurrentDate()
        homeEventsViewModel.getEventsForDate(today, today)
        viewModel.getEventsList()
        viewModel.calendarEvent()
    }

    private fun setMapperAdapter(calendarItem: CalendarModel) {
        val days: MutableList<EventDay> = ArrayList()
        var calendar: Calendar
        @SuppressLint("SimpleDateFormat") val sdf = SimpleDateFormat("yyyy-MM-dd")
        for (item in calendarItem.items) {
            try {
                if (item.data() != null) {
                    calendar = Calendar.getInstance()
                    calendar.time = Objects.requireNonNull(sdf.parse(item.data()))
                    days.add(EventDay(calendar, R.drawable.seekbar_drawable_mark))
                }
            } catch (e: ParseException) {
                e.printStackTrace()
            }
        }

        binding.calendarView.setEvents(days)
        binding.calendarView.setCalendarDayLayout(R.layout.custom_calendar_day)
        binding.calendarView.setFirstDayOfWeek(CalendarWeekDay.MONDAY)
    }
}