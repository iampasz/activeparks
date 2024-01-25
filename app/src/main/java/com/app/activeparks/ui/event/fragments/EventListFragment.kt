package com.app.activeparks.ui.event.fragments

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context.LOCATION_SERVICE
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.activeparks.MainActivity
import com.app.activeparks.data.model.calendar.CalendarModel
import com.app.activeparks.data.model.sportevents.EventList
import com.app.activeparks.data.model.sportevents.ItemEvent
import com.app.activeparks.data.model.sportevents.ItemEventTitle
import com.app.activeparks.data.storage.Preferences
import com.app.activeparks.ui.event.activity.EventFragment
import com.app.activeparks.ui.event.adapter.EventTypeAdapter
import com.app.activeparks.ui.event.adapter.EventsListAdapterKT
import com.app.activeparks.ui.event.interfaces.OnItemClickListener
import com.app.activeparks.ui.event.viewmodel.EventViewModel
import com.app.activeparks.util.extention.gone
import com.app.activeparks.util.extention.removeFragment
import com.app.activeparks.util.extention.visible
import com.applandeo.materialcalendarview.CalendarWeekDay
import com.applandeo.materialcalendarview.EventDay
import com.applandeo.materialcalendarview.listeners.OnDayClickListener
import com.google.android.material.tabs.TabLayout
import com.technodreams.activeparks.R
import com.technodreams.activeparks.databinding.FragmentEventsBinding
import org.koin.androidx.viewmodel.ext.android.activityViewModel
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Objects


class EventListFragment : Fragment(), LocationListener, OnItemClickListener {

    lateinit var binding: FragmentEventsBinding
    private val viewModel: EventViewModel by activityViewModel()
    private var locationManager: LocationManager? = null
    private lateinit var evetTypeAdapter: EventTypeAdapter
    private var nameList: MutableList<ItemEvent> = mutableListOf()
    private val eventsListAdapter = EventsListAdapterKT(this)
    private lateinit var preferences: Preferences


    //private val clubsList: ArrayList<ItemClub> = java.util.ArrayList()

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

        preferences = Preferences(requireContext())
        showCreateEventButton()
        binding.listEvents.adapter = eventsListAdapter
        locationManager = requireActivity().getSystemService(LOCATION_SERVICE) as LocationManager
        viewModel.getEventsList()
        viewModel.calendarEvent()

        binding.closed.setOnClickListener {
            parentFragmentManager.removeFragment(this)
        }
        binding.createEvent.setOnClickListener {
            (requireActivity() as MainActivity).openFragment(FragmentEventCreate())
        }

        viewModel.mySportEventsList.observe(viewLifecycleOwner) { result: EventList ->
            run {
                setAdapter(result)
                // binding.titleText2.text = "Жодного запланованого заходу"
            }
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

        binding.calendarView.setOnDayClickListener(object : OnDayClickListener {
            override fun onDayClick(eventDay: EventDay) {
                val cal = eventDay.calendar
                @SuppressLint("SimpleDateFormat") val dateFormat =
                    SimpleDateFormat("yyyy-MM-dd")

                viewModel.eventsDay(dateFormat.format(cal.time))
                viewModel.getEventsByDay(dateFormat.format(cal.time))
            }
        })
        addEventTitlesList()
        binding.findEvents.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                //filterList(s.toString())
            }
        })

        initToolBar()

    }


    //  fun filterList() {
//        val filteredList = originalList.filter { item ->
//            // Здійснюємо фільтрацію за введеним текстом
//            item.name.contains(query, ignoreCase = true)
//        }

    // Оновлюємо список з відфільтрованими даними
    // adapter.updateList(filteredList)
    //   }

    @SuppressLint("NotifyDataSetChanged")
    fun setAdapter(events: EventList) {
        nameList = events.items as MutableList<ItemEvent>
        eventsListAdapter.differ.submitList(events.items)
        eventsListAdapter.notifyDataSetChanged()
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

    override fun onResume() {
        super.onResume()
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                1
            )
            return
        }
        locationManager?.requestLocationUpdates(LocationManager.PASSIVE_PROVIDER, 0, 50f, this)
    }

    override fun onLocationChanged(location: Location) {
        val latitude = location.latitude
        val longitude = location.longitude
        viewModel.filterCordinate(latitude, longitude)
    }

    override fun onProviderEnabled(provider: String) {}

    override fun onProviderDisabled(provider: String) {}


    private fun addEventTitlesList() {

        val listTitle = ArrayList<ItemEventTitle>()

        val itemList = listOf(
            ItemEventTitle(
                1,
                resources.getString(R.string.my_event),
                true,
                R.drawable.background_green
            ),
            ItemEventTitle(2, resources.getString(R.string.my_clubs), false),
            ItemEventTitle(3, resources.getString(R.string.all_clubs), false),
            ItemEventTitle(4, resources.getString(R.string.public_events), false)
        )

        listTitle.addAll(itemList)

        evetTypeAdapter = EventTypeAdapter(listTitle) { position ->

            for (i in itemList.indices) {
                if (i != position && itemList[i].isSelected) {
                    itemList[i].isSelected = false
                    itemList[i].backgroundResource = R.drawable.background_transparent
                    evetTypeAdapter.notifyItemChanged(i)
                }
            }

            val selectedItem = itemList[position]
            selectedItem.isSelected = !selectedItem.isSelected
            selectedItem.backgroundResource =
                if (selectedItem.isSelected) R.drawable.background_green else R.drawable.background_transparent

            evetTypeAdapter.notifyItemChanged(position)


        }

        binding.listTitle.adapter = evetTypeAdapter
        binding.listTitle.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
    }

    @SuppressLint("CommitTransaction")
    override fun onItemClick(position: Int) {

        Log.i("EventFragmentTag", "${nameList[position].id} ll")
        //viewModel.updateEventData();
        viewModel.setCurrentId(nameList[position].id)
        Log.i("EventFragmentTag", "${viewModel.mId} ll")

        Log.i("EventFragmentTag", "$viewModel VM")

        parentFragmentManager.beginTransaction()
            .add(R.id.constrain_events_container, EventFragment()).commit()
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

//    fun getAllClubs(limit: Int) {
//        val allClubs = repository.getClubsAll(limit.toString()).subscribeOn(Schedulers.io())
//            .observeOn(AndroidSchedulers.mainThread())
//            .subscribe(
//                { result: Clubs ->
//                    clubsList.clear()
//                    clubsList.addAll(result.items)
//                    setClubsAdapter()
//                }
//            ) { error: Throwable? -> }
//    }

    // private fun setClubsAdapter() {
    // binding.listEvents.adapter = ClubsAdaper(requireContext(), clubsList)
    //        .setOnClubsListener { itemClub ->
//                startActivity(
////                    Intent(this@EventListActivity2, ClubActivity::class.java).putExtra(
////                        "id",
////                        itemClub.id
////                    )
//                )
    //   }
    //  }

}