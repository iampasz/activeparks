package com.app.activeparks.ui.clubs.fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.drawable.Drawable
import android.graphics.drawable.LevelListDrawable
import android.net.Uri
import android.os.Bundle
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
import com.app.activeparks.data.model.clubs.UserInviteDeclaration
import com.app.activeparks.data.storage.Preferences
import com.app.activeparks.ui.clubs.ClubsViewModelKT
import com.app.activeparks.ui.event.interfaces.EventScannerListener
import com.app.activeparks.ui.participants.ParticipantsViewModel
import com.app.activeparks.util.extention.gone
import com.app.activeparks.util.extention.removeFragment
import com.app.activeparks.util.extention.visible
import com.bumptech.glide.Glide
import com.technodreams.activeparks.R
import com.technodreams.activeparks.databinding.FragmentClubBinding
import org.koin.androidx.viewmodel.ext.android.activityViewModel
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Locale

@Suppress("DEPRECATION")
class ClubFragment : Fragment(), EventScannerListener, Html.ImageGetter,
    SwipeRefreshLayout.OnRefreshListener {

    lateinit var binding: FragmentClubBinding

    private lateinit var eventMenu: Menu
    private lateinit var joinItem: MenuItem
    private val clubsViewModelKT: ClubsViewModelKT by activityViewModel()
    private val participantsViewModel: ParticipantsViewModel by activityViewModel()

    var clubId = ""
    private var userInClub = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentClubBinding
            .inflate(inflater, container, false)

        clubId = arguments?.getString("CLUB_ID", "") ?: ""
        clubsViewModelKT.currentClubId.value = clubId

        return binding.root
    }

    @SuppressLint("UseCompatLoadingForDrawables", "SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        update()
        initView()
        initListener()
        observe()
    }

    private fun initListener() {
        val inviteToClubString = getString(R.string.invite_to_club)
        val eventNameString = binding.include.nameEvent.text.toString()
        val linkString = "https://ap.sportforall.gov.ua/fc-events/0/$clubId"
        val joinToUsString = getString(R.string.join_to_us)

        binding.share.setOnClickListener {
            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "text/plain"
            intent.putExtra(
                Intent.EXTRA_TEXT,
                "$inviteToClubString  $eventNameString $linkString $joinToUsString"
            )
            startActivity(Intent.createChooser(intent, getString(R.string.app_name)))
        }
        binding.close.setOnClickListener {
            parentFragmentManager.removeFragment(this@ClubFragment)
        }
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
        participantsViewModel.id = clubId
        participantsViewModel.isEvent = false
        clubsViewModelKT.getClubsDetails(clubId)
        clubsViewModelKT.getClubNewsList(clubId)
        clubsViewModelKT.getClubsDetails(clubId)
    }

    override fun onRefresh() {
        binding.swipeRefreshLayout.isRefreshing = false
        clubsViewModelKT.getClubsDetails(clubId)
    }

    private fun initView() {

        eventMenu = binding.navView.menu
        joinItem = eventMenu.findItem(R.id.nav_club_join)
        binding.swipeRefreshLayout.setOnRefreshListener(this)

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
                    val preferences = Preferences(requireContext())
                    val userId = preferences.id
                    val userInviteDeclaration = UserInviteDeclaration(userId)

                    if (userInClub) {
                        clubsViewModelKT.getRejectUser(clubId, userInviteDeclaration)
                    } else {
                        clubsViewModelKT.getApplyUser(clubId, userInviteDeclaration)
                    }
                }
            }
            true
        }

    }

    private fun observe() {

        clubsViewModelKT.clubDetails.observe(viewLifecycleOwner) { clubs ->

            clubs?.logoUrl?.let {
                Glide
                    .with(binding.include.photo.context)
                    .load(clubs.logoUrl)
                    .error(R.drawable.ic_prew)
                    .into(binding.include.photo) }


            clubs?.logoUrl?.let {
                Glide
                    .with(binding.include.photo.context)
                    .load(clubs.logoUrl)
                    .error(R.drawable.ic_prew)
                    .into(binding.include.photo) }


            binding.countParticipant.text = clubs.memberAmount.toString()
            binding.description.text = clubs.description
            binding.include.nameEvent.text = clubs.name

            @SuppressLint("SimpleDateFormat") val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            try {
                val date = format.parse(clubs.createdAt)!!
                binding.createdDate.text = SimpleDateFormat(
                    "dd MMMM yyyy",
                    Locale("uk", "UA")
                ).format(date)
            } catch (e: ParseException) {
                e.printStackTrace()
            }

            if (clubs.instagramUrl != null) {
                binding.circleInstagram.visible()
                binding.circleInstagram.setOnClickListener {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(clubs.instagramUrl))
                    startActivity(intent)
                }
            } else {
                binding.circleInstagram.gone()
            }

            if (clubs.facebookUrl != null) {
                binding.circleFacebook.visible()
                binding.circleFacebook.setOnClickListener {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(clubs.facebookUrl))
                    startActivity(intent)
                }
            } else {
                binding.circleFacebook.gone()
            }

            if (clubs.telegramUrl != null) {
                binding.circleTelegram.visible()
                binding.circleTelegram.setOnClickListener {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(clubs.telegramUrl))
                    startActivity(intent)
                }
            } else {
                binding.circleTelegram.gone()
            }

            if (clubs.youtubeUrl != null) {
                binding.circleYoutube.visible()
                binding.circleYoutube.setOnClickListener {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(clubs.youtubeUrl))
                    startActivity(intent)
                }
            } else {
                binding.circleYoutube.gone()
            }

            if (clubs.clubUser != null) {
                joinItem.title = getString(R.string.cansel)
                userInClub = true
            } else {
                joinItem.title = getString(R.string.join)
                userInClub = false
            }
        }

        clubsViewModelKT.requestToEntry.observe(viewLifecycleOwner) { result ->
            if (result) {
                joinItem.title = getString(R.string.cansel)
                userInClub = true
            } else {
                joinItem.title = getString(R.string.join)
                userInClub = false
            }
        }

        clubsViewModelKT.requestToCansel.observe(viewLifecycleOwner) { result ->
            if (result) {
                joinItem.title = getString(R.string.join)
                userInClub = false
            } else {
                joinItem.title = getString(R.string.cansel)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        update()
    }
}