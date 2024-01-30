package com.app.activeparks.util.extention

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Resources
import android.net.Uri
import android.view.View
import android.widget.ImageView
import com.technodreams.activeparks.R
import java.io.File
import java.io.FileOutputStream

/**
 * Created by O.Dziuba on 09.01.2024.
 */
class FileHelper {
    companion object {
        @SuppressLint("Recycle")
        fun uriToFile(uri: Uri, context: Context): File? {
            try {
                val inputStream = context.contentResolver?.openInputStream(uri)
                if (inputStream != null) {
                    val file = createTempFile("temp_image", null, context.cacheDir)
                    FileOutputStream(file).use { outputStream ->
                        val buffer = ByteArray(4 * 1024)
                        var read: Int
                        while (inputStream.read(buffer).also { read = it } != -1) {
                            outputStream.write(buffer, 0, read)
                        }
                        outputStream.flush()
                    }
                    return file
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return null
        }


        fun changeSize(view: View, resources: Resources) {
            val ratioWidth = 16
            val ratioHeight = 10

            val screenWidth = resources.displayMetrics.widthPixels
            val newHeight = (screenWidth.toFloat() / ratioWidth * ratioHeight).toInt()

            val layoutParams = view.layoutParams
            layoutParams.width = screenWidth
            layoutParams.height = newHeight
            view.layoutParams = layoutParams
        }

        fun changeSize(view: View, resources: Resources, spanCount: Int) {
            val ratioWidth = 16
            val ratioHeight = 10

            val screenWidth = resources.displayMetrics.widthPixels
            val itemWidth = screenWidth / spanCount

            val newHeight = (itemWidth.toFloat() / ratioWidth * ratioHeight).toInt()

            val layoutParams = view.layoutParams
            layoutParams.width = itemWidth
            layoutParams.height = newHeight
            view.layoutParams = layoutParams
        }


        fun changeSize(firstImageView: ImageView, secondImageView: ImageView) {
            val firstImageLayoutParams = firstImageView.layoutParams
            val firstImageHeight = firstImageLayoutParams.height

            val secondImageLayoutParams = secondImageView.layoutParams
            secondImageLayoutParams.width = (firstImageHeight / 3) * 2
            secondImageLayoutParams.height = (firstImageHeight / 3) * 2
            secondImageView.layoutParams = secondImageLayoutParams
        }

        fun changeSizeCircle(firstImageView: ImageView, secondImageView: ImageView) {
            val firstImageLayoutParams = firstImageView.layoutParams
            val firstImageHeight = firstImageLayoutParams.height

            val increasePercentage = 10
            val increaseFactor = 1 + (increasePercentage / 100.0)

            val secondImageLayoutParams = secondImageView.layoutParams
            secondImageLayoutParams.width = (firstImageHeight * increaseFactor).toInt()
            secondImageLayoutParams.height = (firstImageHeight * increaseFactor).toInt()
            secondImageView.layoutParams = secondImageLayoutParams
        }

        fun getWeatherIconResource(iconName: String): Int {
            val resourceId = when (iconName.substring(0, 2)) {
                "01" -> R.drawable.ic_01
                "02" -> R.drawable.ic_02
                "03" -> R.drawable.ic_03
                "04" -> R.drawable.ic_03
                "09" -> R.drawable.ic_09
                "10" -> R.drawable.ic_10
                "11" -> R.drawable.ic_11
                "13" -> R.drawable.ic_13
                "50" -> R.drawable.ic_50
                else -> R.drawable.default_weather_icon
            }
            return resourceId
        }
    }
}