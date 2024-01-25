package com.app.activeparks.ui.homeWithUser.fragments.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.app.activeparks.MainActivity
import com.app.activeparks.ui.userProfile.home.ProfileHomeFragment
import com.app.activeparks.util.extention.FileHelper
import com.app.activeparks.util.extention.gone
import com.bumptech.glide.Glide
import com.technodreams.activeparks.R
import com.technodreams.activeparks.databinding.FragmentMainHomeBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * Created by O.Dziuba on 10.01.2024.
 */
class MainHomeFragment : Fragment() {

    private lateinit var binding: FragmentMainHomeBinding
    private val viewModel: MainHomeViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        observe()
        setListener()

        viewModel.getUser()
    }

    private fun setListener() {
        with(binding) {
            iHomeUser.inNotification.setOnClickListener {
                openFragment(ProfileHomeFragment())
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun observe() {
        with(viewModel) {
            user.observe(viewLifecycleOwner) {
                it?.let {
                    with(binding) {
                        iHomeUser.tvUserName.text = "${it.firstName} ${it.lastName}"
                        Glide.with(requireContext())
                            .load(it.photo)
                            .error(R.drawable.ic_prew)
                            .into(iHomeUser.ivUser)
                        Glide.with(requireContext())
                            .load(it.imageBackground)
                            .error(R.drawable.ic_prew)
                            .into(iHomeUser.vBackgroundUser)
                    }
                } ?: kotlin.run {
                    binding.iHomeUser.tvUserTitle.text = "Ласкаво просимо"
                    binding.iHomeUser.inNotification.gone()
                }
            }
        }
    }

    private fun initView() {
        with(binding) {
            val navHostFragment =
                childFragmentManager.findFragmentById(R.id.navHostActivity) as NavHostFragment
            val navController = navHostFragment.navController
            navHomeUser.setupWithNavController(navController)

            FileHelper.changeSize(iHomeUser.vBackgroundUser, resources)
            FileHelper.changeSize(iHomeUser.vBackgroundUser, iHomeUser.ivUser)
            FileHelper.changeSizeCircle(iHomeUser.ivUser, iHomeUser.ivUserCircle)
            FileHelper.changeSizeCircle(iHomeUser.ivUser, iHomeUser.ivLogoUser)
        }
    }

    private fun openFragment(fragment: Fragment) {
        (requireActivity() as? MainActivity)?.openFragment(fragment)
    }
}