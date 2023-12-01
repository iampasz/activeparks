package com.app.activeparks.ui.event.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.activeparks.ui.event.interfaces.RemoveItemPosition
import com.technodreams.activeparks.databinding.ItemGeoPointBinding
import org.osmdroid.util.GeoPoint

class GeoPointAdapter(
    private val geoPoints: ArrayList<GeoPoint>,
    private val removeItem: RemoveItemPosition
) :
    RecyclerView.Adapter<GeoPointAdapter.GeoPointViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GeoPointViewHolder {
        val binding =
            ItemGeoPointBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return GeoPointViewHolder(binding)
    }

    override fun onBindViewHolder(holder: GeoPointViewHolder, position: Int) {
        val geoPoint = geoPoints[position]
        holder.bind(geoPoint)
    }

    override fun getItemCount(): Int {
        return geoPoints.size
    }

    inner class GeoPointViewHolder(private val binding: ItemGeoPointBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun bind(geoPoint: GeoPoint) {

            val formattedLatitude = String.format("%.4f", geoPoint.latitude)
            val formattedLongitude = String.format("%.4f", geoPoint.longitude)

            binding.longitudeText.text = "Longitude: $formattedLongitude"
            binding.latitudeText.text = "Latitude: $formattedLatitude"
            binding.positionGp.text = absoluteAdapterPosition.toString()
            binding.removeButton.setOnClickListener {
                removeItem.removePosition(absoluteAdapterPosition)
            }

        }
    }
}