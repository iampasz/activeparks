package com.app.activeparks.ui.userProfile.menu

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.app.activeparks.MainActivity
import com.app.activeparks.data.model.user.UserRole
import com.app.activeparks.ui.support.SupportActivity
import com.app.activeparks.ui.track.fragments.listTrack.TracksListFragment
import com.app.activeparks.ui.userProfile.home.ProfileHomeViewModel
import com.app.activeparks.util.URL_INFO_LIST
import com.app.activeparks.util.extention.gone
import com.app.activeparks.util.extention.logOut
import com.bumptech.glide.Glide
import com.technodreams.activeparks.R
import com.technodreams.activeparks.databinding.FragmentMenuProfileBinding
import org.koin.androidx.viewmodel.ext.android.activityViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * Created by O.Dziuba on 09.01.2024.
 */
class MenuProfileFragment : Fragment() {

    private lateinit var binding: FragmentMenuProfileBinding
    private val viewModel: MenuProfileViewModel by viewModel()
    private val profileViewModel: ProfileHomeViewModel by activityViewModel()

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
                    with(binding) {
                        Glide.with(requireContext())
                            .load(it.photo)
                            .error(R.drawable.ic_prew)
                            .into(ivUser)

                        tvUserName.text = "${it.firstName} ${it.lastName}"

                        tvUserRole.text = UserRole.getRoleByKeyId(it.roleId)
                        if (it.isTrainer == 2) {
                            tvFavorite.gone()
                            tvKnowledgeBase.gone()
                            tvAdminEvent.gone()
                        } else if (it.roleId == UserRole.ADMIN.keyId || it.roleId == UserRole.EVENT_MODERATOR.keyId) {
                            tvFavorite.gone()
                            tvKnowledgeBase.gone()
                            tvActiveRoads.gone()
                            tvRoads.gone()
                        } else {
                            tvActiveRoads.gone()
                            tvRoads.gone()
                            tvAdminEvent.gone()
                        }
                    }
                }
            }

            logOut.observe(viewLifecycleOwner) {
                if (it) {
                    logOut(requireActivity())
                    onBack()
                }
            }
        }
    }

    private fun setListener() {
        with(binding) {
            ivLogOut.setOnClickListener {
                showLogOutDialog()
            }
            ivBack.setOnClickListener {
                onBack()
            }
            tvMyClubs.setOnClickListener {
                profileViewModel.selectedTub.value = R.id.navClubUser
                onBack()
            }
            tvMyEvents.setOnClickListener {
                profileViewModel.selectedTub.value = R.id.navEventsUser
                onBack()
            }
            tvStatistic.setOnClickListener {
                profileViewModel.selectedTub.value = R.id.navStatisticsUser
                onBack()
            }
            tvMyVideo.setOnClickListener {
                profileViewModel.selectedTub.value = R.id.navVideoUser
                onBack()
            }
            tvKnowledgeBase.setOnClickListener {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(URL_INFO_LIST))
                startActivity(intent)
            }
            tvApkUpdate.setOnClickListener {
                updateApk()
            }
            tvDeactivationAccount.setOnClickListener {
                showDeleteUserDialog()
            }
            tvSupport.setOnClickListener {
                startActivity(Intent(requireActivity(), SupportActivity::class.java))
            }
            tvActiveRouts.setOnClickListener {
                addFragment(TracksListFragment())
            }
        }
    }

    private fun showLogOutDialog() {
        shodDialog("Ви дійсно бажаєте вийти?") { viewModel.logout() }
    }

    private fun showDeleteUserDialog() {
        shodDialog("Ви дійсно бажаєте видалити аккаунт?") { viewModel.removeUser() }
    }

    private fun shodDialog(title: String, job: () -> Unit) {
        val builder = AlertDialog.Builder(context)

        val dialogClickListener =
            DialogInterface.OnClickListener { dialog: DialogInterface, which: Int ->
                when (which) {
                    DialogInterface.BUTTON_POSITIVE -> {
                        job()
                        if (requireActivity() is MainActivity) {
                            (requireActivity() as MainActivity).navControllerMain?.navigate(R.id.registration_user)
                        }
                    }

                    DialogInterface.BUTTON_NEGATIVE -> dialog.cancel()
                }
            }

        builder.setMessage(title).setPositiveButton("Так", dialogClickListener)
            .setNegativeButton("Ні", dialogClickListener).show()
    }

    private fun updateApk() {
        val appPackageName: String = requireContext().packageName

        try {
            startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("market://details?id=$appPackageName")
                )
            )
        } catch (e: android.content.ActivityNotFoundException) {
            startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://play.google.com/store/apps/details?id=$appPackageName")
                )
            )
        }
    }

    private fun addFragment(fragment: Fragment) {
        (requireActivity() as? MainActivity)?.addFragment(fragment)
    }

    private fun onBack() {
        requireActivity().onBackPressed()
    }
}