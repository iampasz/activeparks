package com.app.activeparks.ui.userProfile.menu

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.app.activeparks.MainActivity
import com.app.activeparks.ui.homeWithUser.fragments.clubs.HomeClubsFragment
import com.app.activeparks.ui.homeWithUser.fragments.event.HomeEventsFragment
import com.app.activeparks.ui.track.fragments.listTrack.TracksListFragment
import com.app.activeparks.ui.userProfile.statisticFragment.StatisticFragment
import com.app.activeparks.ui.userProfile.video.VideoFragmentUserProfile
import com.bumptech.glide.Glide
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.technodreams.activeparks.R
import com.technodreams.activeparks.databinding.FragmentMenuProfileBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * Created by O.Dziuba on 09.01.2024.
 */
class MenuProfileFragment : Fragment() {

    private lateinit var binding: FragmentMenuProfileBinding
    private val viewModel: MenuProfileViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentMenuProfileBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setListener()
        observe()

        viewModel.getUser()
    }

    @SuppressLint("SetTextI18n")
    private fun observe() {
        with(viewModel) {
            userDate.observe(viewLifecycleOwner) {
                it?.let {

                    getRole(it.roleId)

                    with(binding) {
                        Glide.with(requireContext())
                            .load(it.photo)
                            .error(R.drawable.ic_prew)
                            .into(ivUser)


                        tvUserName.text = "${it.firstName} ${it.lastName}"
                    }
                }
            }

            userRole.observe(viewLifecycleOwner) {
                it?.let { binding.tvUserRole.text = it }
            }

            logOut.observe(viewLifecycleOwner) {
                if (it) {
                    val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestEmail()
                        .build()
                    val mGoogleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)
                    mGoogleSignInClient.signOut()
                    onBack()
                }
            }
        }
    }

    private fun setListener() {
        with(binding) {
            ivLogOut.setOnClickListener {
                showDialog()
            }
            ivBack.setOnClickListener {
                onBack()
            }
            tvMyClubs.setOnClickListener {
                openFragment(HomeClubsFragment())
            }
            tvMyEvents.setOnClickListener {
                openFragment(HomeEventsFragment())
            }
            tvStatistic.setOnClickListener {
                openFragment(StatisticFragment())
            }
            tvMyVideo.setOnClickListener {
                openFragment(VideoFragmentUserProfile())
            }
            tvActiveRouts.setOnClickListener {
                addFragment(TracksListFragment())
            }
        }
    }

    private fun showDialog() {
        val builder = AlertDialog.Builder(context)

        val dialogClickListener =
            DialogInterface.OnClickListener { dialog: DialogInterface, which: Int ->
                when (which) {
                    DialogInterface.BUTTON_POSITIVE -> {
                        viewModel.logout()
                        if (requireActivity() is MainActivity) {
                            (requireActivity() as MainActivity).navControllerMain!!.navigate(R.id.registration_user)
                        }
                    }

                    DialogInterface.BUTTON_NEGATIVE -> dialog.cancel()
                }
            }

        builder.setMessage("Ви дійсно бажаєте вийти?").setPositiveButton("Так", dialogClickListener)
            .setNegativeButton("Ні", dialogClickListener).show()
    }

    private fun openFragment(fragment: Fragment) {
        (requireActivity() as? MainActivity)?.openFragment(fragment)
    }

    private fun addFragment(fragment: Fragment) {
        (requireActivity() as? MainActivity)?.addFragment(fragment)
    }

    private fun onBack() {
        requireActivity().onBackPressed()
    }
}