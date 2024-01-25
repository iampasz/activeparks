package com.app.activeparks.ui.active.fragments.map

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorManager
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
import com.app.activeparks.ui.active.util.AddressUtil
import com.app.activeparks.ui.active.util.CalorieCalculator
import com.app.activeparks.util.EasySensorEventListener
import com.app.activeparks.util.MapsViewController
import com.app.activeparks.util.extention.toInfo
import com.technodreams.activeparks.databinding.FragmentMapActivityBinding
import org.koin.androidx.viewmodel.ext.android.activityViewModel
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.overlay.Polyline
import kotlin.math.abs

class MapActivityFragment : Fragment(), LocationListener, EasySensorEventListener {


    private var sensorManager: SensorManager? = null

    private var previousX = 0f
    private var previousY = 0f
    private var previousZ = 0f
    private var threshold = 4f

    private var pathDistance = 0.0
    private var pathDuration = 0L
    private var maxHeight = 0.0
    private var minHeight = 10000.0
    private var maxSpeed = 0.0
    private var kKal = 0
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

        initValue()
        setCurrentLocation()
        observes()

        startCheckLocation()
    }

    private fun initValue() {
        locationManager =
            requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager

        sensorManager = requireActivity().getSystemService(Context.SENSOR_SERVICE) as SensorManager
        sensorManager?.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)?.apply {
            sensorManager?.registerListener(
                this@MapActivityFragment,
                this,
                SensorManager.SENSOR_DELAY_NORMAL
            )
        }
    }

    private fun observes() {
        viewModel.apply {
            checkLocation.observe(viewLifecycleOwner) {
                if (it) {
                    startCheckLocation()
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
        kKal = 0
        previousAltitude = 0.0
        totalAscent = 0.0
        totalDescent = 0.0
        startLocation = null
        previousLocation = null
        viewModel.activityState.startPoint = ""
        viewModel.activityState.weather = ""
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
        if (viewModel.activityState.startPoint.isEmpty() && viewModel.activityState.isTrainingStart) {
            viewModel.activityState.startPoint =
                 AddressUtil.getAddressFromLocation(requireContext(), location)
            viewModel.activityState.stepCount = 0

            mapsViewController?.zoomOnStart()
        }

        if (viewModel.activityState.isTrainingStart && !viewModel.activityState.isPause) {
            val geoPoint = GeoPoint(location)

            calculateParameters(location, geoPoint)

            viewModel.activityState.currentPulse -= 1
        }

        if (viewModel.location == null) {
            viewModel.location = location
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

    private fun saveResult(
        averageSpeed: Double, pathDistance: Double, averagePace: String,
        maxHeight: Double, minHeight: Double, totalAscent: Double,
        totalDescent: Double, currentSpeed: Double, maxSpeed: Double
    ) {
        viewModel.activityState.activityInfoItems.let {
            it[0].number = currentSpeed.toInfo()
            it[1].number = averageSpeed.toInfo()
            it[2].number = maxSpeed.toInfo()
            it[3].number = pathDistance.toInfo()
            kKal = when (viewModel.activityState.activityType.id) {
                0 -> CalorieCalculator.calculateCaloriesForWalk(
                    viewModel.activityState.activityDuration,
                    viewModel.activityState.weight,
                    averageSpeed
                )

                1 -> CalorieCalculator.calculateCaloriesForScandinavianWalk(
                    viewModel.activityState.activityDuration,
                    viewModel.activityState.weight,
                    averageSpeed
                )

                2 -> CalorieCalculator.calculateCaloriesForBicycle(
                    viewModel.activityState.activityDuration,
                    viewModel.activityState.weight,
                    averageSpeed
                )

                else -> CalorieCalculator.calculateCaloriesForRun(
                    viewModel.activityState.activityDuration,
                    viewModel.activityState.weight,
                    averageSpeed
                )
            }
            it[4].number = kKal.toString()
            it[9].number = averagePace
            it[10].number = maxHeight.toInfo()
            it[11].number = (viewModel.activityState.stepCount / 2).toString()
            it[12].number = minHeight.toInfo()
            it[13].number = totalAscent.toInfo()
            it[14].number = totalDescent.toInfo()
        }

        viewModel.updateActivityInfoTrainingItem.value = true
    }

    override fun onSensorChanged(event: SensorEvent) {
        val x = event.values[0]
        val y = event.values[1]
        val z = event.values[2]

        val deltaX = abs(x - previousX)
        val deltaY = abs(y - previousY)
        val deltaZ = abs(z - previousZ)

        if (deltaX > threshold || deltaY > threshold || deltaZ > threshold) {
            viewModel.activityState.stepCount++
        }

        previousX = x
        previousY = y
        previousZ = z
    }
}