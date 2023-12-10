

package com.app.activeparks.ui.event.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.activeparks.ui.event.adapter.GeoPointAdapter
import com.app.activeparks.ui.event.interfaces.RemoveItemPosition
import com.app.activeparks.ui.event.util.EventHelper
import com.app.activeparks.ui.event.util.EventTypes
import com.app.activeparks.ui.event.viewmodel.EventRouteViewModel
import com.app.activeparks.util.MapsViewController
import com.app.activeparks.util.extention.replaceFragment
import com.technodreams.activeparks.R
import com.technodreams.activeparks.databinding.FragmentChangeRouteBinding
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.osmdroid.events.MapEventsReceiver
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.overlay.MapEventsOverlay
import org.osmdroid.views.overlay.Marker

@Suppress("DEPRECATION")
class FragmentChangeRoute : Fragment() {

    lateinit var binding: FragmentChangeRouteBinding

    lateinit var adapter: GeoPointAdapter
    var geoPointsList = ArrayList<GeoPoint>()
    var currentTrainingType = ""
    var markerType = 1

    // private val viewModel: EventRouteViewModel by activityViewModels()
    private val viewModel: EventRouteViewModel by sharedViewModel()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentChangeRouteBinding
            .inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var currentPoint = 0
        val myListener: Marker.OnMarkerDragListener = object : Marker.OnMarkerDragListener {


            override fun onMarkerDrag(marker: Marker) {
            }

            @SuppressLint("NotifyDataSetChanged")
            override fun onMarkerDragEnd(marker: Marker) {

                geoPointsList[currentPoint].longitude = marker.position.longitude
                geoPointsList[currentPoint].latitude = marker.position.latitude

                EventHelper.drawRoute(geoPointsList, binding.editRouteMap)
                EventHelper.drawMarkers(binding.editRouteMap, geoPointsList, this, markerType)

                adapter.notifyDataSetChanged()
            }

            override fun onMarkerDragStart(marker: Marker) {

                for ((i, position) in geoPointsList.withIndex()) {

                    if (position == marker.position) {
                        currentPoint = i
                    }
                }
            }
        }
        observer(myListener)

        val removeItemPositionInstance = object : RemoveItemPosition {
            override fun removePosition(position: Int) {
                geoPointsList.removeAt(position)
                adapter.notifyItemRemoved(position)

                if (geoPointsList.isNotEmpty()) {
                    EventHelper.drawRoute(geoPointsList, binding.editRouteMap)
                    EventHelper.drawMarkers(
                        binding.editRouteMap,
                        geoPointsList,
                        myListener,
                        markerType
                    )
                }
            }
        }

        adapter = GeoPointAdapter(geoPointsList, removeItemPositionInstance)
        binding.recyclerView.adapter = adapter

        val lastPoint = viewModel.getLastMapGeoPoint()

        with(binding) {
            MapsViewController(editRouteMap, requireContext())
            backButton.setOnClickListener {

                sendDataToViewModel()
                parentFragmentManager.replaceFragment(
                    R.id.constrain_events_container,
                    FragmentEventCreate()
                )
            }

            editRouteMap.controller.setCenter(lastPoint)
            editRouteMap.overlays.add(MapEventsOverlay(object : MapEventsReceiver {
                @SuppressLint("NotifyDataSetChanged")
                override fun singleTapConfirmedHelper(p: GeoPoint?): Boolean {
                    when (currentTrainingType) {
                        EventTypes.SIMPLE_TRAINING.type -> {
                            geoPointsList.clear()
                            markerType = 0
                        }
                    }

                    p?.let {
                        geoPointsList.add(it)
                        EventHelper.drawRoute(geoPointsList, binding.editRouteMap)
                        EventHelper.drawMarkers(
                            binding.editRouteMap,
                            geoPointsList,
                            myListener,
                            markerType
                        )
                        adapter.notifyDataSetChanged()
                    }
                    return true
                }

                override fun longPressHelper(p: GeoPoint?): Boolean {
                    return false
                }
            }))
        }
    }

    private fun sendDataToViewModel() {

        viewModel.setGeoPoints(geoPointsList)
        viewModel.setLastMapGeoPoint(binding.editRouteMap.mapCenter)
    }


    private fun observer(myListener: Marker.OnMarkerDragListener) {

        Log.i("MYVIEWMODEL", "here")


        viewModel.dataEvent.observe(viewLifecycleOwner) { newData ->
            currentTrainingType = newData.typeId

            Log.i("MYVIEWMODEL", "$currentTrainingType here")
        }

        viewModel.getGeoPointsLiveData().observe(viewLifecycleOwner) { geoPoints ->
            geoPoints?.let {

                if (geoPoints.isNotEmpty()) {
                    EventHelper.drawRoute(it, binding.editRouteMap)
                    EventHelper.drawMarkers(binding.editRouteMap, it, myListener, markerType)
                    geoPointsList = it
                }

                val layoutManager = LinearLayoutManager(requireContext())
                binding.recyclerView.layoutManager = layoutManager


            }
        }

    }

}


