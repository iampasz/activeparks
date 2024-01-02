package com.app.activeparks.ui.event.activity

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.activeparks.data.model.calendar.CalendarModel
import com.app.activeparks.data.model.sportevents.EventList
import com.app.activeparks.data.model.sportevents.ItemEvent
import com.app.activeparks.data.model.sportevents.ItemEventTitle
import com.app.activeparks.data.model.sportevents.SportEvents
import com.app.activeparks.data.repository.Repository
import com.app.activeparks.data.storage.Preferences
import com.app.activeparks.ui.event.adapter.EventTypeAdapter
import com.app.activeparks.ui.event.adapter.EventsListAdaper
import com.app.activeparks.ui.event.adapter.EventsListAdapterKT
import com.app.activeparks.ui.event.fragments.FragmentEventCreate
import com.app.activeparks.ui.event.interfaces.OnItemClickListener
import com.app.activeparks.ui.event.interfaces.ResponseCallBack
import com.app.activeparks.ui.event.util.EventController
import com.app.activeparks.ui.event.viewmodel.EventRouteViewModel
import com.app.activeparks.ui.event.viewmodel.EventViewModel
import com.app.activeparks.util.extention.replaceFragment
import com.applandeo.materialcalendarview.CalendarWeekDay
import com.applandeo.materialcalendarview.EventDay
import com.applandeo.materialcalendarview.listeners.OnDayClickListener
import com.google.gson.Gson
import com.technodreams.activeparks.R
import com.technodreams.activeparks.databinding.FragmentEventsBinding
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import okhttp3.ResponseBody
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Objects

@Suppress("DEPRECATION")
class EventListActivity2 : AppCompatActivity(), LocationListener, OnItemClickListener {

    lateinit var binding: FragmentEventsBinding
    private val viewModel: EventViewModel by viewModel()
    private val eventRouteViewModel: EventRouteViewModel by viewModel()
    private lateinit var disposable: Disposable
    private var locationManager: LocationManager? = null
    private lateinit var evetTypeAdapter: EventTypeAdapter
    var nameList: MutableList<ItemEvent> = mutableListOf()
    val eventsListAdapter = EventsListAdapterKT(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentEventsBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val setDataResponseSuccessful = object : ResponseCallBack {
            override fun load(responseFromApi: String) {

                val gson = Gson()
                val eventList =
                    gson.fromJson(
                        responseFromApi,
                        EventList::class.java
                    )

                nameList.addAll(eventList.items)
                eventsListAdapter.differ.submitList(nameList)
                binding.listEvents.adapter = eventsListAdapter
            }
        }

        EventController(this).getMyEvents(setDataResponseSuccessful)
        locationManager = getSystemService(LOCATION_SERVICE) as LocationManager


        viewModel.getEventsList()
        viewModel.calendarEvent()

        binding.closed.setOnClickListener { onBackPressed() }
        binding.createEvent.setOnClickListener { updateViewModelData() }

        viewModel.sportEventsList.observe(this) { result: SportEvents ->
            run {
                setAdapter(result)

                // binding.titleText2.text = "Жодного запланованого заходу"
            }
        }

        viewModel.testSportEventsList.observe(this) { result: EventList ->
            run {
                testSetAdapter(result)


                // binding.titleText2.text = "Жодного запланованого заходу"
            }
        }


        viewModel.calendar.observe(
            this
        ) { calendarItem: CalendarModel? ->
            this.setMapperAdapter(
                calendarItem!!
            )
        }

        binding.calendarView.setOnDayClickListener(object : OnDayClickListener {
            override fun onDayClick(eventDay: EventDay) {
                val cal = eventDay.calendar
                @SuppressLint("SimpleDateFormat") val dateFormat =
                    SimpleDateFormat("yyyy-MM-dd")
                viewModel.eventsDay(dateFormat.format(cal.time))
                viewModel.testEventsDay(dateFormat.format(cal.time))

            }
        })
        addEventTitlesList()
        binding.findEvents.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                filterList(s.toString())
            }
        })

    }

    fun filterList(query: String) {
//        val filteredList = originalList.filter { item ->
//            // Здійснюємо фільтрацію за введеним текстом
//            item.name.contains(query, ignoreCase = true)
//        }

        // Оновлюємо список з відфільтрованими даними
        // adapter.updateList(filteredList)
    }

    private fun openCreateEventFragment() {
        supportFragmentManager
            .replaceFragment(R.id.constrain_events_container, FragmentEventCreate())
    }

    fun setAdapter(events: SportEvents) {
        if (events.items.size > 0) {
            findViewById<View>(R.id.list_null).visibility = View.GONE
        }
        binding.listEvents.adapter = EventsListAdaper(
            this,
            events.items
        ).setOnEventListener(object :
            EventsListAdaper.EventsListener {
            override fun onInfo(itemClub: ItemEvent) {
                startActivity(
                    Intent(baseContext, EventActivity::class.java).putExtra(
                        "id",
                        itemClub.id
                    )
                )
            }

            override fun onOpenMaps(lat: Double, lon: Double) {
                val uri = "https://www.google.com/maps/search/?api=1&query=$lat,$lon"
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))
                startActivity(intent)
            }
        })
    }

    @SuppressLint("NotifyDataSetChanged")
    fun testSetAdapter(events: EventList) {

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
        // binding.calendarView.setPreviousButtonImage(resources.getDrawable(R.drawable.ic_arrow_left_gray))

    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.trans_right_in, R.anim.trans_right_out)
    }

    override fun onResume() {
        super.onResume()

        // Request location updates
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
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

    private fun updateViewModelData() {

        val repository: Repository

        val preferences = Preferences(this)

        preferences.server = true
        repository = Repository(preferences)
        disposable = repository.createEmptyEvent()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ responseBody: ResponseBody ->
                val jsonResponse = responseBody.string()
                val gson = Gson()
                val itemEvent =
                    gson.fromJson(
                        jsonResponse,
                        ItemEvent::class.java
                    )


                eventRouteViewModel.updateItemEventData(itemEvent)
                openCreateEventFragment()
            }
            ) { error: Throwable ->
                Log.i("THROWABLE", error.message + " ")
                Toast.makeText(this, "Виникли проблеми, спробуйте пізніше", Toast.LENGTH_SHORT)
                    .show()
            }
    }

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
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
    }

    override fun onItemClick(position: Int) {
        startActivity(
            Intent(baseContext, EventActivity::class.java).putExtra(
                "id",
                nameList[position].id
            )
        )
    }
}