package com.app.activeparks.ui.event.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.app.activeparks.ui.event.MyItemDiffCallback
import com.app.activeparks.ui.event.RemoveItemPosition
import com.technodreams.activeparks.R
import org.osmdroid.api.IGeoPoint
import org.osmdroid.util.GeoPoint

class GeoPointAdapter(
    private val geoPoints: ArrayList<GeoPoint>,
    private val removeItem: RemoveItemPosition
) :
    RecyclerView.Adapter<GeoPointAdapter.GeoPointViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GeoPointViewHolder {
        val view =
            LayoutInflater
                .from(parent.context)
                .inflate(R.layout.item_geo_point, parent, false)
        return GeoPointViewHolder(view)
    }

    override fun onBindViewHolder(holder: GeoPointViewHolder, position: Int) {
        val geoPoint = geoPoints[position]
        holder.bind(geoPoint)
    }

    override fun getItemCount(): Int {
        return geoPoints.size
    }

    inner class GeoPointViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textView: TextView = itemView.findViewById(R.id.textView)
        private val positionGp: TextView = itemView.findViewById(R.id.position_gp)
        private val removeButton: Button = itemView.findViewById(R.id.removeButton)

        fun bind(geoPoint: GeoPoint) {
            textView.text = "Lat: ${geoPoint.latitude}, Lon: ${geoPoint.longitude}"
            positionGp.text = absoluteAdapterPosition.toString()
            removeButton.setOnClickListener {
                removeItem.removePosition(absoluteAdapterPosition)
            }


        }
    }
}