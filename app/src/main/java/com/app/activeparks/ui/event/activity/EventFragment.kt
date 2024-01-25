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
import com.squareup.picasso.Picasso
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

    private val viewModel: EventViewModel by activityViewModel()
    private val participantVM: ParticipantsViewModel by activityViewModel()

    private var mapsViewController: MapsViewController? = null

    lateinit var binding: FragmentEventBinding

    private var isAdmin = false
    private var isEvent = true

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEventBinding
            .inflate(inflater, container, false)

        return binding.root
    }

    @SuppressLint("UseCompatLoadingForDrawables", "SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        eventMenu = binding.navView.menu
        joinItem = eventMenu.findItem(R.id.nav_join)

        binding.swipeRefreshLayout.setOnRefreshListener(this)

        binding.showRout.setOnClickListener {
            showRoutePoint()
        }

        binding.showQr.setOnClickListener {
            startActivity(
                Intent(activity, QrCodeActivity::class.java)
                    .putExtra("pointId", viewModel.location.value)
            )
        }

        initNavigation()

        binding.close.setOnClickListener {
            onBackPressed()
        }

        viewModel.currentId.observe(viewLifecycleOwner) {
            viewModel.updateEventData(viewModel.currentId.value)
        }

        mapsViewController = MapsViewController(binding.eventMap, requireContext())

        viewModel.eventDetails.observe(
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
                        val lat = viewModel.address.location[0]
                        val lon = viewModel.address.location[1]
                        viewModel.location(lat, lon)
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
                binding.include.statusEvent.text = viewModel.statusMapper(events.holdingStatusId)
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
                        viewModel.address.location[0],
                        viewModel.address.location[1]
                    )

                } else if (events.typeId
                        .contains(EventTypes.EVENTS_SIMPLE.type)
                ) {
                    mapsViewController?.setMarker(
                        viewModel.address.location[0],
                        viewModel.address.location[1]
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

            updatePartisipiantVM()
        }

        participantVM.userHeads.observe(viewLifecycleOwner) { item ->
            if (item.items.size > 0) {
                Picasso.get().load(item.items[0].photo).into(binding.include.imageAuthor)
                Picasso.get().load(item.items[0].photo).into(binding.include.imageFirst)
            }
        }

//        participantVM.userMembers.observe(viewLifecycleOwner){
//            members ->
//        }

        viewModel.location.observe(
            viewLifecycleOwner
        ) { location: String? ->
            binding.address.text = location
        }

        initClickListener()
    }


    private fun initClickListener() {
        binding.share.setOnClickListener {
            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "text/plain"
            intent.putExtra(
                Intent.EXTRA_SUBJECT,
                "Хочу тебе запросити до заходу \"" + binding.include.nameEvent.text
                    .toString() + "\""
            )
            intent.putExtra(
                Intent.EXTRA_TEXT,
                "Хочу тебе запросити до заходу \"" + binding.include.nameEvent.text
                    .toString() + "\" \n\n https://ap.sportforall.gov.ua/fc-events/0/" + viewModel.mId + "\n\nПриєднуйся до нас! Та оздоровлюйся разом зі мною! \n\nРозроблено на завдання президента України для проекту “Активні парки”"
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
        val dialog = RoutePointFragment(viewModel.mId)
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
        viewModel.getUpdateEvent()
        viewModel.meetingRecords()
    }

    private fun onBackPressed() {
        parentFragmentManager.removeFragment(this@EventFragment)
    }

    override fun onRefresh() {
        binding.swipeRefreshLayout.setRefreshing(false)
        viewModel.getUpdateEvent()
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
                    viewModel.applyUser()
                }
            }
            true
        }
    }

    private fun updatePartisipiantVM() {

        if (isEvent) {
            participantVM.getEventUser(viewModel.currentId.value, true)
            if (isAdmin) {
                participantVM.getEventUserApplying(viewModel.currentId.value)
            }
        } else {
            participantVM.getClubsUser(viewModel.currentId.value)
            if (isAdmin) {
                participantVM.getClubsUserApplying(viewModel.currentId.value)
            }
        }
    }
}