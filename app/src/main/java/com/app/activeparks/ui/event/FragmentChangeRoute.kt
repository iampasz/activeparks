package com.app.activeparks.ui.event

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.activeparks.ui.event.adapter.GeoPointAdapter
import com.app.activeparks.ui.event.interfaces.RemoveItemPosition
import com.app.activeparks.ui.event.viewmodel.EventRouteViewModel
import com.app.activeparks.util.MapsViewController
import com.technodreams.activeparks.R
import com.technodreams.activeparks.databinding.FragmentChangeRouteBinding
import org.osmdroid.bonuspack.routing.Road
import org.osmdroid.bonuspack.routing.RoadManager
import org.osmdroid.events.MapEventsReceiver
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.overlay.MapEventsOverlay
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.Polyline

class FragmentChangeRoute : Fragment() {

    lateinit var binding: FragmentChangeRouteBinding

    private val widthRoutLine = 10f
    private val colorRouteLine = Color.RED
    private val textSizeCircle = 40f
    private lateinit var adapter: GeoPointAdapter
    var geoPointsList = ArrayList<GeoPoint>()
    var markerList = ArrayList<Marker>()

    val simpleTraining = "848e3121-4a2b-413d-8a8f-ebdd4ecf2840"
    private val routeTraining = "bd09f36f-835c-49e4-88b8-4f835c1602ac"
    var currentTrainingType = ""


    private val viewModel: EventRouteViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentChangeRouteBinding
            .inflate(inflater, container, false)


        viewModel.dataEvent.observe(viewLifecycleOwner) { newData ->
            currentTrainingType = newData.typeId
        }

        viewModel.getGeoPointsLiveData().observe(viewLifecycleOwner) { geoPoints ->
            geoPoints?.let {

                if (geoPoints.isNotEmpty()) {
                    drawRoute(it)
                    drawMarkers(it)
                    geoPointsList = it
                }

                val layoutManager = LinearLayoutManager(requireContext())
                binding.recyclerView.layoutManager = layoutManager

                val removeItemPositionInstance = object : RemoveItemPosition {
                    override fun removePosition(position: Int) {
                        geoPointsList.removeAt(position)
                        adapter.notifyItemRemoved(position)

                        if (geoPointsList.isNotEmpty()) {
                            drawRoute(geoPointsList)
                            drawMarkers(geoPointsList)
                        } else {
                            cleanLines()
                            cleanMarkers()
                        }
                    }
                }

                adapter = GeoPointAdapter(geoPointsList, removeItemPositionInstance)
                binding.recyclerView.adapter = adapter
            }
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val lastPoint = viewModel.getLastMapGeoPoint()

        with(binding) {
            MapsViewController(editRouteMap, requireContext())
            backButton.setOnClickListener { closeFragment() }
            editRouteMap.controller.setCenter(lastPoint)

            editRouteMap.overlays.add(MapEventsOverlay(object : MapEventsReceiver {
                @SuppressLint("NotifyDataSetChanged")
                override fun singleTapConfirmedHelper(p: GeoPoint?): Boolean {
                    when (currentTrainingType) {
                        simpleTraining -> {
                            geoPointsList.clear()
                        }
                    }

                    p?.let {
                        geoPointsList.add(it)
                        markerList.clear()
                        drawRoute(geoPointsList)
                        drawMarkers(geoPointsList)
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


    private fun drawRoute(points: ArrayList<GeoPoint>) {
        cleanLines()
        val road = Road(points)
        val roadOverlay = RoadManager.buildRoadOverlay(
            road,
            colorRouteLine,
            widthRoutLine
        )
        binding.editRouteMap.overlays.add(roadOverlay)
    }

    private fun drawMarkers(points: ArrayList<GeoPoint>) {
        cleanMarkers()
        markerList.clear()

        for ((index, p) in points.withIndex()) {
            val marker = Marker(binding.editRouteMap)

            with(marker) {
                position = p
                when (currentTrainingType) {
                    simpleTraining -> marker.icon =
                        ContextCompat.getDrawable(requireContext(), R.drawable.ic_pin_green)

                    routeTraining -> marker.icon = getMarkerByPosition(index)
                }

                isDraggable = true
                position
                setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_CENTER)
                setOnMarkerDragListener(object : Marker.OnMarkerDragListener {
                    override fun onMarkerDrag(marker: Marker) {
                    }

                    @SuppressLint("NotifyDataSetChanged")
                    override fun onMarkerDragEnd(marker: Marker) {

                        for ((i, point) in markerList.withIndex()) {
                            if (point == marker) {
                                geoPointsList[i] = position
                                point.position = position
                            }
                        }
                        drawRoute(geoPointsList)
                        drawMarkers(geoPointsList)

                        adapter.notifyDataSetChanged()
                    }

                    override fun onMarkerDragStart(marker: Marker) {
                    }
                })
            }


            markerList.add(marker)
            binding.editRouteMap.overlays.add(marker)

        }

        binding.editRouteMap.invalidate()
    }


    private fun getMarkerByPosition(pointNumber: Int): Drawable {
        val text = pointNumber.toString()
        val radius = 50f
        val centerX = 50f
        val centerY = 50f
        val colorCircle = Color.BLUE
        val bitmap =
            Bitmap.createBitmap((radius * 2).toInt(), (radius * 2).toInt(), Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        val paint = Paint()
        paint.color = colorCircle
        paint.isAntiAlias = true

        canvas.drawCircle(centerX, centerY, radius, paint)
        val textBitmap = drawTextToBitmap(bitmap, text)
        val combinedBitmap = combineBitmaps(bitmap, textBitmap)

        return BitmapDrawable(resources, combinedBitmap)
    }

    private fun drawTextToBitmap(bitmap: Bitmap, text: String): Bitmap {
        val paint = Paint(Paint.ANTI_ALIAS_FLAG)
        paint.textSize = textSizeCircle
        paint.color = resources.getColor(R.color.white, null)

        val canvas = Canvas(bitmap)
        val x = (bitmap.width - paint.measureText(text)) / 2
        val y = (bitmap.height + paint.textSize) / 2

        canvas.drawText(text, x, y, paint)

        return bitmap
    }

    private fun combineBitmaps(background: Bitmap, overlay: Bitmap): Bitmap {
        val combined = Bitmap.createBitmap(background.width, background.height, background.config)
        val canvas = Canvas(combined)
        val left = 0f
        val top = 0f

        canvas.drawBitmap(background, left, top, null)
        canvas.drawBitmap(overlay, left, top, null)

        return combined
    }

    private fun sendDataToViewModel() {

        viewModel.setGeoPoints(geoPointsList)
        viewModel.setLastMapGeoPoint(binding.editRouteMap.mapCenter)
    }

    private fun cleanLines() {
        binding.editRouteMap.overlays.removeAll { it is Polyline }
    }

    private fun cleanMarkers() {
        binding.editRouteMap.overlays.removeAll { it is Marker }
    }

    private fun closeFragment() {
        sendDataToViewModel()
        parentFragmentManager
            .beginTransaction()
            .replace(R.id.frame_events_container, FragmentEventCreate())
            .commit()
    }
}