package com.app.activeparks.ui.event.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.drawable.Drawable
import android.graphics.drawable.LevelListDrawable
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
import com.app.activeparks.ui.clubs.ClubsViewModel
import com.app.activeparks.ui.event.interfaces.EventScannerListener
import com.app.activeparks.ui.event.util.EventTypes
import com.app.activeparks.ui.event.viewmodel.EventViewModel
import com.app.activeparks.ui.participants.ParticipantsViewModel
import com.app.activeparks.util.MapsViewController
import com.app.activeparks.util.extention.removeFragment
import com.bumptech.glide.Glide
import com.squareup.picasso.Picasso
import com.technodreams.activeparks.R
import com.technodreams.activeparks.databinding.FragmentClubBinding
import org.koin.androidx.viewmodel.ext.android.activityViewModel
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Locale
import kotlin.math.abs

@Suppress("DEPRECATION")
class ClubFragment : Fragment(), EventScannerListener, Html.ImageGetter,
    SwipeRefreshLayout.OnRefreshListener {

    private lateinit var eventMenu: Menu
    private lateinit var joinItem: MenuItem

    private val viewModel: EventViewModel by activityViewModel()
    private val participantVM: ParticipantsViewModel by activityViewModel()
    private val clubsViewModel: ClubsViewModel by activityViewModel()

    private var mapsViewController: MapsViewController? = null

    lateinit var binding: FragmentClubBinding

    private var isAdmin = false
    private var isEvent = true

    var clubId  = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentClubBinding
            .inflate(inflater, container, false)

        clubId = arguments?.getString("CLUB_ID", "") ?: ""

        return binding.root
    }

    @SuppressLint("UseCompatLoadingForDrawables", "SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        eventMenu = binding.navView.menu
        joinItem = eventMenu.findItem(R.id.nav_club_join)

        binding.swipeRefreshLayout.setOnRefreshListener(this)

        initNavigation()

        binding.close.setOnClickListener {
            onBackPressed()
        }

        viewModel.currentId.observe(viewLifecycleOwner) {
            viewModel.updateEventData(viewModel.currentId.value)
        }

      //  mapsViewController = MapsViewController(binding.eventMap, requireContext())

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
                   // binding.include.typeEvent.text = events.sportsground.title
                } else {
                    if (events.routePoints != null && events.routePoints.size > 0) {
                        val lat = viewModel.address.location[0]
                        val lon = viewModel.address.location[1]
                        viewModel.location(lat, lon)
//                        binding.include.typeEvent.setOnClickListener {
//                            val uri =
//                                "https://google.com/maps/search/?api=1&query=$lat,$lon"
//                            val intent =
//                                Intent(Intent.ACTION_VIEW, Uri.parse(uri))
//                            startActivity(intent)
//                        }
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


            updatePartisipiantVM()
        }



//        participantVM.userMembers.observe(viewLifecycleOwner){
//            members ->
//        }

        viewModel.location.observe(
            viewLifecycleOwner
        ) { location: String? ->
          //  binding.address.text = location
        }

        onClick()
        clubsViewModel.getClubsDetail(clubId)
        initClickListener()
        observe()
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
           // binding.timerConstrain.gone()
            return
        }
        object : CountDownTimer(abs(time), 1000) {
            @SuppressLint("DefaultLocale")
            override fun onTick(millisUntilFinished: Long) {

            }

            override fun onFinish() {
                cancel()
            }
        }.start()
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
        parentFragmentManager.removeFragment(this@ClubFragment)
    }

    override fun onRefresh() {
        binding.swipeRefreshLayout.setRefreshing(false)
        viewModel.getUpdateEvent()
    }

    private fun initNavigation() {

        val navHostFragment =
            childFragmentManager.findFragmentById(R.id.nav_host_clubs) as NavHostFragment
        val navController = navHostFragment.navController
        binding.navView.setupWithNavController(navController)
        binding.navView.setOnNavigationItemSelectedListener { item ->

            when (item.itemId) {
                R.id.nav_club_event, R.id.nav_club_participant, R.id.nav_club_blog -> {
                    NavigationUI.onNavDestinationSelected(item, navController)
                }

                R.id.nav_club_join -> {
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

    private fun observe(){

        clubsViewModel.eventDetails.observe(viewLifecycleOwner){
            clubs ->
            Picasso.get().load(clubs.logoUrl).into(binding.include.photo)
            binding.countParticipant.text = clubs.memberAmount.toString()
            binding.description.text = clubs.description

            @SuppressLint("SimpleDateFormat") val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            try {
                val date = format.parse(clubs.createdAt)!!
                binding.createdDate.setText(SimpleDateFormat("dd MMMM yyyy", Locale("uk", "UA")).format(date))
            } catch (e: ParseException) {
                e.printStackTrace()
            }
        }

    }

    private fun onClick(){
        binding.share.setOnClickListener{
            shareClub()
        }
    }

    private fun shareClub(){
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"
        intent.putExtra(
            Intent.EXTRA_SUBJECT,
            "Хочу тебе запросити до клубу \"" + binding.include.nameEvent.text.toString() + "\""
        )
        intent.putExtra(
            Intent.EXTRA_TEXT,
            "Хочу тебе запросити до клубу \"" + binding.include.nameEvent.text
                .toString() + "\"  \n\n" + "https://ap.sportforall.gov.ua/fc/" + viewModel.mId + " \n\nПриєднуйся до нас! Та оздоровлюйся разом з нами! \n\nРозроблено на завдання президента України для проекту “Активні парки” "
        )
        startActivity(Intent.createChooser(intent, getString(R.string.app_name)))
    }
}