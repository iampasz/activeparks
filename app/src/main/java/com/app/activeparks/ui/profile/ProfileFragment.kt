package com.app.activeparks.ui.profile

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener
import com.app.activeparks.data.model.user.User
import com.app.activeparks.ui.active.fragments.saveActivity.SaveActivityFragment
import com.app.activeparks.ui.event.fragments.MyEventsFragment
import com.app.activeparks.util.extention.visible
import com.bumptech.glide.Glide
import com.technodreams.activeparks.R
import com.technodreams.activeparks.databinding.FragmentProfileBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class ProfileFragment : Fragment(), OnRefreshListener {

    private val profileViewModel: ProfileViewModel by viewModel()
    private lateinit var viewModelOld: ProfileViewModelOld


    lateinit var binding: FragmentProfileBinding
    fun newInstance(): ProfileFragment {
        return ProfileFragment()
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {


        binding = FragmentProfileBinding
            .inflate(inflater, container, false)


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModelOld =
            ViewModelProvider(this, ProfileModelFactory(requireContext()))[ProfileViewModelOld::class.java]

        binding.swipeRefreshLayout.setOnRefreshListener(this)




        openFragment(MyEventsFragment())

    }


    @SuppressLint("SimpleDateFormat", "SetTextI18n", "SuspiciousIndentation")
    private fun observes() {
        profileViewModel.apply {
            navigate.observe(this@ProfileFragment) {
                it?.let { openFragment(it) }
            }
            saved.observe(this@ProfileFragment) {
                if (it) openFragment(SaveActivityFragment())
            }

        }

        viewModelOld.getUser().observe(
            viewLifecycleOwner
        ) { user: User ->
                if (user.firstName.length + user.lastName.length > 1) {
                    binding.name.visible()
                    binding.name.text = user.firstName + " " + user.lastName
                } else {
                    binding.name.text = user.nickname
                }
                Glide.with(this).load(user.photo).error(R.drawable.ic_prew).into(binding.photo)
            binding.role.text = viewModelOld.isRole(user.roleId)
                if (user.sex != null) {
                    view?.findViewById<View>(R.id.layout_sex)?.visibility =
                        View.VISIBLE
                }
                if (viewModelOld.isProfile(user.roleId) == true) {
                    view?.findViewById<View>(R.id.web_title_action)?.visibility =
                        View.VISIBLE
                    view?.findViewById<View>(R.id.web_action)?.visibility =
                        View.VISIBLE
                }
                if (user.birthday != null && user.birthday.isNotEmpty()) {
                    view?.findViewById<View>(R.id.layout_birthday)?.visibility =
                        View.VISIBLE

                }
                if (user.city.length > 1) {
                    view?.findViewById<View>(R.id.layout_location)?.visibility =
                        View.VISIBLE
                    //  binding.address.setText(user.city)
                }
                if (user.aboutMe.length > 1) {
                    view?.findViewById<View>(R.id.title_about)?.visibility =
                        View.VISIBLE
                    //   binding.about.setVisibility(View.VISIBLE)
                    //  binding.about.setText(user.aboutMe)
                }
                if (user.healthState.length > 1) {
                    view?.findViewById<View>(R.id.title_healt)?.visibility =
                        View.VISIBLE
                    //  binding.healt.setVisibility(View.VISIBLE)
                    //  binding.healt.setText(user.healthState)
                }
                if (user.height != null && user.height.length > 1) {
                    view?.findViewById<View>(R.id.item_height)?.visibility =
                        View.VISIBLE
                    //   binding.height.setVisibility(View.VISIBLE)
                    //   binding.height.setText(user.height + " cм")
                }
                if (user.weight != null && user.weight.length > 1) {
                    view?.findViewById<View>(R.id.item_weight)?.visibility =
                        View.VISIBLE
                    //  binding.weight.setVisibility(View.VISIBLE)
                    //   binding.weight.setText(user.weight + " кг")
                }
                if (user.phone.length > 1) {
                    view?.findViewById<View>(R.id.phone_item)?.visibility =
                        View.VISIBLE
                    //   binding.phone.setText(user.phone)
                }
                //  binding.email.setText(user.email)
                //   binding.profileFilling.setProgress(user.profileFilling)

        }

    }


    private fun openFragment(fragment: Fragment) {
        fragment.let {
            parentFragmentManager
                .beginTransaction()
                .replace(R.id.navFragment, it)
                .commit()

        }
        profileViewModel.navigate.value = null
    }

    override fun onResume() {
        super.onResume()
        observes()
    }

    override fun onRefresh() {

        binding.swipeRefreshLayout.isRefreshing = false
        viewModelOld.user()

        when (viewModelOld.select) {
            0 -> viewModelOld.clubs()
            1 -> viewModelOld.event()
            2 -> viewModelOld.journal()
            3 -> viewModelOld.userVideoList()
        }
    }


}