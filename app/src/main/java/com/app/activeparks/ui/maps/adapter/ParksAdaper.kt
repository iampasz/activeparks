package com.app.activeparks.ui.maps.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.app.activeparks.data.model.sportsgrounds.ItemSportsground
import com.bumptech.glide.Glide
import com.technodreams.activeparks.R

class ParksAdaper(context: Context?, private val list: List<ItemSportsground>) :
    RecyclerView.Adapter<ParksAdaper.ViewHolder>() {
    private val inflater: LayoutInflater
    var parksAdaperListener: ParksAdaperListener? = null

    init {
        inflater = LayoutInflater.from(context)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = inflater.inflate(R.layout.item_map_list, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val sportsground = list[position]

        Glide.with(holder.ivLogo.context).load(sportsground.photo).error(R.drawable.ic_prew)
            .into(holder.ivLogo)
        holder.tvTitle.text = sportsground.title
        holder.tvAddress.text = round(sportsground.distanceToPoint, 2).toString() + " км"
        holder.ivRoute.setOnClickListener({ v: View? ->
            parksAdaperListener!!.onClick(
                sportsground.getLocation().get(0),
                sportsground.getLocation().get(1)
            )
        })
        holder.itemView.setOnClickListener({ v: View? ->
            parksAdaperListener!!.onInfoPark(
                sportsground
            )
        })
        holder.ivInfo.setOnClickListener({ v: View? ->
            parksAdaperListener!!.onInfoPark(
                sportsground
            )
        })
    }

    fun round(value: Double, places: Int): Double {
        var value = value
        require(!(places < 0))
        val factor = Math.pow(10.0, places.toDouble()).toLong()
        value = value * factor
        val tmp = Math.round(value)
        return tmp.toDouble() / factor
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvTitle: TextView
        val tvAddress: TextView
        val ivLogo: ImageView
        val ivRoute: ImageView
        val ivInfo: ImageView

        init {
            tvTitle = itemView.findViewById(R.id.tvTitle)
            tvAddress = itemView.findViewById(R.id.tvAddress)
            ivLogo = itemView.findViewById(R.id.ivLogo)
            ivRoute = itemView.findViewById(R.id.ivRoute)
            ivInfo = itemView.findViewById(R.id.ivInfo)
        }
    }

    interface ParksAdaperListener {
        fun onClick(lat: Double?, lon: Double?)
        fun onInfoPark(sportsground: ItemSportsground?)
    }

    fun setOnCliclListener(parksAdaperListener: ParksAdaperListener?): ParksAdaper {
        this.parksAdaperListener = parksAdaperListener
        return this
    }
}
