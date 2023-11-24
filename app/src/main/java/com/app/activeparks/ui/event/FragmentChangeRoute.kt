package com.app.activeparks.ui.event

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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


    private val viewModel: EventRouteViewModel by activityViewModels()


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

        with(binding) {
            MapsViewController(editRouteMap, requireContext())
            backButton.setOnClickListener { closeFragment() }
        }

        observeGeoPoints()


        val layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.layoutManager = layoutManager

        val onItemClickListener = object : RemoveItemPosition {
            override fun removePosition(position: Int) {

                geoPointsList.removeAt(position)
                markerList.removeAt(position)

                if (geoPointsList.size > 0) {
                    drawRoute(geoPointsList)
                    drawMarkers(geoPointsList)
                } else {
                    binding.editRouteMap.overlays.removeAll { it is Polyline }
                    binding.editRouteMap.overlays.removeAll { it is Marker }
                }

                adapter.notifyItemRemoved(position)

                if (position < geoPointsList.size) {
                    adapter.notifyItemRangeChanged(
                        position,
                        geoPointsList.size - position
                    )
                }

            }
        }
        adapter = GeoPointAdapter(geoPointsList, onItemClickListener)
        binding.recyclerView.adapter = adapter


        binding.editRouteMap.overlays.add(MapEventsOverlay(object : MapEventsReceiver {
            override fun singleTapConfirmedHelper(p: GeoPoint?): Boolean {
                p?.let {

                    geoPointsList.add(it)
                    markerList.clear()
                    drawRoute(geoPointsList)
                    drawMarkers(geoPointsList)

                    adapter.notifyItemInserted(0)
                    binding.recyclerView.scrollToPosition(0)

                    for (i in geoPointsList.indices) {
                        adapter.notifyItemRangeChanged(
                            i,
                            geoPointsList.size - i
                        )
                    }
                }
                return true
            }

            override fun longPressHelper(p: GeoPoint?): Boolean {
                return false
            }
        }))

    }

    private fun observeGeoPoints() {
        viewModel.geoPointsLiveData.observe(viewLifecycleOwner) { geoPoints ->

            geoPointsList = geoPoints
            if(geoPoints.size>0){
                drawRoute(geoPointsList)
                drawMarkers(geoPointsList)
            }


            Log.i("MYNAMEIS","geoPointsList $adapter")
            Log.i("MYNAMEIS","geoPointsList ${geoPointsList.size}")

            val onItemClickListener = object : RemoveItemPosition {
                override fun removePosition(position: Int) {

                    geoPointsList.removeAt(position)
                    markerList.removeAt(position)

                    if (geoPointsList.size > 0) {
                        drawRoute(geoPointsList)
                        drawMarkers(geoPointsList)
                    } else {
                        binding.editRouteMap.overlays.removeAll { it is Polyline }
                        binding.editRouteMap.overlays.removeAll { it is Marker }
                    }

                    adapter.notifyItemRemoved(position)

                    if (position < geoPointsList.size) {
                        adapter.notifyItemRangeChanged(
                            position,
                            geoPointsList.size - position
                        )
                    }

                }
            }
            adapter = GeoPointAdapter(geoPointsList, onItemClickListener)
            binding.recyclerView.adapter = adapter



        }
    }

    private fun drawRoute(points: ArrayList<GeoPoint>) {

        binding.editRouteMap.overlays.removeAll { it is Polyline }

        val road = Road(points)
        val roadOverlay = RoadManager.buildRoadOverlay(
            road,
            colorRouteLine,
            widthRoutLine
        )
        binding.editRouteMap.overlays.add(roadOverlay)

    }

    private fun drawMarkers(points: ArrayList<GeoPoint>) {


        binding.editRouteMap.overlays.removeAll { it is Marker }
        markerList.clear()

        for ((index, p) in points.withIndex()) {
            val marker = Marker(binding.editRouteMap)
            marker.position = p
            marker.icon = getMarkerByPosition(index)
            marker.isDraggable = true
            marker.position
            marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_CENTER)

            marker.setOnMarkerDragListener(object : Marker.OnMarkerDragListener {
                override fun onMarkerDrag(marker: Marker) {
                }

                override fun onMarkerDragEnd(marker: Marker) {

                    for ((i, point) in markerList.withIndex()) {
                        if (point == marker) {
                            geoPointsList[i] = marker.position
                            point.position = marker.position
                        }
                    }

                    drawRoute(geoPointsList)
                    drawMarkers(geoPointsList)

                }

                override fun onMarkerDragStart(marker: Marker) {

                }
            })

            markerList.add(marker)
            binding.editRouteMap.overlays.add(marker)

        }

        binding.editRouteMap.invalidate()
    }

    private fun closeFragment() {

        sendDataToViewModel(geoPointsList)

        parentFragmentManager
            .beginTransaction()
            .hide(this)
            .commit()
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

        canvas.drawBitmap(background, 0f, 0f, null)
        canvas.drawBitmap(overlay, 0f, 0f, null)

        return combined
    }

    private fun sendDataToViewModel(geoPoints: ArrayList<GeoPoint>) {
        viewModel.setGeoPoints(geoPointsList)
    }

}