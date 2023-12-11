package com.app.activeparks.ui.event.util

import android.app.Activity
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import androidx.core.content.ContextCompat
import com.technodreams.activeparks.R
import com.technodreams.activeparks.databinding.FragmentEventCreateBinding
import org.osmdroid.bonuspack.routing.Road
import org.osmdroid.bonuspack.routing.RoadManager
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.Polyline
import java.io.File
import java.io.FileOutputStream

class EventHelper {

    companion object {

        fun drawRoute(
            points: ArrayList<GeoPoint>,
            map: MapView
        ) {

            map.overlays.removeAll { it is Polyline }

            val road = Road(points)
            val roadOverlay = RoadManager.buildRoadOverlay(
                road,
                Color.BLUE,
                20f
            )
            map.overlays.add(roadOverlay)
        }

        fun drawMarkers(
            map: MapView,
            points: ArrayList<GeoPoint>,
            myListener: Marker.OnMarkerDragListener,
            markerType: Int
        ) {

            map.overlays.removeAll { it is Marker }


            for ((index, p) in points.withIndex()) {

                val bitmap = getCircleBitmap()
                val marker = Marker(map)
                val textBitmap = drawTextToBitmap(bitmap, index.toString())
                val combinedBitmap = combineBitmaps(bitmap, textBitmap)
                val drawable = BitmapDrawable(map.resources, combinedBitmap)


                with(marker) {
                    position = p

                    when (markerType) {
                        0 -> marker.icon =
                            ContextCompat.getDrawable(map.context, R.drawable.ic_pin_green)

                        1 -> marker.icon = drawable
                    }

                    isDraggable = true
                    position
                    setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_CENTER)
                    setOnMarkerDragListener(myListener)
                }

                map.overlays.add(marker)
            }

            map.invalidate()
        }


        fun saveImageToFile(imageUri: Uri, activity: Activity): File {
            val resolver = activity.contentResolver
            val file: File?

            val inputStream = resolver?.openInputStream(imageUri)

            file = File(activity.filesDir, "image.jpg")

            val outputStream = FileOutputStream(file)
            val buffer = ByteArray(1024)
            var bytesRead: Int

            while (inputStream?.read(buffer).also { bytesRead = it!! } != -1) {
                outputStream.write(buffer, 0, bytesRead)
            }

            inputStream?.close()
            return file
        }

        private fun getCircleBitmap(): Bitmap {
            val radius = 50f
            val centerX = 50f
            val centerY = 50f
            val colorCircle = Color.BLUE
            val bitmap =
                Bitmap.createBitmap(
                    (radius * 2).toInt(),
                    (radius * 2).toInt(),
                    Bitmap.Config.ARGB_8888
                )
            val canvas = Canvas(bitmap)
            val paint = Paint()
            paint.color = colorCircle
            paint.isAntiAlias = true

            canvas.drawCircle(centerX, centerY, radius, paint)

            return bitmap
        }

        private fun drawTextToBitmap(bitmap: Bitmap, text: String): Bitmap {
            val textSizeCircle = 40f
            val paint = Paint(Paint.ANTI_ALIAS_FLAG)

            paint.textSize = textSizeCircle
            paint.color = Color.WHITE

            val canvas = Canvas(bitmap)
            val x = (bitmap.width - paint.measureText(text)) / 2
            val y = (bitmap.height + paint.textSize) / 2

            canvas.drawText(text, x, y, paint)

            return bitmap
        }

        private fun combineBitmaps(background: Bitmap, overlay: Bitmap): Bitmap {
            val combined =
                Bitmap.createBitmap(background.width, background.height, background.config)
            val canvas = Canvas(combined)
            val left = 0f
            val top = 0f

            canvas.drawBitmap(background, left, top, null)
            canvas.drawBitmap(overlay, left, top, null)

            return combined
        }


        fun checkFields(binding: FragmentEventCreateBinding): Boolean {
            with(binding) {
                if (editNameEvent.text.toString().trim().isEmpty()) {
                    scroll.smoothScrollTo(editNameEvent.top, editNameEvent.top)
                    return false
                }

                if (editDescriptionEvent.text.toString().trim().isEmpty()) {
                    scroll.smoothScrollTo(
                        editDescriptionEvent.top,
                        editDescriptionEvent.top
                    )
                    return false
                }

                if (editFullDescription.text.toString().trim().isEmpty()) {
                    scroll.smoothScrollTo(
                        editFullDescription.top,
                        editFullDescription.top
                    )
                    return false
                }


            }
            return true
        }

    }

}