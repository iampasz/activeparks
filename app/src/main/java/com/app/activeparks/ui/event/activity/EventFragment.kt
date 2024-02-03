package com.app.activeparks.ui.event.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.drawable.Drawable
import android.graphics.drawable.LevelListDrawable
import android.net.Uri
import android.os.Bundle
import android.os.CountDownTimer
import android.text.Html
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.app.activeparks.data.model.sportevents.ItemEvent
import com.app.activeparks.ui.event.interfaces.EventScannerListener
import com.app.activeparks.ui.event.util.EventTypes
import com.app.activeparks.ui.event.viewmodel.EventViewModel
import com.app.activeparks.ui.participants.ParticipantsViewModel
import com.app.activeparks.ui.qr.QrCodeActivity
import com.app.activeparks.ui.routepoint.RoutePointFragment
import com.app.activeparks.util.MapsViewController
import com.app.activeparks.util.extention.gone
import com.app.activeparks.util.extention.removeFragment
import com.app.activeparks.util.extention.visible
import com.bumptech.glide.Glide
import com.technodreams.activeparks.R
import com.technodreams.activeparks.databinding.FragmentEventBinding
import org.koin.androidx.viewmodel.ext.android.activityViewModel
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Locale
import kotlin.math.abs

@Suppress("DEPRECATION")
class EventFragment : Fragment(), EventScannerListener, Html.ImageGetter,
    SwipeRefreshLayout.OnRefreshListener {

    private lateinit var eventMenu: Menu
    private lateinit var joinItem: MenuItem

    private val eventViewModel: EventViewModel by activityViewModel()
    private val participantViewModel: ParticipantsViewModel by activityViewModel()

    private var mapsViewController: MapsViewController? = null
    lateinit var binding: FragmentEventBinding
    private var eventId  = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEventBinding
            .inflate(inflater, container, false)

        eventId = arguments?.getString("EVENT_ID", "") ?: ""
        return binding.root
    }

    @SuppressLint("UseCompatLoadingForDrawables", "SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        participantViewModel.isEvent = true
        participantViewModel.id = eventId
        eventViewModel.updateEventData(eventId)
        eventViewModel.setCurrentId(eventId)

        initNavigation()
        onClick()
        initView()
        observe()
        initClickListener()
    }


    private fun initClickListener() {
        val inviteToClubString = getString(R.string.invite_to_event)
        val eventNameString = binding.include.nameEvent.text.toString()
        val linkString = "https://ap.sportforall.gov.ua/fc-events/0/$eventId"
        val joinToUsString = getString(R.string.join_to_us)

        binding.share.setOnClickListener {
            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "text/plain"
            intent
                .putExtra(Intent.EXTRA_SUBJECT,
                    "$inviteToClubString $eventNameString")
            intent
                .putExtra(Intent.EXTRA_TEXT,
                "$inviteToClubString  $eventNameString $linkString  $joinToUsString"

            )
            startActivity(Intent.createChooser(intent, getString(R.string.app_name)))
        }
    }

    private fun startTimer(time: Long) {
        if (time > 0) {
            binding.timerConstrain.gone()
            return
        }
        object : CountDownTimer(abs(time), 1000) {
            @SuppressLint("DefaultLocale")
            override fun onTick(millisUntilFinished: Long) {
                val Days = millisUntilFinished / (24 * 60 * 60 * 1000)
                val Hours = millisUntilFinished / (60 * 60 * 1000) % 24
                val Minutes = millisUntilFinished / (60 * 1000) % 60
                val Seconds = millisUntilFinished / 1000 % 60

                with(binding.timer) {
                    mDay.text = String.format("%02d", Days)
                    mHour.text = String.format("%02d", Hours)
                    mMinutes.text = String.format("%02d", Minutes)
                    mSeconds.text = String.format("%02d", Seconds)
                }
            }

            override fun onFinish() {
                cancel()
            }
        }.start()
    }

    private fun showRoutePoint() {
        val dialog = RoutePointFragment(eventViewModel.mId)
        dialog.show(parentFragmentManager, "route_point_fragment")
    }

    override fun getDrawable(source: String?): Drawable {
        val d = LevelListDrawable()
        @SuppressLint("UseCompatLoadingForDrawables") val empty =
            resources.getDrawable(R.drawable.logo_active_parks, null)
        d.addLevel(/* low = */ 0, /* high = */ 0, /* drawable = */ empty)
        d.setBounds(0, 0, empty.intrinsicWidth, empty.intrinsicHeight)
        return d
    }

    override fun update() {
        eventViewModel.getUpdateEvent()
        eventViewModel.meetingRecords()
    }

    private fun onBackPressed() {
        parentFragmentManager.removeFragment(this@EventFragment)
    }

    override fun onRefresh() {
        binding.swipeRefreshLayout.isRefreshing = false
    }

    private fun initNavigation() {

        val navHostFragment =
            childFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        binding.navView.setupWithNavController(navController)
        binding.navView.setOnNavigationItemSelectedListener { item ->

            when (item.itemId) {
                R.id.nav_about_event, R.id.nav_participant, R.id.nav_gallery -> {
                    NavigationUI.onNavDestinationSelected(item, navController)
                }

                R.id.nav_join -> {
                    eventViewModel.applyUser()
                }
            }
            true
        }
    }

    private fun initView(){
        mapsViewController = MapsViewController(binding.eventMap, requireContext())
        eventMenu = binding.navView.menu
        joinItem = eventMenu.findItem(R.id.nav_join)
        binding.swipeRefreshLayout.setOnRefreshListener(this)
    }

    private fun onClick(){
        binding.close.setOnClickListener {
            onBackPressed()
        }

        binding.showRout.setOnClickListener {
            showRoutePoint()
        }

        binding.showQr.setOnClickListener {
            startActivity(
                Intent(activity, QrCodeActivity::class.java)
                    .putExtra("pointId", eventViewModel.location.value)
            )
        }
    }

    @SuppressLint("SetTextI18n", "UseCompatLoadingForDrawables")
    private fun observe(){
        eventViewModel.eventDetails.observe(
            viewLifecycleOwner
        ) { events: ItemEvent ->

            try {
                Glide.with(this).load(events.imageUrl).error(R.drawable.ic_prew)
                    .into(binding.include.photo)
                binding.include.nameEvent.text = events.title
                if (events.sportsground != null && events.sportsground
                        .title != null
                ) {
                    binding.include.typeEvent.text = events.sportsground.title
                } else {
                    if (events.routePoints != null && events.routePoints.size > 0) {
                        val lat = eventViewModel.address.location[0]
                        val lon = eventViewModel.address.location[1]
                        eventViewModel.location(lat, lon)
                        binding.include.typeEvent.setOnClickListener {
                            val uri =
                                "https://google.com/maps/search/?api=1&query=$lat,$lon"
                            val intent =
                                Intent(Intent.ACTION_VIEW, Uri.parse(uri))
                            startActivity(intent)
                        }
                    }
                }

                startTimer(events.timeZoneDifference)

                @SuppressLint("SimpleDateFormat") val format =
                    SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                try {
                    val date = format.parse(events.startsAt)
                    binding.include.date.text = date?.let {
                        SimpleDateFormat(
                            "dd MMMM yyyy",
                            Locale("uk", "UA")
                        ).format(it)
                    }
                } catch (e: ParseException) {
                    e.printStackTrace()
                }
                if (events.startsAt != null && events.finishesAt != null) {
                    val startIndex = events.startsAt.length.coerceAtMost(events.startsAt.length - 3)
                    val startsAt =
                        events.startsAt.substring(11, startIndex)
                    val endIndex =
                        events.finishesAt.length.coerceAtMost(events.finishesAt.length - 3)
                    val finishesAt =
                        events.finishesAt.substring(11, endIndex)
                    binding.include.endTimeEvent.text = "| $startsAt - $finishesAt"
                }
                binding.include.statusEvent.text = eventViewModel.statusMapper(events.holdingStatusId)
                if (events.holdingStatusId
                        .contains(EventTypes.EVENTS_DURING.type)
                ) {
                    binding.include.greanSquare.background =
                        resources.getDrawable(
                            R.drawable.button_color,
                            null
                        )
                }

                if (events.typeId
                        .contains(EventTypes.WITH_ROUTE.type)
                ) {
                    mapsViewController?.setMarker(
                        eventViewModel.address.location[0],
                        eventViewModel.address.location[1]
                    )

                } else if (events.typeId
                        .contains(EventTypes.EVENTS_SIMPLE.type)
                ) {
                    mapsViewController?.setMarker(
                        eventViewModel.address.location[0],
                        eventViewModel.address.location[1]
                    )
                }
                if (events.eventUser != null) {
                    if (events.eventUser.isApproved) {
                        joinItem.title = getString(R.string.leave)
                    } else {
                        joinItem.title = getString(R.string.cansel)
                    }
                } else {
                    joinItem.title = getString(R.string.join)
                }
            } catch (ignored: Exception) {
            }


            if (events.memberAmount > 1) {
                binding.include.gCounter.visible()
                binding.include.counterText.text = "+" + (events.memberAmount - 1)
            }
        }

        eventViewModel.location.observe(
            viewLifecycleOwner
        ) { location: String? ->
            binding.address.text = location
        }
    }
}