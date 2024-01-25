package com.app.activeparks.ui.userProfile.activityInfo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.app.activeparks.ui.active.model.TypeOfActivity
import com.app.activeparks.util.extention.DataHelper
import com.app.activeparks.util.extention.FileHelper
import com.app.activeparks.util.extention.toInfo
import com.bumptech.glide.Glide
import com.technodreams.activeparks.R
import com.technodreams.activeparks.databinding.FragmentActivityInfoMainBinding
import org.koin.androidx.viewmodel.ext.android.activityViewModel

/**
 * Created by O.Dziuba on 19.01.2024.
 */
class ActivityInfoFragment(
    private val id: String
) : Fragment() {


    private lateinit var binding: FragmentActivityInfoMainBinding
    private val viewModel: ActivityInfoViewModel by activityViewModel()

    private var navController: NavController? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentActivityInfoMainBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observe()
        setListener()
        viewModel.getActivity(id)
        initView()
    }

    private fun setListener() {
        with(binding) {
            iToolbar.ivBack.setOnClickListener {
                requireActivity().onBackPressed()
            }
        }
    }

    private fun observe() {
        with(viewModel) {
            activity.observe(viewLifecycleOwner) {
                it?.let { item ->
                    binding.iToolbar.tvData.text = DataHelper.formatActivityInfoStartDate(item.startsAt * 1000)

                    with(binding.iProfileUser) {

                        tvActivityTitle.text = item.title
                        tvDistanceValue.text =
                            tvDistanceValue.context.getString(R.string.tv_km, item.distance)
                        tvCaloriesValue.text = tvCaloriesValue.context.getString(
                            R.string.tv_kKal,
                            item.calories.toDouble().toInfo()
                        )
                        tvDate.text = DataHelper.formatDateActivity(item.startsAt * 1000)
                        tvTimeValue.text =
                            DataHelper.formatDurationActivity(item.finishesAt - item.startsAt)

                        try {
                            tvActivityTitle.setCompoundDrawablesWithIntrinsicBounds(
                                TypeOfActivity.getTypeOfActivity()[item.activityType.toInt()].img,
                                0,
                                0,
                                0
                            )
                        } catch (_: Exception) {
                        }


                        if (item.coverImage.isEmpty() || item.coverImage == "null") {
                            ivActivity.setImageResource(R.drawable.activity_back)
                        } else {
                            Glide.with(ivActivity.context)
                                .load(item.coverImage)
                                .into(ivActivity)
                        }
                    }

                }
            }
        }
    }

    private fun initView() {
        val navHostFragment =
            childFragmentManager.findFragmentById(R.id.navFragmentsActivityInfo) as NavHostFragment
        navController = navHostFragment.navController
        navController?.let { binding.navActivityInfo.setupWithNavController(it) }


        FileHelper.changeSize(binding.iProfileUser.root, resources)
    }
}