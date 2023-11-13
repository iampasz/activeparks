package com.app.activeparks.ui.active.fragments.map

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
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
import com.app.activeparks.util.MapsViewController
import com.technodreams.activeparks.databinding.FragmentMapActivityBinding
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.overlay.Overlay
import org.osmdroid.views.overlay.Polyline

class MapActivityFragment : Fragment(), LocationListener {

    lateinit var binding: FragmentMapActivityBinding
    private var mapsViewController: MapsViewController? = null
    private var locationManager: LocationManager? = null
    private val viewModel: ActiveViewModel by viewModel()
    private val line = Polyline()
    private var distance = 0.0

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
        setCurrentLocation()

        startCheckLocation()
    }

    private fun observes() {
        viewModel.apply {
            onSuccess.observe(viewLifecycleOwner) { aBoolean: Boolean ->
                if (aBoolean) {
                    binding.mapview.overlayManager.removeIf { overlay: Overlay? -> overlay is Polyline }
                    line.actualPoints.clear()
                }
            }
            listsGeoPoint.observe(viewLifecycleOwner) { lists: List<List<GeoPoint?>?> ->
                if (lists.isNotEmpty()) {
                    line.setPoints(lists.stream().findFirst().get())
                    binding.mapview.overlayManager.add(line)
                    binding.mapview.invalidate()
                }
                checkLocation.observe(viewLifecycleOwner) {
                    if (!it) {
                        locationManager?.removeUpdates(this@MapActivityFragment)
                    }
                }
            }
            save.observe(viewLifecycleOwner) {
                if (it) bitmap = takeScreenshot(binding.mapview)
            }
        }
    }

    fun takeScreenshot(view: View): Bitmap {
        view.isDrawingCacheEnabled = true
        view.buildDrawingCache(true)

        val bitmap = Bitmap.createBitmap(view.drawingCache)
        view.isDrawingCacheEnabled = false

        return bitmap
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
        locationManager?.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 10f, this)
    }

    private fun setCurrentLocation() {
        mapsViewController =
            MapsViewController(binding.mapview, context)
        mapsViewController?.homeView = true
    }

    private var previousLocation: Location? = null
    override fun onLocationChanged(location: Location) {
        val geoPoint = GeoPoint(location)
        if (previousLocation != null) {
//            val distance = previousLocation?.distanceTo(location)
//            val timeElapsed =
//                (location.time - (previousLocation?.time ?: t)) / 1000 // Convert to seconds
//            val speedMetersPerSecond = distance?.div(timeElapsed)
//            val speedKilometersPerHour = speedMetersPerSecond?.times(3.6f)
//            val speedMinutesPerKilometer = 60.0f / (speedKilometersPerHour ?: 1f)
//            binding.topPanel.aivThird.setNumber(((speedMinutesPerKilometer * 10).roundToInt() / 10.0).toString())
        } else {
            previousLocation = location
        }
//        line.addPoint(geoPoint)
//        binding.mapview.overlayManager.add(line)
//        binding.mapview.invalidate()
        distance += 0.1
//        binding.topPanel.aivFirst.setNumber(((distance * 10).roundToInt() / 10.0).toString())
    }
}