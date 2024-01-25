package com.app.activeparks.ui.userProfile.activityInfo.info

import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.app.activeparks.data.model.activity.ActivityItemResponse
import com.app.activeparks.ui.active.model.InfoItem
import com.app.activeparks.ui.active.model.StartInfo
import com.app.activeparks.ui.userProfile.activityInfo.ActivityInfoViewModel
import com.app.activeparks.util.MapsViewController
import com.app.activeparks.util.extention.FileHelper
import com.app.activeparks.util.extention.getListForUI
import com.technodreams.activeparks.databinding.FragmentActivityInfoViewBinding
import org.koin.androidx.viewmodel.ext.android.activityViewModel
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.overlay.Polyline

/**
 * Created by O.Dziuba on 22.01.2024.
 */
class ActivityInfoViewFragment : Fragment() {

    private lateinit var binding: FragmentActivityInfoViewBinding
    private val viewModel: ActivityInfoViewModel by activityViewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentActivityInfoViewBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observe()
    }


    private fun observe() {
        with(viewModel) {
            activity.observe(viewLifecycleOwner) {
                it?.let {
                    with(binding) {
                        tvDescriptionActivity.text = it.description
                        setStartInfo(it)
                        it.routePoints?.let { route -> setCurrentLocation(route) }
                    }
                }
            }
        }
    }

    private var mapsViewController: MapsViewController? = null
    private fun setCurrentLocation(list: List<List<Double>>) {
        list.takeIf { it.isNotEmpty() }?.apply {
            mapsViewController = MapsViewController(
                binding.mapview,
                context,
                GeoPoint(list.first().first(), list.first().last())
            )
            mapsViewController?.homeView = true

            val polyLine = Polyline()
            list.getListForUI().forEach {
                polyLine.addPoint(it)

            }

            binding.mapview.overlayManager.add(polyLine)
            binding.mapview.invalidate()

            mapsViewController?.setPositionMap(list.first().first(), list.first().last())
            mapsViewController?.zoomOnStart()
        } ?: kotlin.run {
            mapsViewController = MapsViewController(
                binding.mapview,
                context
            )
            mapsViewController?.homeView = true
        }
    }

    private fun FragmentActivityInfoViewBinding.setStartInfo(it: ActivityItemResponse) {
        val startInfo = StartInfo()
        startInfo.weather.apply {
            unit = it.weather ?: ""
            img = FileHelper.getWeatherIconResource(it.weatherIcon ?: "01")
        }
        startInfo.startPoint.apply {
            unit = it.address ?: ""
        }
        ivLocation.setActivityInfoItem(startInfo.startPoint)
        calculateWidthLocation(startInfo.startPoint)
        ivWeather.setActivityInfoItem(startInfo.weather)
    }


    @Suppress("DEPRECATION")
    private fun calculateWidthLocation(point: InfoItem) {
        val displayMetrics = DisplayMetrics()
        requireActivity().windowManager.defaultDisplay.getMetrics(displayMetrics)
        val screenWidth = displayMetrics.widthPixels


        if (binding.ivLocation.calculateDesiredWidth() > 0.5 * screenWidth) {
            point.unit = point.unit.replace(", ", ",\n")
            binding.ivLocation.setActivityInfoItem(point)
        }
    }
}