package com.app.activeparks.ui.userProfile.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.app.activeparks.MainActivity
import com.app.activeparks.data.model.user.UserRole
import com.app.activeparks.ui.userProfile.info.UserInfoFragment
import com.app.activeparks.ui.userProfile.menu.MenuProfileFragment
import com.app.activeparks.util.extention.FileHelper
import com.app.activeparks.util.extention.toBoolean
import com.bumptech.glide.Glide
import com.technodreams.activeparks.R
import com.technodreams.activeparks.databinding.FragmentProfileHomeBinding
import org.koin.androidx.viewmodel.ext.android.activityViewModel

/**
 * Created by O.Dziuba on 10.01.2024.
 */
class ProfileHomeFragment : Fragment() {

    private lateinit var binding: FragmentProfileHomeBinding
    private val viewModel: ProfileHomeViewModel by activityViewModel()

    private var navController: NavController? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileHomeBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setListener()
        observe()

        initView()
        viewModel.getUser()
    }

    @SuppressLint("SetTextI18n")
    private fun observe() {
        with(viewModel) {
            userDate.observe(viewLifecycleOwner) {
                it?.let {
                    with(binding) {
                        iProfileUser.tvUserName.text = "${it.firstName} ${it.lastName}"
                        Glide.with(requireContext())
                            .load(it.photo)
                            .error(R.drawable.ic_prew)
                            .into(iProfileUser.ivUser)
                        Glide.with(requireContext())
                            .load(it.imageBackground)
                            .error(R.drawable.ic_prew)
                            .into(iProfileUser.vBackgroundUser)

                        iProfileUser.tvUserRole.text = if (it.isTrainer.toBoolean()) {
                            UserRole.TRAINER.role
                        } else if (it.isCoordinatorReport.toBoolean()) {
                            UserRole.COORDINATOR.role
                        } else {
                            UserRole.USER.role
                        }
                    }
                }
            }

            selectedTub.observe(viewLifecycleOwner) {
                it?.let { id ->
                    navController?.navigate(id)
                    selectedTub.value = null
                }
            }

        }
    }

    private fun setListener() {
        with(binding) {
            iProfileUser.btnMoreInfo.setOnClickListener {
                openFragment(UserInfoFragment())
            }
            iProfileUser.ivMenu.setOnClickListener {
                openFragment(MenuProfileFragment())
            }


            FileHelper.changeSize(iProfileUser.vBackgroundUser, resources)
            FileHelper.changeSize(iProfileUser.vBackgroundUser, iProfileUser.ivUser)
            FileHelper.changeSizeCircle(iProfileUser.ivUser, iProfileUser.ivUserCircle)
            FileHelper.changeSizeCircle(iProfileUser.ivUser, iProfileUser.ivLogoUser)
        }
    }

    private fun initView() {
        val navHostFragment =
            childFragmentManager.findFragmentById(R.id.navFragmentsUserProfile) as NavHostFragment
        navController = navHostFragment.navController
        navController?.let { binding.navProfileUser.setupWithNavController(it) }
    }

    private fun openFragment(fragment: Fragment) {
        (requireActivity() as? MainActivity)?.openFragment(fragment)
    }
}