package com.app.activeparks.ui.event.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.drawable.Drawable
import android.graphics.drawable.LevelListDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.os.PersistableBundle
import android.text.Html
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.HtmlCompat
import androidx.fragment.app.Fragment
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.app.activeparks.data.model.meetings.MeetingsModel.MeetingItem
import com.app.activeparks.data.model.sportevents.ItemEvent
import com.app.activeparks.ui.event.interfaces.EventScannerListener
import com.app.activeparks.ui.event.viewmodel.EventViewModel
import com.app.activeparks.ui.routepoint.RoutePointFragment
import com.app.activeparks.util.MapsViewController
import com.bumptech.glide.Glide
import com.technodreams.activeparks.R
import com.technodreams.activeparks.databinding.ActivityEventBinding
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Locale

class EventActivity : AppCompatActivity(), EventScannerListener, Html.ImageGetter, SwipeRefreshLayout.OnRefreshListener {

    private val viewModel: EventViewModel by viewModel()

    var mapsViewController: MapsViewController? = null

    lateinit var binding: ActivityEventBinding

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        binding = ActivityEventBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.getEvent(intent.getStringExtra("id"))

        mapsViewController = MapsViewController(binding.eventMap, this)

        viewModel.eventDetails.observe(
            this
        ) { events: ItemEvent ->
            try {
                Glide.with(this).load(events.imageUrl).error(R.drawable.ic_prew)
                    .into(binding.include.photo)
                binding.include.nameEvent.setText(events.title)
                if (events.sportsground != null && events.sportsground
                        .title != null
                ) {
                    binding.include.typeEvent.setText(events.sportsground.title)
                } else {
                    if (events.routePoints != null && events.routePoints.size > 0) {
                        val lat = viewModel.address.location[0]
                        val lon = viewModel.address.location[1]
                        viewModel.location(lat, lon)
                        binding.include.typeEvent.setOnClickListener(View.OnClickListener { v: View? ->
                            val uri =
                                "https://google.com/maps/search/?api=1&query=$lat,$lon"
                            val intent =
                                Intent(Intent.ACTION_VIEW, Uri.parse(uri))
                            startActivity(intent)
                        })
                    } else {
                        findViewById<View>(R.id.layout_address).visibility =
                            View.GONE
                    }
                }
                if (events.eventEstimation != null) {
                   // ratingBar.setRating(events.eventEstimation.toFloat())
                }
                @SuppressLint("SimpleDateFormat") val format =
                    SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                try {
                    val date = format.parse(events.startsAt)!!
                    binding.include.date.setText(
                        SimpleDateFormat(
                            "dd MMMM yyyy",
                            Locale("uk", "UA")
                        ).format(date)
                    )
                } catch (e: ParseException) {
                    e.printStackTrace()
                }
                if (events.startsAt != null && events.finishesAt != null) {
                    val startIndex = Math.min(
                        events.startsAt.length,
                        events.startsAt.length - 3
                    )
                    val startsAt =
                        events.startsAt.substring(11, startIndex)
                    val endIndex = Math.min(
                        events.finishesAt.length,
                        events.finishesAt.length - 3
                    )
                    val finishesAt =
                        events.finishesAt.substring(11, endIndex)
                   // mEndEvent.setText("| $startsAt - $finishesAt")
                }
               // mEventStatus.setText(viewModel.statusMapper(events.holdingStatusId))
                //mEventStatus.setVisibility(View.VISIBLE)
                //statusView.setVisibility(View.VISIBLE)
                if (events.holdingStatusId
                        .contains("tg2po97g-96r3-36hr-74ty-6tfgj1p8dzxq")
                ) {
//                    statusView.setBackground(
//                        resources.getDrawable(
//                            R.drawable.button_color,
//                            null
//                        )
//                    )
                }
                if (events.fullDescription != null) {
                    //mDescriptionAction.setVisibility(View.VISIBLE)
                    var web =
                        "<html><head><LINK href=\"https://ap.sportforall.gov.ua/images/index.css\" rel=\"stylesheet\"/></head><body>" + events.fullDescription + "</body></html>"
                    web = web.replace("<img ", "<br><img ")
                   // mDescription.setMovementMethod(LinkMovementMethod.getInstance())
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        HtmlCompat.fromHtml(web, HtmlCompat.FROM_HTML_MODE_LEGACY)
                    } else {
//                        mDescription.setText(
//                            HtmlCompat.fromHtml(
//                                web,
//                                HtmlCompat.FROM_HTML_MODE_LEGACY
//                            )
//                        )
                    }
                }
                if (events.club != null) {
                   // mClubName.setText(events.club.name)
                } else {
                  //  mClubName.setVisibility(View.GONE)
                }
                if (events.typeId
                        .contains("bd09f36f-835c-49e4-88b8-4f835c1602ac")
                ) {
                  //  mLocationAction.setVisibility(View.VISIBLE)
                    findViewById<View>(R.id.layout_location).visibility =
                        View.VISIBLE
                    mapsViewController!!.setMarker(
                        viewModel.address.location[0],
                        viewModel.address.location[1]
                    )
                    if (events.statusId
                            .contains("032fd5ba-f634-469b-3c30-77a1gh63a918") && events.holdingStatusId
                            .contains("tg2po97g-96r3-36hr-74ty-6tfgj1p8dzxq")
                    ) {
                        if (events.eventUser != null && events.eventUser
                                .isCoordinator
                        ) {
                          //  startPointAction.setVisibility(View.VISIBLE)
                          //  startPointAction.setEnabled(true)
                            if (events.routeStartAt == null) {
                            //    startPointAction.setText("Розпочати захід")
                            } else {
                            //    startPointAction.setText("Зупинити захід")
                            }
                        }
                    } else {
                      //  startPointAction.setVisibility(View.GONE)
                    }
                } else if (events.typeId
                        .contains("848e3121-4a2b-413d-8a8f-ebdd4ecf2840")
                ) {
                    findViewById<View>(R.id.layout_location).visibility =
                        View.VISIBLE
                    mapsViewController!!.setMarker(
                        viewModel.address.location[0],
                        viewModel.address.location[1]
                    )
                } else if (events.typeId
                        .contains("e58e5c86-5ca7-412f-94f0-88effd1a45a8")
                ) {
//                    conferenceAction.setOnClickListener(View.OnClickListener { v: View? ->
//                        if (events.conferenceLink != null && events.conferenceLink.length > 0) {
//                            val intent =
//                                Intent(
//                                    Intent.ACTION_VIEW,
//                                    Uri.parse(events.conferenceLink)
//                                )
//                            startActivity(intent)
//                        } else {
//                            Toast.makeText(
//                                this,
//                                "Онлайн конференцій в процесі створення",
//                                Toast.LENGTH_SHORT
//                            )
//                                .show()
//                        }
//                    })
                    if (events.holdingStatusId
                            .contains("6h8ad9c0-fd34-8kr4-h8ft-43vdm3n7do3p")
                    ) {
                        //layoutTimer.setVisibility(View.VISIBLE)
                        //startTimer(events.timeZoneDifference)
                    } else if (events.holdingStatusId
                            .contains("tg2po97g-96r3-36hr-74ty-6tfgj1p8dzxq")
                    ) {
                      //  mRecordsAction.setVisibility(View.GONE)
                      //  layoutTimer.setVisibility(View.GONE)
                       // conferenceAction.setVisibility(View.VISIBLE)
                    } else if (events.holdingStatusId
                            .contains("0q8a6xc0-1nb4-1pr4-h5at-4sw3m0l387yp")
                    ) {
                      //  mRecordsAction.setVisibility(View.VISIBLE)
                      //  conferenceAction.setVisibility(View.GONE)
                    }
                }
                if (events.eventUser != null) {
                    if (events.eventUser.isApproved) {
                      //  mJoinAction.setText("Покинути захід")
                        if (events.holdingStatusId
                                .contains("0q8a6xc0-1nb4-1pr4-h5at-4sw3m0l387yp")
                        ) {
                            findViewById<View>(R.id.layout_rating).visibility =
                                View.VISIBLE
                        }
                    } else {
                     //   mJoinAction.setText("Відмінити запрошення")
                    }
                } else {
                   // mJoinAction.setText("Приєднатись до заходу")
                }
                if (viewModel.userAuth && !events.holdingStatusId
                        .contains("0q8a6xc0-1nb4-1pr4-h5at-4sw3m0l387yp")
                ) {
                 //   mJoinAction.setEnabled(true)
                    if (events.eventUser != null && events.eventUser
                            .isCoordinator
                    ) {
                    //    mJoinAction.setVisibility(View.GONE)
                    } else {
                    //    mJoinAction.setVisibility(View.VISIBLE)
                    }
                }
            } catch (ignored: Exception) {
            }
        }

        viewModel.location.observe(
            this
        ) { location: String? ->
//            mAddress.setText(
//                location
//            )
        }

        viewModel.meetingRecords.observe(
            this
        ) { meetings: List<MeetingItem?>? ->
//            setFragment(
//                MeetingsFragment(meetings)
//            )
        }

        initClickListener()
    }


    fun initClickListener() {
        findViewById<View>(R.id.closed).setOnClickListener { v: View? -> onBackPressed() }
//        mDescriptionAction.setOnClickListener(View.OnClickListener { v: View? ->
//            mDescription.setVisibility(View.VISIBLE)
//            findViewById<View>(R.id.event_fragment).visibility = View.GONE
//            replaceButton()
//            mDescriptionAction.on()
//        })
//        mLocationAction.setOnClickListener(View.OnClickListener { v: View? -> showRoutePoint() })
//        startPointAction.setOnClickListener(View.OnClickListener { v: View? -> viewModel.startEvent() })
//        mRecordsAction.setOnClickListener(View.OnClickListener { v: View? ->
//            viewModel.meetingRecords()
//            findViewById<View>(R.id.event_fragment).visibility = View.GONE
//            replaceButton()
//            mRecordsAction.on()
//        })
//        mPeopleAction.setOnClickListener(View.OnClickListener { v: View? ->
//            setFragment(ParticipantsFragment(viewModel.mId, false, true))
//            replaceButton()
//            mPeopleAction.on()
//        })
//        mJoinAction.setOnClickListener(View.OnClickListener { v: View? ->
//            viewModel.applyUser()
//            mJoinAction.setEnabled(false)
//        })
//        ratingBar.setOnClickListener(View.OnClickListener { v: View? ->
//            viewModel.setEstimation(
//                ratingBar.getRating()
//            )
//        })
//        ratingBar.setOnRatingBarChangeListener(OnRatingBarChangeListener { ratingBar: RatingBar?, rating: Float, fromUser: Boolean ->
//            viewModel.setEstimation(
//                rating
//            )
//        })
        findViewById<View>(R.id.copy_action).setOnClickListener { v: View? ->
            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "text/plain"
            intent.putExtra(
                Intent.EXTRA_SUBJECT,
                "Хочу тебе запросити до заходу \"" + binding.include.nameEvent.getText().toString() + "\""
            )
            intent.putExtra(
                Intent.EXTRA_TEXT,
                "Хочу тебе запросити до заходу \"" + binding.include.nameEvent.getText()
                    .toString() + "\" \n\n https://ap.sportforall.gov.ua/fc-events/0/" + viewModel.mId + "\n\nПриєднуйся до нас! Та оздоровлюйся разом зі мною! \n\nРозроблено на завдання президента України для проекту “Активні парки”"
            )
            startActivity(Intent.createChooser(intent, getString(R.string.app_name)))
        }
    }

    fun replaceButton() {
       // mDescriptionAction.off()
        //mLocationAction.off()
        //mRecordsAction.off()
        //mPeopleAction.off()
    }

    fun startTimer(time: Long) {
        if (time > 0) {
           // conferenceAction.setVisibility(View.VISIBLE)
           // layoutTimer.setVisibility(View.GONE)
            return
        }
        object : CountDownTimer(Math.abs(time), 1000) {
            @SuppressLint("DefaultLocale")
            override fun onTick(millisUntilFinished: Long) {
                val Days = millisUntilFinished / (24 * 60 * 60 * 1000)
                val Hours = millisUntilFinished / (60 * 60 * 1000) % 24
                val Minutes = millisUntilFinished / (60 * 1000) % 60
                val Seconds = millisUntilFinished / 1000 % 60
                //
              //  mDay.setText(String.format("%02d", Days))
               // mHour.setText(String.format("%02d", Hours))
               // mMinutes.setText(String.format("%02d", Minutes))
               // mSeconds.setText(String.format("%02d", Seconds))
            }

            override fun onFinish() {
                viewModel.getEvent(intent.getStringExtra("id"))
                cancel()
            }
        }.start()
    }

    fun showRoutePoint() {
        val dialog = RoutePointFragment(viewModel.mId)
        dialog.show(supportFragmentManager, "route_point_fragment")
    }

    fun setFragment(fragment: Fragment?) {
        findViewById<View>(R.id.event_fragment).visibility = View.VISIBLE
       // mDescription.setVisibility(View.GONE)
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.event_fragment, fragment!!)
            .commit()
    }

    override fun getDrawable(source: String?): Drawable? {
        val d = LevelListDrawable()
        @SuppressLint("UseCompatLoadingForDrawables") val empty =
            resources.getDrawable(R.drawable.logo_active_parks, null)
        d.addLevel(0, 0, empty)
        d.setBounds(0, 0, empty.intrinsicWidth, empty.intrinsicHeight)
//        LoadImage(mDescription.getWidth()).setListener {
//            val t: CharSequence = mDescription.getText()
//            mDescription.setText(t)
//        }.execute(source, d)
        return d
    }
    override fun update() {
        viewModel.getUpdateEvent()
        viewModel.meetingRecords()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.trans_right_in, R.anim.trans_right_out)
    }

    override fun onRefresh() {
       // swipeRefreshLayout.setRefreshing(false)
        viewModel.getUpdateEvent()
    }
}