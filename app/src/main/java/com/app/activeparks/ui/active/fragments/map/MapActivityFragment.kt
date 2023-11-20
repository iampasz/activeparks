package com.app.activeparks.ui.active.fragments.map

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.app.activeparks.ui.active.ActiveViewModel
import com.app.activeparks.ui.active.util.CalorieCalculator
import com.app.activeparks.util.MapsViewController
import com.app.activeparks.util.extention.getAddress
import com.app.activeparks.util.extention.toInfo
import com.technodreams.activeparks.R
import com.technodreams.activeparks.databinding.FragmentMapActivityBinding
import org.koin.androidx.viewmodel.ext.android.activityViewModel
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.overlay.Polyline
import java.io.IOException
import java.util.Locale
import kotlin.math.abs

class MapActivityFragment : Fragment(), LocationListener {

    private var pathDistance = 0.0
    private var pathDuration = 0L
    private var maxHeight = 0.0
    private var minHeight = 0.0
    private var maxSpeed = 0.0
    private var kKal = 0.0
    private var previousAltitude = 0.0
    private var totalAscent = 0.0
    private var totalDescent = 0.0
    private var line = Polyline()
    private var startLocation: Location? = null
    private var previousLocation: Location? = null
    lateinit var binding: FragmentMapActivityBinding
    private var mapsViewController: MapsViewController? = null
    private var locationManager: LocationManager? = null
    private val viewModel: ActiveViewModel by activityViewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentMapActivityBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        locationManager =
            requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        setCurrentLocation()
        observes()

        startCheckLocation()
    }

    private fun observes() {
        viewModel.apply {
            checkLocation.observe(viewLifecycleOwner) {
                if (it) {
                    startCheckLocation()
                } else {
//                    locationManager?.removeUpdates(this@MapActivityFragment)
                }
            }

            updateMap.observe(viewLifecycleOwner) {
                if (it) {
                    binding.mapview.overlayManager.clear()
                    binding.mapview.invalidate()
                    setCurrentLocation()
                    line = Polyline()
                    resetStartValue()

                    updateMap.value = false
                }
            }
        }
    }

    private fun resetStartValue() {
        pathDistance = 0.0
        pathDuration = 0L
        maxHeight = 0.0
        minHeight = 0.0
        maxSpeed = 0.0
        kKal = 0.0
        previousAltitude = 0.0
        totalAscent = 0.0
        totalDescent = 0.0
        startLocation = null
        previousLocation = null
    }

    private fun startCheckLocation() {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        locationManager?.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 2f, this)
    }

    private fun setCurrentLocation() {
        mapsViewController = if (previousLocation == null) {
            MapsViewController(binding.mapview, context)
        } else {
            MapsViewController(binding.mapview, context, GeoPoint(previousLocation))
        }
        mapsViewController?.homeView = true
    }

    override fun onLocationChanged(location: Location) {
        if (viewModel.activityState.startPoint.isEmpty()) {
            viewModel.activityState.startPoint = getAddressFromLocation(requireContext(), location)
        }

        if (viewModel.activityState.isTrainingStart && !viewModel.activityState.isPause) {
            val geoPoint = GeoPoint(location)

            calculateParameters(location, geoPoint)

            viewModel.activityState.currentPulse -= 1
        }
    }

    private fun calculateParameters(location: Location, geoPoint: GeoPoint) {
        startLocation?.let {
            previousLocation?.let {
                val distance = location.distanceTo(it) / 1000
                val duration = location.time - it.time

                pathDistance += distance
                pathDuration += duration

                val currentAltitude = location.altitude
                if (currentAltitude > maxHeight) maxHeight = currentAltitude
                if (currentAltitude < minHeight) minHeight = currentAltitude

                val averageSpeed = pathDistance / (pathDuration / 3600000.0)
                val currentSpeed = location.speed * 3.6
                if (currentSpeed > maxSpeed) maxSpeed = currentSpeed

                val averagePace = (pathDuration / 60000.0) / pathDistance
                val paceMinutes = averagePace.toInt()
                val paceSeconds = ((averagePace - paceMinutes) * 60).toInt()

                val formattedPace = String.format("%02d:%02d", paceMinutes, paceSeconds)

                previousLocation = location


                if (previousAltitude == 0.0) {
                    previousAltitude = currentAltitude
                } else {
                    val altitudeChange: Double = currentAltitude - previousAltitude

                    previousAltitude = currentAltitude

                    if (altitudeChange > 0) {
                        totalAscent += altitudeChange
                    } else if (altitudeChange < 0) {
                        totalDescent += abs(altitudeChange)
                    }
                }

                saveResult(
                    averageSpeed,
                    pathDistance,
                    formattedPace,
                    maxHeight,
                    minHeight,
                    totalAscent,
                    totalDescent,
                    currentSpeed,
                    maxSpeed
                )

                viewModel.activityState.activeRoad.add(geoPoint)

                drawLine(geoPoint)
            }
        } ?: kotlin.run {
            startLocation = location
            previousLocation = location
        }
    }

    private fun drawLine(geoPoint: GeoPoint) {
        try {
            line.addPoint(geoPoint)
            binding.mapview.overlayManager.add(line)
            binding.mapview.invalidate()
        } catch (_: Exception) {
            line = Polyline()
            viewModel.activityState.activeRoad.forEach {
                line.addPoint(it)
            }
            binding.mapview.overlayManager.add(line)
            binding.mapview.invalidate()
        }
    }

    private fun getAddressFromLocation(context: Context, location: Location): String {
        val geocoder = Geocoder(context, Locale.getDefault())
        val addresses: List<Address>?

        try {
            addresses = geocoder.getFromLocation(location.latitude, location.longitude, 1)
            if (!addresses.isNullOrEmpty()) {
                val address = addresses[0]
                val addressStringBuilder = StringBuilder()

                for (i in 0..address.maxAddressLineIndex) {
                    addressStringBuilder.append(address.getAddressLine(i)).append(" ")
                }

                return address.getAddress()
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return getString(R.string.tv_address_not_found)
    }

    private fun saveResult(
        averageSpeed: Double, pathDistance: Double, averagePace: String,
        maxHeight: Double, minHeight: Double, totalAscent: Double,
        totalDescent: Double, currentSpeed: Double, maxSpeed: Double
    ) {
        viewModel.activityInfoItems.let {
            it[0].number = currentSpeed.toInfo()
            it[1].number = averageSpeed.toInfo()
            it[2].number = maxSpeed.toInfo()
            it[3].number = pathDistance.toInfo()
            kKal = when (viewModel.activityState.activityType.id) {
                0 -> CalorieCalculator.calculateCaloriesForWalk(
                    viewModel.activityDuration,
                    viewModel.getWeight(),
                    averageSpeed
                )

                1 -> CalorieCalculator.calculateCaloriesForScandinavianWalk(
                    viewModel.activityDuration,
                    viewModel.getWeight(),
                    averageSpeed
                )

                2 -> CalorieCalculator.calculateCaloriesForBicycle(
                    viewModel.activityDuration,
                    viewModel.getWeight(),
                    averageSpeed
                )

                else -> CalorieCalculator.calculateCaloriesForRun(
                    viewModel.activityDuration,
                    viewModel.getWeight(),
                    averageSpeed
                )
            }
            it[4].number = kKal.toInfo()
            it[8].number = averagePace
            it[9].number = maxHeight.toInfo()
            it[11].number = minHeight.toInfo()
            it[12].number = totalAscent.toInfo()
            it[13].number = totalDescent.toInfo()
        }

        viewModel.updateActivityInfoTrainingItem.value = true
    }
}